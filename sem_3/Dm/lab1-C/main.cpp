#include <iostream>
#include <vector>
#include <string>
#include <queue>
#include <stack>

int check(std::vector<std::string>& matrix, int i, int j) {
    if (i < j) std::swap(i, j);
    return matrix[i][j] == '1';
}

void reverse(std::deque<int> queue, int index) {
    int l = queue.begin() + index;
    for (int k = queue.begin() + 1; k < l; k++) {
        queue[k], queue[l] = queue[l], queue[k];
        l--;
    }
}

int main() {
    int n;
    std::cin >> n;

    std::vector<std::string> mat(n);


    for (int i = 0; i < n - 1; i++) {
        std::cin >> mat[i + 1];
    }

    std::deque<int> queue;
    for (int i = 0; i < n; i++) {
        queue.push_back(i);
    }

    for (int i = 0; i < n * (n - 1); i++) {
        if (!check(mat, queue[0], queue[1])) {
            int index = 2;
            while (!check(mat, queue[0], queue[index]) || !check(mat, queue[1], queue[index + 1])) {
                index++;
            }
            reverse(queue, queue.begin() + 1, queue.begin() + index + 1);
        }
        queue.push_back(queue[0]);
        queue.pop_front();
    }

    for (auto i : queue) {
        std::cout << i + 1 << " ";
    }

    return 0;
}