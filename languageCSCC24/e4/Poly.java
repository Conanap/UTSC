package poly;

import java.util.Arrays;
import java.util.List;

public class Poly {

  /**
   * Demo the use of the required classes.
   * @param args the usual
   */
  public static void main(String[] args) {
    List<String> xs1 = Arrays.asList(new String [] {"aaa", "bbb", "ccc"});
    List<String> ys1 = Arrays.asList(new String [] {"ddd", "eee", "fff"});

    for (String item: Interleaver.interleave(xs1, ys1)) {
      System.out.print(String.format("%s ", item));
    }
    System.out.println();
    System.out.println(Interleaver.interleave(xs1, ys1));
    
    List<Integer> xs2 = Arrays.asList(new Integer [] {1, 3, 5});
    List<Integer> ys2 = Arrays.asList(new Integer [] {42, 47, 50});

    for (Integer item: Interleaver.interleave(xs2, ys2)) {
      System.out.print(String.format("%s ", item));
    }
    System.out.println();
    System.out.println(Interleaver.interleave(xs2, ys2));
    
    List<String> xs3 = Arrays.asList(new String [] {"AAA", "BBB", "CCC"});
    List<Integer> ys3 = Arrays.asList(new Integer [] {1, 2, 3});
    for (Pair<String, Integer> item: Interleaver.toPairs(xs3, ys3)) {
      System.out.print(String.format("(%s,%s) ", item.getFirst(), item.getSecond()));
    }
    System.out.println();
    System.out.println(Interleaver.toPairs(xs3, ys3));
    
    List<Integer> xs4 = Arrays.asList(new Integer [] {1, 2, 3});
    List<String> ys4 = Arrays.asList(new String [] {"AAA", "BBB", "CCC"});
    for (Pair<Integer, String> item: Interleaver.toPairs(xs4, ys4)) {
      System.out.print(String.format("(%s,%s) ", item.getFirst(), item.getSecond()));
    }
    System.out.println();
    System.out.println(Interleaver.toPairs(xs4, ys4));
  }

  /*
   * Output (EXACTLY):
aaa ddd bbb eee ccc fff
[aaa, ddd, bbb, eee, ccc, fff]
1 42 3 47 5 50
[1, 42, 3, 47, 5, 50]
(AAA,1) (BBB,2) (CCC,3)
[(AAA,1), (BBB,2), (CCC,3)]
(1,AAA) (2,BBB) (3,CCC)
[(1,AAA), (2,BBB), (3,CCC)]
   */
}
