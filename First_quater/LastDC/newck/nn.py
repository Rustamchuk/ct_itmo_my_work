import socket

sock = socket.socket()
sock.bind(('10.8.0.214', 666))
while True:
    line = sock.recv(100)
    print(line)