package com.university.lab.app;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Application {

    public static final int COLS_AND_ROWS_VALUE = 3;
    private static final String RELATIVE_FAULT_MSG = "\nRelative fault: %s";

    public static void main(String[] args) {

//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter the number of cols");
//        int cols = in.nextInt();
//        System.out.println("Enter the number of rows");
//        int rows = in.nextInt();
//
//        int[][] arrA = new int[rows][cols];
//        int[] vecB = new int[rows];
//        defaultFillingArrayAndVector(vectorB, in, cols, rows, arrA);
//
//        in.close();

        //todo: all methods are written for a static arrays with COLS_AND_ROWS_VALUE
        double[][] arrayA = new double[COLS_AND_ROWS_VALUE][COLS_AND_ROWS_VALUE];
        double[] vectorB = new double[COLS_AND_ROWS_VALUE];

        testFillingArrayAndVectorForTask(arrayA, vectorB);

        double[] X = gaussMethod(arrayA, vectorB);

        System.out.println("Solution of a system of linear algebraic equations (SLAE): ");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            System.out.printf("x%s=%s\t\t", i + 1, X[i]);
        }
        System.out.println();

        double[][] copyOfA;
        copyOfA = Arrays.copyOf(arrayA, COLS_AND_ROWS_VALUE * COLS_AND_ROWS_VALUE);
        double[] copyOfB;
        copyOfB = Arrays.copyOf(vectorB, vectorB.length);

        double[] vectorF = findResidualVector(copyOfA, copyOfB, X);

        System.out.println("\nResidual vector F: ");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            System.out.printf("%s\t\t", vectorF[i]);
        }
        System.out.println();

        System.out.println("\nResidual vector norm: ");
        double norma = findResidualVectorNorm(vectorF);
        System.out.println(norma);

        double[] vectorB2 = fillSecondVectorB(copyOfA, X);
        double[] X2 = gaussMethod(copyOfA, vectorB2);

        System.out.println("\nAssistant system solution: ");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            System.out.printf("%s\t\t", X[i]);
        }
        System.out.println();

        System.out.println(String.format(RELATIVE_FAULT_MSG,
                findResidualVectorNorm(difference(X, X2)) / findResidualVectorNorm(X)));
        System.out.println();
    }

    private static double[] gaussMethod(double[][] A, double[] b) {
        double[] X = new double[COLS_AND_ROWS_VALUE];

        for (int k = 0; k < COLS_AND_ROWS_VALUE; k++) {
            for (int i = k + 1; i < COLS_AND_ROWS_VALUE; i++) {
                if (abs(A[i][k]) > abs(A[k][k])) {
                    swapReference(A, k, i);
                    swapValues(b, k, i);
                }
            }

            double mainA = A[k][k];
            if (mainA == 0) {
                throw new IllegalArgumentException("invalid value");
            }

            for (int i = k; i < COLS_AND_ROWS_VALUE; i++) {
                A[k][i] /= mainA;
            }

            b[k] /= mainA;

            for (int i = k + 1; i < COLS_AND_ROWS_VALUE; i++) {
                double val = A[i][k];
                for (int j = k; j < COLS_AND_ROWS_VALUE; j++) {
                    A[i][j] -= val * A[k][j];
                }
                b[i] -= val * b[k];
            }
        }

        for (int k = COLS_AND_ROWS_VALUE - 1; k >= 0; k--) {
            X[k] = b[k];
            for (int i = COLS_AND_ROWS_VALUE - 1; i > k; i--) {
                X[k] -= A[k][i] * X[i];
            }
        }
        return X;
    }

    private static double findResidualVectorNorm(double[] vectorF) {
        double norma = abs(vectorF[0]);
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            norma = max(vectorF[i], norma);
        }
        return norma;
    }

    private static double[] findResidualVector(double[][] A, double[] b, double[] X) {
        double[] result = new double[COLS_AND_ROWS_VALUE];
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            result[i] = -b[i];
            for (int j = 0; j < COLS_AND_ROWS_VALUE; j++) {
                result[i] += A[i][j] * X[j];
            }
        }
        return result;
    }

    private static void swapReference(double[][] A, int k, int i) {
        double[] tmp = A[i];
        A[i] = A[k];
        A[k] = tmp;
    }

    private static void swapValues(double[] b, int k, int i) {
        double tmp = b[i];
        b[i] = b[k];
        b[k] = tmp;
    }

    private static double[] fillSecondVectorB(double[][] copyOfA, double[] X) {
        double[] b2 = new double[COLS_AND_ROWS_VALUE];
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            b2[i] = 0;
            for (int j = 0; j < COLS_AND_ROWS_VALUE; j++) {
                b2[i] += copyOfA[i][j] * X[j];
            }
        }
        return b2;
    }

    private static double[] difference(double[] X1, double[] X2) {
        double[] x = new double[COLS_AND_ROWS_VALUE];
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            x[i] = X2[i] - X1[i];
        }
        return x;
    }

    private static void defaultFillingArrayAndVector(double[] vectorB, Scanner in, int cols, int rows, double[][] arrA) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arrA[i][j] = in.nextInt();
            }
        }
        System.out.println("Matrix A:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%s\t", arrA[i][j]);
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < rows; i++) {
            vectorB[i] = in.nextInt();
        }

        System.out.println("vector b:");
        for (int i = 0; i < rows; i++) {
            System.out.printf("%s\t", vectorB[i]);
        }
        System.out.println();
    }

    private static void testFillingArrayAndVectorForTask(double[][] arrayA, double[] vectorB) {
        arrayA[0][0] = 2.30;
        arrayA[0][1] = 5.70;
        arrayA[0][2] = -0.80;

        arrayA[1][0] = 3.50;
        arrayA[1][1] = -2.70;
        arrayA[1][2] = 5.30;

        arrayA[2][0] = 1.70;
        arrayA[2][1] = 2.30;
        arrayA[2][2] = 3.58;

        vectorB[0] = -6.49;
        vectorB[1] = 19.20;
        vectorB[2] = -5.09;

        System.out.println("Matrix A:");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            for (int j = 0; j < COLS_AND_ROWS_VALUE; j++) {
                System.out.printf("%s\t\t", arrayA[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("vector b:");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            System.out.printf("%s\t\t", vectorB[i]);
        }
        System.out.println();
        System.out.println();
    }

}
