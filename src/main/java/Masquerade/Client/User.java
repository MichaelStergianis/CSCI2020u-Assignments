package Masquerade.Client;

import Masquerade.Client.UI.ChatChoice;
import Masquerade.Client.UI.ChatWindow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by michael on 05/04/16.
 */
public class User {
    private String userName;
    private Socket serverSocket;
    private ServerSocket listenSocket;
    private boolean connected;
    private EncryptionEngine encryptionEngine;

    public User(String userName){
        try {
            // by default, user is not connected to a server
            connected = false;
            this.setUserName(userName);
            // our socket and listener to listen for incoming chats
            listenSocket = new ServerSocket(0);
            // make the encryption engine
            encryptionEngine = new EncryptionEngine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public User(String userName, int listenPort){
        try {
            // by default, user is not connected to a server
            connected = false;
            this.setUserName(userName);
            // our socket and listener to listen for incoming chats
            listenSocket = new ServerSocket(listenPort);
            // make the encryption engine
            encryptionEngine = new EncryptionEngine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    synchronized public String getUserName(){
        return this.userName;
    }


    /**
     * connectToServer
     *
     * A function to connect to a server given certain parameters
     * Returns -1 if that username is already taken
     * Returns -2 if it is an invalid socket
     *
     * @param iNetAdress the network address or url of the server as a string
     * @param port the port the server is using as an integer 8020 by default
     * @return
     */
    public int connectToServer(String iNetAdress, int port){
        try {
            connected = true;
            serverSocket = new Socket(iNetAdress, port);
            PrintWriter writer = new PrintWriter(serverSocket.getOutputStream());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(serverSocket.getInputStream())
            );
            writer.println(getUserName() + " " + listenSocket.getLocalPort());
            writer.flush();
            String response = reader.readLine();
            if (response.equals("ERROR: That user name is already taken")){
                // -1 is username already taken
                writer.close();
                reader.close();
                serverSocket.close();
                return -1;
            } // else if -> other errors if needed
            System.out.println(response);
        } catch (IOException e){
            e.printStackTrace();
            // -2 is invalid server
            return -2;
        }
        return 0;
    }

    public int connectToServer(String iNetAdress){
        return connectToServer(iNetAdress, 8020);
    }

    synchronized public Chat startChat(String recipientUser) throws Exception{
        if (!connected){
            throw (new Exception("User is not connected to any server"));
        }
        System.out.println("Starting Chat");
        PrintWriter writer = new PrintWriter(serverSocket.getOutputStream());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(serverSocket.getInputStream())
        );
        writer.println("CHAT" + " " + recipientUser);
        writer.flush();
        Integer firstChar = reader.read();
        byte[] firstCharByte = {firstChar.byteValue()};
        String recipientINet = new String(firstCharByte)
                + reader.readLine();
        // if we got the user name wrong, return an error code so we can show
        // why there was an error
        if (recipientINet.equals("INVALID CLIENT")){
            return null;
        }
        String[] parts = recipientINet.split(" ");
        System.out.println(recipientINet);
        Socket chatSocket = new Socket(parts[0], new Integer((parts[1])));
        // send our username and publicKey
        PrintWriter printWriter = new PrintWriter(chatSocket.getOutputStream());
        printWriter.println(userName + " ");
        printWriter.flush();
//        for (byte i : encryptionEngine.getPublicKey().getEncoded()){
//            printWriter.write(new Integer(i));
//        } printWriter.flush();
        System.out.println("Finished writing pub key");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(chatSocket.getInputStream())
        );
        // receive their public key
        byte[] firstByte = {new Byte(new Integer(bufferedReader.read()).byteValue())};
        String publicKey = (new String(firstByte) + bufferedReader.readLine());
        return initializeChat(chatSocket, recipientUser, publicKey);
    }

    synchronized public void handleChatRequest(Socket chatSocket){
        System.out.println("Request from " + chatSocket.getInetAddress().getHostAddress() + " port: " + chatSocket.getLocalPort());
        try {
            // get incoming info
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(chatSocket.getInputStream())
            );
            byte[] firstByte = {new Byte(new Integer(reader.read()).byteValue())};
            String[] request = (new String(firstByte) + reader.readLine()).split(" ");
            // get the encoded key
            System.out.println("Username is " + request[0]);
            System.out.println("Encoded key is " + request[1]);
            byte[] encodedKey = new byte[0];
//            ArrayList<Byte> keyArray = new ArrayList<>();
//            int inByte = reader.read();
//            while (inByte != -1){
//                System.out.print(inByte);
//                keyArray.add( new Integer(inByte).byteValue() );
//                inByte = reader.read();
//            }
//            System.out.println("\n done reading");
//            byte[] encodedKey = new byte[keyArray.size()];
//            for (int i = 0; i < encodedKey.length; i++) {
//                encodedKey[i] = keyArray.get(i);
//            }
//            System.out.println("This is the encoded key " + encodedKey);
            Chat chat = new Chat(chatSocket, request[0], encryptionEngine);
            chat.setRecipientKey(encodedKey);
            // now set up so that we can ask the user if they want ot chat
            Main.displayChatChoice(chat);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    synchronized public Chat initializeChat(Socket chatSocket, String userName, String publicKey){
        try {
            PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
            writer.println(this.getUserName());
            writer.flush();
            Chat chat = new Chat(chatSocket, userName, encryptionEngine);
            Thread chatThread = new Thread(chat);
            chatThread.start();
            ChatWindow chatWindow = new ChatWindow(chat);
            chatWindow.display();
            System.out.println("I just showed you the chat window!");
            return chat;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    synchronized public void logout() throws Exception{
        if (!connected){
            throw (new Exception("User is not connected to any server"));
        }
        PrintWriter writer = new PrintWriter(serverSocket.getOutputStream());
        writer.println("LOGOUT " + userName);
        writer.flush();
        writer.close();
        serverSocket.close();

    }

    synchronized public ServerSocket getListenSocket() {
        return listenSocket;
    }

    synchronized public EncryptionEngine getEncryptionEngine() {
        return encryptionEngine;
    }

    synchronized public void setUserName(String userName) {
        this.userName = userName;
    }

}
