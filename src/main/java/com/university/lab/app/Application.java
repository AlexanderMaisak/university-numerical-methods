package com.university.lab.app;

import java.util.Scanner;

import static java.lang.Math.*;

public class Application {
    private static final int N = 3;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        double a = 1;
        double k = 2;

        double[] u = new double[N];
        double[] fn = new double[N];

        //System.out.println("Enter values:");

        for (int i = 0; i < N; ++i) {
            System.out.print("u[" + i + "] = ");
            //u[i] = in.nextDouble();
            u[i] = 1;
        }

        System.out.println("======================================================================================================");
        ExplicitEulerMethod(u, a, k);
        System.out.println("======================================================================================================");
        ImplicitEulerMethod(u, a, k, fn);
    }

    public static double f1(double[] u, double a, double k) {
        return ((k - a) / a) * u[1] * u[2];
    }

    public static double f2(double[] u, double a, double k) {
        return ((a + k) / k) * u[0] * u[2];
    }

    public static double f3(double[] u, double a, double k) {
        return ((a - k) / a) * u[0] * u[1];
    }

    public static void FN(double[] uk1, double[] uk, double Tau, double a, double k, double[] Fn) {
        Fn[0] = uk1[0] - uk[0] - Tau * ((k - a) / a * uk[1] * uk[2]);
        Fn[1] = uk1[1] - uk[1] - Tau * ((a + k) / k * uk[2] * uk[0]);
        Fn[2] = uk1[2] - uk[2] - Tau * ((a - k) / a * uk[0] * uk[1]);
    }

    public static void ExplicitEulerMethod(double[] yo, double a, double k) {
        System.out.println("Explicit Euler method:");
        System.out.println("======================================================================================================");

        double k_iter = 0;
        double TauMax = 0.01;
        double T = 1;
        double t = 0;
        double Eps = 0.01;
        double TauMin = 0;
        int d = 0;

        double[] sh = new double[N];
        double[] y = new double[N];
        double[] f = new double[N];

        System.arraycopy(yo, 0, y, 0, N);

        System.out.println("k" + "\t\t" + "t" + "\t\t" + "F1" + "\t\t" + "F2" + "\t\t" + "F3");
        System.out.println("======================================================================================================");

        while (t < T) {
            f[0] = f1(y, a, k);
            f[1] = f2(y, a, k);
            f[2] = f3(y, a, k);

            for (int i = 0; i < N; ++i) {
                sh[i] = Eps / (abs(f[i]) + Eps / TauMax);
            }

            if (sh[0] > sh[1]) {
                TauMin = sh[1];
            } else {
                if (TauMin > sh[2]) {
                    TauMin = sh[2];
                }
            }

            for (int i = 0; i < N; ++i) {
                y[i] = y[i] + TauMin * f[i];
            }

            if (t + TauMin > T) {
                t = T;
                TauMin = T - t;
            }

            t = t + TauMin;
            d++;
            k_iter++;

            System.out.println(d + "\t\t" + t + "\t\t" + y[0] + "\t\t" + y[1] + "\t\t" + y[2]);
        }

        System.out.println("======================================================================================================");
        System.out.println("k_iter = " + k_iter);
        System.out.println("Y1 = " + y[0]);
        System.out.println("Y2 = " + y[1]);
        System.out.println("Y3 = " + y[2]);
    }

    public static void ImplicitEulerMethod(double[] u, double a, double k, double[] Fn) {
        System.out.println("Implicit Euler method:");
        System.out.println("======================================================================================================");
        System.out.println("k" + "\t\t" + "t" + "\t\t" + "F1" + "\t\t" + "F2" + "\t\t" + "F3");
        System.out.println("======================================================================================================");

        int k_iter = 0;
        double Tau = 0;
        double Tau1 = 0;
        double Tau_1 = 0;
        double TauMax = 0.5;
        double t1 = 0;
        double t = 0;
        double T = 1;
        double Eps = 0.01;
        double TauMin = 0.01;

        double[] yk = new double[N];
        double[] yk1 = new double[N];
        double[] yk_1 = new double[N];
        double[] tau = new double[N];

        Tau = TauMin;
        Tau_1 = TauMin;

        for (int i = 0; i < N; ++i) {
            yk1[i] = yk[i] = yk_1[i] = u[i];
        }

        double[] Ek = new double[N];

        while (t < T) {
            t1 = t + Tau;
            NewtonMethod(yk, yk1, Tau, a, k, Fn);
            for (int i = 0; i < N; ++i) {
                Ek[i] = -((Tau / (Tau + Tau_1)) * (yk1[i] - yk[i] - Tau / Tau_1 * (yk[i] - yk_1[i])));
            }
            boolean r = false;
            for (int i = 0; i < N; i++) {
                if (abs(Ek[i]) > Eps && (!r)) {
                    Tau /= 2;
                    Tau1 = Tau;
                    System.arraycopy(yk, 0, yk1, 0, N);
                    r = true;
                }
            }
            if (r) {
                continue;
            }
            for (int i = 0; i < N; ++i) {
                if (abs(Ek[i]) < Eps) {
                    tau[i] = sqrt(abs(Eps / Ek[i])) * Tau;
                }
            }

            Tau1 = tau[0];
            if (Tau1 > tau[1]) {
                Tau1 = tau[1];
            }
            if (Tau1 > tau[2]) {
                Tau1 = tau[2];
            }

            if (Tau1 > TauMax) {
                Tau1 = TauMax;
            }

            System.out.println(k_iter + "\t\t" + t + "\t\t" + yk[0] + "\t\t" + yk[1] + "\t\t" + yk[2]);

            for (int i = 0; i < N; i++) {
                yk_1[i] = yk[i];
                yk[i] = yk1[i];
            }

            Tau_1 = Tau;
            Tau = Tau1;
            t = t1;
            k_iter++;
        }

        System.out.println("======================================================================================================");
        System.out.println("k_iter = " + k_iter);
        System.out.println("Y1 = " + yk[0]);
        System.out.println("Y2 = " + yk[1]);
        System.out.println("Y3 = " + yk[2]);
    }

    public static void NewtonMethod(double[] x, double[] yk, double Tau, double a, double k, double[] Fn) {
        FN(x, yk, Tau, a, k, Fn);

        double[][] Jf = new double[N][N + 1];

        double[] dx = new double[N];
        double d1 = 0;
        double d2 = 0;
        double e1 = 1e-4;
        double e2 = 1e-4;
        double eps = 1e-6;

        do {
            for (int i = 0; i < N; ++i) {
                Jf[i][N] = -Fn[i];
            }

            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    double f1 = 0;
                    double f2 = 0;

                    x[j] += eps;
                    FN(x, yk, Tau, a, k, Fn);
                    f1 = Fn[i];

                    x[j] -= eps;
                    FN(x, yk, Tau, a, k, Fn);
                    f2 = Fn[i];
                    Jf[i][j] = (f1 - f2) / eps;
                }
            }

            GaussMethod(Jf, N, dx);
            for (int i = 0; i < N; ++i) {
                x[i] += dx[i];
            }

            d1 = abs(Jf[0][N]);
            for (int i = 1; i < N; ++i) {
                if (abs(Jf[i][N]) > d1) {
                    d1 = abs(Jf[i][N]);
                }
            }

            if (abs(x[0]) < 1) {
                d2 = abs(dx[0]);
            } else {
                d2 = abs(dx[0] / x[0]);
            }

            for (int i = 1; i < N; ++i) {
                if (abs(x[i]) < 1) {
                    if (d2 < abs(dx[i])) {
                        d2 = abs(dx[i]);
                    } else {
                        if (d2 < abs(dx[i] / x[i])) {
                            d2 = abs(dx[i] / x[i]);
                        }
                    }
                }
            }
        } while (d1 > e1 || d2 > e2);
    }

    public static double[] GaussMethod(double[][] Jac, int n, double[] dx) {
        for (int i = 0; i < n; ++i) {
            double max = abs(Jac[i][i]);
            int my = i;
            for (int j = i; j < n; ++j) {
                if (abs(Jac[j][i]) > max) {
                    max = abs(Jac[j][i]);
                    my = j;
                }
            }

            if (my != i) {
                double[] per = Jac[i];
                Jac[i] = Jac[my];
                Jac[my] = per;
            }

            double amain = Jac[i][i];
            for (int j = 0; j < n + 1; ++j) {
                Jac[i][j] = Jac[i][j] / amain;
            }

            for (int j = i + 1; j < n; ++j) {
                double b = Jac[j][i];
                for (int k = i; k < n + 1; k++) {
                    Jac[j][k] -= Jac[i][k] * b;
                }
            }
        }

        for (int i = n - 1; i > 0; --i) {
            for (int j = i - 1; j >= 0; --j)
                Jac[j][n] -= Jac[j][i] * Jac[i][n];
        }

        for (int i = 0; i < n; ++i) {
            dx[i] = Jac[i][n];
        }

        return dx;
    }
}