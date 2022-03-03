package console;

public class ConsolePrintUtility {

    public void printInitApplication() {
        System.out.println("+ + + + + + + + + + + + + + + + + + + + + + + + +");
        System.out.println("+\t\t\t\t\t\t\t\t\t\t\t\t+");
        System.out.println("+\tNice to meet you, Mr. Clue Less.\t\t\t+");
        System.out.println("+\tI hope you have a wonderful day!\t\t\t+");
        System.out.println("+\t\t\t\t\t\t\t\t\t\t\t\t+");
        System.out.println("+\tWe have a little surprise prepared for you.\t+");
        System.out.println("+\tWrite 'help' to see!\t\t\t\t\t\t+");
        System.out.println("+\t\t\t\t\t\t\t\t\t\t\t\t+");
        System.out.println("+ + + + + + + + + + + + + + + + + + + + + + + + +");
    }

    public void printHelpMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("- show balance");
        System.out.println("- show recipient");
        System.out.println("- exchange [amount] BTC");
        System.out.println("- pay [amount] BTC to [address]");
        System.out.println("- check payment");
        System.out.println("- launch http://www.trust-me.mcg/report.jar");
        System.out.println("- exit");
        System.out.println("-------------------------------------------------");
    }
}
