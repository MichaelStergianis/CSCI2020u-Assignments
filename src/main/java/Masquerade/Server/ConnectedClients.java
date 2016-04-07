package Masquerade.Server;

import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by michael on 06/04/16.
 */
public class ConnectedClients {
    private TreeMap<String, Client> clients;

    public ConnectedClients(){
        clients = new TreeMap<>();
    }

    public boolean isConnected(String userName){
        Client client;
        try {
            client = clients.get(userName);
        } catch (NullPointerException e){
            client = null;
        }
        if (client == null || !client.isConnected()) {
            return false;
        } else {
            return true;
        }
    }

    public void remove(String userName){
        clients.remove(userName);
    }

    public void add(Client client){
        clients.put(client.getUserName(), client);
    }

    public Client get(String userName){
        return clients.get(userName);
    }

    public void printAllClients(){
        List<Client> clientList = new ArrayList<>(clients.values());
        for (Client client: clientList){
            System.out.println(client.getUserName());
        }
    }
}
