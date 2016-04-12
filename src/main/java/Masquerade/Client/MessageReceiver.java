package Masquerade.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by michael on 06/04/16.
 */
public class MessageReceiver implements Runnable{
    Socket socket;
    Chat chat;
    public MessageReceiver(Socket socket, Chat chat){
        this.socket = socket;
        this.chat = chat;
    }

    public void receiveMessage(){
        try {
            InputStream is = socket.getInputStream();
            ArrayList<Byte> byteArray = new ArrayList<>();
            int byteIn = is.read();
            while (byteIn != -1) {
                byteArray.add(new Byte(new Integer(byteIn).byteValue()));
                byteIn = is.read();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (socket.isConnected()){
            receiveMessage();
        }
    }
}
