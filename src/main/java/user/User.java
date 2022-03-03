package user;

import bank.BankAccount;
import blockchain.Wallet;

public class User {
    private final String name;
    private final BankAccount bankAccount;
    private final Wallet wallet;

    public User(String name, BankAccount bankAccount, Wallet wallet) {
        this.name = name;
        this.bankAccount = bankAccount;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
