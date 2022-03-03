package blockchain;

public class Miner {
    private final String name;
    private final Wallet wallet;

    public Miner(String name) {
        this.name = name;
        this.wallet = new Wallet();
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getName() {
        return name;
    }
}
