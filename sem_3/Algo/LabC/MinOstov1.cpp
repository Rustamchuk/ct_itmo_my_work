#include <iostream>
#include <vector>
#include <cmath>
#include <iomanip>
#include <queue>

using namespace std;

struct Point {
    int x, y;
};

double distance(const Point& a, const Point& b);

int n;

int main() {
    cin >> n;
    vector<Point> points(n);
    for (int i = 0; i < n; i++) {
        std::cin >> points[i].x >> points[i].y;
    }

    vector<double> min_distance(n, numeric_limits<double>::max());
    vector<bool> visited(n, false);
    min_distance[0] = 0;
    double total_weight = 0;
    int vertex;
    double dist;

    for (int i = 0; i < n; i++) {
        vertex = -1;
        for (int j = 0; j < n; j++) {
            if (!visited[j] && (vertex == -1 || min_distance[j] < min_distance[vertex])) {
                vertex = j;
            }
        }

        visited[vertex] = true;
        total_weight += min_distance[vertex];

        for (int j = 0; j < n; j++) {
            if (!visited[j]) {
                dist = distance(points[vertex], points[j]);
                min_distance[j] = min(min_distance[j], dist);
            }
        }

        vertex += dist;
        dist = vertex - dist;
        vertex -= dist;
    }
    cout << setprecision(10) << total_weight << endl;
    return 0;
}

double distance(const Point& a, const Point& b) {
    return sqrt(std::pow(a.x - b.x, 2) + std::pow(a.y - b.y, 2));
}
