package info.kgeorgiy.ja.nazarov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import info.kgeorgiy.java.advanced.hello.NewHelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * A non-blocking UDP server that handles requests and sends responses.
 */
public class HelloUDPNonblockingServer extends UDPServer {
    private final Map<Integer, DatagramChannel> channels = new ConcurrentHashMap<>();
    private Selector selector;
    private Queue<ServerContext> responses;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        try {
            selector = Selector.open();
            responses = new ConcurrentLinkedDeque<>();
        } catch (IOException e) {
            UDPUtils.printMessage(ServerErrors.SELECTOR, e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startProcess(int port, String response) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_READ, response);

        channels.put(port, channel);
        executor.submit(this::processRequests);
    }

    /**
     * Processes incoming requests in a non-blocking manner.
     */
    private void processRequests() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isReadable()) {
                        handleRead(key);
                    } else {
                        sendResponse(key);
                    }
                }
            } catch (IOException e) {
                UDPUtils.printMessage(ServerErrors.SELECTOR, e.getMessage());
            }
        }
    }

    /**
     * Handles reading a request from a client.
     *
     * @param key the selection key representing the channel to read from
     */
    private void handleRead(SelectionKey key) {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        SocketAddress address;
        try {
            address = channel.receive(buffer);
            if (address != null) {
                executor.execute(() -> {
                    buffer.flip();
                    String request = StandardCharsets.UTF_8.decode(buffer).toString();
                    String responseFormat = (String) key.attachment();
                    String responseText = responseFormat.replace("$", request);
                    responses.add(new ServerContext(channel, address, responseText));
                    if (key.isValid()) {
                        key.interestOps(SelectionKey.OP_WRITE);
                        selector.wakeup();
                    }
                });
            }
        } catch (IOException e) {
            UDPUtils.printMessage(ServerErrors.RECEIVE, e.getMessage());
        }
    }

    /**
     * Sends a response to the client.
     */
    private void sendResponse(SelectionKey key) {
        if (responses.isEmpty()) {
            key.interestOps(SelectionKey.OP_READ);
            return;
        }
        ServerContext ctx = responses.poll();
        ByteBuffer buffer = ByteBuffer.wrap(ctx.responseText.getBytes(StandardCharsets.UTF_8));
        try {
            ctx.channel.send(buffer, ctx.address);
        } catch (IOException e) {
            super.processResponseError(e);
        }
        key.interestOpsOr(SelectionKey.OP_READ);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nextClose() throws IOException {
        for (DatagramChannel datagramChannel : channels.values()) {
            datagramChannel.close();
        }
        if (selector != null) {
            selector.close();
        }
    }
}
