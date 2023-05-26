#include "Error.h"
#include "return_codes.h"

#include <cstdlib>

template< typename T >
class Vector
{
  public:
	Vector() = default;
	Vector(size_t size)
	{
		data = new T[size];
		sizeData = spaceData = size;
	}
	~Vector() { clear(); }

	void push_back(const T &obj);

	void pop_back();

	void clear();
	bool empty() { return sizeData == 0; }

	void reverse(size_t cnt);

	size_t size() const { return sizeData; }

	T back();

	T &operator[](const size_t i) const;

	Vector< T > &operator=(const Vector &vec);

  private:
	T *data = nullptr;
	size_t sizeData = 0;
	size_t spaceData = 0;

	void juggleArr(size_t newSize);
};

#include "Vector.tpp"