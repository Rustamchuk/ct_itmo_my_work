package info.kgeorgiy.ja.nazarov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class representing a UDP client with common functionality for sending and receiving messages.
 */
public abstract class UDPClient implements HelloClient {
    /**
     * Checks if the current request should be sent based on the context.
     *
     * @param context the client context containing request information
     * @return true if the request should be sent, false otherwise
     */
    protected boolean checkRequest(ClientContext context) {
        if (context.getRequest() > context.getRequests()) {
            UDPUtils.printMessage(ClientContext.THREAD, context.getThread(), ClientContext.FINISHED);
            return false;
        }
        UDPUtils.printMessage(ClientContext.SENT, context.getMessage());
        return true;
    }

    /**
     * Checks if the received response matches the expected message in the context.
     *
     * @param response the received response
     * @param context  the client context containing request information
     * @return true if the response is valid, false otherwise
     */
    protected boolean checkResponse(String response, ClientContext context) {
        if (response.contains(context.getExpectedMessage())) {
            UDPUtils.printMessage(ClientContext.RECEIVED, response);
            return true;
        }
        UDPUtils.printMessage(ClientErrors.RELATION, response);
        return false;
    }

    /**
     * Main method to run the UDP client.
     *
     * @param args command line arguments: host, port, request prefix, number of threads, number of requests
     */
    public static void main(String[] args) {
        if (UDPUtils.checkValidArgs(args, 5)) {
            UDPUtils.printMessage(ClientErrors.USAGE);
            return;
        }
        try {
            String hostName = UDPUtils.parseStringArg(args[0], ClientErrors.HOST_NAME);
            int hostPort = UDPUtils.parseIntArg(args[1], 0, ClientErrors.HOST_PORT);
            String requestPrefix = UDPUtils.parseStringArg(args[2], ClientErrors.REQ_PREFIX);
            int threadsNumber = UDPUtils.parseIntArg(args[3], 1, ClientErrors.THREAD_NUM);
            int requestsNumber = UDPUtils.parseIntArg(args[4], 1, ClientErrors.REQ_NUM);

            new HelloUDPClient().run(hostName, hostPort, requestPrefix, threadsNumber, requestsNumber);
        } catch (NumberFormatException e) {
            UDPUtils.printMessage(ClientErrors.NUM_FORMAT + e.getMessage());
        } catch (IllegalArgumentException ignored) {
        }
    }
}
