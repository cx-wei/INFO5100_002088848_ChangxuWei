package Exercises.exercise6;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class CryptoExercise {
    
    public static void main(String[] args) throws Exception {
        // Create Alice and Bob
        Person alice = new Person("Alice");
        Person bob = new Person("Bob");
        
        System.out.println("=== Symmetric Encryption (AES-256 GCM) ===");
        symmetricEncryptionDemo(alice, bob);
        
        System.out.println("\n=== Asymmetric Encryption (RSA-2048) ===");
        asymmetricEncryptionDemo(alice, bob);
        
        System.out.println("\n=== Digital Signatures (RSA-2048) ===");
        signatureDemo(alice, bob);
    }
    
    // Symmetric encryption demo using AES-256 GCM
    public static void symmetricEncryptionDemo(Person sender, Person recipient) throws Exception {
        // Generate a shared secret key (in real life, this would be securely exchanged)
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256
        SecretKey secretKey = keyGen.generateKey();
        
        // Sender encrypts a message
        String originalMessage = "Hello Bob, this is a secret message from Alice!";
        byte[] iv = new byte[12]; // 96-bit Initialization Vector (IV) for GCM
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128-bit auth tag
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        
        byte[] cipherText = cipher.doFinal(originalMessage.getBytes(StandardCharsets.UTF_8));
        
        // Combine IV and ciphertext for transmission (IV doesn't need to be secret)
        byte[] ivAndCipherText = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
        System.arraycopy(cipherText, 0, ivAndCipherText, iv.length, cipherText.length);
        
        System.out.println(sender.name + " sends encrypted message: " + 
                           Base64.getEncoder().encodeToString(ivAndCipherText));
        
        // Recipient decrypts the message
        byte[] receivedIv = new byte[12];
        byte[] receivedCipherText = new byte[ivAndCipherText.length - 12];
        System.arraycopy(ivAndCipherText, 0, receivedIv, 0, 12);
        System.arraycopy(ivAndCipherText, 12, receivedCipherText, 0, receivedCipherText.length);
        
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
        spec = new GCMParameterSpec(128, receivedIv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        
        byte[] decryptedText = cipher.doFinal(receivedCipherText);
        String decryptedMessage = new String(decryptedText, StandardCharsets.UTF_8);
        
        System.out.println(recipient.name + " received and decrypted: " + decryptedMessage);
    }
    
    // Asymmetric encryption demo using RSA-2048
    public static void asymmetricEncryptionDemo(Person sender, Person recipient) throws Exception {
        // Generate key pairs if not already exists
        if (sender.keyPair == null) {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            sender.keyPair = keyGen.generateKeyPair();
        }
        
        if (recipient.keyPair == null) {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            recipient.keyPair = keyGen.generateKeyPair();
        }
        
        // Sender encrypts a message with recipient's public key
        String originalMessage = "Hello Bob, this is an RSA encrypted message!";
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, recipient.keyPair.getPublic());
        
        byte[] cipherText = cipher.doFinal(originalMessage.getBytes(StandardCharsets.UTF_8));
        
        System.out.println(sender.name + " sends RSA encrypted message: " + 
                           Base64.getEncoder().encodeToString(cipherText));
        
        // Recipient decrypts the message with their private key
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, recipient.keyPair.getPrivate());
        
        byte[] decryptedText = cipher.doFinal(cipherText);
        String decryptedMessage = new String(decryptedText, StandardCharsets.UTF_8);
        
        System.out.println(recipient.name + " received and decrypted: " + decryptedMessage);
    }
    
    // Digital signature demo using RSA-2048
    public static void signatureDemo(Person sender, Person recipient) throws Exception {
        // Generate key pairs if not already exists
        if (sender.keyPair == null) {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            sender.keyPair = keyGen.generateKeyPair();
        }
        
        if (recipient.keyPair == null) {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            recipient.keyPair = keyGen.generateKeyPair();
        }
        
        // Sender signs a message
        String message = "Hello Bob, please verify this signed message from Alice.";
        
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(sender.keyPair.getPrivate());
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        
        byte[] digitalSignature = signature.sign();
        
        System.out.println(sender.name + " sends message: " + message);
        System.out.println("Signature: " + Base64.getEncoder().encodeToString(digitalSignature));
        
        // Recipient verifies the signature
        signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(sender.keyPair.getPublic());
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        
        boolean isVerified = signature.verify(digitalSignature);
        
        System.out.println(recipient.name + " verifies signature: " + 
                          (isVerified ? "VALID" : "INVALID"));
    }
    
    static class Person {
        String name;
        KeyPair keyPair;
        
        public Person(String name) {
            this.name = name;
        }
    }
}