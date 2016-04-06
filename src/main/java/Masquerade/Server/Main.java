package Masquerade.Server;

import java.io.File;

/**
 * Created by michael on 21/03/16.
 */
public class Main {
    public static void main (String[] args){
        File sharedFile = new File(args[0]);
        if (!sharedFile.exists() || !sharedFile.isDirectory()){
            System.exit(1);
        }
        Host host = new Host(8020, new File(args[0]));
        host.handleRequests();
    }
}
