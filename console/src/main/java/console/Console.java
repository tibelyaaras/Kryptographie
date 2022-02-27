package console;

import java.util.Scanner;

public class Console {
    private final ConsolePrintUtility printUtility;
    //private Report report;


    private boolean isRunning;
    private String input;

    public Console() {
        this.printUtility = new ConsolePrintUtility();
        //this.report = new Report();

        printUtility.printInitApplication();
        this.isRunning = true;

        while(isRunning) {
            this.input = handleInput();

            switch (input) {
                case "help":
                    printUtility.printHelpMenu();
                    break;
                case "show balance":
                case "show b":
                    break;
                case "show recipient":
                case "show r":
                    break;
                case "check payment":
                case "check":
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
                    System.out.println("Did you make a typing error?");
                    System.out.println("Please write more carefully or else I cant understand you");
                    break;
            }

            if (input.startsWith("exchange")) {

            } else if (input.startsWith("pay")) {

            }
        }
    }

    private String handleInput() {
        Scanner in = new Scanner(System.in);
        return in.next();
    }


}
