#include <iostream>
using namespace std;

int main() {
   int t, n, temp;
   int max = -(std::numeric_limits<int>::max());
   cin >> t;
   for(int i = 0; i < t; i++) {
      cin >> n;
      for(int j = 0; j < n; j++) {
         cin >> temp;
         if(max < temp)
            max = temp;
      }
      cout << max;
      max = -(std::numeric_limits<int>::max());
   }
   return 0;
}