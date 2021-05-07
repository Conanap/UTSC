package channelBrowser;

import java.util.TreeMap;
import java.util.NavigableSet;
import java.util.Iterator;

// Concrete Iterator
public class ConcreteChannelIterator implements ChannelIterator {

  private TreeMap<Float, String> channels;
  private NavigableSet<Float> key;
  private Iterator<Float> i;
  private int currentPos = 0;
  private float prev;

  public ConcreteChannelIterator(TreeMap<Float, String> channels) {
    this.channels = channels;
    this.key = channels.navigableKeySet();
    this.i = this.key.iterator();
  }

  public boolean hasNext() {
    return i.hasNext();
  }

  public String next() {
    return this.channels.get(i.next());
  }
}
