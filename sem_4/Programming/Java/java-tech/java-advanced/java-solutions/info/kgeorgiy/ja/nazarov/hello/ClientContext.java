package info.kgeorgiy.ja.nazarov.hello;

import java.net.InetAddress;
import java.util.Arrays;

public class ClientContext {
    /**
     * TIMEOUT for answer
     */
    public static final int TIMEOUT = 100;
    /**
     * CAPACITY for array msg
     */
    public static final int CAPACITY = 1024;
    /**
     * MSG_PREFIX prefix for responses messages
     */
    public static final String MSG_PREFIX = "Hello, ";
    public static final String RECEIVED = "Received: ";
    public static final String SENT = "Sent: ";
    public static final String THREAD = "Thread ";
    public static final String STARTED = " started.";
    public static final String FINISHED = " finished.";

    private final String prefix;
    private final int threadId;
    private final int requests;
    public final InetAddress address;
    public final int port;
    private int requestId;

    ClientContext(String prefix, int threadId, int requests) {
        this.prefix = prefix;
        this.threadId = threadId;
        this.requests = requests;
        this.requestId = 1;
        this.address = null;
        this.port = 0;
    }

    ClientContext(String prefix, int threadId, int requests, InetAddress address, int port) {
        this.prefix = prefix;
        this.threadId = threadId;
        this.requests = requests;
        this.requestId = 1;
        this.address = address;
        this.port = port;
    }

    public int getRequests() {
        return requests;
    }

    public int getRequest() {
        return requestId;
    }

    public int getThread() {
        return threadId;
    }

    public void nextRequest() {
        requestId++;
    }

    public String getMessage() {
        return prefix + threadId + "_" + requestId;
    }

    public String getExpectedMessage() {
        return MSG_PREFIX + getMessage();
    }
}
