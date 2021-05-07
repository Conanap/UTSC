package traders;

import java.util.ArrayList;
public abstract class TradeObservable {
  private ArrayList<TradeObserver> tradeObserverCollection = 
      new ArrayList<TradeObserver>();
  
  public abstract int getPrice();
  
  public void registerObserver(TradeObserver o) {
    if(!tradeObserverCollection.contains(o))
      tradeObserverCollection.add(o);
  }
  
  public void unregisterObserver(TradeObserver o) {
    tradeObserverCollection.remove(o);
  }
  
  public void notifyObservers() {
    int price = this.getPrice();
    for(TradeObserver x: tradeObserverCollection)
      x.update(price);
  }
}
