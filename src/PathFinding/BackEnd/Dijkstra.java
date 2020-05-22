package PathFinding.BackEnd;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Dijkstra {
    private enum status { unvisited, visited, wall, start, stop, path }     // nodes status
    private NODE[][] GRID = new NODE[40][20];    // grid to find path in
    boolean FOUND_END = false;                   // only true if found true and done
    HashSet<NODE> visited = new HashSet<NODE>();    // nodes that have been visited
    HashSet<NODE> unvisited = new HashSet<NODE>();  // nodes that have not been visited
    NODE CURRENT;           // the current node that we are checking it's neighbors
    int SX, SY, EX, EY;     // coordinates of the start and stop tiles
    int STEP_SIZE = 10;     // how many squares to set with each iteration

    public Dijkstra(String[][] strings) {
        init(strings);      // initialize GRID to strings
    }

    private void init(String[][] strings){
        for(int i = 0; i < strings.length; i++)
            for(int j = 0; j < strings[0].length; j++) {        // create new node and set it to the status of strings
                if(strings[i][j].equals("orange")) {        // start square
                    SX = i;
                    SY = j;
                }

                if(strings[i][j].equals("red")){            // end square
                    EX = i;
                    EY = j;
                    System.out.printf("EX: %s\tEY: %d\n", EX, EY);
                }
                unvisited.add(new NODE(makeStatus(strings[i][j]), 1000.0, i, j));   // add all squares
            }
        getStart(); // get the x, y values of start
    }

    private status makeStatus(String str){      // return converted status
        if(str.equals("black"))
            return status.wall;
        else if(str.equals("orange"))
            return status.start;
        else if(str.equals("red"))
            return status.stop;
        else
            return status.unvisited;  // default to unknown
    }

    private void getStart(){            // sets coordinate of the start index
        Iterator<NODE> itr = unvisited.iterator();
        while(itr.hasNext()){
            NODE working = itr.next();
            if(working.getStatus() == status.start) {
                System.out.println(working.getStatus());
                working.setDistance(0);
            }
        }
    }

    // returns the node with the smallest distance in unvisited
    private NODE getSmallestDistance() {
        NODE ret = new NODE(status.wall, 1000.0, -1, -1);
        double smallest = 1000.0;

        for(NODE n : unvisited) {
            if (n.distance < smallest){
                smallest = n.distance;
                ret = n;
            }
        }

        return ret;
    }

    // finds a NODE based on coordinates
    private NODE findNode(int x, int y) {
        for(NODE n : unvisited) {
            if(x == n.X && y == n.Y)
                return n;
        }
        return null;
    }

    private boolean calculateDistance(NODE rhs, double add){
        rhs.distance = CURRENT.distance + add;
        return rhs.stat == status.stop;                 // return if you found the end node or not
    }

    // calculate distances for all valid squares
    private boolean checkNodes() {
        NODE working;

        working = findNode(CURRENT.X, CURRENT.Y + 1);
        if(working != null && working.stat == status.stop)
            return true;

        if(working != null && working.stat == status.unvisited) {
            calculateDistance(working, 1.0);
        }
        working = findNode(CURRENT.X, CURRENT.Y - 1);
        if(working != null && working.stat == status.unvisited) {
            calculateDistance(working, 1.0);
        }
        working = findNode(CURRENT.X + 1, CURRENT.Y);
        if(working != null && working.stat == status.unvisited) {
            calculateDistance(working, 1.0);
        }
        working = findNode(CURRENT.X - 1, CURRENT.Y);
        if(working != null && working.stat == status.unvisited) {
            calculateDistance(working, 1.0);
        }
        return false;
    }

    public boolean step(boolean Diagonals, double distance) {             // returns true if end tile found
        for(int i = 0; i < STEP_SIZE; i++) {
            CURRENT = getSmallestDistance();        // get the node with the smallest distance
            FOUND_END = checkNodes();               // check all it's neighbors
            unvisited.remove(CURRENT);
            CURRENT.stat = status.visited;
            visited.add(CURRENT);

            //printUnvisited();
            if(FOUND_END){      // if we found the end tile
                //getShortestPath();
                return true;
            }
        }
        return false;
    }

    public void getShortestPath(){
        double min;
        NODE current = GRID[EX][EY];

        while(current.distance != 0.0) {        // while not at start
            min = Math.min(GRID[current.X + 1][current.Y].distance, GRID[current.X - 1][current.Y].distance);
            min = Math.min(min, GRID[current.X][current.Y + 1].distance);
            min = Math.min(min, GRID[current.X][current.Y - 1].distance);     // min now has the shortest distance arround the current node

            current.stat = status.path;     // make blue

            if (min == GRID[current.X + 1][current.Y].distance)         // set current to lowest distance
                current = GRID[current.X + 1][current.Y];
            else if (min == GRID[current.X - 1][current.Y].distance)
                current = GRID[current.X - 1][current.Y];
            else if (min == GRID[current.X][current.Y + 1].distance)
                current = GRID[current.X][current.Y + 1];
            else if (min == GRID[current.X][current.Y - 1].distance)
                current = GRID[current.X][current.Y - 1];

        }
    }   // en get shortest path

    // makes the entire GRID into a 2D array of strings based on color of tiles
    public String[][] makeStrings() {
        String[][] ret = new String[40][20];
        makeGrid();

        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 20; j++) {
                if (GRID[i][j].stat == status.unvisited)
                    ret[i][j] = "lightGrey";
                else if (GRID[i][j].stat == status.visited)
                    ret[i][j] = "darkGrey";
                else if (GRID[i][j].stat == status.wall)
                    ret[i][j] = "black";
                else if (GRID[i][j].stat == status.stop)
                    ret[i][j] = "red";
                else if (GRID[i][j].stat == status.start)
                    ret[i][j] = "orange";
                else if(GRID[i][j].stat == status.path){
                    ret[i][j] = "blue";
                }
            }
        }
        ret[SX][SY] = "orange";     // always show start and stop
        //System.out.printf("SX: %d\tSY: %d\n", SX, SY);
        ret[EX][EY] = "red";

        return ret;
    }

    // changes sets "visited" and "unvisited" into a 2D array
    private void makeGrid() {
        for(NODE n : unvisited) {
            GRID[n.X][n.Y] = n;
        }
        for(NODE n : visited) {
            GRID[n.X][n.Y] = n;
        }
    }

    public void printGRID() {           // temp print function
        for (int i = 0; i < GRID[0].length; i++){ //[] nodes : GRID) {
            System.out.println();
            for (int j = 0; j < GRID.length; j++){ // n : nodes) {
                System.out.printf("%-10s %-10.2f,", GRID[j][i].stat, GRID[j][i].distance);
            }
        }
        System.out.println();
    }

    public void printUnvisited(){
        for(NODE n : unvisited) {
            System.out.println(n);
        }
    }

    private static class NODE implements Comparator {
        private status stat;        // status of node
        private double distance;       // distance from start to this node
        int X, Y;

//        public NODE() {
//            stat = status.unvisited;
//            distance = 0.0;
//            X = 0;
//            Y = 0;
//        }

        public NODE(status s, double d, int x, int y) {
            stat = s;

            if (d >= 0)
                distance = d;
            if(x > -1 && x < 40)
                X = x;
            if(y > -1 && y < 20)
                Y = y;
        }

        public void setStatus(status s){
            stat = s;
        }

        public void setDistance(double d) {
            if(d >= 0.0)   // distance cannot be negative
                distance = d;
        }

        public status getStatus(){
            return stat;
        }

        public double getDistance(){
            return distance;
        }

        @Override
        public int compare(Object o, Object t1) {
            return (int)(((NODE)o).getDistance() - ((NODE)t1).getDistance());
        }

        @Override
        public boolean equals(Object o){
            if(o.getClass() == getClass()) {
                if (X == ((NODE) o).X && Y == ((NODE) o).Y)
                    return true;
                else return false;
            }
            else {
                return false;
            }
        }

        @Override
        public String toString(){
            return stat + " " + distance + " " + X + " " + Y;
        }
    }   // end of NODE
}   // end of Dijkstra
