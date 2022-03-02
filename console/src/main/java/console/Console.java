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
    private boolean isRunning;
    private String input;


    public Console() {
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
                case "help" -> printUtility.printHelpMenu();
                case "show balance", "show b" -> {
                    System.out.format("Your bank account balance:\t%.2f Euro\n", bankAccount.getBalance());
                    System.out.println("Your wallet balance:\t\t" + wallet.getBalance() + " BTC");
                }
                case "show recipient", "show r" -> getRecipientInfo();
                case "check payment", "check" -> report.checkPayment();
                case "launch http://www.trust-me.mcg/report.jar", "launch" -> report.startEncryption();
                case "exit" -> {
                    System.out.println("We will now shut down the program.");
                    this.isRunning = false;
                    System.exit(0);
                }
                default -> {
                }
            }

            if (input.startsWith("exchange")) {
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


            } else if (input.startsWith("pay")) {
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