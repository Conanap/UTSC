package traders;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class Ouzo extends Observable {

  private ArrayList<Observer> tradeObserverCollection = 
      new ArrayList<Observer>();
  
  public int getPrice() {
    // TODO Auto-generated method stub
    Random random = new Random();
    return random.nextInt(101);
  }

  public void priceChange() {
    this.setChanged();
    System.out.println("Ouzo price changed!");
    this.notifyObservers(this.getPrice());
    this.clearChanged();
  }
  
  @Override
  public void notifyObservers(Object price) {
    for(Observer x: tradeObserverCollection) {
      x.update(this, price);
    }
  }
  
  @Override
  public void addObserver(Observer o) {
    if(!tradeObserverCollection.contains(o))
      tradeObserverCollection.add(o);
  }
}
