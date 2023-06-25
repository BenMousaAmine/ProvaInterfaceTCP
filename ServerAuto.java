package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAuto {

    static final int portNumber = 1234;

    public static void main(String[] args) {

        // Server TCP
        System.out.println("Server Car started!");

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server Connected!");

                HandleFrameRequest handleFrameRequest = new HandleFrameRequest(clientSocket);
                Thread thread = new Thread((Runnable) handleFrameRequest);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
