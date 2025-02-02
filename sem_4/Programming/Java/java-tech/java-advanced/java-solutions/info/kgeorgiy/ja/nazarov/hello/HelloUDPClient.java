package info.kgeorgiy.ja.nazarov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * Implementation of the {@link HelloClient} interface. Provides the ability to
 * web client work
 */
public class HelloUDPClient extends UDPClient {
    /**
     * {@inheritDoc}
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 1; i <= threads; i++) {
            int threadId = i;
            executor.submit(() -> handleThread(host, port, prefix, threadId, requests));
        }

        shutDown(executor);
    }

    private InetAddress resolveHost(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            UDPUtils.printMessage(ClientErrors.UNKNOWN_HOST, host);
            return null;
        }
    }

    public void handleThread(String host, int port, String prefix, int threadId, int requests) {
        InetAddress address = resolveHost(host);
        if (address == null) {
            return;
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(ClientContext.TIMEOUT);
            ClientContext context = new ClientContext(prefix, threadId, requests, address, port);
            for (int requestId = 1; requestId <= requests; requestId++) {
                sendAndReceive(socket, context);
                context.nextRequest();
            }
        } catch (SocketException e) {
            UDPUtils.printMessage(ClientErrors.SOCKET, e.getMessage());
        }
    }

    private void sendAndReceive(DatagramSocket socket, ClientContext context) {
        while (!Thread.interrupted()) {
            try {
                handleWrite(socket, context);
                if (handleRead(socket, context)) {
                    break;
                }
            } catch (IOException e) {
                UDPUtils.printMessage(ClientErrors.RELATION, e.getMessage());
            }
        }
    }

    private void handleWrite(DatagramSocket socket, ClientContext context) throws IOException {
        if (!checkRequest(context)) {
            return;
        }

        String message = context.getMessage();
        byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, context.address, context.port);
        socket.send(sendPacket);
    }

    private boolean handleRead(DatagramSocket socket, ClientContext context) throws IOException {
        byte[] receiveData = new byte[ClientContext.CAPACITY];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, ClientContext.CAPACITY);
        socket.receive(receivePacket);
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

        return super.checkResponse(response, context);
    }

    /**
     * shutting down
     *
     * @param executor executor
     */
    protected static void shutDown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            UDPUtils.printMessage(ClientErrors.SHUT_DOWN);
            executor.shutdownNow();
        }
    }
}
