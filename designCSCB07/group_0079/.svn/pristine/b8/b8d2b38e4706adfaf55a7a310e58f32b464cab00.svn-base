package channelBrowser;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Random;

// Concrete Aggregate
public class ConcreteTV implements TV {
  private TreeMap<Float, String> channels = new TreeMap<Float, String>();
  
  public ConcreteTV(List<String> c) {
    Iterator<String> i = c.iterator();
    Random random = new Random();
    while (i.hasNext()) {
      String s = i.next();
      channels.put(random.nextFloat(), s);
    }
  }

  public ChannelIterator getIterator() {
    return new ConcreteChannelIterator(channels);
  }

  public Iterator<String> iterator() {
    return this.getIterator();
  }
}
