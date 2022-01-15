package solver;

import java.util.Arrays;

public class Matrix {
    Row[] rows;

    public Matrix(int size) {
        rows = new Row[size];
    }

    public void setMatrix(Complex[][] values) {
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new Row(values[i]);
        }
    }

    public void swapRow(int index1, int index2) {
        Complex[] values1 = Arrays.copyOf(rows[index1].values, rows[index1].values.length);
        rows[index1].values = Arrays.copyOf(rows[index2].values, rows[index2].values.length);
        rows[index2].values = Arrays.copyOf(values1, values1.length);
    }

    public void swapColumn(int index1, int index2) {
        for (Row row : rows) {
            Complex toSwap = row.values[index1];
            row.values[index1] = row.values[index2];
            row.values[index2] = toSwap;
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Row row : rows) {
            for (int j = 0; j < row.values.length; j++) {
                output.append(row.values[j]).append(" ");
            }
            output.append("\n");
        }
        return String.valueOf(output);
    }
}
