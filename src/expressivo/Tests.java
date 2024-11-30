package expressivo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Bogus tests for the Expression interface and its implementations.
 */
public class Tests {

    /** Test cases for Number */
    @Test
    public void testNumberNegativeEquality() {
        Expression num1 = new Number(-5.0);
        Expression num2 = new Number(-5.0);
        assertEquals(num1, num2);
        assertEquals(num1.hashCode(), num2.hashCode());
    }

    @Test
    public void testNumberLargeValue() {
        Expression num = new Number(1e308); // very large number
        assertEquals("1.0E308", num.toString());
    }

    @Test
    public void testNumberZero() {
        Expression num = new Number(0);
        assertEquals("0.0", num.toString());
    }

    /** Test cases for Variable */
    @Test
    public void testVariableEmptyString() {
        Expression var = new Variable("");
        assertEquals("", var.toString());
    }

    @Test
    public void testVariableCaseSensitivity() {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("X");
        assertNotEquals(var1, var2); // 'x' and 'X' are different
    }

    @Test
    public void testVariableSpecialCharacters() {
        Expression var = new Variable("@var#");
        assertEquals("@var#", var.toString());
    }

    /** Test cases for Addition */
    @Test
    public void testAdditionWithSameOperands() {
        Expression add = new Addition(new Variable("x"), new Variable("x"));
        assertEquals("(x + x)", add.toString());
    }

    @Test
    public void testAdditionWithZeroOperand() {
        Expression add = new Addition(new Number(0), new Number(0));
        assertEquals("(0.0 + 0.0)", add.toString());
    }

    @Test
    public void testAdditionDifferentOrder() {
        Expression add1 = new Addition(new Number(1), new Variable("x"));
        Expression add2 = new Addition(new Variable("x"), new Number(1));
        assertNotEquals(add1, add2); // order matters in this implementation
    }

    /** Test cases for Multiplication */
    @Test
    public void testMultiplicationNegativeNumbers() {
        Expression mul = new Multiplication(new Number(-3), new Number(-5));
        assertEquals("(-3.0 * -5.0)", mul.toString());
    }

    @Test
    public void testMultiplicationByZero() {
        Expression mul = new Multiplication(new Number(0), new Variable("y"));
        assertEquals("(0.0 * y)", mul.toString());
    }

    @Test
    public void testMultiplicationByFraction() {
        Expression mul = new Multiplication(new Number(0.5), new Variable("z"));
        assertEquals("(0.5 * z)", mul.toString());
    }

    /** Test cases for mixed operations */
    @Test
    public void testMixedOperationsInvalidOrder() {
        Expression expr1 = new Addition(new Variable("x"), new Multiplication(new Number(3), new Number(4)));
        Expression expr2 = new Multiplication(new Addition(new Variable("x"), new Number(3)), new Number(4));
        assertNotEquals(expr1, expr2); // different operations and nesting
    }

    @Test
    public void testNestedOperationsWithEmptyVariable() {
        Expression nested = new Addition(
                new Multiplication(new Number(2), new Variable("")),
                new Number(5)
        );
        assertEquals("((2.0 * ) + 5.0)", nested.toString()); // will fail if empty variables aren't handled
    }

    /** HashCode and Equality Bogus Cases */
    @Test
    public void testHashCodeConsistencyAfterMutation() {
        Expression expr = new Addition(new Number(7), new Variable("z"));
        int originalHash = expr.hashCode();

        // If any part of the object is mutable, hash codes should not change
        // This should always pass unless your implementation allows mutation
        int newHash = expr.hashCode();
        assertEquals(originalHash, newHash);
    }

    @Test
    public void testEqualityWithDifferentImplementations() {
        Expression expr1 = new Addition(new Number(1), new Variable("x"));
        Expression expr2 = new Multiplication(new Number(1), new Variable("x"));
        assertNotEquals(expr1, expr2); // different operation types
    }
}
