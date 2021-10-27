package ru.n_korotkov.oop.calculator;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CalculatorTest {

    private static Calculator calculator;

    @BeforeAll
    static void initCalculator() {
        calculator = new Calculator();
    }

    static Stream<Arguments> expressionProvider() {
        return Stream.of(
            arguments("0", 0.0),
            arguments("   0   ", 0.0),
            arguments("0.239", 0.239),
            arguments("+0.57", 0.57),
            arguments("-0.57", -0.57),
            arguments("5.7e5", 5.7e5),
            arguments("-6.4e-10", -6.4e-10),
            arguments("sin + - 1 2 1", 0.0),
            arguments("     sin  + -    1 2  1        ", 0.0),
            arguments("+ 1 + 1 + 1 + 1 + 1 + 1 1", 7.0),
            arguments("sqrt 2", Math.sqrt(2.0)),
            arguments(
                "pow + log 6 / 3 5 sin cos sqrt 3",
                Math.pow(Math.log(6.0) + 3.0 / 5.0, Math.sin(Math.cos(Math.sqrt(3.0))))
            ),
            arguments("/  5 0",  Double.POSITIVE_INFINITY),
            arguments("/ -5 0",  Double.NEGATIVE_INFINITY),
            arguments("/  0 0",  Double.NaN),
            arguments("sqrt -5", Double.NaN),
            arguments("log  0",  Double.NEGATIVE_INFINITY),
            arguments("log -5",  Double.NaN),
            arguments("pow -0.6 1.5", Double.NaN)
        );
    }

    static Stream<Arguments> invalidExpressionProvider() {
        return Stream.of(
            arguments(""),
            arguments("    "),
            arguments("+"),
            arguments("+ +"),
            arguments("+ 1"),
            arguments("1 +"),
            arguments("1 2"),
            arguments("+ - 1 2 3 4"),
            arguments("1 + 2 - 3 + 4"),
            arguments("+ 1 2 - 3 + 4"),
            arguments("+ - 1 2 3 + 4"),
            arguments("nonExistingOp"),
            arguments("+ 3 nonExistingOp 2 1")
        );
    }

    @ParameterizedTest
    @MethodSource("expressionProvider")
    void calculatorEvaluateTest(String expr, double expectedResult) {
        assertEquals(expectedResult, calculator.evaluate(expr));
    }

    @ParameterizedTest
    @MethodSource("invalidExpressionProvider")
    void invalidExpressionEvaluateThrows(String expr) {
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate(expr));
    }

}
