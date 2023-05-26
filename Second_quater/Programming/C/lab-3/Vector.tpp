template< typename T >
void Vector<T>::push_back(const T &obj)
{
	if (sizeData >= spaceData)
	{
		if (spaceData == 0)
		{
			spaceData++;
		}
		spaceData *= 2;
		juggleArr(spaceData);
	}
	data[sizeData++] = obj;
}

template< typename T >
void Vector< T >::pop_back()
{
	if (sizeData == 0)
	{
		throw Error(ERROR_DATA_INVALID, "Pop for empty arr");
	}
	sizeData--;
	if (sizeData <= spaceData / 2)
	{
		spaceData /= 2;
		juggleArr(spaceData);
	}
}

template< typename T >
void Vector< T >::reverse(size_t cnt)
{
	for (size_t j = 0; j < cnt / 2; j++)
	{
		size_t l = sizeData - cnt + j;
		size_t r = sizeData - j - 1;
		T a = data[l];
		data[l] = data[r];
		data[r] = a;
	}
}

template< typename T >
void Vector< T >::clear()
{
	delete[] data;
	data = nullptr;
	sizeData = 0;
	spaceData = 0;
}

template< typename T >
T Vector< T >::back()
{
	if (sizeData == 0)
	{
		throw Error(ERROR_DATA_INVALID, "Back for empty arr");
	}
	return data[sizeData - 1];
}

template< typename T >
T &Vector< T >::operator[](const size_t i) const
{
	if (i >= sizeData)
	{
//		T a = i;
//		return a;
		throw Error(ERROR_DATA_INVALID, "index out of bounds");
	}
	return data[i];
}

template< typename T >
Vector< T > &Vector< T >::operator=(const Vector &vec)
{
	clear();
	sizeData = vec.sizeData;
	spaceData = vec.spaceData;
	data = new T[spaceData];
	for (size_t i = 0; i < sizeData; i++)
	{
		data[i] = vec.data[i];
	}
	return *this;
}

template< typename T >
void Vector< T >::juggleArr(size_t newSize)
{
	T *newData = new T[newSize];
	for (size_t i = 0; i < sizeData; i++)
	{
		newData[i] = data[i];
	}
	delete[] data;
	data = newData;
}