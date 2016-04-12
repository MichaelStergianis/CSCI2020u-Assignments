package Masquerade.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * Created by michael on 07/04/16.
 */
public class EncryptionEngine {
    private Cipher cipher;
    private KeyPair keyPair;

    public EncryptionEngine(){
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            // make our keypair and cipher
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            keyPair = kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (NoSuchPaddingException e){
            e.printStackTrace();
        }
    }

    synchronized public String decrypt(byte[] encodedBytes){
        if (encodedBytes == null){
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decodedBytes = cipher.doFinal(encodedBytes);
            return new String(decodedBytes);
        } catch (InvalidKeyException e){
            e.printStackTrace();
        } catch (IllegalBlockSizeException e){
            e.printStackTrace();
        } catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    synchronized public byte[] encrypt(PublicKey publicKey, String message){
        return encrypt(publicKey, message.getBytes());
    }

    synchronized public byte[] encrypt(PublicKey publicKey, byte[] bytes){
        if (bytes == null){
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encodedBytes = cipher.doFinal(bytes);
            return encodedBytes;
        } catch (InvalidKeyException e){
            e.printStackTrace();
        } catch (IllegalBlockSizeException e){
            e.printStackTrace();
        } catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    synchronized public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }
}
