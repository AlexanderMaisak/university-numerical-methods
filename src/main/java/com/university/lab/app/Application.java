package com.university.lab.app;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Application {

    public static final int APPROXIMATING_POLYNOMIAL_DEGREE = 9;
    public static final int NUMBER_OF_MEASUREMENTS = 4;

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
        double[] x = new double[APPROXIMATING_POLYNOMIAL_DEGREE];
        double[] y = new double[APPROXIMATING_POLYNOMIAL_DEGREE];

        fillingMatrix_X(x);
        fillingMatrix_Y(x, y);

        for (int i = 0; i < APPROXIMATING_POLYNOMIAL_DEGREE; ++i) {
            System.out.println("x[" + (i + 1) + "] = " + x[i] + "\t" + "y[" + (i + 1) + "] = " + y[i]);
        }
        System.out.println();

        double[] PowerX = new double[2 * NUMBER_OF_MEASUREMENTS];
        for (int i = 0; i < 2 * NUMBER_OF_MEASUREMENTS; ++i) {
            PowerX[i] = 0;
        }

        for (int i = 1; i <= 2 * NUMBER_OF_MEASUREMENTS; ++i) {
            for (int j = 0; j < APPROXIMATING_POLYNOMIAL_DEGREE; ++j) {
                PowerX[i - 1] += Math.pow(x[j], i);
            }
        }

        for (int i = 0; i < 2 * NUMBER_OF_MEASUREMENTS; ++i) {
            System.out.println("PowerX[" + (i + 1) + "] = " + PowerX[i]);
        }
        System.out.println();

        double[][] sumX = new double[NUMBER_OF_MEASUREMENTS + 1][NUMBER_OF_MEASUREMENTS + 2];

        for (int i = 1; i <= NUMBER_OF_MEASUREMENTS + 1; ++i) {
            for (int j = 1; j <= NUMBER_OF_MEASUREMENTS + 1; ++j) {
                if ((i + j) >= 3) {
                    sumX[i - 1][j - 1] = PowerX[i + j - 3];
                }
            }
        }
        sumX[0][0] = APPROXIMATING_POLYNOMIAL_DEGREE;

        outputMatrix(sumX, NUMBER_OF_MEASUREMENTS + 1);
        for (int i = 0; i <= NUMBER_OF_MEASUREMENTS; ++i) {
            for (int j = 0; j < APPROXIMATING_POLYNOMIAL_DEGREE; ++j) {
                sumX[i][NUMBER_OF_MEASUREMENTS + 1] += (y[j] * Math.pow(x[j], i));
            }
        }
        outputMatrix(sumX, NUMBER_OF_MEASUREMENTS + 1);

        double[][] copyA = new double[NUMBER_OF_MEASUREMENTS + 1][NUMBER_OF_MEASUREMENTS + 2];
        copyMatrix(sumX, copyA, NUMBER_OF_MEASUREMENTS + 1);

        double[] An;
        An = gauss(copyA, NUMBER_OF_MEASUREMENTS + 1, NUMBER_OF_MEASUREMENTS + 2);

        for (int i = 0; i < NUMBER_OF_MEASUREMENTS + 1; ++i) {
            System.out.println("a[" + (i + 1) + "] = " + An[i]);
        }

        System.out.println();
        System.out.println("Sigma = " + Math.sqrt(standardDeviation(x, y, An, NUMBER_OF_MEASUREMENTS + 1)));

        System.out.print("y(x) = ");
        for (int i = NUMBER_OF_MEASUREMENTS; i > 0; --i) {
            System.out.print(An[i] + "x^" + i + " + ");
        }
        System.out.println(An[0]);
    }

    private static double[] gauss(double[][] matrix, int n, int m) {
        double elem = 0;
        for (int j = 0; j < n; j++) {
            double max = 0;
            int coord_str = 0;
            for (int t = j; t < n; t++) {
                if (Math.abs(matrix[t][j]) > max) {
                    max = Math.abs(matrix[t][j]); coord_str = t;
                }
            }
            if (max > Math.abs(matrix[j][j])) {
                double[] ptr = matrix[j];
                matrix[j] = matrix[coord_str];
                matrix[coord_str] = ptr;
            }
            elem = matrix[j][j];
            for (int c = j; c < m; c++) {
                matrix[j][c] /= elem;
            }

            for (int i2 = j + 1; i2 < n; i2++) {
                elem = matrix[i2][j];
                for (int k = j; k < m; k++)
                    matrix[i2][k] -= elem * matrix[j][k];
            }
        }

        double[] xx = new double[m];
        xx[n - 1] = matrix[n - 1][n];
        for (int i = n - 2; i >= 0; i--) {
            xx[i] = matrix[i][n];
            for (int j = i + 1; j < n; j++)
                xx[i] -= matrix[i][j] * xx[j];
        }
        System.out.println();

        return xx;
    }

    private static void fillingMatrix_X(double[] x) {
        x[0] = 0;
        x[1] = 1;
        x[2] = 2;
        x[3] = 3;
        x[4] = 4;
        x[5] = 5;
        x[6] = 6;
        x[7] = 7;
        x[8] = 8;
    }

    private static void fillingMatrix_Y(double[] x, double[] y) {
        for (int i = 0; i < NUMBER_OF_MEASUREMENTS; i++) {
            y[i] = 1 / (1 + x[i]);
        }
    }

    private static void outputMatrix(double[][] A, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < (n + 1); j++) {
                System.out.printf("%s\t\t", A[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void copyMatrix(double[][] A, double[][] copyA, int n) {
        for (int i = 0; i < n; i++) {
            if (n + 1 >= 0) System.arraycopy(A[i], 0, copyA[i], 0, n + 1);
        }
    }

    private static double standardDeviation(double[] dataX, double[] dataY, double[] An, double n) {
        double S = 0, temp;
        for (int i = 0; i < APPROXIMATING_POLYNOMIAL_DEGREE; i++) {
            temp = dataY[i];
            for (int j = 0; j < n; j++) {
                temp -= An[j] * Math.pow(dataX[i], j);
            }
            S += temp * temp;
        }
        S /= (APPROXIMATING_POLYNOMIAL_DEGREE - NUMBER_OF_MEASUREMENTS - 1);

        return S;
    }

}
