// **********************************************************
// Assignment3:
// UTORID user_name: fungalbi
//
// Author: Albion Ka Hei Fung
//
//
// Honor Code: I pledge that this program represents my own
// program fode and that I have foded on my own. I received
// help from no one in designing and debugging my program.
// *********************************************************
package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javaIO.FileOut;

public class FileOutTest {

  private String expected;
  private FileOut fo;
  private final String separator =
      "----------------------------------------------------------------"
          + "-------\n";
  private final String name = "fungalbi1002444321ConTestFileForFOTestUnitTest";

  @Before
  public void setup() {
    fo = new FileOut(name);
    expected = separator;
  }

  @After
  public void tearDown() {
    File file = new File(name);
    file.delete();
  }

  @Test
  public void testSendEOF() {
    Field field;
    String out = "";
    String temp;
    BufferedReader br;
    StringBuilder sb = new StringBuilder();
    sb.append("Yoro");
    try {
      field = fo.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      field.set(fo, sb);
    } catch (Exception e) {
      fail("can't get field");
      return;
    }
    fo.sendEOF();
    try {
      br = new BufferedReader(new FileReader(name));
    } catch (FileNotFoundException e) {
      fail("didn't create file");
      return;
    }
    try {
      temp = br.readLine();
      while (temp != null) {
        out += temp;
        temp = br.readLine();
        br.close();
      }
    } catch (IOException e) {
      fail("IOException");
      return;
    }
    assertEquals("Yoro", out);
  }

  @Test
  public void testSendOutputTwoPublications() {
    String[] out = {"Cape Baldy", "666", "4",
        "One Punch Notebook\nSale guide\n", "13", "0"};
    fo.sendOutput(out);
    Field field;
    StringBuilder sb;
    try {
      field = fo.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(fo);
    } catch (Exception e) {
      fail("can't get field");
      return;
    }
    String fompout = sb.toString();
    expected += ("1. Name of Author:\nCape Baldy\n2. Number of All Cita"
        + "tions:\n666\n3. Number of i10-index after 2009:\n4\n"
        + "4. Title of the first 3 publications:\n1- One Punch Notebook\n"
        + "2- Sale guide\n5. Total paper citation (first 5 papers):\n"
        + "13\n6. Total Co-authors:\n0\n");
    assertEquals(expected, fompout);
  }


  @Test
  public void testSendOutputThreePublications() {
    String[] out = {"Cape Baldy", "666", "4",
        "One Punch Notebook\nSale guide\nPretend to be Charanko\n", "13", "0"};
    fo.sendOutput(out);
    Field field;
    StringBuilder sb;
    try {
      field = fo.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(fo);
    } catch (Exception e) {
      fail("can't get field");
      return;
    }
    String fompout = sb.toString();
    expected += ("1. Name of Author:\nCape Baldy\n2. Number of All Cita"
        + "tions:\n666\n3. Number of i10-index after 2009:\n4\n"
        + "4. Title of the first 3 publications:\n1- One Punch Notebook\n"
        + "2- Sale guide\n3- Pretend to be Charanko\n5. Total paper citation"
        + " (first 5 papers):\n" + "13\n6. Total Co-authors:\n0\n");
    assertEquals(expected, fompout);
  }

  @Test
  public void testSendError() {
    fo.sendError("you wot m8");
    Field field;
    StringBuilder sb;
    try {
      field = fo.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(fo);
    } catch (Exception e) {
      fail("can't get field");
      return;
    }

    String fompout = sb.toString();
    String exp = "you wot m8\n";
    assertEquals(exp, fompout);
  }

  @Test
  public void testSendCoauthor() {
    ArrayList<String> out = new ArrayList<String>();
    out.add("Demon Cyborg");
    out.add("Waste King");
    out.add("Silver Fang");
    out.add("Pri-Pri-Prisoner");
    fo.sendCoauthor(out);

    Field field;
    StringBuilder sb;
    try {
      field = fo.getClass().getSuperclass().getDeclaredField("sb");
      field.setAccessible(true);
      sb = (StringBuilder) field.get(fo);
    } catch (Exception e) {
      fail("can't get field");
      return;
    }
    String fompout = sb.toString();
    expected += ("7. Co-Author list sorted: (Total: 4):\n"
        + "Demon Cyborg\nPri-Pri-Prisoner\nSilver Fang\nWaste King\n");
    assertEquals(expected, fompout);
  }

}
