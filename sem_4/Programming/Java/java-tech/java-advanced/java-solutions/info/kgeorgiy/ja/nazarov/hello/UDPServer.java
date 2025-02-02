package info.kgeorgiy.ja.nazarov.hello;

import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class representing a UDP server with common functionality for handling requests and responses.
 */
public abstract class UDPServer implements NewHelloServer {
    /**
     * CAPACITY for array msg
     */
    protected static final int CAPACITY = 1024;
    /**
     * executor service
     */
    protected ExecutorService executor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(int threads, Map<Integer, String> ports) {
        if (ports == null || ports.isEmpty()) {
            UDPUtils.printMessage(ServerErrors.PORT);
            return;
        }

        int totalThreads = threads + ports.size();
        if (totalThreads <= 0) {
            UDPUtils.printMessage(ServerErrors.THREADS);
            return;
        }

        setUp();

        executor = Executors.newFixedThreadPool(totalThreads);
        ports.forEach((port, responseFormat) -> {
            try {
                startProcess(port, responseFormat);
            } catch (IOException e) {
                UDPUtils.printMessage(ServerErrors.SOCKET, e.getMessage());
            }
        });
    }

    /**
     * Sets up the server. This method should be implemented by subclasses.
     */
    protected abstract void setUp();

    /**
     * Starts the server process on the specified port with the given response format.
     *
     * @param port     the port to bind the server to
     * @param response the response format to send back to clients
     * @throws IOException if an I/O error occurs
     */
    protected abstract void startProcess(int port, String response) throws IOException;

    /**
     * Handles errors that occur while sending responses.
     *
     * @param e the IOException that occurred
     */
    protected void processResponseError(IOException e) {
        UDPUtils.printMessage(ServerErrors.SEND, e.getMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            nextClose();
        } catch (IOException e) {
            UDPUtils.printMessage(ServerErrors.SHUT_DOWN, e.getMessage());
        }

        if (executor != null) {
            executor.shutdownNow();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    UDPUtils.printMessage(ServerErrors.SHUT_DOWN);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Closes the server resources. This method should be implemented by subclasses.
     *
     * @throws IOException if an I/O error occurs
     */
    protected abstract void nextClose() throws IOException;

    /**
     * Main method to run the UDP server.
     *
     * @param args command line arguments: port, number of threads
     */
    public static void main(String[] args) {
        if (UDPUtils.checkValidArgs(args, 2)) {
            UDPUtils.printMessage(ServerErrors.USAGE);
            return;
        }
        HelloUDPServer server = null;
        try {
            int port = UDPUtils.parseIntArg(args[0], 0, ServerErrors.HOST_PORT);
            int workThreads = UDPUtils.parseIntArg(args[1], 1, ServerErrors.THREAD_NUM);

            server = new HelloUDPServer();
            server.start(port, workThreads);
        } catch (NumberFormatException e) {
            UDPUtils.printMessage(ServerErrors.NUM_FORMAT, e.getMessage());
        } catch (IllegalArgumentException ignored) {
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }
}
