package com.university.lab.app;

import static java.lang.Math.*;

public class Application {

    private static final double EPS1 = 1e-4;
    private static final double EPS2 = 1e-5;
    private static final double a = 1;
    private static final double b = 1;

    public static void main(String[] args) {

        double trapezoid;
        double simpson;
        double cubeSimpson;

        final double a = 0.0;
        final double b = 1.047;

        trapezoid = trapezoidMethod(a, b);
        System.out.println("Trapezoid method:\n" + trapezoid + '\n');
        simpson = simpsonMethod(a, b);
        System.out.println("Simpson's method:\n" + simpson + '\n');

        final double a1 = 0.0;
        final double b1 = 1.0;
        final double c1 = 1.0;
        final double d1 = 2.0;

        cubeSimpson = simpsonCubeMethod(a1, b1, c1, d1);
        System.out.println("Simpson's cubature method:\n" + cubeSimpson + '\n');
    }

    public static double function(double x) {
        return exp(x / 2) / pow((x + 1), 0.5);
    }

    public static double function(double x, double y) {

        return sin(x * x + y * y) / (1 + a * x + b * y);
    }

    public static double trapezoidMethod(double a, double b) {
        double sum = 0;
        double previousSum = 0;
        double h = (b - a);
        int amount = 0;

        do {
            previousSum = sum;
            sum = 0;
            for (int i = 1; i <= ((b - a) / h) - 1; i++) {
                sum += 2 * function(a + h * i);
            }
            sum = (function(a) + sum + function(b)) * h / 2;
            h /= 2;
            amount++;
        } while (abs(sum - previousSum) > 3 * EPS1);

        System.out.println("Trapezoid: " + amount + " iter");

        return sum;
    }

    public static double simpsonMethod(double a, double b) {
        double sum = 0;
        double previousSum = 0;
        double h = (b - a) / 2;
        int amount = 0;

        do {
            previousSum = sum;
            sum = 0;
            for (int i = 1; i <= ((b - a) / h); i += 2) {
                sum += 4 * function(a + h * i);
            }
            for (int i = 2; i <= ((b - a) / h) - 1; i += 2) {
                sum += 2 * function(a + h * i);
            }
            sum = (function(a) + sum + function(b)) * h / 3;
            h /= 2;
            amount++;
        } while (abs(sum - previousSum) > 15 * EPS2);

        System.out.println("Simpson's: " + amount + " iter");

        return sum;
    }

    public static double simpsonCubeMethod(double a, double b, double c, double d) {
        int m = 10;
        int n = 2 * m;
        double hx = (b - a) / (2 * n);
        double hy = (d - c) / (2 * m);
        double sum = 0;

        double[] Xi = new double[2 * n + 1];
        Xi[0] = a;

        for (int i = 1; i <= 2 * n; i++)
            Xi[i] = Xi[i - 1] + hx;
        ;

        double[] Yi = new double[2 * m + 1];
        Yi[0] = c;

        for (int j = 1; j <= 2 * m; j++)
            Yi[j] = Yi[j - 1] + hy;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum += function(Xi[2 * i], Yi[2 * j]);
                sum += 4 * function(Xi[2 * i + 1], Yi[2 * j]);
                sum += function(Xi[2 * i + 2], Yi[2 * j]);
                sum += 4 * function(Xi[2 * i], Yi[2 * j + 1]);
                sum += 16 * function(Xi[2 * i + 1], Yi[2 * j + 1]);
                sum += 4 * function(Xi[2 * i + 2], Yi[2 * j + 1]);
                sum += function(Xi[2 * i], Yi[2 * j + 2]);
                sum += 4 * function(Xi[2 * i + 1], Yi[2 * j + 2]);
                sum += function(Xi[2 * i + 2], Yi[2 * j + 2]);
            }
        }
        sum *= (hx * hy / 9);
        return sum;
    }
}