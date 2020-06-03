/*
* This Abstract class is used to implement different searching algorithms.
* The class takes in a 2D array of strings that represent differnct structures along the path
* such as empty spaces, walls, the start of the path, the end of the path, and the path that has
* been taken so far.
* */
package PathFinding.BackEnd.Algiorithms;

public abstract class Algorithm {

    public abstract int step(boolean Diagonals);        // runs the implemented path finding algorithm
    public abstract void clear();                       // clears all data but keeps the same start and stop points
    public abstract String[][] makeStrings();           // creates a 2D array of strings for returning
    public abstract void restart(String[][] rhs);       // restarts all data with new start and stop point
    public abstract int getDistance();                  // returns the distance from start to stop point

}
