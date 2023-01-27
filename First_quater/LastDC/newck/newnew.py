import socket

host = '10.8.0.1'
port = 666                  # The same port as used by the server
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# s = socket.socket()
s.bind((host, port))
s.listen(10)
# s.sendall(b'Hello, world')
print(s.recv(200))
s.close()