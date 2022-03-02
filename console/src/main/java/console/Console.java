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
    private final Network network;
    // Bitcoin Transactions
    private boolean isRunning;
    private String input;


    public Console() {

        this.network = new Network();
        this.printUtility = new ConsolePrintUtility();
        this.report = new Report();

        // Victim
        this.bankAccount = Bank.generateBankAccount("Clue Less", 5000);
        this.wallet = new Wallet();


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
                    System.out.format("Your bank account balance:\t%.2f Euro\n", bankAccount.getBalance());
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
                input = input.replace(",", ".");
                input = input.replace("exchange", "");

                if (input.contains("BTC"))
                    input = input.replace("BTC", "");

                double exchange = Double.parseDouble(input);

                exchangeBTC(exchange);


            } else if (input.startsWith("pay")) {
                input = input.replace("pay ", "");
                input = input.replace("BTC", "");
                input = input.replace("to", "");

                String[] data = input.split("\\s+");

                payBTC(Double.parseDouble(data[0]), data[1]);
            }
        }
    }


    private void exchangeBTC(double amount) {
        double currentBalance = this.bankAccount.getBalance();
        double exchangeInEuro = amount / 0.000019;

        if (currentBalance >= exchangeInEuro) {
            this.bankAccount.setBalance(currentBalance - exchangeInEuro);
            Network.getInstance().buyBitcoin(this.wallet, amount);
        }
    }


    private void payBTC(double amount, String recipient) {
        if (recipient.equals(StringUtility.getStringFromKey(report.getWallet().getPublicKey()))) {
            Network.getInstance().addTransaction(this.wallet.sendFunds((PublicKey) StringUtility.getKeyFromString(recipient), amount));
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