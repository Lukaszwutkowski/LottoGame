package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(NumberGeneratorFacade generatorFacade, NumberReceiverFacade receiverFacade, PlayerRepository playerRepository){
        WinnersRetriever winnerGenerator = new WinnersRetriever();
        return new ResultCheckerFacade(generatorFacade, receiverFacade, playerRepository, winnerGenerator);
    }
}
