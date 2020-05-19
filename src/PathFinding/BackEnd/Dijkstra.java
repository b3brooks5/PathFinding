package PathFinding.BackEnd;

import javax.swing.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Dijkstra {
    private enum status { unvisited, visited, wall, start, stop }     // nodes status
    private NODE[][] GRID = new NODE[40][20];   // grid to find path in
    boolean FOUND_END = false;                  // only true if found true and done
    Integer startX, startY;
    HashSet<NODE> visited = new HashSet<NODE>();
    HashSet<NODE> unvisited = new HashSet<NODE>();
    NODE CURRENT;

    public Dijkstra(String[][] strings) {
        init(strings);      // initialize GRID to strings
    }

    private void init(String[][] strings){
        for(int i = 0; i < strings.length; i++)
            for(int j = 0; j < strings[0].length; j++) {        // create new node and set it to the status of strings
//                if(makeStatus(strings[i][j]) == status.start){
//                    System.out.printf("i: %d, j: %d\n", i, j);
//                }
                unvisited.add(new NODE(makeStatus(strings[i][j]), 1000.0, i, j));
            }
        getStart(); // get the x, y values of start
        //printGRID();
    }

    private status makeStatus(String str){      // return converted status
        if(str.equals("black"))
            return status.wall;
        else if(str.equals("red"))
            return status.start;
        else if(str.equals("orange"))
            return status.stop;
        else
            return status.unvisited;  // default to unknown
    }

    private void getStart(){            // sets coordinate of the start index
        Iterator<NODE> itr = unvisited.iterator();
        while(itr.hasNext()){
            NODE working = itr.next();
            if(working.getStatus() == status.start) {
                System.out.printf("Found start\n");
                System.out.println(working.getStatus());
                System.out.printf("X: %d\tY: %d\n", working.X, working.Y);
                working.setDistance(1);
                //CURRENT = working;
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

        //ret.setDistance(smallest);
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

    private void calculateDistance(NODE rhs, double add){
        rhs.distance = CURRENT.distance + add;
    }

    private void checkNodes() {
        NODE working;
        working = findNode(CURRENT.X, CURRENT.Y + 1);
        if(working != null && working.stat == status.unvisited) {
            calculateDistance(working, 1.0);        // add by specified amount
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
    }

    public void step() {
        // cet node with smallest path so far
        CURRENT = getSmallestDistance();
        checkNodes();
        unvisited.remove(CURRENT);
        CURRENT.stat = status.visited;
        visited.add(CURRENT);

        // check all connections to current node
        // give them a distance
        // go to that node, mark it as visited, and repeat again

    }

    public String[][] makeStrings() {
        String[][] ret = new String[40][20];
        makeGrid();

        for (int i = 0; i < 40; i++)
            for (int j = 0; j < 20; j++) {
                if(GRID[i][j].stat == status.unvisited)
                    ret[i][j] = "lightGrey";
                else if(GRID[i][j].stat == status.visited)
                    ret[i][j] = "darkGrey";
                else if(GRID[i][j].stat == status.wall)
                    ret[i][j] = "black";
                else if(GRID[i][j].stat == status.stop)
                    ret[i][j] = "orange";
                else if(GRID[i][j].stat == status.start)
                    ret[i][j] = "red";
            }

        return ret;
    }

    public void makeGrid() {
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
        System.out.printf("Start X: %d\tStart Y: %d\n", startX, startY );
    }

    private static class NODE implements Comparator {
        private status stat;        // status of node
        private double distance;       // distance from start to this node
        int X, Y;

        public NODE() {
            stat = status.unvisited;
            distance = 0.0;
            X = 0;
            Y = 0;
        }

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
