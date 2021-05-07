package traders;

import java.util.Observable;
import java.util.Observer;

public class BearTrader implements Observer {

  private String name;
  
  public BearTrader(String name) {
    this.name = name;
  }

  @Override
  public void update(Observable o, Object price) {
    // TODO Auto-generated method stub
    if((int)price < 50)
      System.out.println("This is "+this.name+
          " and I want to buy safe stuff!");
  }

}
