package Masquerade.Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by michael on 21/03/16.
 */
public class ClientConnectionHandler implements Runnable{
    private Socket clientSocket;
    private File sharedFolder;

    public ClientConnectionHandler(Socket clientSocket, File sharedFolder){
        this.clientSocket = clientSocket;
        this.sharedFolder = sharedFolder;
    }
    public void run(){
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            String line = in.readLine();
            System.out.println(line + " request from "  + clientSocket.getInetAddress());
            String[] request = line.split(" ");
            if (request == null){
                returnError();
            } else if (request[0].equals("DIR")) {
                sendDir();
            } else if (request[0].equals("UPLOAD")) {
                receive(request[1]);
            } else if (request[0].equals("DOWNLOAD")) {
                send(request[1]);
            } else {
                returnError();
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendDir(){
        String[] dirs = sharedFolder.list();
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            // The server will send a list of file names separated by commas
            for (String file: dirs) {
                out.print(file + ",");
            }
            out.println();
            out.flush();
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    synchronized private void receive(String fileName){
        try {
            InputStream in = clientSocket.getInputStream();
            File file = new File(sharedFolder, fileCollision(fileName, 0));
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            int inByte = in.read();
            while (inByte != -1){
                fout.write(inByte);
                inByte = in.read();
            }
            fout.flush();
            fout.close();
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    synchronized private void send(String fileName){
        try {
            File file = new File(sharedFolder, fileName);
            if (file.isDirectory()){
                System.out.println("Request for directory " + fileName + " denied");
                OutputStream os = clientSocket.getOutputStream();
                os.write(-1);
                os.flush();
                os.close();
                return;
            }
            FileInputStream fin = new FileInputStream(file);
            OutputStream outStream = clientSocket.getOutputStream();
            int inByte = fin.read();
            while (inByte != -1){
                outStream.write(inByte);
                inByte = fin.read();
            }
            outStream.flush();
            fin.close();
            outStream.close();
        } catch (IOException e){

        }
    }
    public String fileCollision(String fileName, int i){
        File file;
        if (i > 0){
            file = new File(sharedFolder, fileName + " (" + i + ")");
        } else {
            file = new File(sharedFolder, fileName);
        }
        if (!file.exists()){
            return file.getName();
        } else {
            return fileCollision(fileName, ++i);
        }
    }
    //    private void receive(String fileName, BufferedReader in){
//        try {
//            PrintWriter out = new PrintWriter(new FileOutputStream(new File(sharedFolder, fileName)));
//            String line;
//            while ( (line = in.readLine()) != null){
//               out.print(line);
//            }
//            out.flush();
//            out.close();
//            in.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
    //    private void send(String fileName){
//        try {
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
//            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
//            String line;
//            while ( (line = in.readLine()) != null){
//                out.print(line);
//            }
//            out.flush();
//            out.close();
//            in.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//    }
    private void returnError(){

    }
}
