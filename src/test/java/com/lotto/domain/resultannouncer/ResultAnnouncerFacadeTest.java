package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResponseDto;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.ResultDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static com.lotto.domain.resultannouncer.MessageResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultAnnouncerFacadeTest {

    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void it_should_return_response_with_lose_message_if_ticket_is_not_winning_ticket(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 9, 9, 21, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);
    }

    @Test
    public void it_should_return_response_with_win_message_if_ticket_is_winning_ticket(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 9, 9, 21, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);
    }

    @Test
    public void it_should_return_response_with_wait_message_if_date_is_before_announcement_time(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 9, 9, 21, 0, 0);
        String hash = "123";
        Clock clock = Clock.fixed(LocalDateTime.of(2023, 9, 8, 21, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 8, 9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 8, 9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);
    }

    @Test
    public void it_should_return_response_with_hash_does_not_exist_message_if_hash_does_not_exist(){
        //given
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());

        when(resultCheckerFacade.findByHash(hash)).thenReturn(null);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);
    }

    @Test
    public void it_should_return_response_with_hash_does_not_exist_if_response_is_not_saved_to_db_yet(){
        //given
        LocalDateTime drawDate = LocalDateTime.of(2023, 9, 9, 21, 0, 0);
        String hash = "123";
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 8, 9))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto1 = resultAnnouncerFacade.checkResult(hash);
        String underTest = resultAnnouncerResponseDto1.responseDto().hash();
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(underTest);
        //then

        ResultAnnouncerResponseDto expectResult = new ResultAnnouncerResponseDto(resultAnnouncerResponseDto.responseDto(), ALREADY_CHECKED.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectResult);
    }

}