package console;

import bank.Bank;
import bank.BankAccount;
import blockchain.Wallet;
import report.Report;

import java.util.Scanner;

public class Console {
    private Report report;

    // Victim
    private final BankAccount bankAccount;
    private final Wallet wallet;

    // Console Application
    private final ConsolePrintUtility printUtility;
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
                    break;
                case "check payment":
                case "check":
                    report.checkPayment();
                    break;
                case "launch http://www.trust-me.mcg/report.jar":
                case "launch":
                    //report.startEncryption();
                    break;
                case "exit":
                    System.out.println("We will now shut down the program.");
                    this.isRunning = false;
                    break;
                default:
                    break;
            }


            if (input.startsWith("exchange")) {
                double exchange = Double.parseDouble(input.split("\\s+")[1]);

                double currentBalance = this.bankAccount.getBalance();

                double exchangeInEuro = exchange / 0.000019;

                if(currentBalance >= exchangeInEuro) {
                    this.bankAccount.setBalance(currentBalance - exchangeInEuro);
                }

            }
            else if (input.startsWith("pay")) {
                double ransomAmount = report.getRansomAmount();
            }
        }
    }


    private String handleInput() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }
}