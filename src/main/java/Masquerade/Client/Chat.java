package Masquerade.Client;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import javafx.scene.control.TextArea;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
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
    private String sender;
    private String recipient;
    private PublicKey recipientKey;
    private Socket chatSocket;
    private TextArea textArea;
    private EncryptionEngine encryptionEngine;
    private boolean approved;

    public Chat(){
        this.isRecipientKeySet = false;
        this.isRecipientSet = false;
        this.textArea = new TextArea();
        this.approved = false;
    }

    public Chat(Socket chatSocket){
        this.isRecipientKeySet = false;
        this.isRecipientSet = false;
        this.chatSocket = chatSocket;
        this.textArea = new TextArea();
        this.approved = false;
    }

    public Chat(Socket chatSocket, String sender, String recipient, EncryptionEngine encryptionEngine){
        this.isRecipientKeySet = false;
        this.isRecipientSet = true;
        this.chatSocket = chatSocket;
        this.sender = sender;
        this.recipient = recipient;
        this.textArea = new TextArea();

        this.encryptionEngine = encryptionEngine;
        this.approved = false;
    }



    synchronized public void sendMessage(String message){
        String fullMessage = sender + ":\n" + message + "\n";
        addTextArea(fullMessage);
        byte[] encryptedMessage = encryptionEngine.encrypt(recipientKey, fullMessage);
        try {
            OutputStream os = chatSocket.getOutputStream();
            os.write(encryptedMessage.length);
            os.write(encryptedMessage);
            os.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    synchronized public void recieveMessage(byte[] encodedBytes){
        String decryptedMessage = encryptionEngine.decrypt(encodedBytes);
        try {
            if (decryptedMessage.equals("DISCONNECT")) {
                addTextArea(recipient + " disconnected");
                chatSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        addTextArea(decryptedMessage);
    }

    private void initialize(){
        if (!isRecipientSet || !isRecipientKeySet){
//            System.out.println("isRecipientSet=" + isRecipientSet + "\nisRecipientKeySet=" + isRecipientKeySet);
            return;
        }
        System.out.println("initialize chat, starting message receiver");
        MessageReceiver mr = new MessageReceiver(chatSocket, this);
        Thread messageReceiverThread = new Thread(mr);
        messageReceiverThread.start();
    }
    // get and set
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
        this.isRecipientSet = true;
    }

    public PublicKey getRecipientKey() {
        return recipientKey;
    }

    public void setRecipientKey(PublicKey recipientKey) {
        this.recipientKey = recipientKey;
        this.isRecipientKeySet = true;
    }

    public void setRecipientKey(byte[] recipientKey){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.recipientKey =  keyFactory.generatePublic(new X509EncodedKeySpec(recipientKey));
            this.isRecipientKeySet = true;
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
            OutputStream os = chatSocket.getOutputStream();
            byte[] message = "DISCONNECT".getBytes();
            byte[] encryptedMessage = encryptionEngine.encrypt(recipientKey, message);
            os.write(encryptedMessage.length);
            os.write(encryptedMessage);
            chatSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // run
    @Override
    public void run(){
        this.initialize();
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
