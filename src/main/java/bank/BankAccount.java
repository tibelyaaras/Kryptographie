package bank;

public class BankAccount extends Bank{
    private String owner;
    private String iban;
    private double balance;

    public BankAccount(String owner, String iban, double balance) {
        this.owner = owner;
        this.iban = iban;
        this.balance = balance;
    }



    public String getOwner() {
        return owner;
    }

    public String getIban() {
        return iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
