package com.university.lab.app;

import java.util.Scanner;

public class Application {

    public static final int COLS_AND_ROWS_VALUE = 3;

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

        int[][] arrayA = new int[COLS_AND_ROWS_VALUE][COLS_AND_ROWS_VALUE];
        int[] vectorB = new int[COLS_AND_ROWS_VALUE];

        testFillingArrayAndVectorForTask(arrayA, vectorB);

    }

    private static void defaultFillingArrayAndVector(int[] vectorB, Scanner in, int cols, int rows, int[][] arrA) {
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

    private static void testFillingArrayAndVectorForTask(int[][] arrayA, int[] vectorB) {
        arrayA[0][0] = 1;
        arrayA[0][1] = 2;
        arrayA[0][2] = 1;

        arrayA[1][0] = -1;
        arrayA[1][1] = -2;
        arrayA[1][2] = -2;

        arrayA[2][0] = 0;
        arrayA[2][1] = 1;
        arrayA[2][2] = 1;

        vectorB[0] = 1;
        vectorB[1] = 1;
        vectorB[2] = 2;

        System.out.println("Matrix A:");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            for (int j = 0; j < COLS_AND_ROWS_VALUE; j++) {
                System.out.printf("%s\t", arrayA[i][j]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("vector b:");
        for (int i = 0; i < COLS_AND_ROWS_VALUE; i++) {
            System.out.printf("%s\t", vectorB[i]);
        }
        System.out.println();
        System.out.println();
    }

}
