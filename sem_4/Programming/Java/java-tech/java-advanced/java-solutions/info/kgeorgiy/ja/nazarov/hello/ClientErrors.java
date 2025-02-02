package info.kgeorgiy.ja.nazarov.hello;

/**
 * Enum for errors during work
 */
public enum ClientErrors {
    UNKNOWN_HOST("Unknown host: "),
    SOCKET("Socket error: "),
    RELATION("Failed to send/receive packet: "),
    SHUT_DOWN("Some executor tasks did not terminate."),
    USAGE("Usage: HelloUDPClient <host> <port> <prefix> <threads> <requests>"),
    HOST_NAME("Error: URL cannot be null or empty."),
    REQ_PREFIX("Error: requestPrefix cannot be null or empty."),
    HOST_PORT("Error: hostPort must be at least 0."),
    THREAD_NUM("Error: threadsNumber must be at least 1."),
    REQ_NUM("Error: requestsNumber must be at least 1."),
    NUM_FORMAT("Error parsing arguments: ");

    private final String message;

    ClientErrors(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}