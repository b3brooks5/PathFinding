package PathFinding.BackEnd;

import java.util.*;

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
                }
                unvisited.add(new NODE(makeStatus(strings[i][j]), 1000.0, i, j));   // add all squares
            }
        getStart(); // get the x, y values of start
    }

    // return converted status
    private status makeStatus(String str){
        if(str.equals("black"))
            return status.wall;
        else if(str.equals("orange"))
            return status.start;
        else if(str.equals("red"))
            return status.stop;
        else
            return status.unvisited;  // default to unknown
    }

    // sets coordinate of the start index
    private void getStart(){
        Iterator<NODE> itr = unvisited.iterator();
        while(itr.hasNext()){
            NODE working = itr.next();
            if(working.getStatus() == status.start) {
                working.setDistance(0);
            }
        }
    }

    // returns the node with the smallest distance in unvisited
    private NODE getSmallestDistance() {
        NODE ret = new NODE(status.wall, 1001.0, -1, -1);
        double smallest = 1001.0;

        System.out.println(unvisited);
        for(NODE n : unvisited) {
            if (n.distance < smallest) {
                smallest = n.distance;
                ret = n;
            }
        }
        System.out.println(ret);
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

    // calculates the new distance
    private void calculateDistance(NODE rhs, double add){
        double dis = CURRENT.distance + add;
        if(dis < rhs.distance)
            rhs.distance = CURRENT.distance + add;
    }

    // restart the board with new start and stop points
    public void restart(String[][] rhs){
        visited.clear();
        unvisited.clear();
        FOUND_END = false;

        init(rhs);
    }

    // cleared the grid but keeps the same start and stop positions
    public void clear(){
        String[][] work = new String[40][20];       // new empty GRID

        for (String[] strings : work) {
            Arrays.fill(strings, "lightGrey");
        }
        work[SX][SY] = "orange";        // use old start, stop
        work[EX][EY] = "red";

        restart(work);      // restart with newly created board
    }

    // Check one node
    private void oneNode(int x, int y, double add){
        NODE working;

        working = findNode(x, y);
        if(working != null && (working.stat == status.unvisited || working.stat ==  status.stop)) {
            calculateDistance(working, add);
        }
    }

    // calculate distances for all valid squares
    private boolean checkNodes(boolean Diagonals, double add) {
        NODE working;

        working = findNode(CURRENT.X, CURRENT.Y);

        System.out.println("Working: " + working);
        if(CURRENT != null && CURRENT.stat == status.stop) {
            System.out.println("It ran");
            return true;
        }

        if(Diagonals){  // we are allowing diagonals
            oneNode(CURRENT.X + 1, CURRENT.Y + 1, add);
            oneNode(CURRENT.X + 1, CURRENT.Y - 1, add);
            oneNode(CURRENT.X - 1, CURRENT.Y + 1, add);
            oneNode(CURRENT.X - 1, CURRENT.Y - 1, add);
        }

        oneNode(CURRENT.X, CURRENT.Y + 1, 1.0);
        oneNode(CURRENT.X, CURRENT.Y - 1, 1.0);
        oneNode(CURRENT.X + 1, CURRENT.Y, 1.0);
        oneNode(CURRENT.X - 1, CURRENT.Y, 1.0);



        return false;
    }

    public int step(boolean Diagonals, double distance) {             // returns true if end tile found
        for(int i = 0; i < STEP_SIZE; i++) {
            CURRENT = getSmallestDistance();        // get the node with the smallest distance

            if(CURRENT.stat == status.wall && CURRENT.distance == 1001.0 && CURRENT.X == 0 && CURRENT.Y == 0)
                return -1;
            System.out.println("Current stat: " + CURRENT.stat);
            FOUND_END = checkNodes(Diagonals, distance);               // check all it's neighbors
            System.out.println(FOUND_END);
            unvisited.remove(CURRENT);

            CURRENT.stat = status.visited;
            visited.add(CURRENT);

            if(FOUND_END)      // if we found the end tile
                return 0;
        }
        return 1;
    }

    // finds the min of two nodes
    private NODE myMin(NODE r, NODE l){
        if(l.distance < r.distance)     // return l if it's less then r
            return l;
        else return r;      // otherwise keep r
    }

    // changes the color of the tiles that make up the shortest path of the algorithm
    public void getShortestPath(boolean Diagonal) {
        NODE current = GRID[EX][EY];                        // the current node start at the stop node
        ArrayList<NODE> neighbors = new ArrayList<>();      // stores the neighbors of the current
        NODE smallest = null;                                // node for changing values

        while(current.distance != 0.0) {
            if (current.X + 1 < 40)                                // if statements grab all neighboring nodes
                neighbors.add(GRID[current.X + 1][current.Y]);          // and first checks if they are in the grid
                                                                        // and not null
            if (current.X - 1 >= 0)
                neighbors.add(GRID[current.X - 1][current.Y]);

            if (current.Y + 1 < 20)
                neighbors.add(GRID[current.X][current.Y + 1]);

            if (current.Y - 1 >= 0)
                neighbors.add(GRID[current.X][current.Y - 1]);

            if(Diagonal){       // diagonals were alloed
                if (current.X + 1 < 40 && current.Y + 1 < 20)
                    neighbors.add(GRID[current.X + 1][current.Y + 1]);

                if (current.X + 1 < 40 && current.Y - 1 >= 0)
                    neighbors.add(GRID[current.X + 1][current.Y - 1]);

                if ( current.X - 1 >= 0 && current.Y + 1 < 20)
                    neighbors.add(GRID[current.X - 1][current.Y + 1]);

                if (current.X - 1 >= 0 && current.Y - 1 >= 0)
                    neighbors.add(GRID[current.X - 1][current.Y - 1]);
            }


            if (neighbors.size() >= 1) {            // get the smallest node
                smallest = neighbors.get(0);
                for (int i = 0; i < neighbors.size(); i++) {
                    smallest = myMin(smallest, neighbors.get(i));
                }
            }
            else{
                System.out.println("ERROR: Working is NULL");
            }

            current.stat = status.path;     // change color for printing
            current = smallest;
            neighbors.clear();              // clear for next loop
        }   // end while loop
    }   // end getShortestPath

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

    public void printDistances(){
        makeGrid();
        for(int i = 0; i < 40; i++){
            System.out.printf("%-9d", i);
        }

        for(int i = 0; i < GRID[0].length; i++){
            System.out.println();
            for(int j = 0; j < GRID.length; j++){
                System.out.printf("%-7.2f, ", GRID[j][i].distance);
            }
        }
//        for(NODE[] row : GRID) {
//            System.out.println();
//            for (NODE n : row) {
//                System.out.printf("%-7.2f, ", n.distance);
//            }
//        }
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
