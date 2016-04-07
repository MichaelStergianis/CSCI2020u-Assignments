package Masquerade.Client;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Created by michael on 06/04/16.
 */
public class Chat implements Runnable{
    PublicKey reipientKey;
    Socket chatSocket;
    public Chat(PublicKey recipientKey, Socket chatSocket){
        this.reipientKey = recipientKey;
        this.chatSocket = chatSocket;
    }



    synchronized public void sendMessage(String message){

    }

    synchronized public String recieveMessage(byte[] encodedBytes){

        return null;
    }

    @Override
    public void run(){
    }

}
