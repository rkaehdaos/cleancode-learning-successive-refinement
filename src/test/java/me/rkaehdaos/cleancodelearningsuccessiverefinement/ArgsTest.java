package me.rkaehdaos.cleancodelearningsuccessiverefinement;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;


class ArgsTest {
    public static final Logger log = LoggerFactory.getLogger(ArgsTest.class);

    @Test
    void testCreateWithNoSchemaOrArguments() throws Exception {
        // given
        Args args = new Args("", new String[0]);
        // then
        assertThat(args.cardinality()).isEqualTo(0);
    }

    @Test
    @Disabled
    void testWithNoSchemaButWithOneArgument() throws Exception {
        try {
            Args args = new Args("", new String[]{"-x"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
        }
    }


    @Test
    void testSimpleDoublePresent() throws Exception {
        //given
        Args args = new Args("x##", new String[]{"-x", "42.3"});

        //then

        assertThat(args.cardinality()).isEqualTo(1);
        assertThat(args.has('x'));
        assertThat(args.getDouble('x')).isEqualTo(42.3);
    }

    @Test
    void testInvalidDouble() throws Exception {
        //given
        try {
            Args args = new Args("x##", new String[]{"-x", "forty two"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_DOUBLE);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
            assertThat(e.getErrorParameter()).isEqualTo("forty two");
        }


    }

}