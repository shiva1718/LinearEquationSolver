package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class LinearEquation {
    Matrix matrix;
    int rowCount;
    Complex[] answers;

    public LinearEquation(Matrix matrix, int numOfRows) {
        this.matrix = matrix;
        this.rowCount = numOfRows;
        answers = new Complex[numOfRows];
        Arrays.fill(answers, Complex.NaN);
    }

    public Solution solve() {
        for (int r = 0; r < rowCount; r++) {
            Complex number = matrix.rows[r].values[r];
            if (!number.equals(Complex.ONE)) {
                System.out.println("is not 1");
                if (number.getReal() != 0 || number.getImaginary() != 0) {
                    System.out.println("is not zero");
                    compressEquation(r, number);
                } else {
                    boolean isFoundInThisColumn = false;
                    boolean isFoundInThisRow = false;
                    boolean isFoundInDiagonal = false;
                    for (int i = r + 1; i < rowCount; i++) {
                        if (matrix.rows[i].values[r].getReal() != 0
                              && matrix.rows[i].values[r].getImaginary() != 0) {
                            matrix.swapRow(r, i);
                            isFoundInThisColumn = true;
                            System.out.printf("R%d <-> R%d\n", r + 1, i + 1);
                            break;
                        }
                    }
                    if (!isFoundInThisColumn) {
                        for (int i = r + 1; i < rowCount; i++) {
                            if (matrix.rows[r].values[i].getReal() != 0
                                  && matrix.rows[r].values[i].getImaginary() != 0) {
                                matrix.swapColumn(r, i);
                                isFoundInThisRow = true;
                                System.out.printf("C%d <-> C%d\n", r + 1, i + 1);
                                break;
                            }
                        }
                    }
                    if (!isFoundInThisRow && !isFoundInThisColumn) {
                        for (int i = r + 1; i < rowCount; i++) {
                            for (int j = r + 1; j < rowCount; j++) {
                                if (matrix.rows[i].values[j].getReal() != 0
                                      && matrix.rows[i].values[j].getImaginary() != 0) {
                                    matrix.swapRow(r, i);
                                    matrix.swapColumn(r, j);
                                    System.out.printf("R%d <-> R%d\n", r + 1, i + 1);
                                    System.out.printf("C%d <-> C%d\n", r + 1, j + 1);
                                    isFoundInDiagonal = true;
                                    break;
                                }
                            }
                            if (isFoundInDiagonal) {
                                break;
                            }
                        }
                    }
                    boolean isFoundSolution = isFoundInThisColumn || isFoundInThisRow || isFoundInDiagonal;
                    if (!isFoundSolution) {
                        System.out.println("Line 69");
                        if (Arrays.stream(matrix.rows[r].values)
                                .limit(matrix.rows[r].values.length - 1)
                                .filter(value -> value.getReal() == 0 && value.getImaginary() == 0)
                                .count() == matrix.rows[r].values.length - 1) {
                            if (matrix.rows[r].values[matrix.rows[r].values.length - 1].getReal() == 0 &&
                                    matrix.rows[r].values[matrix.rows[r].values.length - 1].getImaginary() == 0) {
                                System.out.println("Infinitely many solutions 75");
                                return Solution.INFINITE;
                            } else {
                                System.out.println("No Solution 78");
                                return Solution.ZERO;
                            }
                        } else {
                            System.out.println("Infinitely many solutions 81");
                            return Solution.INFINITE;
                        }
                    } else {
                        number = matrix.rows[r].values[r];
                        if (number.getReal() != 0 && number.getImaginary() != 0) {
                            compressEquation(r, number);
                        }
                    }
                }
            }
            for (int i = r + 1; i < rowCount; i++) {
                Complex factor = matrix.rows[i].values[r].negate();
                if (factor.getReal() != 0 || factor.getImaginary() != 0) {

                    System.out.printf("%s * R%d + R%d -> R%d\n", factor, r + 1, i + 1, i + 1);
                    Complex[] factoredValues = matrix.rows[r].multiplyAndGet(factor);
                    matrix.rows[i].addValues(factoredValues);
                    printResult();
                }
            }
            System.out.println("r=" + r);

        }
        printResult();
        Solution solution = setVariablesValue();
        printVariablesValue();
        return solution;
    }

    private void compressEquation(int r, Complex number) {
        System.out.printf("R%d / %s -> R%d\n", r + 1, number.toString(), r + 1);
        for (int i = 0; i < matrix.rows[r].values.length; i++) {
            matrix.rows[r].values[i] = matrix.rows[r].values[i].divide(number);
        }
        printResult();
    }

    public void printResult() {
        System.out.println(matrix.toString());
    }

    public Solution setVariablesValue() {
        for (int i = answers.length - 1; i >= 0; i--) {
            Row thisRow = matrix.rows[i];
            Complex[] values = thisRow.values;
            Complex rightSideValue = values[values.length - 1];
            boolean isFoundNonZeroNumber = false;
            for (int j = 0; j < values.length - 1; j++) {
                if (values[j].getReal() != 0 || values[j].getImaginary() != 0) {
                    isFoundNonZeroNumber = true;
                }
                if (!answers[j].isNaN()) {
                    rightSideValue = rightSideValue.subtract(values[j].multiply(answers[j]));
                }
            }
            if (isFoundNonZeroNumber) {
                answers[i] = rightSideValue;
            } else {
                return Solution.ZERO;
            }
        }
        return Solution.ONE;
    }

    public void printVariablesValue() {
        for (Complex answer : answers) {
            System.out.println(answer);
        }
    }

    public void outputToFile(String fileName) {
        File file = new File(fileName);
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            for (Complex answer : answers) {
                double realPart = answer.getReal();
                double imaginaryPart = answer.getImaginary();
                if (realPart == 0) {
                    if (imaginaryPart == 0) {
                        fileWriter.println(0);
                    } else {
                        fileWriter.println(imaginaryPart + "i");
                    }
                } else if (imaginaryPart == 0) {
                    fileWriter.println(realPart);
                } else if (imaginaryPart == 1) {
                    fileWriter.println(realPart + "+i");
                } else if (imaginaryPart == -1) {
                    fileWriter.println(realPart + "-i");
                } else {
                    fileWriter.printf("%f%+fi\n", realPart, imaginaryPart);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static void outputSpecialCaseToFile(String fileName, String toOutput) {
        File file = new File(fileName);
        try (PrintWriter fileWriter = new PrintWriter(file)) {
            fileWriter.print(toOutput);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
