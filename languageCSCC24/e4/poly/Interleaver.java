package poly;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Interleaver {
    public static <T> List<T> interleave(List<T> li1, List<T> li2) {
        List<T> ret = new ArrayList<T>();
        Iterator<T> it1 = li1.iterator();
        Iterator<T> it2 = li2.iterator();

        while(it1.hasNext() && it2.hasNext()) {
            ret.add(it1.next());
            ret.add(it2.next());
        }

        return ret;
    }

    public static <T1, T2> List<Pair<T1, T2>> toPairs(List<T1> li1, List<T2> li2) {
        List<Pair<T1, T2>> ret = new ArrayList<Pair<T1, T2>>();
        Iterator<T1> it1 = li1.iterator();
        Iterator<T2> it2 = li2.iterator();

        while(it1.hasNext() && it2.hasNext())
            ret.add(new Pair<T1, T2>(it1.next(), it2.next()));

        return ret;
    }
}
