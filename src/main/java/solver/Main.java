package solver;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String inputFileName = "";
        String outputFileName = "";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-in".equals(arg)) {
                inputFileName = args[i + 1];
            } else if ("-out".equals(arg)) {
                outputFileName = args[i + 1];
            }
        }
        File inputFile = new File(inputFileName);
//        System.out.println(inputFileName);
        try (Scanner kb = new Scanner(inputFile)) {
            int numOfVariables = kb.nextInt();
            int numOfEquations = kb.nextInt();
            Set<Integer> toRemove = new HashSet<>();
            Complex[][] complexInputMatrix = new Complex[numOfEquations][numOfVariables + 1];
            int numOfSignificantEquations = numOfEquations;
            for (int i = 0; i < numOfEquations; i++) {
                for (int j = 0; j < numOfVariables + 1; j++) {
                    String inputString = kb.next();
                    String[] numberParts = inputString.split("\\b[+-]");
//                    System.out.println(Arrays.toString(numberParts));
                    double imaginaryPart = 0;
                    double realPart = 0;
                    if (numberParts[0].contains("i")) {
                        imaginaryPart = Double.parseDouble(
                              numberParts[0].replace("i", ""));
                        inputString = inputString.replace(numberParts[0], "");
                        if (!inputString.isEmpty()) {
                            realPart = Double.parseDouble(inputString);
                        }
                    } else {
                        realPart = Double.parseDouble(numberParts[0]);
                        inputString = inputString.replaceFirst(numberParts[0], "");
                        if (!inputString.isEmpty()) {
                            imaginaryPart = Double.parseDouble(
                                  inputString.replace("i", ""));
                        }
                    }
                    complexInputMatrix[i][j] = new Complex(realPart, imaginaryPart);
//                    System.out.print(complexInputMatrix[i][j].toString() + " ");
                }
//                System.out.println();
                if (Arrays.stream(complexInputMatrix[i])
                        .limit(numOfVariables)
                        .filter(value -> value.getReal() == 0 && value.getImaginary() == 0)
                        .count() == numOfVariables &&
                        complexInputMatrix[i][numOfVariables].getReal() == 0 &&
                        complexInputMatrix[i][numOfVariables].getImaginary() == 0) {

                    toRemove.add(i);
//                    System.out.println("added " + i);
                    numOfSignificantEquations--;
                }
                if (i > 0) {
                    int finalI = i;
                    if (Arrays.stream(complexInputMatrix)
                            .limit(i)
                            .anyMatch(
                                  doubles -> Arrays.equals(doubles, complexInputMatrix[finalI]))) {
                        numOfSignificantEquations--;
                    }
                }
            }
            Complex[][] complexSignificantInput
                  = new Complex[numOfSignificantEquations][numOfVariables + 1];
            int controller = 0;
            for (int i = 0; i < numOfEquations; i++) {
                if (toRemove.contains(i)) {
//                    System.out.println("removed this " + i);
//                    System.out.println("numOfSignificantEquations = " + numOfSignificantEquations);
//                    System.out.println("numOfEquations = " + numOfEquations);
                    controller++;
                    continue;
                }
                if (numOfVariables + 1 >= 0)
                    System.arraycopy(complexInputMatrix[i], 0, complexSignificantInput[i - controller], 0, numOfVariables + 1);
            }
//            System.out.println(Arrays.deepToString(complexInputMatrix));
//            System.out.println(Arrays.deepToString(complexSignificantInput));
            Matrix matrix = new Matrix(numOfSignificantEquations);
            matrix.setMatrix(complexSignificantInput);
            LinearEquation solver = new LinearEquation(matrix, numOfSignificantEquations);
            Solution numOfSolution = solver.solve();
            switch (numOfSolution) {
                case ZERO -> LinearEquation.outputSpecialCaseToFile(outputFileName,
                      "No solutions");
                case ONE -> solver.outputToFile(outputFileName);
                case INFINITE -> LinearEquation.outputSpecialCaseToFile(outputFileName,
                      "Infinitely many solutions");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
