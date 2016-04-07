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
            String userName = line.split(" ")[0];
            if (!clients.isConnected(userName)) {
                try {
                    this.client.setUserName(userName);
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
            while (client.isConnected()){
                Integer firstChar = reader.read();
                byte[] firstCharByte = {firstChar.byteValue()};
                String request =  new String(firstCharByte) + reader.readLine();
                String[] reqs = request.split(" ");
                if (reqs[0].equals("LOGOUT")) {
                    clientSocket.close();
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
                                    + new Integer(client.getSocket().getPort()).toString()
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
