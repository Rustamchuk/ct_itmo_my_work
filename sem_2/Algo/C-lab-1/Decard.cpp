#include <iostream>
#include <algorithm>
#include <string>
#include <vector>
#include <ctime>
#include <map>

typedef long long ll;



int main() {
  std::ios_base::sync_with_stdio(0);
  ll n, c, k, y = 0;
  std::cin >> n;
  pnode p = nullptr;
  std::vector<std::string> res(n);
  std::vector<triple<ll, ll, ll>> data;

  for (int i = 0; i < n; i++) {
    std::cin >> k >> y;
    data.push_back(triple<ll, ll, ll>(k, y, i + 1));
  }
  std::sort(data.begin(), data.end(), cmp);
  p = fast(data);

  std::cout << "YES\n";
  print(p, 0, res);

  for (int i = 0; i < n; i++) {
    std::cout << res[i] << "\n";
  }
  return 0;
}