#define ZLIB
// #define LIBDEFLATE
// #define ISAL

#include "return_codes.h"

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef ZLIB
#include <zlib.h>
#elif defined(LIBDEFLATE)
#include <libdeflate.h>
#elif defined(ISAL)
#include <include/igzip_lib.h>
#else
#error "Not found library"
#endif

typedef unsigned char byte;
typedef unsigned long ulong;

typedef struct
{
	char *type;
	byte *data;
	byte *filter;
	int wasD;
	int wasF;
	int colour;
	int streams;
	size_t height;
	size_t width;
} PNM;

typedef struct
{
	int code;
	char *message;
} ERROR;

typedef struct
{
	int PLTE;
	int IDAT;
	byte *libraryPLTE;
	byte *compressData;
} IDATPLTE;

int isPNG(FILE *filePNG);
int getChunkSize(FILE *filePNG, ERROR *error);
void getChunkName(FILE *filePNG, byte *name, ERROR *error);
int isChunk(const byte *word, const char *name);
void readChunk(FILE *filePNG, byte *data, ulong size, ERROR *error);
void parseIHDR(PNM *pnm, byte *options, ERROR *error);
void parsePLTE(PNM *pnm, const byte *library, ulong size);
void decompressIDAT(PNM *pnm, byte *dataIDAT, ulong sizeIDAT, ERROR *error);
void decompressPLTE(PNM *pnm, const byte *dataPLTE, ERROR *error);
void filter(PNM *pnm, ERROR *error);
void filterSub(PNM *pnm, ulong line);
void filterUp(PNM *pnm, ulong line);
void filterAverage(PNM *pnm, ulong line);
void filterPaeth(PNM *pnm, ulong line);
void workIHDR(FILE *filePNG, PNM *pnm, ERROR *error);
void workPLTEIDAT(FILE *filePNG, PNM *pnm, IDATPLTE *dataIP, ERROR *error);
void fillPNM(FILE *filePNM, PNM *pnm, ERROR *error);

int main(int argc, char *argv[])
{
	if (argc != 3)
	{
		fprintf(stderr, "Wrong number args in argc");
		return ERROR_PARAMETER_INVALID;
	}
	FILE *filePNG = fopen(argv[1], "rb");
	if (!filePNG)
	{
		fprintf(stderr, "Cannot open PNG file");
		return ERROR_CANNOT_OPEN_FILE;
	}
	if (!isPNG(filePNG))
	{
		fprintf(stderr, "Was open not PNG file");
		fclose(filePNG);
		return ERROR_DATA_INVALID;
	}
	ERROR error = { SUCCESS, "" };
	PNM pnm;
	pnm.wasD = 0;
	pnm.wasF = 0;
	workIHDR(filePNG, &pnm, &error);
	if (error.code != SUCCESS)
	{
		fclose(filePNG);
		goto finish;
	}

	IDATPLTE dataIP;
	dataIP.IDAT = 0;
	dataIP.PLTE = 0;
	workPLTEIDAT(filePNG, &pnm, &dataIP, &error);
	fclose(filePNG);
	if (dataIP.PLTE)
	{
		free(dataIP.libraryPLTE);
	}
	if (dataIP.IDAT)
	{
		free(dataIP.compressData);
	}
	if (error.code != SUCCESS)
	{
		goto finish;
	}

	FILE *filePNM = fopen(argv[2], "wb");
	if (!filePNM)
	{
		error.code = ERROR_CANNOT_OPEN_FILE;
		error.message = "Cannot open filePNM";
		goto finish;
	}
	fillPNM(filePNM, &pnm, &error);
	fclose(filePNM);

finish:
	if (pnm.wasD)
	{
		free(pnm.data);
	}
	if (pnm.wasF)
	{
		free(pnm.filter);
	}
	if (error.code != SUCCESS)
	{
		fprintf(stderr, "%s", error.message);
	}
	return error.code;
}

int isPNG(FILE *filePNG)
{
	const byte signature[] = { 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };
	byte actualSignature[8];
	if (fread(actualSignature, sizeof(byte), 8, filePNG) != 8)
	{
		return 0;
	}
	for (int i = 0; i < 8; i++)
	{
		if (signature[i] != actualSignature[i])
		{
			return 0;
		}
	}
	return 1;
}

void workIHDR(FILE *filePNG, PNM *pnm, ERROR *error)
{
	ulong size = getChunkSize(filePNG, error);
	if (error->code != SUCCESS)
	{
		return;
	}

	byte name[4];
	getChunkName(filePNG, name, error);
	if (error->code != SUCCESS)
	{
		return;
	}
	if (!isChunk(name, "IHDR"))
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Not expected chunk, expected IHDR";
		return;
	}

	if (size != 13)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Wrong number of size IHDR";
		return;
	}
	byte options[13];
	readChunk(filePNG, options, size, error);
	if (error->code != SUCCESS)
	{
		return;
	}
	parseIHDR(pnm, options, error);
}

void workPLTEIDAT(FILE *filePNG, PNM *pnm, IDATPLTE *dataIP, ERROR *error)
{
	int PLTE = 0;
	int IDAT = 0;
	ulong lastSize;
	byte name[4] = { 0, 0, 0, 0 };
	ulong size;
	while (!isChunk(name, "IEND"))
	{
		size = getChunkSize(filePNG, error);
		if (error->code != SUCCESS)
		{
			return;
		}

		getChunkName(filePNG, name, error);
		if (error->code != SUCCESS)
		{
			return;
		}

		if (isChunk(name, "PLTE"))
		{
			if (pnm->colour == 0 || size % 3 != 0 || size > 256 * 3 || PLTE != 0)
			{
				error->code = ERROR_DATA_INVALID;
				error->message = "Not expected PLTE";
				return;
			}
			PLTE++;
			dataIP->libraryPLTE = (byte *)malloc(size * sizeof(byte));
			if (dataIP->libraryPLTE == NULL)
			{
				error->code = ERROR_OUT_OF_MEMORY;
				error->message = "Cannot get memory for array libraryPLTE";
				return;
			}
			dataIP->PLTE += 1;
			readChunk(filePNG, dataIP->libraryPLTE, size, error);
			if (error->code != SUCCESS)
			{
				return;
			}
			parsePLTE(pnm, dataIP->libraryPLTE, size);
			if (error->code != SUCCESS)
			{
				return;
			}
		}
		else if (isChunk(name, "IDAT"))
		{
			if (pnm->colour == 3 && PLTE == 0)
			{
				error->code = ERROR_DATA_INVALID;
				error->message = "Expected PLTE";
				return;
			}
			IDAT++;
			if (IDAT == 1)
			{
				dataIP->compressData = (byte *)malloc(size * sizeof(byte));
				if (dataIP->compressData == NULL)
				{
					error->code = ERROR_DATA_INVALID;
					error->message = "Cannot get memory for array ComressData";
					return;
				}
				dataIP->IDAT += 1;
				readChunk(filePNG, dataIP->compressData, size, error);
				lastSize = size;
			}
			else
			{
				dataIP->compressData = (byte *)realloc(dataIP->compressData, (lastSize + size) * sizeof(byte));
				if (dataIP->compressData == NULL)
				{
					error->code = ERROR_DATA_INVALID;
					error->message = "Cannot get memory for array ComressData";
					return;
				}
				byte *data = (byte *)malloc(size * sizeof(byte));
				if (data == NULL)
				{
					error->code = ERROR_DATA_INVALID;
					error->message = "Cannot get memory for array data";
					return;
				}
				readChunk(filePNG, data, size, error);
				if (error->code != SUCCESS)
				{
					free(data);
					return;
				}
				memcpy(dataIP->compressData + lastSize, data, size * sizeof(byte));
				lastSize += size;
				free(data);
			}
			if (error->code != SUCCESS)
			{
				return;
			}
		}
		else if (fseek(filePNG, (size + 4) * sizeof(byte), 1))
		{
			error->code = ERROR_DATA_INVALID;
			error->message = "Don't expected end of file";
			return;
		}
	}
	if (IDAT == 0)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "There's no IDAT";
		return;
	}
	decompressIDAT(pnm, dataIP->compressData, lastSize, error);
	if (error->code != SUCCESS)
	{
		return;
	}

	filter(pnm, error);
	if (error->code != SUCCESS)
	{
		return;
	}

	if (PLTE != 0)
	{
		decompressPLTE(pnm, dataIP->libraryPLTE, error);
		if (error->code != SUCCESS)
		{
			return;
		}
	}
}

void fillPNM(FILE *filePNM, PNM *pnm, ERROR *error)
{
	ulong size = pnm->width * pnm->height * pnm->streams;
	fprintf(filePNM, "%s\n%zu %zu\n%i\n", pnm->type, pnm->width, pnm->height, 255);
	ulong res = fwrite(pnm->data, sizeof(byte), size, filePNM);
	if (res != size)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Error: Failed to write all elements to file.";
		return;
	}
}

int byteToInt(const byte *size, int start, int finish)
{
	int move = 1;
	int res = 0;
	for (int i = finish - 1; i >= start; i--)
	{
		res += ((int)size[i]) * move;
		move *= 256;
	}
	return res;
}

int getChunkSize(FILE *filePNG, ERROR *error)
{
	byte size[4];
	ulong n = fread(size, sizeof(byte), 4, filePNG);
	if (n != 4)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Don't get 4 size bytes";
		return 0;
	}
	return byteToInt(size, 0, 4);
}

void getChunkName(FILE *filePNG, byte *name, ERROR *error)
{
	if (fread(name, sizeof(byte), 4, filePNG) != 4)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Don't get 4 name bytes";
	}
}

void parseIHDR(PNM *pnm, byte *options, ERROR *error)
{
	pnm->width = byteToInt(options, 0, 4);
	pnm->height = byteToInt(options, 4, 8);
	if (options[8] != 0x08)
	{
		error->code = ERROR_UNSUPPORTED;
		error->message = "Support only color depth 0x08";
		return;
	}
	pnm->colour = options[9];
	if (pnm->colour == 0)
	{
		pnm->type = "P5";
		pnm->streams = 1;
	}
	else if (pnm->colour == 2)
	{
		pnm->type = "P6";
		pnm->streams = 3;
	}
	else if (pnm->colour == 3)
	{
		pnm->type = "P6";
		pnm->streams = 1;
	}
	else
	{
		error->code = ERROR_UNSUPPORTED;
		error->message = "Support only 0,2,3 color type";
		return;
	}
	if (options[10] != 0x00)
	{
		error->code = ERROR_UNSUPPORTED;
		error->message = "Support only Deflate";
		return;
	}
	if (options[11] != 0x00)
	{
		error->code = ERROR_UNSUPPORTED;
		error->message = "Support only 0x00 like Filter module";
		return;
	}
	if (options[12] != 0x00)
	{
		error->code = ERROR_UNSUPPORTED;
		error->message = "Don't support through line methods";
		return;
	}
}

void parsePLTE(PNM *pnm, const byte *library, ulong size)
{
	for (int i = 0; i < size / 3; i++)
	{
		if (library[i * 3] != library[i * 3 + 1] || library[i * 3] != library[i * 3 + 2] || library[i * 3 + 1] != library[i * 3 + 2])
		{
			return;
		}
	}
	pnm->type = "P5";
}

void readChunk(FILE *filePNG, byte *data, ulong size, ERROR *error)
{
	if (fread(data, sizeof(byte), size, filePNG) != size)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Don't get expected number of bytes";
		return;
	}
	if (fseek(filePNG, sizeof(byte) * 4, 1))
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Don't fseek expected number of bytes";
		return;
	}	 // control sum
}

void decompressIDAT(PNM *pnm, byte *dataIDAT, const ulong sizeIDAT, ERROR *error)
{
	ulong size = pnm->height * pnm->width * pnm->streams + pnm->height;

	pnm->data = (byte *)malloc(pnm->height * pnm->width * pnm->streams * sizeof(byte));
	pnm->filter = (byte *)malloc(pnm->height * sizeof(byte));
	byte *allData = (byte *)malloc(size * sizeof(byte));
	if (pnm->data == NULL || allData == NULL)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Cannot get size for array in decompressIDAT";
		return;
	}
	pnm->wasD += 1;
	if (pnm->filter == NULL)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Cannot get size for array in decompressIDAT";
		return;
	}
	pnm->wasF += 1;
#ifdef ZLIB
	switch (uncompress(allData, &size, dataIDAT, sizeIDAT))
	{
	case Z_OK:
		break;
	case Z_MEM_ERROR:
		free(allData);
		error->code = ERROR_OUT_OF_MEMORY;
		error->message = "Memory error in uncompress";
		return;
	case Z_BUF_ERROR:
		free(allData);
		error->code = ERROR_DATA_INVALID;
		error->message = "Buffer error in uncompress";
		return;
	case Z_DATA_ERROR:
		free(allData);
		error->code = ERROR_DATA_INVALID;
		error->message = "DATA error in uncompress";
		return;
	default:
		free(allData);
		error->code = ERROR_UNKNOWN;
		error->message = "Unknown error in uncompress ZLIB";
		return;
	}
#endif
#ifdef LIBDEFLATE
	struct libdeflate_decompressor *decompressor = libdeflate_alloc_decompressor();
	size_t res = libdeflate_zlib_decompress(decompressor, dataIDAT, sizeIDAT, allData, size, NULL);
	libdeflate_free_decompressor(decompressor);
	switch (res)
	{
	case LIBDEFLATE_SUCCESS:
		break;
	case LIBDEFLATE_BAD_DATA:
		free(allData);
		error->code = ERROR_DATA_INVALID;
		error->message = "DATA error in uncompress";
		return;
	case LIBDEFLATE_SHORT_OUTPUT:
		free(allData);
		error->code = ERROR_OUT_OF_MEMORY;
		error->message = "Memory error in uncompress";
		return;
	default:
		free(allData);
		error->code = ERROR_UNKNOWN;
		error->message = "Unknown error in uncompress LIBDEFLATE";
		return;
	}
#endif
#ifdef ISAL
	struct inflate_state state;
	isal_inflate_init(&state);
	state.crc_flag = ISAL_ZLIB;
	state.next_in = dataIDAT;
	state.avail_in = sizeIDAT;
	state.next_out = allData;
	state.avail_out = size;
	int status = isal_inflate_stateless(&state);
	if (status != ISAL_DECOMP_OK && status != ISAL_END_INPUT)
	{
		free(allData);
		error->code = ERROR_OUT_OF_MEMORY;
		error->message = "Cannot ISAL uncompress";
		return;
	}
#endif
	int indF = 0;
	int indD = 0;
	for (int i = 0; i < size; i++)
	{
		if (i % (pnm->width * pnm->streams + 1) == 0)
		{
			pnm->filter[indF++] = allData[i];
			continue;
		}
		pnm->data[indD++] = allData[i];
	}
	free(allData);
}

void decompressPLTEP5(PNM *pnm, const byte *dataPLTE)
{
	for (int i = 0; i < pnm->width * pnm->height; i++)
	{
		pnm->data[i] = dataPLTE[pnm->data[i] * 3];
	}
}

void decompressPLTE(PNM *pnm, const byte *dataPLTE, ERROR *error)
{
	if (pnm->type[1] == '5')
	{
		decompressPLTEP5(pnm, dataPLTE);
		return;
	}
	ulong size = pnm->height * pnm->width * 3;
	ulong sizeIndex = pnm->height * pnm->width;
	byte *indexData = (byte *)malloc(sizeIndex * sizeof(byte));
	if (indexData == NULL)
	{
		error->code = ERROR_DATA_INVALID;
		error->message = "Cannot get size for array indexData in decompressPLTE";
		return;
	}
	for (int i = 0; i < sizeIndex; i++)
	{
		indexData[i] = pnm->data[i];
	}
	pnm->data = (byte *)realloc(pnm->data, size);
	if (pnm->data == NULL)
	{
		free(indexData);
		error->code = ERROR_DATA_INVALID;
		error->message = "Cannot add size for array data in PNM in decompressPLTE";
		return;
	}
	for (int i = 0; i < sizeIndex; i++)
	{
		pnm->data[i * 3] = dataPLTE[indexData[i] * 3];
		pnm->data[i * 3 + 1] = dataPLTE[indexData[i] * 3 + 1];
		pnm->data[i * 3 + 2] = dataPLTE[indexData[i] * 3 + 2];
	}
	pnm->streams = 3;
	free(indexData);
}

int isChunk(const byte *word, const char *name)
{
	return name[0] == word[0] && name[1] == word[1] && name[2] == word[2] && name[3] == word[3];
}

void filter(PNM *pnm, ERROR *error)
{
	for (int i = 0; i < pnm->height; i++)
	{
		switch (pnm->filter[i])
		{
		case 0:
			break;
		case 1:
			filterSub(pnm, i);
			break;
		case 2:
			filterUp(pnm, i);
			break;
		case 3:
			filterAverage(pnm, i);
			break;
		case 4:
			filterPaeth(pnm, i);
			break;
		default:
			error->code = ERROR_DATA_INVALID;
			error->message = "Unknown filter type";
			return;
		}
	}
}

void filterSub(PNM *pnm, ulong line)
{
	for (size_t i = pnm->streams + line * pnm->width * pnm->streams; i < (line + 1) * pnm->width * pnm->streams; i++)
	{
		pnm->data[i] += pnm->data[i - pnm->streams];
	}
}

void filterUp(PNM *pnm, ulong line)
{
	if (!line)
	{
		return;
	}
	for (size_t i = line * pnm->width * pnm->streams; i < (line + 1) * pnm->width * pnm->streams; i++)
	{
		pnm->data[i] += pnm->data[i - pnm->width * pnm->streams];
	}
}

void filterAverage(PNM *pnm, ulong line)
{
	if (!line)
	{
		for (size_t i = pnm->streams + line * pnm->width * pnm->streams; i < (line + 1) * pnm->width * pnm->streams; i++)
		{
			pnm->data[i] += pnm->data[i - pnm->streams] / 2;
		}
		return;
	}
	for (size_t i = line * pnm->width * pnm->streams; i < line * pnm->width * pnm->streams + pnm->streams; i++)
	{
		pnm->data[i] += pnm->data[i - pnm->width * pnm->streams] / 2;
	}

	for (size_t i = pnm->streams + line * pnm->width * pnm->streams; i < (line + 1) * pnm->width * pnm->streams; i++)
	{
		pnm->data[i] += (pnm->data[i - pnm->streams] + pnm->data[i - pnm->width * pnm->streams]) / 2;
	}
}

void filterPaeth(PNM *pnm, ulong line)
{
	if (!line)
	{
		filterSub(pnm, line);
		return;
	}
	for (size_t i = line * pnm->width * pnm->streams; i < line * pnm->width * pnm->streams + pnm->streams; i++)
	{
		pnm->data[i] += pnm->data[i - pnm->width * pnm->streams];
	}
	for (size_t i = pnm->streams + line * pnm->width * pnm->streams; i < (line + 1) * pnm->width * pnm->streams; i++)
	{
		int a = pnm->data[i - pnm->streams];
		int b = pnm->data[i - pnm->width * pnm->streams];
		int c = pnm->data[i - pnm->width * pnm->streams - pnm->streams];
		int p = a + b - c;
		int pa = abs(p - a);
		int pb = abs(p - b);
		int pc = abs(p - c);
		if (pa <= pb && pa <= pc)
		{
			pnm->data[i] += a;
		}
		else if (pb <= pc)
		{
			pnm->data[i] += b;
		}
		else
		{
			pnm->data[i] += c;
		}
	}
}