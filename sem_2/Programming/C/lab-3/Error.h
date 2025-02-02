#include <exception>

class Error : std::exception
{
  public:
	Error(const int newCode, const char *msg)
	{
		code = newCode;
		message = msg;
	}

	int getCode() { return code; }
	const char *getMessage() { return message; }

  private:
	int code;
	const char *message;
};