package channelBrowser;

// Aggregate interface
public interface TV extends Iterable<String>{

  public ChannelIterator getIterator();

  // other TV methods

}
