package solver;

import java.util.Objects;

public class Complex {
    private double real;
    private double imaginary;
    public static Complex NaN = new Complex(Double.NaN, Double.NaN);
    public static Complex ONE = new Complex(1, 0);
    public static Complex ZERO = new Complex(0, 0);
    public static Complex INFINITE = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);


    public Complex(double realPart, double imaginaryPart) {
        this.real = realPart;
        this.imaginary = imaginaryPart;

    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex negate() {
        if (isNaN()) {
            return NaN;
        }
        return new Complex(-real, -imaginary);
    }

    public Complex add(Complex toAdd) {
        if (isNaN() || toAdd.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(real + toAdd.real, imaginary + toAdd.imaginary);
    }

    public Complex subtract(Complex toSubtract) {
        if (isNaN() || toSubtract.isNaN()) {
            return Complex.NaN;
        }
        return new Complex(real - toSubtract.real, imaginary - toSubtract.imaginary);
    }

    public Complex divide(Complex toDivideBy) {
        if (this.isNaN() || toDivideBy.isNaN() || toDivideBy.equals(ZERO)) {
            return Complex.NaN;
        }
        if (!this.isInfinite() && toDivideBy.isInfinite()) {
            return ZERO;
        }

        //(a + bi) / (c + di) = (ac + bd + (bc - ad)i) / (c^2 + d^2)
        double a = real;
        double b = imaginary;
        double c = toDivideBy.real;
        double d = toDivideBy.imaginary;

        double realResult = a * c + b * d;
        double imaginaryResult = b * c - a * d;
        double dividingResult = c * c + d * d;
        realResult = realResult / dividingResult;
        imaginaryResult = imaginaryResult / dividingResult;
        return new Complex(realResult, imaginaryResult);
    }

    public Complex multiply(Complex factor) {
        if (isNaN() || factor.isNaN()) {
            return Complex.NaN;
        }
        if (isInfinite() || factor.isInfinite()) {
            return Complex.INFINITE;
        }

//        (a + bi) * (c + di) = (ac - bd) + (ad + bc)i;
        double a = real;
        double b = imaginary;
        double c = factor.real;
        double d = factor.imaginary;

        return new Complex(a * c - b * d, a * d + b * c);
    }

    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imaginary);
    }

    public boolean isInfinite() {
        return Double.isInfinite(real) || Double.isInfinite(imaginary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.real, real) == 0 && Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public String toString() {
        if (imaginary == 0) {
            if (real == 0) {
                return "0";
            } else {

                return "" + real;
            }
        } else if (imaginary > 0) {
            if (real == 0) {
                return imaginary + "";
            } else {
                return real + " + " + imaginary + "i";
            }
        } else {
            if (real == 0) {
                return imaginary + "";
            } else {
                return real + " - " + Math.abs(imaginary) + "i";
            }
        }
    }
}
