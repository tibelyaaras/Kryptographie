package bank;

import java.util.Random;

public class Bank {


    public static BankAccount generateBankAccount(String owner, double balance) {
        return new BankAccount(owner, Bank.generateIBAN(), balance);
    }


    static String generateIBAN() {
        Random rand = new Random();
        StringBuilder card = new StringBuilder("DE");

        for (int i = 0; i < 14; i++) {
            int n = rand.nextInt(10);
            card.append(n);
        }

        return String.valueOf(card);
    }
}
