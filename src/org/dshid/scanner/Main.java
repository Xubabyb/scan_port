package org.dshid.scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int THREADS = 100;
    public static final int START = 0;
    public static final int TIMEOUT = 100;
    public static final int FINISH = 65535;


    public static void main(String[] args) {
        scan("localhost");
    }

    public static void scan(String host) {
        System.out.println("Scan starting");
        var executor = Executors.newFixedThreadPool(THREADS);

        for (int i = START; i <= FINISH; i++) {
            final int port = i;
            executor.execute(() -> {
                var inetSocketAddress = new InetSocketAddress(host, port);
                try (var socket = new Socket();) {
                    socket.connect(inetSocketAddress, TIMEOUT);
                    System.out.printf("Host: %s, port %d is opened\n", host, port);
                } catch (IOException ignore) {

                }
            });

        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Scan finished!");

    }
}
