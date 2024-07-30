package info.kgeorgiy.ja.mikhailov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPServer implements HelloServer {
    private ExecutorService executorService;
    private DatagramSocket socket;

    @Override
    public void start(int port, int threads) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("error while creating socket connected to port (pot number: +" + port + ").");
            return;
        }
        executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                    try {
                        byte[] data = new byte[socket.getReceiveBufferSize()];
                        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                        socket.receive(receivePacket);
                        String receivedText = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        byte[] responseData = ("Hello, " + receivedText).getBytes();
                        DatagramPacket response = new DatagramPacket(responseData, responseData.length, receivePacket.getSocketAddress());
                        try {
                            socket.send(response);
                        } catch (IOException e) {
                            System.err.println("I/O occurs while sending: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            System.err.println("I/O exception with datagram: " + e.getMessage());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        socket.close();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static final String requiredForm = "Required form: <port> <number of threads>.";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Arguments length must be 2. " + requiredForm);
            return;
        }
        Scanner ss = null;
        try (HelloUDPServer helloUDPServer = new HelloUDPServer()){
            int port = Integer.parseInt(Objects.requireNonNull(args[0]));
            int numberOfThreads = Integer.parseInt(Objects.requireNonNull(args[1]));
            helloUDPServer.start(port, numberOfThreads);
            ss = new Scanner(System.in);
            while (true) {
                if (ss.hasNext() && ss.next().equals("quit")) {
                    break;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Arguments must be non-null.");
        } catch (NumberFormatException e) {
            System.err.println(requiredForm);
        } finally {
            try {
                Objects.requireNonNull(ss).close();
            } catch (NullPointerException ignored) {}
        }
    }
}
