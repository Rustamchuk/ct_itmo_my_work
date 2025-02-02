package info.kgeorgiy.ja.nazarov.hello;

/**
 * Enum for errors during work
 */
public enum ServerErrors {
    PORT("No ports provided to start the server."),
    THREADS("Invalid number of threads or ports to start the server."),
    SOCKET("Socket error: "),
    SEND("Failed to send response: "),
    RECEIVE("Failed to receive packet: "),
    SHUT_DOWN("Some executor tasks did not terminate."),
    USAGE("Usage: HelloUDPClient <port> <threads>"),
    HOST_PORT("Error: hostPort must be at least 0."),
    THREAD_NUM("Error: threadsNumber must be at least 1."),
    NUM_FORMAT("Error parsing arguments: "),
    SELECTOR("Selector error: ");

    ServerErrors(String s) {
    }
}
