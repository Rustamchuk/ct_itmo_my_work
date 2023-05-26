#include "Vector.h"
#include <string_view>

const uint32_t MAX_NUMBER = 268435456;	  // 16^7

const int EQUAL = 0;
const int GREAT = 1;
const int LESS = -1;
const int WRONG = -2;

class LN
{
  public:
	LN(long long number = 0);
	LN(const char *str) { parseString(str); }
	LN(std::string_view str) { parseString(str); }
	LN(const LN &ln) { copy(ln); }
	LN(LN &&ln)
	{
		copy(ln);
		ln.clear();
	}

	LN &operator=(const LN &second)
	{
		copy(second);
		return *this;
	}
	LN &operator=(LN &&second);

	LN operator+(const LN &second) const;
	void operator+=(const LN &second) { *this = *this + second; }

	LN operator-(const LN &second) const;
	void operator-=(const LN &second) { *this = *this - second; }

	LN operator*(const LN &second) const;
	void operator*=(const LN &second) { *this = *this * second; }

	LN operator/(const LN &second) const;
	void operator/=(const LN &second) { *this = *this / second; }

	LN operator%(const LN &second) const;
	void operator%=(const LN &second) { *this = *this % second; }

	LN operator~() const;
	LN operator-() const;

	int operator>(const LN &second) const { return universalCompare(*this, second) == GREAT; }
	int operator>=(const LN &second) const
	{
		int r = universalCompare(*this, second);
		return r == GREAT || r == EQUAL;
	}
	int operator<(const LN &second) const { return universalCompare(*this, second) == LESS; }
	int operator<=(const LN &second) const
	{
		int r = universalCompare(*this, second);
		return r == LESS || r == EQUAL;
	}
	int operator==(const LN &second) const { return universalCompare(*this, second) == EQUAL; }
	int operator!=(const LN &second) const { return universalCompare(*this, second) != EQUAL; }

	operator long long() const;
	operator bool() const;

	Vector< char > toString() const;

  private:
	bool negate = false;
	bool isNaN = false;

	Vector< uint32_t > numberBlocks;

	void parseString(std::string_view str);

	void copy(const LN &ln);
	void clear();
	static LN makeNaN();

	void pushBack(uint32_t number);
	static void pushBack(Vector< char > &str, char sym);

	static int universalCompare(const LN &a, const LN &b);

	static LN sum(const LN &a, const LN &b);
	static LN sub(const LN &a, const LN &b);
	static LN mul(const LN &a, const LN &b);
	static LN div(const LN &a, const LN &b);
//	static LN divBig(const LN &a, const LN &b);
	static LN sqrt(const LN &a);

	static LN abs(const LN &a);
	static LN mid(const LN &a);
	void delZeroStart();

	static LN karatsuba_multiply(const LN &a, const LN &b);
};

inline LN operator"" _ln(const char *str)
{
	return str;
}