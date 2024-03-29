// **********************************************************
// Assignment0:
// UTORID: fungalbi
// UT Student #: 1002444321
// Author: Albion Ka Hei Fung
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences. In this semester
// we will select any three of your assignments from total of 5 and run it
// for plagiarism check.
// *********************************************************
// Albion Ka Hei Fung
// v0.0.3c
package driver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import a0.Cfiltering;

public class CfilteringDriver {

  /**
   * Reads user movie ratings from a text file, calculates similarity scores and
   * prints a score matrix.
   */
  public static void main(String[] args) {
    try {

      // open file to read
      String fileName;
      Scanner in = new Scanner(System.in);
      System.out.println("Enter the name of input file? ");
      fileName = in.nextLine();
      FileInputStream fStream = new FileInputStream(fileName);
      BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

      // Read dimensions: number of users and number of movies
      int numberOfUsers = Integer.parseInt(br.readLine());
      int numberOfMovies = Integer.parseInt(br.readLine());

      /*
       * create a new Cfiltering object that contains: a) 2d matrix
       * i.e.userMovieMatrix (#users*#movies) b) 2d matrix i.e. userUserMatrix
       * (#users*#users)
       */
      Cfiltering cfObject = new Cfiltering(numberOfUsers, numberOfMovies);
      // this is a blankline being read
      br.readLine();

      // read each line of movie ratings and populate the
      // userMovieMatrix
      String row;
      int x = 0, y = 0;
      int temp;
      while ((row = br.readLine()) != null) {
        // allRatings is a list of all String numbers on one row
        String allRatings[] = row.split(" ");
        for (String singleRating : allRatings) {
          // make the String number into an integer
          // populate userMovieMatrix
          // casting rating string into an int
          temp = Integer.parseInt(singleRating);
          // add the int into the user movie matrix
          cfObject.populateUserMovieMatrix(y, x, temp);
          // iterate horizontal index
          x++;
        }
        // iterate vertical index, reset horizontal index
        x = 0;
        y++;
      }
      // close the file and stream reader
      // System.out.println("For debugging:Finished reading file");
      fStream.close();
      in.close();

      /*
       * COMPLETE THIS ( I.E. CALL THE APPROPRIATE FUNCTIONS THAT DOES THE
       * FOLLOWING)
       */
      // TODO:1.) CALCULATE THE SIMILARITY SCORE BETWEEN USERS.
      // TODO:2.) PRINT OUT THE userUserMatrix
      // TODO:3.) PRINT OUT THE MOST SIMILAR PAIRS OF USER AND THE MOST
      // DISSIMILAR
      // PAIR OF USERS.
      // printing to conform to desired output
      System.out.println("\n");
      // calculate all the score
      cfObject.calculateSimilarityScoreForAllPairsOfUser();
      // print the user matrix
      cfObject.printUserUserMatrix();
      // printing to conform to desired output
      System.out.println("\n");
      // print all similar
      cfObject.findAndprintMostSimilarPairOfUsers();
      // printing to conform to desired output
      System.out.println("\n");
      // print all dissimilar
      cfObject.findAndprintMostDissimilarPairOfUsers();

    } catch (FileNotFoundException e) {
      System.err.println("Do you have the input file in the root folder "
          + "of your project?");
      System.err.print(e.getMessage());
    } catch (IOException e) {
      System.err.print(e.getMessage());
    }
  }

}
