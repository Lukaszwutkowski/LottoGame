package com.lotto.domain.numberreceiver;

import static org.junit.jupiter.api.Assertions.*;

public class HashGeneratorTestImpl implements HashGenerable {

    private final String hash;

    HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    public HashGeneratorTestImpl() {
        hash = "123";
    }

    @Override
    public  String getHash() {
        return hash;
    }

}