#include <iostream>

using namespace std;

struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

int main() {
    ListNode *list1;
    list1 = new ListNode(1);
    list1->next = new ListNode(2);
    list1->next->next = new ListNode(4);
    ListNode *list2;
    list2 = new ListNode(1);
    list2->next = new ListNode(3);
    list2->next->next = new ListNode(4);

    ListNode *ans;
    if (list1->val < list2->val) {
        ans = list1;
        list1 = list1->next;
    } else {
        ans = list2;
        list2 = list2->next;
    }
    while (list1 && list2) {
        if (list1->val < list2->val) {
            list1 = list1->next;
            continue;
        }
        list2 = list2->next;
    }
    while (list1) {
        list1 = list1->next;
    }
    while (list2) {
        list2 = list2->next;
    }
    cout << ans->val;
}