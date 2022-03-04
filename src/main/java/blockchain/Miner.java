package blockchain;

import user.User;

public class Miner extends User {

    public Miner(String name) {
        super(name, new Wallet());
    }
}