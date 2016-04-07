package Masquerade.Server;

import java.net.Socket;

/**
 * Created by michael on 06/04/16.
 */
public class Client {
    private String userName;
    private String iNet;
    private int port;
    private Socket socket;

    public Client(Socket socket){
        this.socket = socket;
    }
    public Client(Socket socket, String userName){
        this.userName = userName;
        this.socket = socket;
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public Socket getSocket(){
        return this.socket;
    }
}
