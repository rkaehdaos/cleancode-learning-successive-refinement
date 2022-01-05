package me.rkaehdaos.cleancodelearningsuccessiverefinement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;


class ArgsTest {
    public static final Logger log = LoggerFactory.getLogger(ArgsTest.class);

    @Test
    @DisplayName("스키마, 아규먼트 없는 테스트 생성")
    void testCreateWithNoSchemaOrArguments() throws Exception {
        // given
        Args args = new Args("", new String[0]);
        // then
        assertThat(args.cardinality()).isEqualTo(0);
    }

    @Test
    @DisplayName("스키마없이 1개 인자")
    void testWithNoSchemaButWithOneArgument() throws Exception {
        try {
            Args args = new Args("", new String[]{"-x"});
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
        }
    }

}