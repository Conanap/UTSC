// Albion Ka Hei Fung
// June 6, 2016
// v0.0.1
//1002444321
package aircraft;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FlightDataRecorderTest {

  @Test
  public void testAverage() {
    // no element ave
    FlightDataRecorder r = new FlightDataRecorder();
    assertEquals(0, r.average(), 0f);
    // 1 element ave
    r.record(1.0);
    assertEquals(1, r.average(), 0f);
    // 2 element ave
    r.record(2.0);
    assertEquals(1.5, r.average(), 0f);
  }

  @Test
  public void testGetLastDataPoints() {
    FlightDataRecorder r = new FlightDataRecorder();
    List <Double> t = new ArrayList<Double>();
    // no elements
    assertEquals(t.size(), r.getLastDataPoints(0).size());
    r.record(2.0);
    // 1 element
    t = r.getLastDataPoints(1);
    // test value
    assertEquals(2.0, t.get(0), 0f);
    // test size
    assertEquals(1, t.size(), 0f);
    // 2 elements
    r.record(3.0);
    t = r.getLastDataPoints(2);
    // test data point
    assertEquals(3.0, t.get(1), 0f);
    // test size
    assertEquals(2, t.size(), 0);
  }
  
  @Test
  public void testGetRecordedData() {
    FlightDataRecorder r = new FlightDataRecorder();
    List <Double> t;
    // testing 5 elements
    for (double i = 0.0; i < 5; i += 1.0) {
      r.record(i);
    }
    t = r.getRecordedData();
    // make sure each element is correct
    for (double i = 0.0; i < 5; i += 1.0) {
      assertEquals(i, t.get((int)(i)), 0);
    }
  }
  
  @Test
  public void testRecord() {
    FlightDataRecorder r = new FlightDataRecorder();
    r.record(1.0);
    // 1 element test, assumes get last data points work
    List <Double> t = r.getLastDataPoints(1);
    assertEquals(1.0, t.get(0), 0f);
    for (double i = 2.0; i <= 11.0; i+= 1.0){
      r.record(i);
    }
    
  }
}
