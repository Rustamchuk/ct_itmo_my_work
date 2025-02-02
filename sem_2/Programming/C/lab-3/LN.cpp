#include "LN.h"

LN::LN(long long number)
{
	if (number < 0)
	{
		negate = true;
		number *= -1;
	}
	do
	{
		pushBack(number % MAX_NUMBER);
		number /= MAX_NUMBER;
	} while (number != 0);
}

void LN::parseString(std::string_view str)
{
	size_t pos = 0;
	if (str[pos] == '-')
	{
		pos++;
		negate = true;
	}
	size_t size = str.size();
	uint32_t numberBlock = 0;
	uint32_t max = MAX_NUMBER / 16;
	uint32_t multiply = 1;
	for (size_t i = size - 1; i < size && i >= pos; i--)
	{
		if (str[i] >= '0' && str[i] <= '9')
		{
			numberBlock += (str[i] - '0') * multiply;
		}
		else if (str[i] >= 'A' && str[i] <= 'F')
		{
			numberBlock += (str[i] - 'A' + 10) * multiply;
		}
		else if (str[i] >= 'a' && str[i] <= 'f')
		{
			numberBlock += (str[i] - 'a' + 10) * multiply;
		}
		else
		{
			throw Error(ERROR_DATA_INVALID, "Unexpected symbol");
		}
		multiply *= 16;
		if (numberBlock >= max || multiply >= MAX_NUMBER)
		{
			pushBack(numberBlock);
			numberBlock = 0;
			multiply = 1;
		}
	}
	if (numberBlocks.empty() || numberBlock != 0)
	{
		pushBack(numberBlock);
	}
}

void LN::copy(const LN &ln)
{
	numberBlocks = ln.numberBlocks;
	negate = ln.negate;
	isNaN = ln.isNaN;
}

void LN::pushBack(uint32_t number)
{
	try
	{
		numberBlocks.push_back(number);
	} catch (std::bad_alloc &e)
	{
		throw Error(ERROR_OUT_OF_MEMORY, "Push back error");
	}
}

void LN::clear()
{
	numberBlocks.clear();
	negate = false;
	isNaN = false;
}

LN &LN::operator=(LN &&second)
{
	copy(second);
	second.clear();
	return *this;
}

LN LN::operator+(const LN &second) const
{
	if (isNaN || second.isNaN)
	{
		return makeNaN();
	}
	if (negate && !second.negate)
	{
		return abs(second) - abs(*this);
	}
	if (!negate && second.negate)
	{
		return abs(*this) - abs(second);
	}
	if (numberBlocks.size() >= second.numberBlocks.size())
	{
		return sum(*this, second);
	}
	return sum(second, *this);
}

LN LN::sum(const LN &a, const LN &b)
{
	bool mindOne = false;
	LN res = a;
	size_t size = b.numberBlocks.size();
	for (size_t i = 0; i < size; i++)
	{
		res.numberBlocks[i] += b.numberBlocks[i] + mindOne;
		mindOne = res.numberBlocks[i] / MAX_NUMBER;
		res.numberBlocks[i] %= MAX_NUMBER;
	}
	if (mindOne)
	{
		for (size_t i = size; i < res.numberBlocks.size(); i++)
		{
			res.numberBlocks[i] += mindOne;
			res.numberBlocks[i] %= MAX_NUMBER;
			mindOne = res.numberBlocks[i] / MAX_NUMBER;
			if (!mindOne)
			{
				break;
			}
		}
		if (mindOne)
		{
			res.pushBack(mindOne);
		}
	}
	return res;
}

LN LN::operator-(const LN &second) const
{
	if (isNaN || second.isNaN)
	{
		return makeNaN();
	}
	if (negate && !second.negate)
	{
		return -(-*this + second);
	}
	if (!negate && second.negate)
	{
		return *this + (-second);
	}
	int relation = universalCompare(abs(*this), abs(second));
	if (relation == EQUAL)
	{
		return {};
	}
	if (relation == LESS)
	{
		return -(second - *this);
	}
	return sub(*this, second);
}

LN LN::sub(const LN &a, const LN &b)
{
	LN res = a;
	bool mindOne = false;
	size_t size = b.numberBlocks.size();
	for (size_t i = 0; i < size; i++)
	{
		res.numberBlocks[i] -= (b.numberBlocks[i] + mindOne);
		if (res.numberBlocks[i] > MAX_NUMBER)
		{
			mindOne = true;
			res.numberBlocks[i] = MAX_NUMBER - (MAX_NUMBER * 16 - res.numberBlocks[i]);
		}
		else
		{
			mindOne = false;
		}
	}
	if (mindOne)
	{
		for (size_t i = size; i < a.numberBlocks.size(); i++)
		{
			res.numberBlocks[i] -= mindOne;
			if ((res.numberBlocks[i] > MAX_NUMBER))
			{
				res.numberBlocks[i] = MAX_NUMBER - (MAX_NUMBER * 16 - res.numberBlocks[i]);
			}
			else
			{
				mindOne = false;
				break;
			}
		}
	}
	res.delZeroStart();
	return res;
}

LN LN::operator*(const LN &second) const
{
	if (isNaN || second.isNaN)
	{
		return makeNaN();
	}
	LN zero;
	if (*this == zero || second == zero)
	{
		return zero;
	}
	LN res = mul(abs(*this), abs(second));
	if (negate != second.negate)
	{
		return -res;
	}
	return res;
}

LN LN::mul(const LN &a, const LN &b)
{
	LN res;
	for (size_t i = 0; i < a.numberBlocks.size() + b.numberBlocks.size() + 1; i++)
	{
		res.pushBack(0);
	}
	for (size_t i = 0; i < b.numberBlocks.size(); i++)
	{
		for (size_t j = 0; j < a.numberBlocks.size(); j++)
		{
			uint64_t f = a.numberBlocks[j];
			uint64_t cur = f * b.numberBlocks[i] + res.numberBlocks[i + j];
			res.numberBlocks[i + j + 1] += cur / MAX_NUMBER;
			res.numberBlocks[i + j] = cur % MAX_NUMBER;
		}
	}
	res.delZeroStart();
	return res;
}

LN LN::operator/(const LN &second) const
{
	LN zero;
	if (isNaN || second.isNaN || *this == zero)
	{
		return makeNaN();
	}
	if (second == zero)
	{
		return zero;
	}
	int relation = universalCompare(abs(*this), abs(second));
	if (relation == EQUAL)
	{
		if (negate != second.negate)
		{
			return -1;
		}
		return 1;
	}
	if (relation == LESS)
	{
		return zero;
	}
	LN res;
	if (abs(second) == LN(1))
	{
		res = abs(*this);
	}
	else
	{
		//		if (numberBlocks.size() > 30)
		//		{
		//			size_t difference =
		//				numberBlocks.size() > second.numberBlocks.size()
		//					? numberBlocks.size() - second.numberBlocks.size()
		//					: second.numberBlocks.size() - numberBlocks.size();
		//			res = divBig(abs(*this), abs(second));
		//			goto sign;
		//		}
		res = div(abs(*this), abs(second));
	}
sign:
	if (negate != second.negate)
	{
		return -res;
	}
	return res;
}

LN LN::karatsuba_multiply(const LN &a, const LN &b)
{
	if (a.numberBlocks.size() <= 32 || b.numberBlocks.size() <= 32)
	{
		return mul(a, b);
	}

	size_t half = std::min(a.numberBlocks.size(), b.numberBlocks.size()) / 2;
	LN a1, b1, c1, d1;
	LN result;
	try
	{
		a1.numberBlocks = Vector< uint32_t >(half);
		b1.numberBlocks = Vector< uint32_t >(a.numberBlocks.size() - half);
		c1.numberBlocks = Vector< uint32_t >(half);
		d1.numberBlocks = Vector< uint32_t >(b.numberBlocks.size() - half);
		result.numberBlocks = Vector< uint32_t >(a.numberBlocks.size() + b.numberBlocks.size());
	} catch (std::bad_alloc &e)
	{
		throw Error(ERROR_OUT_OF_MEMORY, "No memory for Karatsuba multy");
	}
	for (size_t i = 0; i < half; i++)
	{
		a1.numberBlocks[i] = a.numberBlocks[a.numberBlocks.size() - i - 1];
		c1.numberBlocks[i] = b.numberBlocks[b.numberBlocks.size() - i - 1];
	}
	for (size_t i = half; i < a.numberBlocks.size(); i++)
	{
		b1.numberBlocks[i - half] = a.numberBlocks[a.numberBlocks.size() - i - 1];
	}
	for (size_t i = half; i < b.numberBlocks.size(); i++)
	{
		d1.numberBlocks[i - half] = b.numberBlocks[b.numberBlocks.size() - i - 1];
	}
	a1.numberBlocks.reverse(a1.numberBlocks.size());
	b1.numberBlocks.reverse(b1.numberBlocks.size());
	c1.numberBlocks.reverse(c1.numberBlocks.size());
	d1.numberBlocks.reverse(d1.numberBlocks.size());

	LN ac = karatsuba_multiply(a1, c1);
	LN bd = karatsuba_multiply(b1, d1);
	LN abcd = karatsuba_multiply(a1 + b1, c1 + d1) - ac - bd;

	for (size_t i = 0; i < result.numberBlocks.size(); i++)
	{
		result.numberBlocks[i] = 0;
	}
	for (size_t i = 0; i < ac.numberBlocks.size(); i++)
	{
		result.numberBlocks[2 * half + i] += ac.numberBlocks[i];
	}
	for (size_t i = 0; i < abcd.numberBlocks.size(); i++)
	{
		result.numberBlocks[half + i] += abcd.numberBlocks[i];
		if (result.numberBlocks[half + i] > MAX_NUMBER)
		{
			result.numberBlocks[half + i + 1] += result.numberBlocks[half + i] / MAX_NUMBER;
			result.numberBlocks[half + i] %= MAX_NUMBER;
		}
	}
//	for (size_t i = 0; i < bd.numberBlocks.size(); i++)
//	{
//		result.numberBlocks[i] += bd.numberBlocks[i];
//	}
	result += bd;
	result.delZeroStart();
	return result;
}

LN LN::div(const LN &a, const LN &b)
{
	LN l;
	LN r = a;
	LN one = 1_ln;
	while (l < r - one)
	{
		LN m = mid(l + r);
//				LN mul = m * b;
		LN mul = karatsuba_multiply(m, b);
		int relate = universalCompare(mul, a);
		if (relate == LESS)
		{
			l = m;
		}
		else if (relate == GREAT)
		{
			r = m;
		}
		else
		{
			return m;
		}
	}
	return l;
}

// LN LN::divBig(const LN &a, const LN &b)
//{
//	LN cur;
//	Vector< uint32_t > res;
//	for (size_t i = a.numberBlocks.size() - 1; i > a.numberBlocks.size() - b.numberBlocks.size(); i--)
//	{
//		cur.pushBack(a.numberBlocks[i]);
//	}
//	cur.numberBlocks.reverse(cur.numberBlocks.size());
//	for (size_t i = a.numberBlocks.size() - b.numberBlocks.size(); i < a.numberBlocks.size(); i--)
//	{
//		cur *= MAX_NUMBER;
//		cur += a.numberBlocks[i];
//		size_t ans = 0;
//		while (cur >= b)
//		{
//			cur -= b;
//			ans++;
//		}
//		res.push_back(ans);
//	}
//	res.reverse(res.size());
//	LN fin;
//	fin.numberBlocks = res;
//	fin.delZeroStart();
//	return fin;
// }

LN LN::operator%(const LN &second) const
{
	LN res = abs(*this) - abs(*this / second * second);
	if (negate != second.negate)
	{
		return -res;
	}
	return res;
}

LN LN::operator~() const
{
	if (abs(*this) == LN())
	{
		return {};
	}
	if (negate)
	{
		return makeNaN();
	}
	return sqrt(*this);
}

LN LN::sqrt(const LN &a)
{
	LN l;
	LN r = a;
	LN one = 1_ln;
	while (l < r - one)
	{
		LN m = mid(l + r);
		if (m * m <= a)
		{
			l = m;
		}
		else
		{
			r = m;
		}
	}
	return l;
}

LN LN::mid(const LN &a)
{
	LN res = a;
	bool mindOne = false;
	size_t size = res.numberBlocks.size();
	for (size_t i = size - 1; i < size; i--)
	{
		long long cur = (!mindOne ? 0 : MAX_NUMBER) + res.numberBlocks[i];
		res.numberBlocks[i] = cur / 2;
		mindOne = cur % 2;
	}
	res.delZeroStart();
	return res;
}

LN LN::operator-() const
{
	if (*this == 0_ln)
	{
		return {};
	}
	LN res = *this;
	res.negate = !res.negate;
	return res;
}

LN LN::abs(const LN &a)
{
	if (a >= LN())
	{
		return a;
	}
	return -a;
}

void LN::delZeroStart()
{
	size_t pos = numberBlocks.size() - 1;
	while (numberBlocks[pos] == 0 && pos > 0)
	{
		pos--;
		numberBlocks.pop_back();
	}
}

int LN::universalCompare(const LN &a, const LN &b)
{
	if (a.isNaN || b.isNaN)
	{
		return WRONG;
	}
	if (a.negate && !b.negate)
	{
		return LESS;
	}
	if (!a.negate && b.negate)
	{
		return GREAT;
	}
	if (a.numberBlocks.size() != b.numberBlocks.size())
	{
		if (a.negate && a.numberBlocks.size() < b.numberBlocks.size() ||
			!a.negate && a.numberBlocks.size() > b.numberBlocks.size())
		{
			return GREAT;
		}
		return LESS;
	}
	int res = EQUAL;
	size_t size = a.numberBlocks.size();
	for (size_t i = size - 1; i < size; i--)
	{
		if (a.numberBlocks[i] < b.numberBlocks[i])
		{
			res = LESS;
			break;
		}
		if (a.numberBlocks[i] > b.numberBlocks[i])
		{
			res = GREAT;
			break;
		}
	}
	if (res == EQUAL)
	{
		return EQUAL;
	}
	if (res == GREAT && !a.negate || res == LESS && a.negate)
	{
		return GREAT;
	}
	return LESS;
}

LN LN::makeNaN()
{
	LN ln;
	ln.isNaN = true;
	return ln;
}

LN::operator long long() const
{
	if (isNaN)
	{
		throw Error(ERROR_DATA_INVALID, "NaN is not a number");
	}
	LN max = LN(9223372036854775807);
	if (abs(*this) > max)
	{
		throw Error(ERROR_DATA_INVALID, "Too big number for long long");
	}
	long long res = 0;
	for (size_t i = 0; i < numberBlocks.size(); i++)
	{
		res *= MAX_NUMBER;
		res += numberBlocks[i];
	}
	return res;
}

LN::operator bool() const
{
	if (*this == LN() || isNaN)
	{
		return false;
	}
	return true;
}

Vector< char > LN::toString() const
{
	Vector< char > res;
	if (isNaN)
	{
		pushBack(res, 'N');
		pushBack(res, 'a');
		pushBack(res, 'N');
		return res;
	}
	if (*this == LN())
	{
		pushBack(res, '0');
		return res;
	}
	if (negate)
	{
		pushBack(res, '-');
	}
	for (size_t i = numberBlocks.size() - 1; i < numberBlocks.size(); i--)
	{
		uint32_t curNumber = numberBlocks[i];
		size_t cnt = 0;
		while (curNumber != 0)
		{
			uint32_t digit = curNumber % 16;
			if (digit < 10)
			{
				pushBack(res, '0' + digit);
			}
			else
			{
				pushBack(res, 'A' + digit - 10);
			}
			curNumber /= 16;
			cnt++;
		}
		if (i != numberBlocks.size() - 1)
		{
			for (size_t k = cnt; k < 7; k++)
			{
				pushBack(res, '0');
			}
			cnt = 7;
		}
		res.reverse(cnt);
	}
	return res;
}

void LN::pushBack(Vector< char > &str, char sym)
{
	try
	{
		str.push_back(sym);
	} catch (std::bad_alloc &e)
	{
		throw Error(ERROR_OUT_OF_MEMORY, "Push back error");
	}
}
