17
https://raw.githubusercontent.com/javmarina/Nintendo-Switch-Remote-Control/master/gui/server/src/main/java/com/javmarina/server/SerialAdapter.java
package com.javmarina.server;

import com.fazecast.jSerialComm.SerialPort;
import com.javmarina.util.Controller;
import com.javmarina.util.CrcUtils;
import com.javmarina.util.GeneralUtils;

import java.io.IOException;


@SuppressWarnings("NumericCastThatLosesPrecision")
public class SerialAdapter {

    private enum Status {
        OUT_OF_SYNC,
        SYNCED,
        SYNCING
    }

    private static final int WRITE_TIMEOUT = 0; // Blocking write
    // TODO: Change constant to Windows latency+2
    private static final int READ_TIMEOUT = 18; // Default buffer delay in Windows is 16ms

    // Commands to send to MCU
    //private static final byte COMMAND_NOP = 0x00;
    private static final byte COMMAND_SYNC_1 = 0x33;
    private static final byte COMMAND_SYNC_2 = (byte) 0xCC;
    private static final byte COMMAND_SYNC_START = (byte) 0xFF;

    // Responses from MCU
    //private static final byte RESP_USB_ACK = (byte) 0x90;
    private static final byte RESP_UPDATE_ACK = (byte) 0x91;
    private static final byte RESP_UPDATE_NACK = (byte) 0x92;
    private static final byte RESP_SYNC_START = (byte) 0xFF;
    private static final byte RESP_SYNC_1 = (byte) 0xCC;
    private static final byte RESP_SYNC_OK = 0x33;

    private final SerialPort serialPort;
    private Status status = Status.OUT_OF_SYNC;

    SerialAdapter(final SerialPort serialPort, final int baudrate) {
        serialPort.setBaudRate(baudrate);
        serialPort.setNumDataBits(8);
        serialPort.setParity(SerialPort.NO_PARITY);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        serialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_WRITE_BLOCKING | SerialPort.TIMEOUT_READ_BLOCKING,
                READ_TIMEOUT,
                WRITE_TIMEOUT);
        serialPort.openPort();
        this.serialPort = serialPort;
    }

    /*
    1. Send nine 0xFF bytes.
    2. Wait 1ms, then read back all the bytes that have been received. You can receive anywhere from 1 to 9 bytes, and the
    response will always end in 0xFF. Note that you may receive multiple 0xFF bytes. Alternatively, you can clear the
    receive buffer, then send another 0xFF byte and wait for a response back - when the AVR is not in "synchronized"
    state, it will always send a 0xFF byte to signal that it is ready to begin synchronization.
    3. Send a 0x33 byte and wait for the response (0xCC).
    4. Send a 0xCC byte and wait for the response (0x33).
    */
    @SuppressWarnings("ReuseOfLocalVariable")
    void sync(final boolean forceSync) throws IOException {
        final long t1 = System.currentTimeMillis();
        if (status == Status.SYNCING) {
            // Another thread started syncing process and it hasn't finished yet
            return;
        }
        if (status == Status.SYNCED && !forceSync) {
            // Already synced and don't want to force sync
            return;
        }
        status = Status.SYNCING;
        errorRate = 0.0f;

        // Send 9x 0xFF's to fully flush out buffer on device
        // Device will send back 0xFF (RESP_SYNC_START) when it is ready to sync
        final byte b = COMMAND_SYNC_START;
        final byte[] bufferFlushBytes = {b,b,b,b,b,b,b,b,b};
        serialPort.writeBytes(bufferFlushBytes, bufferFlushBytes.length);
        System.out.println("Bytes written");

        long timestamp = System.currentTimeMillis();
        int available = 0;
        while (System.currentTimeMillis() - timestamp < READ_TIMEOUT) {
            final int now = serialPort.bytesAvailable();
            if (now > available) {
                available = now;
                timestamp = System.currentTimeMillis();
            }
        }
        if (available >= 1 && available <= 9) {
            // OK, read last received byte
            final byte[] rx = new byte[available];
            serialPort.readBytes(rx, available);
            System.out.println("Received " + available + " bytes: " + GeneralUtils.byteArrayToString(rx));
            if (rx[available-1] == RESP_SYNC_START) {
                // MCU ready to start synchronization
                System.out.println("RESP_SYNC_START received as last byte");
                sendByte(COMMAND_SYNC_1);
                System.out.println("Sending COMMAND_SYNC_1");
                byte response = readByte();
                System.out.println("Response: " + response);

                if (response == RESP_SYNC_1) {
                    // First step done
                    System.out.println("RESP_SYNC_1 received");
                    sendByte(COMMAND_SYNC_2);
                    System.out.println("Sending COMMAND_SYNC_2");
                    response = readByte();
                    System.out.println("Response: " + response);

                    if (response == RESP_SYNC_OK) {
                        // Synchronized!!
                        System.out.println("RESP_SYNC_OK received");
                        status = Status.SYNCED;
                        System.out.println("Synchronization took " + (System.currentTimeMillis() - t1) + " ms");
                        return;
                    }
                }
            }
        }

        status = Status.OUT_OF_SYNC;
        System.out.println("Couldn't sync");
        throw new IOException("Couldn't sync with the AVR MCU");
    }

    synchronized void closePort() {
        serialPort.closePort();
    }

    /*private void waitForData(final int sleep) {
        final long t0 = System.currentTimeMillis();
        long t1 = t0;
        int available = serialPort.bytesAvailable();
        while (t1-t0 < sleep || available == 0) {
            GeneralUtils.sleep(sleep);
            available = serialPort.bytesAvailable();
            t1 = System.currentTimeMillis();
        }
    } */

    /* private byte waitForByte() throws IOException {
        final long timestamp = System.currentTimeMillis();
        int size;
        while (true) {
            if (System.currentTimeMillis() - timestamp > TIMEOUT) {
                throw new IOException("Serial read timeout (waited " + TIMEOUT + " ms)");
            }
            size = serialPort.bytesAvailable();
            if (size > 0) {
                final byte[] array = new byte[size];
                serialPort.readBytes(array, array.length);
                if (size == 1) {
                    return array[0];
                } else {
                    throw new IOException("Expected only one byte but got " + size + " bytes. Message: " +
                            GeneralUtils.byteArrayToString(array));
                }
            }
        }
    }*/

    /* private int waitForData() throws IOException {
        final long timestamp = System.currentTimeMillis();
        int bytes = 0;
        while (true) {
            sleep(1);
            if (System.currentTimeMillis() - timestamp > 100) {
                // 100 ms elapsed
                throw new IOException("Serial read timeout");
            }
            if (serialPort.bytesAvailable() > bytes || bytes == 0) {
                bytes = serialPort.bytesAvailable();
            } else {
                return bytes;
            }
        }
    } */

    /*
     * Utilities for single-byte communication
     */

    private final byte[] txByteBuffer = new byte[1];
    private final byte[] rxByteBuffer = new byte[1];

    private synchronized void sendByte(final byte b) {
        txByteBuffer[0] = b;
        serialPort.writeBytes(txByteBuffer, 1);
    }

    private synchronized byte readByte() {
        rxByteBuffer[0] = 0; // Clear buffer
        serialPort.readBytes(rxByteBuffer, 1);
        return rxByteBuffer[0];
    }

    /*
     * Utilities for packet transmission
     */

    private final byte[] packetCrc = new byte[9];
    private float errorRate = 0.0f;

    synchronized boolean sendPacket(final byte[] packet) {
        assert serialPort != null;
        assert packet.length == 8;

        if (status != Status.SYNCED) {
            System.out.println("sendPacket() error: serial communication is not currently synced");
            try {
                sync(false); // Will ignore if status == SYNCING or SYNCED
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        System.arraycopy(packet, 0, packetCrc, 0, 8);
        packetCrc[8] = CrcUtils.crc(packet);

        serialPort.writeBytes(packetCrc, packetCrc.length);
        final byte b = readByte();
        switch (b) {
            case RESP_UPDATE_ACK:
                errorRate = GeneralUtils.lowPassFilter(errorRate, 0, 0.005f);
                break;
            case RESP_UPDATE_NACK:
                // CRC failed
                errorRate = GeneralUtils.lowPassFilter(errorRate, 1, 0.005f);
                if (errorRate > 0.08) {
                    System.out.println("Max error rate reached, resynchronizing...");
                    try {
                        // Try to sync again even though status == SYNCED
                        sync(true);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return b == RESP_UPDATE_ACK;
    }

    public String testSpeed(final int samples) {
        long sum = 0;
        long min = Long.MAX_VALUE;
        long max = 0;
        int errorCount = 0;

        try {
            sync(false);
        } catch (final IOException e) {
            return "Sync error, test aborted";
        }

        for (int i = 0; i < samples; i++) {
            final long t0 = System.currentTimeMillis();
            final boolean error = !sendPacket(Controller.EMPTY_PACKET);
            final long t1 = System.currentTimeMillis();

            if (error) {
                errorCount++;
            } else {
                final long delta = t1-t0;
                if (delta < min) {
                    min = delta;
                }
                if (delta > max) {
                    max = delta;
                }
                sum += delta;
            }
        }

        final int validSamples = samples-errorCount;
        if (validSamples > 0) {
            final double avg = (double) sum / validSamples;
            return String.format("Minimum: %d ms\r\nMaximum: %d ms\r\nAverage: %.3f ms\r\nError count: %d", min, max, avg, errorCount);
        } else {
            return "No packets were sent correctly";
        }
    }
}
