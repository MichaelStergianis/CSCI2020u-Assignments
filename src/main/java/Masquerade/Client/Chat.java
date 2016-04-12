package Masquerade.Client;

import javafx.scene.control.TextArea;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by michael on 06/04/16.
 */
public class Chat implements Runnable{
    private boolean isRecipientSet;
    private boolean isRecipientKeySet;
    private String recipient;
    private PublicKey recipientKey;
    private Socket chatSocket;
    private TextArea textArea;
    private EncryptionEngine encryptionEngine;

    public Chat(){
        this.isRecipientKeySet = false;
        this.isRecipientSet = false;
        this.textArea = new TextArea();
    }

    public Chat(Socket chatSocket){
        this.isRecipientKeySet = false;
        this.isRecipientSet = false;
        this.chatSocket = chatSocket;
        this.textArea = new TextArea();
    }

    public Chat(Socket chatSocket, String recipient, EncryptionEngine encryptionEngine){
        this.isRecipientKeySet = false;
        this.isRecipientSet = false;
        this.chatSocket = chatSocket;
        this.recipient = recipient;
        this.textArea = new TextArea();
        this.encryptionEngine = encryptionEngine;
    }



    synchronized public void sendMessage(String message){

    }

    synchronized public String recieveMessage(byte[] encodedBytes){

        return null;
    }

    private void initialize(){
        if (!isRecipientSet || !isRecipientKeySet){
            return;
        }
        ArrayList<Byte> recipientKeyArray = new ArrayList<>();
        MessageReceiver mr = new MessageReceiver(chatSocket, this);
        Thread messageReceiverThread = new Thread(mr);
        messageReceiverThread.start();
        try {
            PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
            sendPubKey(writer);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    // get and set
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public PublicKey getRecipientKey() {
        return recipientKey;
    }

    public void setRecipientKey(PublicKey recipientKey) {
        this.recipientKey = recipientKey;
    }

    public void setRecipientKey(byte[] recipientKey){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.recipientKey =  keyFactory.generatePublic(new X509EncodedKeySpec(recipientKey));
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (InvalidKeySpecException e){
            e.printStackTrace();
        }
    }

    public TextArea getTextArea(){
        return textArea;
    }
    public void addTextArea(String message){
        String oldText = textArea.textProperty().get();
        String newText = oldText + "\n" + message;
        textArea.textProperty().setValue(newText);
    }

    public void disconnect(){
        try {
            PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
            writer.println("DISCONNECT");
            writer.flush();
            chatSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendPubKey(PrintWriter writer){
        writer.println(encryptionEngine.getPublicKey());
        writer.flush();
    }
    // run
    @Override
    public void run(){
        initialize();
    }
}
