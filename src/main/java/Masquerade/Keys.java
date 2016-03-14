package Masquerade;

import javafx.scene.control.Label;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Scanner;

/**
 * Created by michael on 14/03/16.
 */
public class Keys {
    private KeyStore key;

    Keys() {
        try {
            this.key = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public char[] checkPassword(String pass, String pass2, Label label) {
        if (pass.equals(pass2)) {
            if (pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}$")) {
                label.setText("                        ");
                return pass.toCharArray();
            } else {
                label.setText("Password is unacceptable");
            }
        } else {
            label.setText("Passwords do not match");
        }
        return null;
    }


}
