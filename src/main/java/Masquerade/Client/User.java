package Masquerade.Client;

import Masquerade.Client.UI.ChatWindow;

import java.io.*;
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
        OutputStream os = chatSocket.getOutputStream();
        // send our username
        os.write(userName.length());
        os.write(userName.getBytes());
        os.flush();
        // send pubKey
        os.write(encryptionEngine.getPublicKey().getEncoded().length);
        os.write(encryptionEngine.getPublicKey().getEncoded());
        os.flush();
        // receive their public key
        InputStream is = chatSocket.getInputStream();
        int keyLength = is.read();
        byte[] encodedKey = new byte[keyLength];
        is.read(encodedKey);
        return initializeChat(chatSocket, recipientUser, encodedKey);
    }

    synchronized public void handleChatRequest(Socket chatSocket){
        System.out.println("Request from " + chatSocket.getInetAddress().getHostAddress() + " port: " + chatSocket.getLocalPort());
        try {
            // get userName
            InputStream is = chatSocket.getInputStream();
            int userNameLength = is.read();
            byte[] userName = new byte[userNameLength];
            is.read(userName);

            // get the encoded key
            int length = is.read();
            byte[] encodedKey = new byte[length];
            is.read(encodedKey);

            // send our key
            OutputStream os = chatSocket.getOutputStream();
            os.write(encryptionEngine.getPublicKey().getEncoded().length);
            os.write(encryptionEngine.getPublicKey().getEncoded());

            // set up chat
            Chat chat = new Chat(chatSocket, this.userName, new String(userName), encryptionEngine);
            chat.setRecipientKey(encodedKey);

            // create thread and window
            Thread chatThread = new Thread(chat);
            chatThread.start();
            Main.createChatWindow(chat);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    synchronized public Chat initializeChat(Socket chatSocket, String userName, byte[] publicKey){
            Chat chat = new Chat(chatSocket, this.userName, userName, encryptionEngine);
            chat.setRecipientKey(publicKey);
            Thread chatThread = new Thread(chat);
            chatThread.start();
            ChatWindow chatWindow = new ChatWindow(chat);
            chatWindow.display();
            return chat;
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
