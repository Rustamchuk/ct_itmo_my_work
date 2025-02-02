package info.kgeorgiy.ja.nazarov.hello;

import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Implementation of the {@link NewHelloServer} interface. Provides the ability to
 * web client work
 */
public class HelloUDPServer extends UDPServer {
    /**
     * sockets map
     */
    private final Map<Integer, DatagramSocket> sockets = new ConcurrentHashMap<>();

    @Override
    protected void setUp() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startProcess(int port, String response) throws IOException {
        DatagramSocket socket = new DatagramSocket(port);

        sockets.put(port, socket);
        executor.submit(() -> processRequests(socket, response));
    }

    /**
     * process requests
     *
     * @param socket         socket
     * @param responseFormat responseFormat
     */
    private void processRequests(DatagramSocket socket, String responseFormat) {
        byte[] receiveData = new byte[CAPACITY];
        while (!socket.isClosed()) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, CAPACITY);
            if (receivePacket(socket, receivePacket)) {
                String request = extractMessage(receivePacket);
                String responseText = responseFormat.replace("$", request);
                sendResponse(socket, receivePacket.getAddress(), receivePacket.getPort(), responseText);
            }
        }
    }

    /**
     * receive Packet
     *
     * @param socket socket
     * @param packet packet
     */
    private boolean receivePacket(DatagramSocket socket, DatagramPacket packet) {
        try {
            socket.receive(packet);
            return true;
        } catch (IOException e) {
            if (!socket.isClosed()) {
                UDPUtils.printMessage(ServerErrors.RECEIVE, e.getMessage());
            }
            return false;
        }
    }

    /**
     * receive Packet
     *
     * @param packet packet
     */
    private String extractMessage(DatagramPacket packet) {
        return new String(packet.getData(), 0, packet.getLength());
    }

    /**
     * send response method
     *
     * @param socket       socket
     * @param address      address
     * @param port         port
     * @param responseText responseText
     */
    private void sendResponse(DatagramSocket socket, InetAddress address, int port, String responseText) {
        byte[] sendData = responseText.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            super.processResponseError(e);
        }
    }

    @Override
    public void nextClose() {
        sockets.values().forEach(DatagramSocket::close);
    }
}