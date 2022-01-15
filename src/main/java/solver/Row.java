package solver;

import java.util.Arrays;

public class Row {
    Complex[] values;

    public Row(Complex[] values) {
        this.values = values;
    }

    public Complex[] multiplyAndGet(Complex factor) {
        Complex[] multipliedValues = new Complex[values.length];
        for (int i = 0; i < values.length; i++) {
            multipliedValues[i] = values[i].multiply(factor);
//            multipliedValues[i] = values[i] * factor;
        }
        return multipliedValues;
    }

    public void addValues(Complex[] valuesToAdd) {
        System.out.println("valuesToAdd = " + Arrays.toString(valuesToAdd));
        System.out.println("values = " + Arrays.toString(values));
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].add(valuesToAdd[i]);
//            values[i] += valuesToAdd[i];
        }
        System.out.println("values = " + Arrays.toString(values));
    }
}
