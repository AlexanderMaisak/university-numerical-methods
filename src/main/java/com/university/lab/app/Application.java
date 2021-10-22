package com.university.lab.app;

import java.util.Scanner;

import static java.lang.Math.*;

public class Application {

    private static final int N = 2;
    private static final double EPS1 = 1e-7;
    private static final double EPS2 = 1e-9;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        double[] F = new double[N];
        double[] x = new double[N];
        double[][] Jac = new double[N][N + 1];
        double[] dx = new double[N];

        System.out.println("Newton's Method");

        System.out.println();
        System.out.println("2x1^3 - x2^2 - 1 = 0;");
        System.out.println("x1x2^3 - x2 - 4 = 0;");
        System.out.println();

        System.out.print("Enter the number of iterations: ");
        int iter = in.nextInt();
        System.out.println();

        System.out.println("Enter the initial approximation:");
        for (int i = 0; i < N; ++i) {
            System.out.print("Enter value " + (i + 1) + ": ");
            x[i] = in.nextDouble();
        }

        newtonMethod(Jac, F, x, dx, iter);
    }

    public static void equations(double[] F, double[] x) {
        F[0] = 2 * x[0] * x[0] * x[0] - x[1] * x[1] - 1;
        F[1] = x[0] * x[1] * x[1] * x[1] - x[1] - 4;
    }

    public static void newtonMethod(double[][] Jac, double[] F, double[] x, double[] dx, int iter) {
        System.out.println("IterNumber" + "\t\t\t" + "del1" + "\t\t\t\t\t" + "del2");
        System.out.println("=============================================================");

        boolean IER = false;

        int k = 0;
        while (true) {
            equations(F, x);
            jacobi(Jac, F, x);
            for (int i = 0; i < N; ++i) {
                F[i] *= -1;
            }
            gaussMethod(Jac, F, dx, x);
            double max = 0;
            equations(F, x);
            for (int i = 0; i < N; ++i) {
                if (abs(F[i]) > max) {
                    max = abs(F[i]);
                }
            }
            double del1 = max;
            max = 0;
            for (int i = 0; i < N; ++i) {
                if (abs(x[i]) < 1 && abs(dx[i]) > max) {
                    max = abs(dx[i]);
                }
                if (abs(x[i]) >= 1 && abs(dx[i] / x[i]) > max) {
                    max = abs(dx[i] / x[i]);
                }
            }
            double del2 = max;
            System.out.println("\t" + (k + 1) + "\t\t" + del1 + "\t\t" + del2);
            k++;
            if (del1 <= EPS2 && del2 <= EPS2 || k >= iter) {
                if (k >= iter) {
                    IER = true;
                }
                break;
            }
        }

        System.out.println("=============================================================");
        System.out.println("X1 = " + x[0]);
        System.out.println("X2 = " + x[1]);

        if (IER) {
//            System.out.println("IER = 2");
        }
    }

    public static void jacobi(double[][] Jac, double[] F, double[] x) {
        double f1 = 0;
        double f2 = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                x[j] += EPS1;
                equations(F, x);
                f1 = F[i];
                x[j] -= EPS1;
                equations(F, x);
                f2 = F[i];
                Jac[i][j] = (f1 - f2) / EPS1;
                Jac[i][N] = -F[i];
            }
        }
    }

    public static void gaussMethod(double[][] Jac, double[] F, double[] dx, double[] x) {
        for (int i = 0; i < N; i++) {
            double max = abs(Jac[i][i]);
            int my = i;

            for (int t = i; t < N; ++t) {
                if (abs(Jac[t][i]) > max) {
                    max = abs(Jac[t][i]);
                    my = t;
                }
            }

            if (my != i) {
                double[] per = Jac[i];
                Jac[i] = Jac[my];
                Jac[my] = per;
            }

            double amain = Jac[i][i];
            for (int z = 0; z < N + 1; ++z) {
                Jac[i][z] = Jac[i][z] / amain;
            }

            for (int j = i + 1; j < N; ++j) {
                double b = Jac[j][i];
                for (int z = i; z < N + 1; ++z) {
                    Jac[j][z] = Jac[j][z] - Jac[i][z] * b;
                }
            }
        }

        for (int i = N - 1; i > 0; --i) {
            for (int j = i - 1; j >= 0; --j) {
                Jac[j][N] = Jac[j][N] - Jac[j][i] * Jac[i][N];
            }
        }

        for (int i = 0; i < N; ++i) {
            dx[i] = Jac[i][N];
        }

        for (int i = 0; i < N; i++) {
            x[i] += Jac[i][N];
        }
    }
}