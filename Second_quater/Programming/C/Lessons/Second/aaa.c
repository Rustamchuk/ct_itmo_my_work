#include <math.h>
#include <stdio.h>
#include <stdlib.h>

// multiply matrix a and b
void multiply(double **a, double **b, double **c, int n) {
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      c[i][j] = 0;
      for (int k = 0; k < n; ++k) {
        c[i][j] += a[i][k] * b[k][j];
      }
    }
  }
}
void multiplyWriteToFirst(double **a, double **b, double **c, int n) {
  multiply(a, b, c, n);
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      a[i][j] = c[i][j];
    }
  }
}
void multiplyWriteToSecond(double **a, double **b, double **c, int n) {
  multiply(a, b, c, n);
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      b[i][j] = c[i][j];
    }
  }
}


double **multiplyF(double **a, double **b, int n) {
  double **c = (double **) malloc(n * sizeof(double *));
  for (int i = 0; i < n; ++i) {
    c[i] = (double *) malloc(n * sizeof(double));
  }
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      c[i][j] = 0;
      for (int k = 0; k < n; ++k) {
        c[i][j] += a[i][k] * b[k][j];
      }
    }
  }
  return c;
}

//double **multiplyMatrix(double **a, double **b, int n, int k, int shift) {
//    double **c = (double **) malloc(n * sizeof(double *));
//    for (int i = 0; i < n; ++i) {
//        c[i] = (double *) malloc(n * sizeof(double));
//    }
//    for (int i = 0; i < n; ++i) {
//        for (int j = 0; j < n; ++j) {
//            c[i][j] = 0;
//            for (int k = 0; k < n; ++k) {
//                c[i][j] += a[i][k] * b[k][j];
//            }
//        }
//    }
//    return c;
//}


// transpose matrix a
double **transposeMatrix(double **a, int n) {
  double **b = (double **) malloc(n * sizeof(double *));
  for (int i = 0; i < n; ++i) {
    b[i] = (double *) malloc(n * sizeof(double));
  }
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      b[i][j] = a[j][i];
    }
  }
  return b;
}

void printMatrix(double **arr, int n) {
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      printf("%-12g ", arr[i][j]);
    }
    printf("\n");
  }
}
//double **getIdentityMatrix(int n) {
//    double **arr = (double **) malloc(n * sizeof(double *));
//    for (int i = 0; i < n; ++i) {
//        arr[i] = (double *) malloc(n * sizeof(double));
//    }
//    for (int i = 0; i < n; ++i) {
//        for (int j = 0; j < n; ++j) {
//            if (i == j) {
//                arr[i][j] = 1;
//            } else {
//                arr[i][j] = 0;
//            }
//        }
//    }
//    return arr;
//}
//
//double **subtractIdentity(double **a, int k, int shift) {
//    for (int i = shift; i < k + shift; ++i) {
//        a[i][i] = (1 + a[i][i]);
//    }
//    return a;
//}
//
//double **divideMatrix(double **a, double b, int n) {
//    for (int i = 0; i < n; ++i) {
//        for (int j = 0; j < n; ++j) {
//            a[i][j] /= b;
//        }
//    }
//    return a;
//}

// multiply matrix a and a^T
double **getPMatrix(const double *u, double **P, double alpha, int k, int shift) {
  for (int i = 0; i < k; ++i) {
    for (int j = 0; j < k; ++j) {
      //            c[i][j] = (a[i] * a[j] / (alpha * alpha)) * 2;
      //            c[i + shift][j + shift] = (a[i] * a[j] / (4 * alpha)) * 2;
      //            c[i + shift][j + shift] *= -1;
      P[i + shift][j + shift] = (u[i] * u[j] / (alpha * alpha)) * 2;
      P[i + shift][j + shift] *= -1;
    }
  }
  for (int i = 0; i < k; ++i) {
    P[i + shift][i + shift] = P[i + shift][i + shift] + 1;
  }
  return P;
}

double **getHMatrix(const double *u, double **P, int n) {
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      P[i][j] = u[i] * u[j] * 2;
      P[i][j] *= -1;
    }
  }
  for (int i = 0; i < n; ++i) {
    P[i][i] = P[i][i] + 1;
  }
  return P;
}

double **householderDecomposition(double **a, double **P, double **R, double **Q, int n) {
  int shift = 0;
  for (int i = 0; i < n; ++i) {
    for (int j = 0; j < n; ++j) {
      P[i][j] = 0;
      Q[i][j] = 0;
    }
  }
  for (int i = 0; i < n; ++i) {
    P[i][i] = 1;
    Q[i][i] = 1;
  }
  for (int k = n; k >= 2; --k) {
    double *x = (double *) malloc(k * sizeof(double));
    double alpha = 0;
    for (int i = 0; i < k; ++i) {
      x[i] = a[i + shift][shift];
      alpha += x[i] * x[i];
    }
    //        printf("x = \n");
    //        for (int i = 0; i < k; ++i) {
    //            printf("%f ", x[i]);
    //        }
    //        printf("\n");

    alpha = sqrt(alpha);
    //        printf("alpha = %f\n", alpha);
    x[0] -= alpha;
    // print x
    //        printf("u = \n");
    //        for (int i = 0; i < k; ++i) {
    //            printf("%f ", x[i]);
    //        }
    //        printf("\n");

    double uSize = 0;
    for (int i = 0; i < k; ++i) {
      uSize += x[i] * x[i];
    }
    uSize = sqrt(uSize);
    //        printf("uSize = %f\n", uSize);


    getPMatrix(x, P, uSize, k, shift);
    //        printf("P = \n");
    //        printMatrix(P, n);

    if (shift > 0) {
      P[shift - 1][shift - 1] = 1;
      for (int i = shift; i < n; ++i) {
        P[shift - 1][i] = 0;
        P[i][shift - 1] = 0;
      }
    }

    a = multiplyF(P, a, n);
    //        a = C;
    //        printf("a = \n");
    //        printMatrix(a, n);

    Q = multiplyF(Q, P, n);

    //        printf("Q = \n");
    //        printMatrix(Q, n);

    shift++;
  }
  //    R = transposeMatrix(Q, n);
  //    printf("Q^T = \n");
  //    printMatrix(R, n);
  //
  //    R = multiply(R, a, n);
  //    printf("R = \n");
  //    printMatrix(R, n);
  //
  //    a = multiply(R, Q, n);
  //    printf("A new = \n");
  //    printMatrix(a, n);

  a = multiplyF(a, Q, n);

  //    printf("A new = \n");
  //    printMatrix(a, n);
  return a;
}
void hessenberg(double **A, double **h, double **r, int n) {
  for (int i = 0; i < n - 2; i++) {
    double *x = (double *) malloc((n - i - 1) * sizeof(double));
    double w = 0;
    for (int j = 0; j < (n - i - 1); j++) {
      x[j] = A[j + 1 + i][i];
      w += x[j] * x[j];
    }
    w = sqrt(w);
    if (x[0] > 0) {
      w *= -1;
    }
    double d = 0;
    x[0] = w - x[0];
    d += x[0] * x[0];
    for (int j = 1; j < (n - i - 1); j++) {
      x[j] *= -1;
      d += x[j] * x[j];
    }
    for (int j = 0; j < (n); j++) {
      for (int k = 0; k < (n); k++) {
        if (j == k) {
          h[j][j] = 1;
        } else {
          h[j][k] = 0;
        }
      }
    }
    for (int j = 1 + i; j < n; j++) {
      for (int k = 1 + i; k < n; k++) {
        h[j][k] -= 2 * x[j - i - 1] * x[k - i - 1] / (d == 0 ? 1 : d);
        ;
      }
    }
    for (int k = 0; k < n; k++) {
      for (int j = 0; j < n; j++) {
        double cur = 0;
        for (int l = 0; l < n; l++) {
          double a = h[j][l] * A[l][k];
          cur += a;
        }
        if (fabs(cur) < 1e-7) cur = 0;
        r[j][k] = cur;
      }
    }
    for (int k = 0; k < n; k++) {
      for (int j = 0; j < n; j++) {
        A[k][j] = r[k][j];
      }
    }
    for (int k = 0; k < n; k++) {
      for (int j = 0; j < n; j++) {
        double cur = 0;
        for (int l = 0; l < n; l++) {
          cur += A[j][l] * h[l][k];
        }
        if (fabs(cur) < 1e-7) cur = 0;
        r[j][k] = cur;
      }
    }
    for (int k = 0; k < n; k++) {
      for (int j = 0; j < n; j++) {
        A[k][j] = r[k][j];
      }
    }
    free(x);
  }
}

typedef struct {
  double u1, u2;
  double v1, v2;
  int complex;
} squareEquationResult;

squareEquationResult solveSquareEquation(double **matrix, int size) {
  squareEquationResult result;
  //    double a = matrix[size - 2 - offset][size - 2 - offset];
  //    double b = matrix[size - 2 - offset][size - 1 - offset];
  //    double c = matrix[size - 1 - offset][size - 2 - offset];
  //    double d = matrix[size - 1 - offset][size - 1 - offset];
  double a = matrix[size - 2][size - 2];
  double b = matrix[size - 2][size - 1];
  double c = matrix[size - 1][size - 2];
  double d = matrix[size - 1][size - 1];

  printf("a = %f, b = %f, c = %f, d = %f \n", a, b, c, d);
  double D = a * a + d * d - 2 * a * d + 4 * b * c;
  if (D < 0) {
    // future
    printf("D < 0");
    result.complex = 1;
    result.u1 = (a + d) / 2;
    result.u2 = result.u1;
    result.v1 = sqrt(-D) / 2;
  } else {
    result.complex = 0;
    result.u1 = (a + d + sqrt(D)) / 2;
    result.u2 = (a + d - sqrt(D)) / 2;
    result.v1 = 0;
    result.v2 = 0;
  }
  return result;
}
typedef struct {
  double real;
  double imaginary;
  int complex;
} eigenValue;
double **doubleShift(double **a, double **H, double **R, double **Q, double **C, int size, eigenValue * lambda) {
  //    int shift = 0;
  //    for (int i = 0; i < size; ++i) {
  //        for (int j = 0; j < size; ++j) {
  //            P[i][j] = 0;
  //            Q[i][j] = 0;
  //        }
  //    }
  //    for (int i = 0; i < size; ++i) {
  //        P[i][i] = 1;
  //        Q[i][i] = 1;
  //    }
  //    double *lambda = (double *) malloc(size * sizeof(double));

  int n = size;
  int shift = 0;
  while (1) {
    squareEquationResult eqRes = solveSquareEquation(a, n);
    //        printf("u1 = %f, u2 = %f\n", eqRes.u1, eqRes.u2);
    //        printf("v1 = %f, v2 = %f\n", eqRes.v1, eqRes.v2);

    double t1 = a[0][0] * a[0][0] - a[0][0] * (eqRes.u1 + eqRes.u2) + eqRes.u1 * eqRes.u2 + eqRes.v1 * eqRes.v1 + a[0][1] * a[1][0];
    double t2 = a[1][0] * (a[0][0] + a[1][1] - eqRes.u1 - eqRes.u2);
    double t3 = a[2][1] * a[1][0];

    //        printf("t1 = %f, t2 = %f, t3 = %f\n", t1, t2, t3);

    double s = sqrt(t1 * t1 + t2 * t2 + t3 * t3);
    if (t1 >= 0) {
      s = -s;
    }
    //        printf("s = %f\n", s);
    double u = 1 / sqrt(2 * s * (s - t1));

    //        printf("u = %f\n", u);
    double *w = (double *) malloc(n * sizeof(double));
    w[0] = u * (t1 - s);
    w[1] = u * t2;
    w[2] = u * t3;
    for (int i = 3; i < n; ++i) {
      w[i] = 0;
    }
    double wSize = 0;
    for (int i = 0; i < n; ++i) {
      wSize += w[i] * w[i];
    }
    wSize = sqrt(wSize);

    //        printf("w = ");
    //        for (int i = 0; i < n; ++i) {
    //            printf("%f ", w[i]);
    //        }
    //        printf("\n");
    //    printf("wSize = %f\n", wSize);


    getHMatrix(w, H, n);

    //        printf("H = \n");
    //        printMatrix(H, size);

    multiplyWriteToSecond(H, a, C, n);
    multiplyWriteToFirst(a, H, C, n);

    for (int i = 1; i < n - 1; ++i) {
      for (int j = 0; j < n; ++j) {
        w[j] = 0;
      }
      s = 0;
      for (int j = i; j < n; ++j) {
        s += a[j][i - 1] * a[j][i - 1];
      }
      s = sqrt(s);
      if (a[i][i - 1] >= 0) {
        s = -s;
      }
      u = 1 / sqrt(2 * s * (s - a[i][i - 1]));
      w[i] = u * (a[i][i - 1] - s);
      for (int j = i + 1; j < n; ++j) {
        w[j] = u * a[j][i - 1];
      }

      getHMatrix(w, H, n);
      //            printf("H = \n");
      //            printMatrix(H, size);

      multiplyWriteToSecond(H, a, C, n);
      multiplyWriteToFirst(a, H, C, n);
      //            printf("A in = \n");
      //            printMatrix(a, size);
    }
    //        double E = 0.00001;
    double E = 0.00001;
    if (fabs(a[n - 1][n - 2]) < E) {
      lambda[n - 1].complex = 0;
      lambda[n - 1].real = a[n - 1][n - 1];
      printf("lambda real = %f n = %i\n", lambda[n - 1].real, n - 1);
      n = n - 1;
      shift++;
    }

    if (fabs(a[n - 2][n - 3]) < E || n == 2) {
      squareEquationResult res = solveSquareEquation(a, n);
      if (res.complex) {
        lambda[n - 2].real = res.u1;
        lambda[n - 1].real = res.u2;

        lambda[n - 2].imaginary = res.v1;
        lambda[n - 1].imaginary = -res.v1;

        lambda[n - 2].complex = 1;
        lambda[n - 1].complex = 1;

        printf("lambda complex1 = %f %f n = %i\n", lambda[n - 2].real, lambda[n - 2].imaginary, n - 2);
        printf("lambda complex2 = %f %f n = %i\n", lambda[n - 1].real, lambda[n - 1].imaginary, n - 1);
      } else {
        lambda[n - 2].real = res.u1;
        lambda[n - 1].real = res.u2;

        lambda[n - 2].complex = 0;
        lambda[n - 1].complex = 0;
        printf("lambda real1 = %f n = %i\n", lambda[n - 2].real, n - 2);
        printf("lambda real2 = %f n = %i\n", lambda[n - 1].real, n - 1);
      }

      n = n - 2;
      shift += 2;
    }
    if (n == 1) {
      printf("lambda real2 = %f n = %i\n", lambda[n - 1].real, n - 1);
      lambda[n - 1].real = a[n - 1][n - 1];
      lambda[n - 1].complex = 0;
      n = 0;
    }
    if (n == 0) {
      break;
      //            return a;
    }
  }
  //    return a;
}
int main(int argc, char *argv[]) {
  FILE *f;
  f = fopen(argv[1], "r");
  int n;
  fscanf(f, "%i", &n);
  double **arr = (double **) malloc(n * sizeof(double *));
  for (int i = 0; i < n; ++i) {
    arr[i] = (double *) malloc(n * sizeof(double));
  }
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      fscanf(f, "%lf", &arr[i][j]);
    }
  }
  fclose(f);
  double *ans = (double *) malloc(n * sizeof(double));

  double **A = arr;
  double **P = (double **) malloc(n * sizeof(double *));
  double **R = (double **) malloc(n * sizeof(double *));
  double **Q = (double **) malloc(n * sizeof(double *));
  double **C = (double **) malloc(n * sizeof(double *));
  for (int i = 0; i < n; ++i) {
    P[i] = (double *) malloc(n * sizeof(double));
    R[i] = (double *) malloc(n * sizeof(double));
    Q[i] = (double *) malloc(n * sizeof(double));
    C[i] = (double *) malloc(n * sizeof(double));
  }
  //    for (int i = 0; i < 10000; ++i) {
  hessenberg(A, Q, R, n);
  //    printMatrix(A, n);
  eigenValue *lambda = (eigenValue *) malloc(n * sizeof(eigenValue));
  doubleShift(A, P, R, Q, C, n, lambda);
  //    }
  //    printf("Result = \n");
  f = fopen(argv[2], "w");

  for (int i = 0; i < n; ++i) {
    if (lambda[i].complex) {
      fprintf(f, "%f +%fi\n", lambda[i].real, lambda[i].imaginary);
      i++;
      fprintf(f, "%f %fi\n", lambda[i].real, lambda[i].imaginary);
    } else {
      fprintf(f, "%f\n", lambda[i].real);
    }
  }
  fclose(f);
  //    printf("\n");
  //    printMatrix(arr, n);
  //    f = fopen(argv[2], "w");
  //    for (int i = 0; i < n; ++i) {
  //        fprintf(f, "%f\n", ans[i]);
  //    }
  //    fclose(f);
  //    3
  //    12 -51 4
  //    6 167 -68
  //      -4 24 -41
  return 0;
}