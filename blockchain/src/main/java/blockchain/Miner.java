package blockchain;

public class Miner {
    private String name;
    private Wallet wallet;

    public Miner (String name){
        this.name=name;
        this.wallet=new Wallet();
    }
}
