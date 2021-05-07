package channelBrowser;

import java.util.Arrays;
import java.util.List;

public class TVBrowser {
  public static void main(String[] args) {
    List<String> channels =
        Arrays.asList(new String[] {"BBC", "CBC", "CNN", "ERT"});
    TV aNewTV = new ConcreteTV(channels);
    ChannelIterator i = aNewTV.getIterator();
    while (i.hasNext()) {
      String someChannel = i.next();
      System.out.println(someChannel);
    }
    System.out.println("Let's print them all once more!");
    i = aNewTV.getIterator();
    while (i.hasNext()) {
      String someChannel = i.next();
      System.out.println(someChannel);
    }
    System.out.println("Oups.");
  }
}
