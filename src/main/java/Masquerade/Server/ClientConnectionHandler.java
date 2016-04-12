package Masquerade.Server;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by michael on 21/03/16.
 */
public class ClientConnectionHandler implements Runnable {
    private Socket clientSocket;
    private ConnectedClients clients;
    Client client;

    public ClientConnectionHandler(Client client, ConnectedClients clients) {
        this.client = client;
        clientSocket = client.getSocket();
        this.clients = clients;
    }

    public void run() {
        try {
            InputStream inStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            String line = reader.readLine();
            System.out.println(line + " request from " + clientSocket.getInetAddress());
            String[] parts = line.split(" ");
            String userName = parts[0];
            int listenPort = new Integer(parts[1]);
            if (!clients.isConnected(userName)) {
                try {
                    this.client.setUserName(userName);
                    this.client.setListenPort(listenPort);
                    clients.add(this.client);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                writer.println("OK");
                writer.flush();
            } else {
                writer.println("ERROR: That user name is already taken");
                writer.flush();
                writer.close();
                reader.close();
                clientSocket.close();
                return;
            }
            chatHandler(writer, reader);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chatHandler(PrintWriter writer, BufferedReader reader){
        try {
            Integer firstChar = 0;
            while (firstChar != -1){
                firstChar = reader.read();
                byte[] firstCharByte = {firstChar.byteValue()};
                String request =  new String(firstCharByte) + reader.readLine();
                System.out.println(request);
                String[] reqs = request.split(" ");
                if (reqs[0].equals("LOGOUT")) {
                    clientSocket.close();
                    System.out.println("Logging out " + client.getUserName());
                    clients.remove(client.getUserName());
                    return;
                } else if (reqs[0].equals("CHAT")) {
                    if (clients.isConnected(reqs[1])) {
                        Client client = clients.get(reqs[1]);
                        if (client.getUserName().equals(this.client.getUserName())){
                            writer.println("INVALID CLIENT");
                            writer.flush();
                        } else {
                            writer.println(
                                    client.getSocket().getInetAddress().getHostAddress() + " "
                                    + new Integer(client.getListenPort())
                            );
                            writer.flush();
                        }
                    } else {
                        writer.println("INVALID CLIENT");
                    }
                    writer.flush();
                } else {
                    writer.println("INVALID INPUT");
                    writer.flush();
                }
            }
            System.out.println(client.getUserName() + " disconnected");
            clients.remove(client.getUserName());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
