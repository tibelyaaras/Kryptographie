package user;

import bank.BankAccount;
import blockchain.Wallet;

public class User {
    protected final String name;
    protected final BankAccount bankAccount;
    protected final Wallet wallet;

    public User(String name, BankAccount bankAccount, Wallet wallet) {
        this.name = name;
        this.bankAccount = bankAccount;
        this.wallet = wallet;
    }

    public User (String name,Wallet wallet){
        this.name = name;
        this.bankAccount = null;
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
