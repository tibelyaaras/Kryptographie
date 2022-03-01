package console;

import bank.Bank;
import bank.BankAccount;
import blockchain.Network;
import blockchain.StringUtility;
import blockchain.Wallet;
import report.Report;

import java.security.PublicKey;
import java.util.Scanner;

public class Console {
    // Victim
    private final BankAccount bankAccount;
    private final Wallet wallet;
    // Console Application
    private final ConsolePrintUtility printUtility;
    private final Report report;
    // Bitcoin Transactions
    private final Network network;
    private boolean isRunning;
    private String input;


    public Console() {
        this.printUtility = new ConsolePrintUtility();
        this.report = new Report();

        // Victim
        this.bankAccount = Bank.generateBankAccount("Clue Less", 5000);
        this.wallet = new Wallet();

        // Bitcoin Transactions
        this.network = Network.getInstance();

        // Console Application
        printUtility.printInitApplication();

        while (!isRunning) {
            this.input = handleInput();

            if (input.equals("help")) {
                this.isRunning = true;
                printUtility.printHelpMenu();
                try {
                    initBlackmail();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initBlackmail() throws Exception {

        while (isRunning) {
            this.input = handleInput();

            switch (input) {
                case "help":
                    printUtility.printHelpMenu();
                    break;
                case "show balance":
                case "show b":
                    System.out.println("Your bank account balance:\t" + bankAccount.getBalance() + " Euro");
                    System.out.println("Your wallet balance:\t\t" + wallet.getBalance() + " BTC");
                    break;
                case "show recipient":
                case "show r":
                    getRecipientInfo();
                    break;
                case "check payment":
                case "check":
                    report.checkPayment();
                    break;
                case "launch http://www.trust-me.mcg/report.jar":
                case "launch":
                    report.startEncryption();
                    break;
                case "exit":
                    System.out.println("We will now shut down the program.");
                    this.isRunning = false;
                    break;
                default:
                    break;
            }


            if (input.startsWith("exchange")) {
                String inpu = input.replace(",", ".");
                String inp = inpu.replace("exchange", "");
                String in = inp.replace("BTC", "");

                double exchange = Double.parseDouble(in);

                exchangeBTC(exchange);


            } else if (input.startsWith("pay")) {
                String inpu = input.replace("pay ", "");
                String inp = inpu.replace("BTC", "");
                String in = inp.replace("to", "");

                String[] data = in.split("\\s+");

                if (Double.parseDouble(data[0]) >= report.getRansomAmount()) {
                    if (wallet.getBalance() >= report.getRansomAmount()) {
                        payBTC(Double.parseDouble(data[0]), data[1]);
                    }
                }
            }
        }
    }


    private void exchangeBTC(double amount) {
        double currentBalance = this.bankAccount.getBalance();
        double exchangeInEuro = amount / 0.000019;

        if (currentBalance >= exchangeInEuro) {
            this.bankAccount.setBalance(currentBalance - exchangeInEuro);
            this.network.buyBitcoin(this.wallet, amount);
        }
    }


    private void payBTC(double amount, String recipient) {
        if (recipient.equals(StringUtility.getStringFromKey(report.getWallet().getPublicKey()))) {
            Network.getInstance().addTransaction(wallet.sendFunds((PublicKey) StringUtility.getKeyFromString(recipient), amount));
        }
    }


    private void getRecipientInfo() {
        System.out.println("Recipient:\t" + StringUtility.getStringFromKey(report.getWallet().getPublicKey()));
    }


    private String handleInput() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }
}