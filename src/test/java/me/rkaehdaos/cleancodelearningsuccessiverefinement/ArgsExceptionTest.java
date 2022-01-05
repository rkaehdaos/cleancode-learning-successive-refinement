package me.rkaehdaos.cleancodelearningsuccessiverefinement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ArgsExceptionTest {
    @Test()
    void testOKMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.OK);

        //then
        assertThatThrownBy(() -> e.errorMessage())
                .isInstanceOf(Exception.class)
                .hasMessage("TILT: Should not get here.");
    }


    @Test
    void testUnexpectedMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("인자 -x 예상치 못함.");
    }

    @Test
    void testMissingStringMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.MISSING_STRING, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("문자열 파라미터 -x를 찾을 수 없음.");
    }

    @Test
    void testInvalidIntegerMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.INVALID_INTEGER, 'x', "forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x는 정수를 기대했지만 forty two.");
    }

    @Test
    void testMissingIntegerMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.MISSING_INTEGER, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("정수형 파라미터 -x를 찾을 수 없음.");
    }

    @Test
    void testInvalidDoubleMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.INVALID_DOUBLE, 'x', "forty two");

        //then
        assertThat(e.errorMessage()).isEqualTo("Argument -x는 double을 기대했지만 forty two.");
    }

    @Test
    void testMissingDoubleMessage() throws Exception {
        //given
        ArgsException e = new ArgsException(ArgsException.ErrorCode.MISSING_DOUBLE, 'x', null);

        //then
        assertThat(e.errorMessage()).isEqualTo("double 파라미터 -x를 찾을 수 없음.");
    }

}