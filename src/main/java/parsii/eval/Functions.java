/*
 * Made with all the love in the world
 * by scireum in Remshalden, Germany
 *
 * Copyright by scireum GmbH
 * http://www.scireum.de - info@scireum.de
 */

package parsii.eval;

import net.jafama.FastMath;

import java.util.List;

/**
 * Contains a set of predefined standard functions.
 * <p>
 * Provides mostly functions defined by {@link FastMath}
 */
public class Functions {

    /**
     * Provides access to {@link FastMath#sin(double)}
     */
    public static final Function SIN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.sin(a);
        }
    };

    /**
     * Provides access to {@link FastMath#sinh(double)}
     */
    public static final Function SINH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.sinh(a);
        }
    };

    /**
     * Provides access to {@link FastMath#cos(double)}
     */
    public static final Function COS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.cos(a);
        }
    };

    /**
     * Provides access to {@link FastMath#cosh(double)}
     */
    public static final Function COSH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.cosh(a);
        }
    };

    /**
     * Provides access to {@link FastMath#tan(double)}
     */
    public static final Function TAN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.tan(a);
        }
    };

    /**
     * Provides access to {@link FastMath#tanh(double)}
     */
    public static final Function TANH = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.tanh(a);
        }
    };

    /**
     * Provides access to {@link FastMath#abs(double)}
     */
    public static final Function ABS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.abs(a);
        }
    };

    /**
     * Provides access to {@link FastMath#asin(double)}
     */
    public static final Function ASIN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.asin(a);
        }
    };

    /**
     * Provides access to {@link FastMath#acos(double)}
     */
    public static final Function ACOS = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.acos(a);
        }
    };

    /**
     * Provides access to {@link FastMath#atan(double)}
     */
    public static final Function ATAN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.atan(a);
        }
    };

    /**
     * Provides access to {@link FastMath#atan2(double, double)}
     */
    public static final Function ATAN2 = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return FastMath.atan2(a, b);
        }
    };

    /**
     * Provides access to {@link FastMath#round(double)}
     */
    public static final Function ROUND = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.round(a);
        }
    };

    /**
     * Provides access to {@link FastMath#floor(double)}
     */
    public static final Function FLOOR = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.floor(a);
        }
    };

    /**
     * Provides access to {@link FastMath#ceil(double)}
     */
    public static final Function CEIL = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.ceil(a);
        }
    };

    /**
     * Provides access to {@link FastMath#pow(double, double)}
     */
    public static final Function POW = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return FastMath.pow(a, b);
        }
    };

    /**
     * Provides access to {@link FastMath#sqrt(double)}
     */
    public static final Function SQRT = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.sqrt(a);
        }
    };

    /**
     * Provides access to {@link FastMath#exp(double)}
     */
    public static final Function EXP = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.exp(a);
        }
    };

    /**
     * Provides access to {@link FastMath#log(double)}
     */
    public static final Function LN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.log(a);
        }
    };

    /**
     * Provides access to {@link FastMath#log10(double)}
     */
    public static final Function LOG = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.log10(a);
        }
    };

    /**
     * Provides access to {@link FastMath#min(double, double)}
     */
    public static final Function MIN = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return FastMath.min(a, b);
        }
    };

    /**
     * Provides access to {@link FastMath#max(double, double)}
     */
    public static final Function MAX = new BinaryFunction() {
        @Override
        protected double eval(double a, double b) {
            return FastMath.max(a, b);
        }
    };

    /**
     * Provides access to {@link FastMath#random()} which will be multiplied by the given argument.
     */
    public static final Function RND = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.random() * a;
        }
    };

    /**
     * Provides access to {@link FastMath#signum(double)}
     */
    public static final Function SIGN = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.signum(a);
        }
    };

    /**
     * Provides access to {@link FastMath#toDegrees(double)}
     */
    public static final Function DEG = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.toDegrees(a);
        }
    };

    /**
     * Provides access to {@link FastMath#toRadians(double)}
     */
    public static final Function RAD = new UnaryFunction() {
        @Override
        protected double eval(double a) {
            return FastMath.toRadians(a);
        }
    };

    /**
     * Provides an if-like function
     * <p>
     * It expects three arguments: A condition, an expression being evaluated if the condition is non zero and an
     * expression which is being evaluated if the condition is zero.
     */
    public static final Function IF = new IfFunction();

    private Functions() {
    }

    private static class IfFunction implements Function {
        @Override
        public int getNumberOfArguments() {
            return 3;
        }

        @Override
        public double eval(List<Expression> args) {
            double check = args.get(0).evaluate();
            if (Double.isNaN(check)) {
                return check;
            }
            if (FastMath.abs(check) > 0) {
                return args.get(1).evaluate();
            } else {
                return args.get(2).evaluate();
            }
        }

        @Override
        public boolean isNaturalFunction() {
            return false;
        }
    }
}
