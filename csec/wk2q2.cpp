#include <iostream>
using namespace std;

int main() {
    string input;
    char search;
    int count;

    cin >> input;
    cin >> search;

    for (char c : input) {
        if(c == search)
            count++;
    }
    
    cout <<count;
}