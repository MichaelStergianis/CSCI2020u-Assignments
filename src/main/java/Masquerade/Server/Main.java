package Masquerade.Server;

import java.io.File;

/**
 * Created by michael on 21/03/16.
 */
public class Main {
    public static void main (String[] args){
        Host host = new Host(8020);
        host.handleRequests();
    }
}
