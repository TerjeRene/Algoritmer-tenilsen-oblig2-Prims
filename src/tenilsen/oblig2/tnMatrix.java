/*
 * DA-ALG1000, Oblig 2, 2014.
 * terje.nilsen@student.hive.no (tenilsen)
 *
 * 
 */

package tenilsen.oblig2;

import java.util.Arrays;

/**
 *
 * @author Terje Rene E. Nilsen - terje.nilsen@student.hive.no (tenilsen).
 * NOTES:
 * _All_ counting starts on 0. 
 * Some standard values is set to -1, makes spotting errors easier.
 * Which means that negative numbers as matrix cost are not supported.
 * 
 * NTL is short for Note To Later.
 */
public class tnMatrix  {
    // NTL: final = may only be init once.
    private final int matrixSize; // the size of the matrix
    private final int[][] theMap; // the matrix itself
    private int theCost;          // the calculated cost
    private final int biggestValue = 100; /* Make sure this is bigger than the 
    biggest cost. It is used as an initiation value for 'lowestCostTemp' in
    findNextConnection(). Putting it here to avoid "spaghetti-code".    */
    String [] connectedNodes; /* Holds the connected nodes as strings.
                                Like this: [from,to]
                                          [A,C] 
                                          [A,B] 
                                          [C,F] 
                                          [B,E] 
                                          [E,G] 
                                          [E,H] 
                                          [G,I] 
                                          [H,D] 
                                */
    
    // private boolean tnDebug = false; // For debug. 
    // String theConnections = ""; // NTL: for extensive debug. 
    
    public tnMatrix() {
        // a standard matrix 9x9 for testing purposes
        matrixSize = 9;
        /* The map of houses.
            Value = cost between houses.
            0 = no route between houses. */
        theMap = new int[][] { 
        //   A, B, C, D, E, F, G, H, I
     /*A*/ { 0,10, 8,13, 0, 0, 0, 0, 0},
     /*B*/ {10, 0, 0,20,12, 0, 0, 0, 0},
     /*C*/ { 8, 0, 0,30, 0,12, 0, 0, 0},
     /*D*/ {13,20,30, 0, 0, 0,25,11, 0},
     /*E*/ { 0,12, 0, 0, 0, 0, 5, 7, 0},
     /*F*/ { 0, 0,12, 0, 0, 0,15, 0, 0},
     /*G*/ { 0, 0, 0,25, 5,15, 0, 9, 8},        
     /*H*/ { 0, 0, 0,11, 7, 0, 9, 0,10},
     /*I*/ { 0, 0, 0, 0, 0, 0, 8,10, 0}
        };
    }
    
    public tnMatrix (int[][] givenMatrixMap, int givenMatrixSize) {
        matrixSize = givenMatrixSize; 
        theMap = Arrays.copyOf(givenMatrixMap, matrixSize);
    }
    
    public void minimalSpanningTree(int mStart) {
        theCost = 0; // Better reset this..
        connectedNodes = new String [matrixSize-1]; // ..and this.
        int[] visitedNodes = new int[matrixSize]; // The nodes we have visited.
        Arrays.fill(visitedNodes, -1); /* Fill with a number we do not use
        If visitedNodes[] is full of 0 it breaks checkInArray() used in
        findNextConnection(). */
        
        visitedNodes[0] = mStart; // We start here..
        int visitedNodesNumber = 1; // ..and continue to this.

        while (visitedNodesNumber < (matrixSize)) {
            visitedNodes[visitedNodesNumber] = findNextConnection(visitedNodes,visitedNodesNumber); // Find next node.
            visitedNodesNumber++;
        }
    }
    
    private int findNextConnection(int[] visitedNodes, int visitedNodesNumber) {
        int costTemp;
        int lowestCostTemp = biggestValue; // A number bigger than the biggest cost.
        int nextNode = -1; 
        int fromNode = -1;
        int fromNodeTemp;
        for (int i = 0; i < visitedNodesNumber; i++) {
            fromNodeTemp = visitedNodes[i]; //
            for (int h = 0; h < matrixSize; h++) {
                if (!checkInArray(h, visitedNodes)) { // If we didnt visit h, evaluate it.
                    costTemp = theMap[fromNodeTemp][h];
                    if ((costTemp < lowestCostTemp) && (costTemp != 0)) {
                        lowestCostTemp = costTemp; // Found a new lowest cost!
                        nextNode = h; // The lowest cost is between this node..
                        fromNode = fromNodeTemp; // ..and this node.
                    }
                } 
            }
        }
        connectedNodes[visitedNodesNumber-1] = numberToLetter(fromNode) + " "+ numberToLetter(nextNode);
        theCost += lowestCostTemp;
         return nextNode;
    }
    
    /*  checkInArray() I borrowed this since there is no .indexOf() for arrays in java.
    Acuired from:
    http://www.dreamincode.net/forums/topic/239725-check-if-a-number-is-in-array/page__view__findpost__p__1389223
    */
    private static boolean checkInArray(int currentState, int[] myArray) {
        boolean found = false;
        for (int i = 0; !found && (i < myArray.length); i++) {
            found = (myArray[i] == currentState);
        }
        return found;
    }

    @Override
    public String toString() {
         String temp="", caption=" ";
         // No spaghetti-coded caption. Prof. Milica Barjaktarovic would approve.
            for (int w = 0; w < matrixSize; w++) {
                caption += "  " + numberToLetter(w);
                temp += numberToLetter(w) + " ";
                for (int i = 0; i < matrixSize; i++)
                {
                    if (theMap[w][i] < 10) {
                        temp += " ";
                    } 
                    temp +=(theMap[w][i] + " ");
                }
                temp += ("\n");
            }
            temp +=("\n");
            caption +=("\n");
       return caption + temp;
    }
    
    public String nodesToStringFancy() {
        String theReturn = "";
        String[] sortedNodes = Arrays.copyOf(connectedNodes, matrixSize-1);
        Arrays.sort(sortedNodes, 0, matrixSize-1); // NTL: start and end needed for arrays < 10 elements.
        theReturn += "Chronological:\tSorted:\n"; 
        for (int i = 0; i < matrixSize-1; i++) {
            theReturn += "\t[" + connectedNodes[i] + "] \t" +
                    "[" + sortedNodes[i] +"] \n";
        }
        return theReturn;
    }
    public int getMatrixSize() {
        return matrixSize;
    }
    
    public int[][] getMatrix() {
        return theMap;
    }
    public static String numberToLetter(int nr) {
        switch (nr) {
            case 0: 
                return "A";
            case 1: 
                return "B";
            case 2: 
                return "C";
            case 3: 
                return "D";
            case 4: 
                return "E";
            case 5: 
                return "F";
            case 6: 
                return "G";
            case 7: 
               return "H";
            case 8: 
                return "I";
            default: 
                return "INVALID NUMBER: "+nr;
        }
    }
    
    public static int letterToNumber(String letter) {
        switch (letter.toUpperCase()) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;                
            default: 
                return -1;
        }
    }

    public int getCost() {
        return theCost;
    }
    // stuff for debuging:
    /*
    public void setDebug(boolean theNewStatus) {
        tnDebug = theNewStatus;
    }
    public boolean getDebug() {
        return tnDebug;
    }
    private void printDebug(String theDebugMessage) {
        if (tnDebug) {
            System.out.println(theDebugMessage);
        }
    }
    */
}
