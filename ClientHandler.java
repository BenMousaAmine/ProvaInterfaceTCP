package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

// gestione di cliente conessi ai server
public class ClientHandler implements Runnable {

    Socket clientSocket;
    private PrintWriter out;

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        InetAddress address = clientSocket.getInetAddress();
        int port = clientSocket.getPort();
        System.out.println("connected: " + address + " on port: " + port);
    }

    void handle() {
        out = null; // allocate to write answer to client.
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            readLoop(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Boolean readLoop(BufferedReader in, PrintWriter out) {
        // waits for data and reads it in until connection dies
        // readLine() blocks until the server receives a new line from client
        String s = "";
        // cliemt manager si uso il primo metodo di messagi
        // ClientManager cm = ClientManager.getInstance();
        // Warehouse cosi uso il magazino di prodtti
        WareHouse wh = WareHouse.getInstance();


        try {
            while ((s = in.readLine()) != null) {

                switch (s.toUpperCase()) {
                    case "MORE_EXPENSIVE": {
                        System.out.println(wh.moreExptoJson());
                        out.println(wh.moreExptoJson());
                        break;
                    }
                    case "ALL": {
                        System.out.println(wh.ToJSon());
                        out.println(wh.ToJSon());
                        break;
                    }
                    case "ALL_SORTED": {
                        System.out.println(wh.allSortedJson());
                        out.println(wh.allSortedJson());
                        break;
                    }
                    default: {
                        System.out.println("error");
                        out.println("error");
                    }
                }


                //in questa il server mi risondi cosa l'ho mandato
                // System.out.println("Amine");
                // cm.broadcast(s, this);
                // out.println(s.toUpperCase());!!!!!!!!
                //cosi il server mi rispondi cosa in formato json (guardo il metodo toSon in WareHouse)
                // System.out.println(wh.toJSon());!!!!!
                //cosi mi stampa il totale di costo di prodotti
                //   System.out.println(wh.sum()+" Dollar");
                out.flush();
            }
            InetAddress address = clientSocket.getInetAddress();
            int port = clientSocket.getPort();
            System.out.println("done on: " + address + " on port: " + port);
            // cm.remove(this);
            //  System.out.println("Number clients: " + cm.nClients());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void run() {
        handle();
    }
}
