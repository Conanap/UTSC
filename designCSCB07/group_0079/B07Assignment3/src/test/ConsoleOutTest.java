// **********************************************************
// Assignment3:
// UTORID user_name: fungalbi
//
// Author: Albion Ka Hei Fung
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// *********************************************************
package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import javaIO.ConsoleOut;

public class ConsoleOutTest {
  private final String separator =
      "----------------------------------------------------------------"
          + "-------\n";

  private ConsoleOut co;
  private ByteArrayOutputStream oc;
  private String expected;

  @Before
  public void setup() {
    co = new ConsoleOut();
    oc = new ByteArrayOutputStream();
    System.setOut(new PrintStream(oc));
    expected = separator;
  }

  @Test
  public void testSendEOF() {
    Field field;
    StringBuilder sb;
    try {
      field = co.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = new StringBuilder();
      sb.append("hi");
      field.set(co, sb);
    } catch (Exception e) {
      fail("Couldn't get field");
      return;
    }
    co.sendEOF();
    assertEquals("hi\n", oc.toString());
  }

  @Test
  public void testSendOutputTwoPublications() {
    String[] out = {"Cape Baldy", "666", "4",
        "One Punch Notebook\nSale guide\n", "13", "0"};
    co.sendOutput(out);
    Field field;
    StringBuilder sb;
    try {
      field = co.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(co);
    } catch (Exception e) {
      System.out.println(e);
      fail("Couldn't get field");
      return;
    }
    String compout = sb.toString();
    expected += ("1. Name of Author:\nCape Baldy\n2. Number of All Cita"
        + "tions:\n666\n3. Number of i10-index after 2009:\n4\n"
        + "4. Title of the first 3 publications:\n1- One Punch Notebook\n"
        + "2- Sale guide\n5. Total paper citation (first 5 papers):\n"
        + "13\n6. Total Co-authors:\n0\n");
    assertEquals(expected, compout);
  }


  @Test
  public void testSendOutputThreePublications() {
    String[] out = {"Cape Baldy", "666", "4",
        "One Punch Notebook\nSale guide\nPretend to be Charanko\n", "13", "0"};
    co.sendOutput(out);
    Field field;
    StringBuilder sb;
    try {
      field = co.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(co);
    } catch (Exception e) {
      fail("Couldn't get field");
      return;
    }
    String compout = sb.toString();
    expected += ("1. Name of Author:\nCape Baldy\n2. Number of All Cita"
        + "tions:\n666\n3. Number of i10-index after 2009:\n4\n"
        + "4. Title of the first 3 publications:\n1- One Punch Notebook\n"
        + "2- Sale guide\n3- Pretend to be Charanko\n5. Total paper citation"
        + " (first 5 papers):\n" + "13\n6. Total Co-authors:\n0\n");
    assertEquals(expected, compout);
  }

  @Test
  public void testSendError() {
    co.sendError("you wot m8");
    Field field;
    StringBuilder sb;
    try {
      field = co.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(co);
    } catch (Exception e) {
      fail("Couldn't get field");
      return;
    }

    String compout = sb.toString();
    String exp = "you wot m8\n";
    assertEquals(exp, compout);
  }

  @Test
  public void testSendCoauthor() {
    ArrayList<String> out = new ArrayList<String>();
    out.add("Demon Cyborg");
    out.add("Waste King");
    out.add("Silver Fang");
    out.add("Pri-Pri-Prisoner");
    co.sendCoauthor(out);

    Field field;
    StringBuilder sb;
    try {
      field = co.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(co);
    } catch (Exception e) {
      fail("Couldn't get field");
      return;
    }
    String compout = sb.toString();
    expected += ("7. Co-Author list sorted: (Total: 4):\n"
        + "Demon Cyborg\nPri-Pri-Prisoner\nSilver Fang\nWaste King\n");
    assertEquals(expected, compout);
  }

}
