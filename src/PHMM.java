/**
 * Created by Aya on 4/27/2016.
 */

import java.util.ArrayList;

/**
 *This PHMM Algorithm solves for DNA sequences only.

 *Assumptions:
    *no gaps and at least one char has prob >= 0.4 ==> match
    *no gaps and each char has prob < 0.4 ==> insertion
    *gaps has prob >= 0.4 ==> insertion
    *gaps has prob < 0.4 ==> deletion
 */

public class PHMM {
  private static final double prob = 0.4;

  //First Example:
//  private static char[] seq1 = {'A', 'C', 'A', '-', '-', '-', 'A', 'T', 'G'};
//  private static char[] seq2 = {'T', 'C', 'A', 'A', 'C', 'T', 'A', 'T', 'C'};
//  private static char[] seq3 = {'A' ,'C', 'A', 'C', '-', '-', 'A', 'G', 'C'};
//  private static char[] seq4 = {'A', 'G', 'A', '-', '-', '-', 'A', 'T', 'C'};
//  private static char[] seq5 = {'A', 'C', 'C', 'G', '-', '-', 'A', 'T', 'C'};

  //Second Example:
  private static char[] seq1 = {'C', 'C', '-', '-', 'A', 'T', 'C'};
  private static char[] seq2 = {'G', 'C', 'A', 'A', 'A', 'G', 'C'};
  private static char[] seq3 = {'G' ,'T', 'A', 'T', 'A', 'T', 'C'};
  private static char[] seq4 = {'G', 'C', '-', '-', 'C', 'T', 'G'};
  private static char[] seq5 = {'C', 'C', '-', '-', 'A', 'T', 'C'};

  //Third Example:
//  private static char[] seq1 = {'A', 'C', 'G', 'T', 'A', '-', 'T'};
//  private static char[] seq2 = {'A', '-', 'G', 'T', 'A', '-', 'T'};
//  private static char[] seq3 = {'A' ,'C', 'A', 'T', 'A', '-', 'T'};
//  private static char[] seq4 = {'A', 'C', 'G', 'T', 'A', '-', 'T'};
//  private static char[] seq5 = {'-', 'C', 'G', '-', 'A', 'G', 'T'};

  private static char[][] MSA = {seq1, seq2, seq3, seq4, seq5};

  private static int no_of_seq = MSA.length;

  private static int A = 0;
  private static int C = 0;
  private static int G = 0;
  private static int T = 0;
  private static int gap = 0;

  private static double AProb = 0;
  private static double CProb = 0;
  private static double GProb = 0;
  private static double TProb = 0;
  private static double GapProb = 0;

  private static boolean match = false;
  private static boolean deletion = false;
  private static boolean insertion = false;

  /* This method determines the state, it takes two args:
   * the first is probability for every Nucleic acid
   * and the second is the gap prob
   */
  public static void determineState(int i){
    initializeVariables();
    for (int j = 0; j < no_of_seq; j++) {
      if (MSA[j][i] == 'A')
        A++;
      else if (MSA[j][i] == 'C')
        C++;
      else if (MSA[j][i] == 'G')
        G++;
      else if (MSA[j][i] == 'T')
        T++;
      else if (MSA[j][i] == '-')
        gap++;
    }

    AProb = (double) A/no_of_seq;
    CProb = (double) C/no_of_seq;
    GProb = (double) G/no_of_seq;
    TProb = (double) T/no_of_seq;
    GapProb = (double) gap/no_of_seq;

    double[] probs = new double[]{AProb, CProb, GProb, TProb};

    double max = 0;
    for (int j = 0; j < probs.length; j++) {
      if (probs[j] >= max)
        max = probs[j];
    }

    if (max >= prob && gap == 0)
      match = true;
    else if ((max < prob && gap == 0) || GapProb >= prob)
      insertion = true;
    else if (GapProb < prob)
      deletion = true;
  }

  //Match state
  public static void matchState(){
    System.out.println("State: Match.");
    System.out.println("A: "+AProb);
    System.out.println("C: "+CProb);
    System.out.println("G: "+GProb);
    System.out.println("T: "+TProb);
  }

  //Deletion State
  public static void DeletionState(double AProb, double CProb, double GProb, double TProb){

  }

  //Insertion State
  public static void InsertionState(double AProb, double CProb, double GProb, double TProb){

  }

  public static void initializeVariables(){
    A = 0;
    C = 0;
    G = 0;
    T = 0;
    gap = 0;

    AProb = 0;
    CProb = 0;
    GProb = 0;
    TProb = 0;
    GapProb = 0;

    match = false;
    deletion = false;
    insertion = false;
  }

  public static void main(String [] args){
    int noOfCols = MSA[0].length;
    String[] states = new String[noOfCols];

    for (int i = 0; i < seq1.length; i++) {

      determineState(i);

      if (match)
        states[i] = "Match";
      else if (deletion)
        states[i] = "Deletion";
      else if (insertion)
        states[i] = "Insertion";
    }

    int matchCount = 0;
    for (int i = 0; i < states.length-1; i++) {
      if (states[i].equalsIgnoreCase("Match")){
        determineState(i);
        System.out.println("State: Match"+(matchCount+1)+".");
        System.out.println("A: " + AProb);
        System.out.println("C: " + CProb);
        System.out.println("G: " + GProb);
        System.out.println("T: " + TProb);

        determineState(i+1);
        if (states[i+1].equalsIgnoreCase("Match"))
          System.out.println("Go to Match"+(matchCount+2)+" state with prob: "+1);
        else if (states[i+1].equalsIgnoreCase("Insertion")){
          System.out.println("Go to Match"+(matchCount+2)+" state with prob: "+(GapProb));
          System.out.println("Go to Insert state with prob: "+(1-GapProb));
        }
        System.out.println("----------------------------------------------------");
        matchCount++;
      }

      else if (states[i].equalsIgnoreCase("Deletion")){
        determineState(i);
      }

      else if (states[i].equalsIgnoreCase("Insertion")){
        int count = 0;
        int total = 0;
        int AIns = 0, CIns = 0, GIns = 0, TIns = 0;
        determineState(i+1);
        while (states[i+1].equalsIgnoreCase("Insertion")){
          count++;
          i++;
          determineState(i+1);
        }
        for (int j = count; j >= 0; j--) {
          for (int k = 0; k < no_of_seq; k++) {
            if(MSA[k][i-j] == 'A'){
              total++;
              AIns++;
            }
            else if(MSA[k][i-j] == 'C'){
              total++;
              CIns++;
            }
            else if(MSA[k][i-j] == 'G'){
              total++;
              GIns++;
            }
            else if(MSA[k][i-j] == 'T'){
              total++;
              TIns++;
            }
          }
        }
        int matchIns = 0;
        int insertIns = 0;
        int totalIns = 0;
        for (int j = 0; j < no_of_seq; j++) {
          int no_of_nonGaps = 0;
          for (int k = count; k >= 0; k--) {
            if (MSA[j][i-k] != '-')
              no_of_nonGaps++;
          }
          if (no_of_nonGaps > 0){
            matchIns++;
            insertIns += no_of_nonGaps-1;
          }
        }
        totalIns = matchIns + insertIns;
        double insProb = (double) insertIns / totalIns;
        double mProb = (double) matchIns / totalIns;

        System.out.println("Stay in insert state with probability: " + insProb);
        System.out.println("State: Insert.");
        System.out.println("A: " + (double)AIns/total);
        System.out.println("C: " + (double)CIns/total);
        System.out.println("G: " + (double)GIns/total);
        System.out.println("T: " + (double)TIns/total);
        System.out.println("Go to Match"+(matchCount+1)+" state with prob: "+(mProb));
        System.out.println("----------------------------------------------------");
      }
    }
    if (states[states.length-1].equalsIgnoreCase("Match")){
      determineState(states.length-1);
      System.out.println("State: Match"+(matchCount+1)+".");
      System.out.println("A: " + AProb);
      System.out.println("C: " + CProb);
      System.out.println("G: " + GProb);
      System.out.println("T: " + TProb);
    }

    else if (states[states.length-1].equalsIgnoreCase("Deletion")){
      determineState(states.length-1);
    }

    else if (states[states.length-1].equalsIgnoreCase("Insertion")){
      int count = 0;
      int total = 0;
      int AIns = 0, CIns = 0, GIns = 0, TIns = 0;
      for (int j = count; j >= 0; j--) {
        for (int k = 0; k < no_of_seq; k++) {
          if(MSA[k][states.length-1-j] == 'A'){
            total++;
            AIns++;
          }
          else if(MSA[k][states.length-1-j] == 'C'){
            total++;
            CIns++;
          }
          else if(MSA[k][states.length-1-j] == 'G'){
            total++;
            GIns++;
          }
          else if(MSA[k][states.length-1-j] == 'T'){
            total++;
            TIns++;
          }
        }
      }
      int matchIns = 0;
      int insertIns = 0;
      int totalIns = 0;
      for (int j = 0; j < no_of_seq; j++) {
        int no_of_nonGaps = 0;
        for (int k = count; k >= 0; k--) {
          if (MSA[j][states.length-1-k] != '-')
            no_of_nonGaps++;
        }
        if (no_of_nonGaps > 0){
          matchIns++;
          insertIns += no_of_nonGaps-1;
        }
      }
      totalIns = matchIns + insertIns;
      double insProb = (double) insertIns / totalIns;
      double mProb = (double) matchIns / totalIns;

      System.out.println("Stay in insert state with probability: " + insProb);
      System.out.println("State: Insert.");
      System.out.println("A: " + (double)AIns/total);
      System.out.println("C: " + (double)CIns/total);
      System.out.println("G: " + (double)GIns/total);
      System.out.println("T: " + (double)TIns/total);
      System.out.println("Go to Match"+(matchCount+1)+" state with prob: "+(mProb));
      System.out.println("----------------------------------------------------");
    }
  }
}
