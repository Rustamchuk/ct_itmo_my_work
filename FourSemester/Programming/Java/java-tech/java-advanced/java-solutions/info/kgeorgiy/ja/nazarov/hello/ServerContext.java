package info.kgeorgiy.ja.nazarov.hello;

import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class ServerContext {
    public final DatagramChannel channel;
    public final SocketAddress address;
    public final String responseText;

    public ServerContext(DatagramChannel channel, SocketAddress address, String responseText) {
        this.channel = channel;
        this.address = address;
        this.responseText = responseText;
    }
}
