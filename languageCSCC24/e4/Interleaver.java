package poly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Interleaver {

  /**
  * Interleaves 2 lists.
  * @param li1 A list to interleave
  * @param li2 A list to interleave, must be same type as li1
  * @return List of interleaved items
  */
  public static <T> List<T> interleave(List<T> li1, List<T> li2) {
    List<T> ret = new ArrayList<T>();
    Iterator<T> it1 = li1.iterator();
    Iterator<T> it2 = li2.iterator();

    while (it1.hasNext() && it2.hasNext()) {
      ret.add(it1.next());
      ret.add(it2.next());
    }

    return ret;
  }

  /**
  * Makes lists into list of pairs.
  * @param li1 First list to make into pair
  * @param li2 Second list to make into pair
  * @return The list of pairs
  */
  public static <T1, T2> List<Pair<T1, T2>> toPairs(List<T1> li1, List<T2> li2) {
    List<Pair<T1, T2>> ret = new ArrayList<Pair<T1, T2>>();
    Iterator<T1> it1 = li1.iterator();
    Iterator<T2> it2 = li2.iterator();

    while (it1.hasNext() && it2.hasNext()) {
      ret.add(new Pair<T1, T2>(it1.next(), it2.next()));
    }

    return ret;
  }
}
