package me.rkaehdaos.cleancodelearningsuccessiverefinement;

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
    void testWithNoSchemaButWithMultipleArguments() throws Exception {
        try {
            Args args = new Args("", new String[]{"-x", "-y"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.UNEXPECTED_ARGUMENT);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
        }
    }

    @Test
    void testNonLetterSchema() throws Exception {
        try {
            Args args = new Args("*", new String[]{});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_ARGUMENT_NAME);
            assertThat(e.getErrorArgumentId()).isEqualTo('*');
        }
    }

    @Test
    void testInvalidArgumentFormat() throws Exception {
        try {
            Args args = new Args("f~", new String[]{});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_FORMAT);
            assertThat(e.getErrorArgumentId()).isEqualTo('f');
        }
    }

    @Test
    void testSimpleBooleanPresent() throws Exception {
        //given
        Args args = new Args("x", new String[]{"-x"});

        //then
        assertThat(args.cardinality()).isEqualTo(1);
        assertThat(args.getBoolean('x')).isTrue();
    }

    @Test
    void testSimpleStringPresent() throws Exception {
        //given
        Args args = new Args("x*", new String[]{"-x", "param"});

        //then
        assertThat(args.cardinality()).isEqualTo(1);
        assertThat(args.has('x')).isTrue();
        assertThat(args.getString('x')).isEqualTo("param");
    }


    @Test
    void testMissingStringArgument() throws Exception {
        try {
            Args args = new Args("x*", new String[]{"-x"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_STRING);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
        }
    }

    @Test
    void testSpaceInFormat() throws Exception {
        //given
        Args args = new Args("x, y", new String[]{"-xy"});

        //then
        assertThat(args.cardinality()).isEqualTo(2);
        assertThat(args.has('x')).isTrue();
        assertThat(args.has('y')).isTrue();
    }

    @Test
    void testSimpleIntPresent() throws Exception {
        //given
        Args args = new Args("x#", new String[]{"-x", "42"});

        //then
        assertThat(args.cardinality()).isEqualTo(1);
        assertThat(args.has('x'));
        assertThat(args.getInt('x')).isEqualTo(42);
    }

    @Test
    void testInvalidInteger() throws Exception {
        //given
        try {
            Args args = new Args("x#", new String[]{"-x", "forty two"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.INVALID_INTEGER);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
            assertThat(e.getErrorParameter()).isEqualTo("forty two");
        }
    }

    @Test
    void testMissingInteger() throws Exception {
        //given
        try {
            Args args = new Args("x#", new String[]{"-x"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_INTEGER);
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

    @Test
    void testMissingDouble() throws Exception {
        //given
        try {
            Args args = new Args("x##", new String[]{"-x"});
            fail("Arg 생성자에서 예외가 떨어져야 함");
        } catch (ArgsException e) {
            assertThat(e.getErrorCode()).isEqualTo(ArgsException.ErrorCode.MISSING_DOUBLE);
            assertThat(e.getErrorArgumentId()).isEqualTo('x');
        }
    }

}