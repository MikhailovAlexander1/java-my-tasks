package info.kgeorgiy.ja.mikhailov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPClient implements HelloClient {
    public void run(String host, int port, String prefix, int threadsNumber, int requestsNumberInEachThread) {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);

        for (int i = 1; i <= threadsNumber; i++) {
            final int numberOfCurrentThread = i;
            executorService.submit(() -> {
                try (DatagramSocket datagramSocket = new DatagramSocket()) {
                    datagramSocket.setSoTimeout(10);
                    byte[] dataResponse = new byte[datagramSocket.getReceiveBufferSize()];
                    DatagramPacket responsePacket = new DatagramPacket(dataResponse, datagramSocket.getReceiveBufferSize());
                    for (int requestNumber = 1; requestNumber <= requestsNumberInEachThread; requestNumber++) {
                        String request = String.format("%s%d_%d", prefix, numberOfCurrentThread, requestNumber);
                        byte[] byteRequest = request.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket requestPacket = new DatagramPacket(byteRequest, byteRequest.length, socketAddress);
                        while (true) {
                            try {
                                datagramSocket.send(requestPacket);
                                datagramSocket.receive(responsePacket);
                                String result = new String(responsePacket.getData(), 0, responsePacket.getLength());
                                if (result.contains(request)) {
                                    System.out.println(result);
                                    break;
                                }
                            } catch (IOException e) {
                                System.err.println("I/O occurs while sending a request " + e.getMessage());
                            }
                        }
                    }
                } catch (SocketException e) {
                    System.err.println("The socket could not be opened," +
                            "or the socket could not bind to the specified local port." + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination((long) threadsNumber * requestsNumberInEachThread * 10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static final String requiredForm = "Required form: <host> <requests prefix> <port number> <number of threads> <number of requests in each thread>.";

    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Arguments length must be 5. " + requiredForm);
            return;
        }
        try {
            String host = args[0];
            int port = Integer.parseInt(Objects.requireNonNull(args[1]));
            String prefix = args[2];
            int threadsNumber = Integer.parseInt(Objects.requireNonNull(args[3]));
            int requestNumberInEachThread = Integer.parseInt(Objects.requireNonNull(args[4]));
            new HelloUDPClient().run(host, port, prefix, threadsNumber, requestNumberInEachThread);
        } catch (NullPointerException e) {
            System.err.println("Arguments must be non-null.");
        } catch (NumberFormatException e) {
            System.err.println(requiredForm);
        }
    }
}
