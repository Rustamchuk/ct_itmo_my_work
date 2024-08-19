package info.kgeorgiy.ja.nazarov.hello;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * A non-blocking UDP client that sends requests to a specified server and handles responses.
 */
public class HelloUDPNonblockingClient extends UDPClient {
    private static class KeyIterator implements Iterator {
        private final Iterator<SelectionKey> iterator;

        public KeyIterator(Set<SelectionKey> selectedKeys) {
            this.iterator = selectedKeys.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public SelectionKey next() {
            SelectionKey key = iterator.next();
            iterator.remove();
            return key;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        Selector selector = null;
        try {
            InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(host), port);
            selector = Selector.open();

            for (int i = 1; i <= threads; i++) {
                DatagramChannel channel = DatagramChannel.open();
                channel.configureBlocking(false);
                channel.connect(address);
                channel.register(selector, SelectionKey.OP_WRITE, new ClientContext(prefix, i, requests));
                UDPUtils.printMessage(ClientContext.THREAD, i, ClientContext.STARTED);
            }

            handleThread(selector, address, prefix);
        } catch (IOException e) {
            UDPUtils.printMessage(ClientErrors.SOCKET, e.getMessage());
        } finally {
            shutDown(selector);
        }
    }

    /**
     * Handles the main loop for sending and receiving messages using the selector.
     *
     * @param selector the selector to manage channels
     * @param address  the server address
     * @param prefix   the request prefix
     * @throws IOException if an I/O error occurs
     */
    public void handleThread(Selector selector, InetSocketAddress address, String prefix) throws IOException {
            while (!Thread.interrupted() && !selector.keys().isEmpty()) {
                selector.select(ClientContext.TIMEOUT);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                if (selectedKeys.isEmpty()) {
                    for (final SelectionKey selectionKey : selector.keys()) {
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    }
                    continue;
                }
                sendAndReceive(new KeyIterator(selectedKeys));
            }
    }

    /**
     * Sends requests and receives responses for the selected keys.
     *
     * @param queue the iterator over the selected keys
     * @throws IOException if an I/O error occurs
     */
    private void sendAndReceive(KeyIterator queue) throws IOException {
        while (queue.hasNext()) {
            SelectionKey key = queue.next();

            DatagramChannel channel = (DatagramChannel) key.channel();
            ClientContext context = (ClientContext) key.attachment();

            if (key.isReadable()) {
                key.interestOps(SelectionKey.OP_WRITE);
                if (handleRead(channel, context)) {
                    context.nextRequest();
                }
                continue;
            }
            key.interestOps(SelectionKey.OP_READ);
            handleWrite(channel, context);
        }
    }

    /**
     * Handles reading a response from the channel.
     *
     * @param channel the datagram channel
     * @param context the client context containing request information
     * @return true if the response is valid, false otherwise
     * @throws IOException if an I/O error occurs
     */
    private boolean handleRead(DatagramChannel channel, ClientContext context) throws IOException {
        byte[] byteArray = new byte[ClientContext.CAPACITY];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        channel.receive(buffer);
        String response = new String(byteArray, 0, buffer.position(), StandardCharsets.UTF_8);

        return super.checkResponse(response, context);
    }

    /**
     * Handles writing a request to the channel.
     *
     * @param channel the datagram channel
     * @param context the client context containing request information
     * @throws IOException if an I/O error occurs
     */
    private void handleWrite(DatagramChannel channel, ClientContext context) throws IOException {
        if (!checkRequest(context)) {
            channel.close();
            return;
        }

        String message = context.getMessage();
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        channel.send(buffer, channel.getRemoteAddress());
    }

    public void shutDown(Selector selector) {
        try {
            if (selector != null) {
                for (SelectionKey key : selector.keys()) {
                    key.channel().close();
                }
                selector.close();
            }
        } catch (IOException ignored) {
        }
    }
}
