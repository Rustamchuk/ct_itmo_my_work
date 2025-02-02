from requests import Session


a = "http://10.8.0.1/"
headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 YaBrowser/22.11.0.2419 Yowser/2.5 Safari/537.36"}
session = Session()

for i in range(10011):
    b = session.post(a, headers=headers, allow_redirects=True)
    if i > 9900:
        print(b.text)
    if i % 1000 == 0:
        print(i)
