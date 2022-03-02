package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static blockchain.StringUtility.blueOutput;

public class Block {
    private final String previousHash;
    private final long timeStamp;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private String merkleRoot;
    private String hash;
    private int nonce;
    private PublicKey miner;
    private double reward;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String calculateHash() {
        return StringUtility.applySha256(previousHash + timeStamp + nonce + merkleRoot + StringUtility.getStringFromKey(miner) + reward);
    }

    public void mineBlock(int difficulty, Miner miner) {
        this.miner = miner.getWallet().getPublicKey();
        this.reward = Configuration.instance.reward;

        TransactionOutput reward = new TransactionOutput(miner.getWallet().getPublicKey(), this.reward, "BlockReward-" + merkleRoot + "-" + previousHash);

        merkleRoot = StringUtility.getMerkleRoot(transactions);
        String target = StringUtility.getDifficultyString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        Network.getInstance().getUtx0Map().put(reward.getID(), reward);

        System.out.printf(blueOutput("new block [%s] was mined by Miner %s%n"), hash, miner.getName());
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        if (!Objects.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                System.out.println("transaction failed to process");
                return;
            }
        }

        transactions.add(transaction);
        System.out.println("transaction added to block");
    }
}