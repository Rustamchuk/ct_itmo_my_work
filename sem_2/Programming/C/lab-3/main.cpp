#include "LN.h"

#include <fstream>
#include <iostream>
#include <string>

void parse(std::ifstream &file, Vector< LN > &stack);
LN pop(Vector< LN > &stack);
void pushBack(Vector< LN > &stack, const LN &ln);

int main(int argc, char *argv[])
{
	if (argc != 3)
	{
		std::cerr << "Wrong number args in argc";
		return ERROR_PARAMETER_INVALID;
	}
	std::ifstream fileIN(argv[1]);
	if (!fileIN.is_open())
	{
		std::cerr << "Cannot open IN file";
		return ERROR_CANNOT_OPEN_FILE;
	}
	Vector< LN > stack;
	try
	{
		parse(fileIN, stack);
	} catch (Error &e)
	{
		fileIN.close();
		std::cerr << e.getMessage();
		return e.getCode();
	}
	fileIN.close();

	std::ofstream fileOUT(argv[2]);
	if (!fileOUT.is_open())
	{
		std::cerr << "Cannot open OUT file";
		return ERROR_CANNOT_OPEN_FILE;
	}
	try
	{
		for (size_t i = stack.size() - 1; i < stack.size(); i--)
		{
			Vector< char > out = stack[i].toString();
			for (size_t j = 0; j < out.size(); j++)
			{
				fileOUT << out[j];
			}
			fileOUT << '\n';
		}
	} catch (Error &e)
	{
		fileOUT.close();
		std::cerr << e.getMessage();
		return e.getCode();
	}
	fileOUT.close();
	return SUCCESS;
}

void parse(std::ifstream &file, Vector< LN > &stack)
{
	std::string cur;
	while (std::getline(file, cur))
	{
		if (cur == "+" || cur == "+=")
		{
			pushBack(stack, pop(stack) + pop(stack));
		}
		else if (cur == "-" || cur == "-=")
		{
			LN a = pop(stack);
			pushBack(stack, a - pop(stack));
		}
		else if (cur == "*" || cur == "*=")
		{
			pushBack(stack, pop(stack) * pop(stack));
		}
		else if (cur == "/" || cur == "/=")
		{
			LN a = pop(stack);
			pushBack(stack, a / pop(stack));
		}
		else if (cur == "%" || cur == "%=")
		{
			LN a = pop(stack);
			pushBack(stack, a % pop(stack));
		}
		else if (cur == "~")
		{
			pushBack(stack, ~pop(stack));
		}
		else if (cur == "_")
		{
			pushBack(stack, -pop(stack));
		}
		else if (cur == "<")
		{
			LN a = pop(stack);
			pushBack(stack, a < pop(stack));
		}
		else if (cur == "<=")
		{
			LN a = pop(stack);
			pushBack(stack, a <= pop(stack));
		}
		else if (cur == ">")
		{
			LN a = pop(stack);
			pushBack(stack, a > pop(stack));
		}
		else if (cur == ">=")
		{
			LN a = pop(stack);
			pushBack(stack, a >= pop(stack));
		}
		else if (cur == "==")
		{
			pushBack(stack, pop(stack) == pop(stack));
		}
		else if (cur == "!=")
		{
			pushBack(stack, pop(stack) != pop(stack));
		}
		else
		{
			pushBack(stack, LN(cur));
		}
	}
}

LN pop(Vector< LN > &stack)
{
	if (stack.empty())
	{
		throw Error(ERROR_DATA_INVALID, "No pop for empty vector");
	}
	LN a = stack.back();
	stack.pop_back();
	return a;
}

void pushBack(Vector< LN > &stack, const LN &ln)
{
	try
	{
		stack.push_back(ln);
	} catch (std::bad_alloc &e)
	{
		throw Error(ERROR_OUT_OF_MEMORY, "Push back error");
	}
}
