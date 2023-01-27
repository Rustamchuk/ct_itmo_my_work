import socket

host = "10.8.0.1"
port = 1234                  # The same port as used by the server
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((host, port))
# s.sendall(b'Hello, world')
data = s.recv(200)
print('Received', repr(data))
print(s.recv(200))
print(s.recv(200))
print(s.recv(200))
for i in range(1, 61):
    s.sendall(str(eval(s.recv(200))).encode("utf-8"))
    s.recv(200)
print(s.recv(1024))
print(s.recv(1024))
s.close()