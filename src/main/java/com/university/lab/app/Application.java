package com.university.lab.app;

public class Application {

    public static final int APPROXIMATING_POLYNOMIAL_DEGREE = 7;
    public static final int NUMBER_OF_MEASUREMENTS = 1;

    public static void main(String[] args) {
        double[] x = new double[APPROXIMATING_POLYNOMIAL_DEGREE];
        double[] y = new double[APPROXIMATING_POLYNOMIAL_DEGREE];

        fillingMatrix_X(x);
        fillingMatrix_Y(y);

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

        for (int i = 0; i < NUMBER_OF_MEASUREMENTS + 1; i++) {
            System.out.println("a[" + (i + 1) + "] = " + An[i]);
        }

        System.out.println();
        System.out.println("Sigma = " + Math.sqrt(standardDeviation(x, y, An, NUMBER_OF_MEASUREMENTS + 1)));
        System.out.println();
        System.out.print("y(x) = ");
        System.out.print(An[1] + " * x + " + An[0]);
    }

    private static double[] gauss(double[][] matrix, int n, int m) {
        double elem = 0;
        for (int j = 0; j < n; j++) {
            double max = 0;
            int rowCoord = 0;
            for (int t = j; t < n; t++) {
                if (Math.abs(matrix[t][j]) > max) {
                    max = Math.abs(matrix[t][j]);
                    rowCoord = t;
                }
            }
            if (max > Math.abs(matrix[j][j])) {
                double[] ptr = matrix[j];
                matrix[j] = matrix[rowCoord];
                matrix[rowCoord] = ptr;
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

        double[] X = new double[m];
        X[n - 1] = matrix[n - 1][n];
        for (int i = n - 2; i >= 0; i--) {
            X[i] = matrix[i][n];
            for (int j = i + 1; j < n; j++)
                X[i] -= matrix[i][j] * X[j];
        }
        System.out.println();

        return X;
    }

    private static void fillingMatrix_X(double[] x) {
        x[0] = 19.1;
        x[1] = 25.0;
        x[2] = 30.1;
        x[3] = 36.0;
        x[4] = 40.0;
        x[5] = 45.1;
        x[6] = 50.0;
    }

    private static void fillingMatrix_Y(double[] y) {
        y[0] = 76.30;
        y[1] = 77.80;
        y[2] = 79.75;
        y[3] = 80.80;
        y[4] = 82.35;
        y[5] = 83.90;
        y[6] = 85.0;

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
