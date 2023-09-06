package com.lotto.domain.numbergenerator;

import java.util.Set;

public class NumberGeneratorTestImpl implements RandomNumberGenerable {
    private final Set<Integer> generatedNumbers;

    NumberGeneratorTestImpl() {
        generatedNumbers = Set.of(1,2,3,4,5,6);
    }

    NumberGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public Set<Integer> generateSixRandomNumbers() {
        return generatedNumbers;
    }
}
