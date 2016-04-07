package Masquerade.Server;

/**
 * Created by michael on 06/04/16.
 */
public class Chat {
    Client[] clients;
    public Chat(Client client1, Client client2){
        clients = new Client[2];
        clients[0] = client1;
        clients[1] = client2;
    }
}
