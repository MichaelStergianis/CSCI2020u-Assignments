package Masquerade.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by michael on 07/04/16.
 */
public class ChatListener implements Runnable{
    private User user;

    public ChatListener(User user){
        this.user = user;
    }

    @Override
    public void run(){
        handleRequests();
    }

    public void handleRequests(){
        try {
            System.out.println("ChatListener: Handling Requests");
            while (true) {
                ServerSocket listenSocket = user.getListenSocket();
                Socket chatSocket = listenSocket.accept();
                user.handleChatRequest(chatSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
