package Masquerade.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by michael on 21/03/16.
 */
public class Host {
    private ServerSocket serverSocket;
    private ConnectedClients clients;

    public Host(){
        try {
            serverSocket = new ServerSocket(8020);
            clients = new ConnectedClients();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Host(int port){
        try {
            serverSocket = new ServerSocket(port);
            clients = new ConnectedClients();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleRequests(){
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client currentClient = new Client(clientSocket);
                Thread clientThread = new Thread(
                        new ClientConnectionHandler(currentClient, clients));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}