#include "return_codes.h"

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

const double EPSILON = 1e-3;

void QRAlgorithm(double **A, double **Q, double **R, double *v, int n);

void hessenberg(double **A, double **H, double **R, double *v, int n);

void householder(double **A, double **Q, double **R, double *v, int n);

void complexToFile(FILE *file, double real, double imaginary);

_Bool isEquals(double a, double b);

int main(int argc, char *argv[])
{
	if (argc != 3)
	{
		fprintf(stderr, "Error number of arguments\n");
		return ERROR_PARAMETER_INVALID;
	}

	FILE *inFile = fopen(argv[1], "r");
	if (inFile == NULL)
	{
		fprintf(stderr, "File wasn't open\n");
		return ERROR_CANNOT_OPEN_FILE;
	}

	int n;
	fscanf(inFile, "%d", &n);

	int finishCode = SUCCESS;
	double **A = (double **)malloc(sizeof(double *) * n);
	double **Q = (double **)malloc(n * sizeof(double *));
	double **R = (double **)malloc(n * sizeof(double *));
	double *v = (double *)malloc(n * sizeof(double));
	if (A == NULL || Q == NULL || R == NULL || v == NULL)
	{
		fprintf(stderr, "Array couldn't create, memory error\n");
		finishCode = ERROR_OUT_OF_MEMORY;
		if (inFile)
		{
			fclose(inFile);
		}
		goto finishWork;
	}

	for (int i = 0; i < n; i++)
	{
		A[i] = malloc(sizeof(double) * n);
		Q[i] = malloc(n * sizeof(double));
		R[i] = malloc(n * sizeof(double));
		if (A[i] == NULL || Q[i] == NULL || R[i] == NULL)
		{
			fprintf(stderr, "Array couldn't create, memory error\n");
			finishCode = ERROR_OUT_OF_MEMORY;
			if (inFile)
			{
				fclose(inFile);
			}
			goto finishWork;
		}

		for (int j = 0; j < n; j++)
		{
			fscanf(inFile, "%lf", &A[i][j]);
		}
	}
	if (inFile)
	{
		fclose(inFile);
	}

	QRAlgorithm(A, Q, R, v, n);

	FILE *outFile = fopen(argv[2], "w");
	if (outFile == NULL)
	{
		fprintf(stderr, "File wasn't open\n");
		finishCode = ERROR_CANNOT_OPEN_FILE;
		goto finishWork;
	}

	for (int i = 0; i < n; i++)
	{
		if (i > 0 && isEquals(A[i][i], A[i - 1][i - 1]))
		{
			complexToFile(outFile, A[i][i], A[i][i - 1]);
		}
		else if (i != n - 1 && isEquals(A[i][i], A[i + 1][i + 1]))
		{
			complexToFile(outFile, A[i][i], A[i][i + 1]);
		}
		else
		{
			if (fabs(A[i][i]) <= EPSILON)
			{
				A[i][i] = 0;
			}
			fprintf(outFile, "%g\n", A[i][i]);
		}
	}
	if (outFile)
	{
		fclose(outFile);
	}

finishWork:
	for (int i = 0; i < n; ++i)
	{
		if (A != NULL && A[i] != NULL)
		{
			free(A[i]);
		}
		if (Q != NULL && Q[i] != NULL)
		{
			free(Q[i]);
		}
		if (R != NULL && R[i] != NULL)
		{
			free(R[i]);
		}
	}
	if (Q != NULL)
	{
		free(Q);
	}
	if (R != NULL)
	{
		free(R);
	}
	if (A != NULL)
	{
		free(A);
	}
	if (v != NULL)
	{
		free(v);
	}

	return finishCode;
}

void complexToFile(FILE *file, double real, double imaginary)
{
	if (imaginary > 0)
	{
		fprintf(file, "%g +%gi\n", real, imaginary);
	}
	else
	{
		fprintf(file, "%g %gi\n", real, imaginary);
	}
}

_Bool isEquals(double a, double b)
{
	return fabs(a - b) <= EPSILON;
}

void QRAlgorithm(double **A, double **Q, double **R, double *v, int n)
{
	double error = EPSILON + 1;
	int max = 10000;
	int y = 0;

	hessenberg(A, Q, R, v, n);
	while (error > EPSILON && y < max)
	{
		error = 0;
		householder(A, Q, R, v, n);
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				double cur = 0;
				for (int k = 0; k < n; k++)
				{
					cur += R[i][k] * Q[k][j];
				}
				A[i][j] = cur;

				if (i > j && fabs(A[i][j]) > error)
				{
					if (i - 1 == j)
					{
						if (j > 0 && isEquals(A[i - 1][j], A[i - 2][j - 1]))
						{
							continue;
						}
						if (j != n - 1 && isEquals(A[i - 1][j], A[i][j + 1]))
						{
							continue;
						}
					}
					error = fabs(A[i][j]);
				}
			}
		}
		y++;
	}
	for (int i = 0; i < n - 1; i++)
	{
		if (fabs(A[i + 1][i]) > EPSILON)
		{
			A[i][i] = A[i][i] + A[i + 1][i + 1];
			A[i][i] /= 2;
			A[i + 1][i + 1] = A[i][i];
			A[i + 1][i] = fabs(A[i + 1][i]) + fabs(A[i][i + 1]);
			A[i + 1][i] /= 2;
			A[i][i + 1] = A[i + 1][i] * -1;
			i++;
		}
	}
}

void hessenberg(double **A, double **H, double **R, double *v, int n)
{
	for (int i = 0; i < n - 2; i++)
	{
		double w = 0;
		for (int j = 0; j < (n - i - 1); j++)
		{
			v[j] = A[j + 1 + i][i];
			w += v[j] * v[j];
		}
		w = sqrt(w);
		if (v[0] > 0)
		{
			w *= -1;
		}
		double d = 0;
		v[0] = w - v[0];
		d += v[0] * v[0];
		for (int j = 1; j < (n - i - 1); j++)
		{
			v[j] *= -1;
			d += v[j] * v[j];
		}
		for (int j = 0; j < n; j++)
		{
			for (int k = 0; k < n; k++)
			{
				if (j == k)
				{
					H[j][j] = 1;
				}
				else
				{
					H[j][k] = 0;
				}
			}
		}
		for (int j = 1 + i; j < n; j++)
		{
			for (int k = 1 + i; k < n; k++)
			{
				H[j][k] -= 2 * v[j - i - 1] * v[k - i - 1] / (d == 0 ? 1 : d);
			}
		}
		for (int k = 0; k < n; k++)
		{
			for (int j = 0; j < n; j++)
			{
				double cur = 0;
				for (int l = 0; l < n; l++)
				{
					double a = H[j][l] * A[l][k];
					cur += a;
				}
				if (fabs(cur) < EPSILON)
				{
					cur = 0;
				}
				R[j][k] = cur;
			}
		}
		for (int k = 0; k < n; k++)
		{
			for (int j = 0; j < n; j++)
			{
				A[k][j] = R[k][j];
			}
		}
		for (int k = 0; k < n; k++)
		{
			for (int j = 0; j < n; j++)
			{
				double cur = 0;
				for (int l = 0; l < n; l++)
				{
					cur += A[j][l] * H[l][k];
				}
				if (fabs(cur) < EPSILON)
				{
					cur = 0;
				}
				R[j][k] = cur;
			}
		}
		for (int k = 0; k < n; k++)
		{
			for (int j = 0; j < n; j++)
			{
				A[k][j] = R[k][j];
			}
		}
	}
}

void householder(double **A, double **Q, double **R, double *v, int n)
{
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			if (i != j)
			{
				Q[i][j] = 0;
			}
			else
			{
				Q[i][j] = 1;
			}
			R[i][j] = A[i][j];
		}
	}

	for (int k = 0; k < n - 1; k++)
	{
		double x = sqrt(R[k][k] * R[k][k] + R[k + 1][k] * R[k + 1][k]) * (R[k][k] >= 0 ? 1 : -1);
		v[0] = R[k][k] + x;
		v[1] = R[k + 1][k];
		double vDel = sqrt(v[0] * v[0] + v[1] * v[1]);
		v[0] /= vDel == 0 ? 1 : vDel;
		v[1] /= vDel == 0 ? 1 : vDel;
		for (int i = k; i < n; i++)
		{
			double temp = 0;
			for (int j = k; j < k + 2; j++)
			{
				temp += R[j][i] * v[j - k];
			}
			for (int j = k; j < k + 2; j++)
			{
				R[j][i] -= 2 * temp * v[j - k];
			}
		}
		for (int i = 0; i < n; i++)
		{
			double temp = 0;
			for (int j = k; j < k + 2; j++)
			{
				temp += Q[i][j] * v[j - k];
			}
			for (int j = k; j < k + 2; j++)
			{
				Q[i][j] -= 2 * temp * v[j - k];
			}
		}
	}
}