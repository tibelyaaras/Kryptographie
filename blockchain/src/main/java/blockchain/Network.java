package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Network {
    private static Network instance;

    private HashMap<String, TransactionOutput> utx0Map = new HashMap<>();
    private int transactionSequence;
    private ArrayList<Block> blockchain = new ArrayList<>();
    private List<Miner> miners = new ArrayList<>();
    private Transaction genesisTransaction;
    private Wallet satoshiNakamoto;
    private Block previousBlock;

    public Network() {
        this.instance=this;

        this.transactionSequence=0;

        this.miners.add(new Miner("Bob"));
        this.miners.add(new Miner("Eve"));
        this.miners.add(new Miner("Sam"));

        this.satoshiNakamoto=new Wallet();

        this.genesisTransaction=new Transaction(satoshiNakamoto.getPublicKey(),satoshiNakamoto.getPublicKey(),1,null);
        this.genesisTransaction.generateSignature(satoshiNakamoto.getPrivateKey());
        this.genesisTransaction.setId("0");
        this.genesisTransaction.getOutputs().add(
                new TransactionOutput(
                        this.genesisTransaction.getRecipient(),
                        this.genesisTransaction.getValue(),
                        this.genesisTransaction.getId()
                        )
        );
        this.utx0Map.put(
                this.genesisTransaction.getOutputs().get(0).getID(),
                this.genesisTransaction.getOutputs().get(0)
        );
        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(this.genesisTransaction);
        addBlock(genesisBlock);
    }

    public void buyBitcoin(Wallet reciverWallet,double amount){
        Transaction bitcoinSend=this.satoshiNakamoto.sendFunds(reciverWallet.getPublicKey(),amount);
        Block nBlock=new Block(this.previousBlock.getHash());
        nBlock.addTransaction(bitcoinSend);
        this.addBlock(nBlock);
    }

    private void addBlock(Block block){
        this.blockchain.add(block);
        this.previousBlock = block;
    }

    public static Network getInstance() {
        if (instance == null) instance = new Network();
        return instance;
    }

    public HashMap<String, TransactionOutput> getUtx0Map() {
        return this.utx0Map;
    }

    public int getTransactionSequence(){
        return transactionSequence;
    }

    public void incrementTransactionSequence(){
        transactionSequence++;
    }

}
