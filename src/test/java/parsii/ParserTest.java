/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii;

import org.junit.Assert;
import org.junit.Test;
import parsii.eval.*;
import parsii.tokenizer.ParseException;

import java.util.List;


/**
 * Tests the {@link Parser} class.
 *
 * @author Andreas Haufler (aha@scireum.de)
 * @since 2013/09
 */
public class ParserTest {
    private static final Parser p;
    static {
        p = new Parser();
    }
    @Test
    public void simple() throws ParseException {
        Assert.assertEquals(-109d, p.parse("1 - (10 - -100)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.01d, p.parse("1 / 10 * 10 / 100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-89d, p.parse("1 + 10 - 100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(91d, p.parse("1 - 10 - -100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(91d, p.parse("1 - 10  + 100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-109d, p.parse("1 - (10 + 100)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-89d, p.parse("1 + (10 - 100)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(100d, p.parse("1 / 1 * 100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.01d, p.parse("1 / (1 * 100)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.01d, p.parse("1 * 1 / 100").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(7d, p.parse("3+4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(7d, p.parse("3      +    4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-1d, p.parse("3+ -4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-1d, p.parse("3+(-4)").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void number() throws ParseException {
        Assert.assertEquals(4003.333333d, p.parse("3.333_333+4_000").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.03, p.parse("3e-2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(300d, p.parse("3e2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(300d, p.parse("3e+2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(320d, p.parse("3.2e2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.032, p.parse("3.2e-2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.03, p.parse("3E-2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(300d, p.parse("3E2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(300d, p.parse("3E+2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(320d, p.parse("3.2E2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.032, p.parse("3.2E-2").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void precedence() throws ParseException {
        // term vs. product
        Assert.assertEquals(19d, p.parse("3+4*4").evaluate(), BinaryOperation.EPSILON);
        // product vs. power
        Assert.assertEquals(20.25d, p.parse("3^4/4").evaluate(), BinaryOperation.EPSILON);
        // relation vs. product
        Assert.assertEquals(1d, p.parse("3 < 4*4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0d, p.parse("3 > 4*4").evaluate(), BinaryOperation.EPSILON);
        // brackets
        Assert.assertEquals(28d, p.parse("(3 + 4) * 4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(304d, p.parse("3e2 + 4").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1200d, p.parse("3e2 * 4").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void signed() throws ParseException {
        Assert.assertEquals(-2.02, p.parse("-2.02").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(2.02, p.parse("+2.02").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1.01, p.parse("+2.02 + -1.01").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(-4.03, p.parse("-2.02 - +2.01").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(3.03, p.parse("+2.02 + +1.01").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void blockComment() throws ParseException {
        Assert.assertEquals(29, p.parse("27+ /*xxx*/ 2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(29, p.parse("27+/*xxx*/ 2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(29, p.parse("27/*xxx*/+2").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void startingWithDecimalPoint() throws ParseException {
        Assert.assertEquals(.2, p.parse(".2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(.2, p.parse("+.2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(.4, p.parse(".2+.2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(.4, p.parse(".6+-.2").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void signedParentheses() throws ParseException {
        Assert.assertEquals(0.2, p.parse("-(-0.2)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1.2, p.parse("1-(-0.2)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0.8, p.parse("1+(-0.2)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(2.2, p.parse("+(2.2)").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void trailingDecimalPoint() throws ParseException {
        Assert.assertEquals(2., p.parse("2.").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void signedValueAfterOperand() throws ParseException {
        Assert.assertEquals(-1.2, p.parse("1+-2.2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(3.2, p.parse("1++2.2").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(6 * -1.1, p.parse("6*-1.1").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(6 * 1.1, p.parse("6*+1.1").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void variables() throws ParseException {
        Scope scope = new Scope();
        
        Variable   a    = scope.create("a");
        Variable   b    = scope.create("b");
        Expression expr = p.parse("3*a + 4 * b", scope);
        Assert.assertEquals(0d, expr.evaluate(), BinaryOperation.EPSILON);
        a.setValue(2);
        Assert.assertEquals(6d, expr.evaluate(), BinaryOperation.EPSILON);
        b.setValue(3);
        Assert.assertEquals(18d, expr.evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(18d, expr.evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void functions() throws ParseException {
        Assert.assertEquals(0d, p.parse("1 + sin(-pi) + cos(pi)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(4.72038341576d, p.parse("tan(sqrt(euler ^ (pi * 3)))").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(3d, p.parse("| 3 - 6 |").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(3d, p.parse("if(3 > 2 && 2 < 3, 2+1, 1+1)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(2d, p.parse("if(3 < 2 || 2 > 3, 2+1, 1+1)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(2d, p.parse("min(3,2)").evaluate(), BinaryOperation.EPSILON);
        
        
        // Test a var arg method...
        p.registerFunction("avg", new Function() {
            @Override
            public int getNumberOfArguments() {
                return -1;
            }
            
            @Override
            public double eval(List<Expression> args) {
                double avg = 0;
                if (args.isEmpty()) {
                    return avg;
                }
                for (Expression e : args) {
                    avg += e.evaluate();
                }
                return avg / args.size();
            }
            
            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Assert.assertEquals(3.25d, p.parse("avg(3,2,1,7)").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void multiInstance() throws ParseException {
        Parser p2 = new Parser();
        p2.registerFunction("avg", new Function() {
            @Override
            public int getNumberOfArguments() {
                return -1;
            }
            
            @Override
            public double eval(List<Expression> args) {
                double avg = 0;
                if (args.isEmpty()) {
                    return avg;
                }
                for (Expression e : args) {
                    avg += e.evaluate();
                }
                return avg / args.size();
            }
            
            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Parser p3 = new Parser();
        p3.registerFunction("avg", new Function() {
            @Override
            public int getNumberOfArguments() {
                return -1;
            }
            
            @Override
            public double eval(List<Expression> args) {
                double avg = 0;
                if (args.isEmpty()) {
                    return avg;
                }
                for (Expression e : args) {
                    avg += e.evaluate();
                }
                return avg;
            }
            
            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Assert.assertEquals(3.25d, p2.parse("avg(3,2,1,7)").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(13d, p3.parse("avg(3,2,1,7)").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void scopes() throws ParseException {
        Scope    root      = new Scope();
        Variable a         = root.getVariable("a").withValue(1);
        Scope    subScope1 = new Scope().withParent(root);
        Scope    subScope2 = new Scope().withParent(root);
        Variable b1        = subScope1.getVariable("b").withValue(2);
        Variable b2        = subScope2.getVariable("b").withValue(3);
        Variable c         = root.getVariable("c").withValue(4);
        Variable c1        = subScope1.getVariable("c").withValue(5);
        Assert.assertEquals(c, c1);
        Variable d  = root.getVariable("d").withValue(9);
        Variable d1 = subScope1.create("d").withValue(7);
        Assert.assertNotEquals(d, d1);
        Expression expr1 = p.parse("a + b + c + d", subScope1);
        Expression expr2 = p.parse("a + b + c + d", subScope2);
        Assert.assertEquals(15d, expr1.evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(18d, expr2.evaluate(), BinaryOperation.EPSILON);
        a.setValue(10);
        b1.setValue(20);
        b2.setValue(30);
        c.setValue(40);
        c1.setValue(50);
        Assert.assertEquals(87d, expr1.evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(99d, expr2.evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void errors() throws ParseException {
        // We expect the parser to continue after an recoverable error!
        try {
            p.parse("test(1 2)+sin(1,2)*34-34.45.45+");
            Assert.fail(); //Should never reach this.
        } catch (ParseException e) {
            Assert.assertEquals(5, e.getErrors().size());
        }
        
        // We expect the parser to report an invalid quantifier.
        try {
            p.parse("1x");
            Assert.fail(); //Should never reach this.
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
        
        // We expect the parser to report an unfinished expression
        try {
            p.parse("1(");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
        
        // We expect the parser to report an unexpected separator.
        try {
            p.parse("3ee3");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
        
        // We expect the parser to report an unexpected separator.
        try {
            p.parse("3e3.3");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
        
        // We expect the parser to report an unexpected token.
        try {
            p.parse("3e");
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
    }
    
    @Test
    public void relationalOperators() throws ParseException {
        // Test for Issue with >= and <= operators (#4)
        Assert.assertEquals(1d, p.parse("5 <= 5").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1d, p.parse("5 >= 5").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0d, p.parse("5 < 5").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(0d, p.parse("5 > 5").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void quantifiers() throws ParseException {
        Assert.assertEquals(1000d, p.parse("1K").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1000d, p.parse("1M * 1m").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1d, p.parse("1n * 1G").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1d, p.parse("(1M / 1k) * 1m").evaluate(), BinaryOperation.EPSILON);
        Assert.assertEquals(1d, p.parse("1u * 10 k * 1000  m * 0.1 k").evaluate(), BinaryOperation.EPSILON);
    }
    
    @Test
    public void getVariables() throws ParseException {
        Scope s = new Scope();
        p.parse("a*b+c+e+wer", s);
        Assert.assertTrue(s.getNames().contains("a"));
        Assert.assertTrue(s.getNames().contains("b"));
        Assert.assertTrue(s.getNames().contains("c"));
        Assert.assertTrue(s.getNames().contains("e"));
        Assert.assertTrue(s.getNames().contains("wer"));
        Assert.assertFalse(s.getNames().contains("x"));
        
        // pi and euler are always defined...
        Assert.assertEquals(7, s.getVariables().size());
    }
    
    @Test
    public void errorOnUnknownVariable() throws ParseException {
        Scope s = new Scope();
        try {
            s.create("a");
            s.create("b");
            p.parse("a*b+c", s);
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getErrors().size());
        }
        
        s.create("c");
        p.parse("a*b+c", s);
    }
    
    @Test
    public void removeVariable() throws ParseException {
        Scope s = new Scope();
        s.create("X");
        Assert.assertNotNull(s.find("X"));
        Assert.assertNotNull(s.remove("X"));
        Assert.assertNull(s.find("X"));
    }
    
    @Test
    public void removeVariableFromSubscope() throws ParseException {
        Scope s     = new Scope();
        Scope child = new Scope().withParent(s);
        s.create("X");
        Assert.assertNotNull(child.find("X"));
        Assert.assertNull(child.remove("X"));
        Assert.assertNotNull(child.find("X"));
    }
}
