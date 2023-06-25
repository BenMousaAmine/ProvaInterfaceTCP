package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HandleFrameRequest implements Runnable {
    Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public HandleFrameRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            setupStreams();

            String request = receiveRequest();
            String response = handleRequest(request);
            sendResponse(response);

            closeStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void closeStreams() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private String receiveRequest() throws IOException {
        return in.readLine();
    }

    private void sendResponse(String response) {
        out.println(response);
        out.flush();
    }

    private String handleRequest(String request) {
        switch (request) {
            case "ALL":
                return handleAllRequest();
            case "MORE_EXPENSIVE":
                return handleMoreExpensiveRequest();
            case "ALL_SORTED":
                return handleSortedByNameRequest();
            default:
                return "Invalid request";
        }
    }

    private String handleAllRequest() {
        List<Car> carList = WareHouse.getInstance().getCarList();
        return transformToJson(carList);
    }

    private String handleMoreExpensiveRequest() {
        List<Car> carList = WareHouse.getInstance().mostExpensiveCar();
        return transformToJson(carList);
    }

    private String handleSortedByNameRequest() {
        List<Car> carList = WareHouse.getInstance().allSorted();
        return transformToJson(carList);
    }

    private String transformToJson(List<Car> carList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(carList);
    }
}
