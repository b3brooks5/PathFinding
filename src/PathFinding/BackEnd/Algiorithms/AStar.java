package PathFinding.BackEnd.Algiorithms;

import PathFinding.ColorConversions;

import java.util.*;

public class AStar extends Algorithm {
    enum status {unvisited, visited, wall, stop, start, path}
    private int StartX, StartY, EndX, EndY;
    private PriorityQueue<NODE> calculated = new PriorityQueue<>();
    private ArrayList<NODE> visited = new ArrayList<>();
    private ArrayList<NODE> unvisited = new ArrayList<>();
    private NODE CURRENT, END;
    private int SIZE_RUN = 20;

    public AStar(String[][] strings){
        init(strings);
    }

    // initializes new unvisited list for searching
    private void init(String[][] strings) {
        for (int x = 0; x < strings.length; x++){
            for(int y = 0; y < strings[0].length; y++){ // start and stop need to be found first so Scores can be calculated

                if(strings[x][y].equals("orange")){      // save start and end points
                    StartX = x;
                    StartY = y;
                }
                else if(strings[x][y].equals("red")){
                    EndX = x;
                    EndY = y;
                }
            }
        }

        calculated.add(new NODE(StartX, StartY, makeStatus(strings[StartX][StartY]), 0));    //start is always 0
        unvisited.add(new NODE(EndX, EndY, makeStatus(strings[EndX][EndY]), 1000));

        for (int x = 0; x < strings.length; x++){
            for(int y = 0; y < strings[0].length; y++){
                if(!strings[x][y].equals("orange") && !strings[x][y].equals("red"))
                    unvisited.add(new NODE(x, y, makeStatus(strings[x][y]), 1000)); // add all other nodes
            }
        }
        CURRENT = null;
    }

    // return converted status
    private status makeStatus(String str){
        switch (str) {
            case "black":
                return status.wall;
            case "orange":
                return status.start;
            case "red":
                return status.stop;
            default:
                return status.unvisited;  // default to unknown
        }
    }

    // retrieves one node in given collection
    private <T extends Collection<NODE> > NODE getNODE(int x, int y, T container){
        for(NODE n : container) {
            if(n.X == x && n.Y == y && n.stat != status.wall)
                return n;
        }
        return null;    // if node not in collection
    }

    // scores the node based on it;s position relative to the end node
    public void score(int x, int y, int d){
        NODE working = getNODE(x, y, unvisited);

        if(working != null) {
            working.setDistance(d + 1);

            if(working.stat != status.stop)
                working.stat = status.visited;

            calculated.add(working);        // it's score has now been calculated
            unvisited.remove(working);
        }
        else {
            working = getNODE(x, y, calculated);

            if(working != null) {
                if(working.Distance > d + 1) {
                    calculated.remove(working);
                    working.setDistance(d+1);
                    calculated.add(working);
                }
            }
        }
    }

    // Scores the surrounding nodes around 'c' passed in
    private void ScoreSurrounding(boolean Diagonals){
        score(CURRENT.X + 1, CURRENT.Y, CURRENT.Distance);
        score(CURRENT.X - 1, CURRENT.Y, CURRENT.Distance);
        score(CURRENT.X, CURRENT.Y + 1, CURRENT.Distance);
        score(CURRENT.X, CURRENT.Y -1 , CURRENT.Distance);

        if(Diagonals){      // if we are allowing diagonals
            score(CURRENT.X + 1, CURRENT.Y -1, CURRENT.Distance);
            score(CURRENT.X - 1, CURRENT.Y + 1, CURRENT.Distance);
            score(CURRENT.X - 1 , CURRENT.Y - 1, CURRENT.Distance);
            score(CURRENT.X + 1, CURRENT.Y + 1 , CURRENT.Distance);
        }
    }

    // retrieves the smallest neighbor based on distance
    private NODE getSmallestNeighbor(NODE center, boolean Diagonals){
        PriorityQueue<NODE> neighbors = new PriorityQueue<>(new DistanceComparator());
        NODE working;



        working = getNODE(center.X + 1, center.Y, visited);
        if (working != null)
            neighbors.add(working);

        working = getNODE(center.X - 1, center.Y, visited);
        if (working != null)
            neighbors.add(working);

        working = getNODE(center.X, center.Y + 1, visited);
        if (working != null)
            neighbors.add(working);

        working = getNODE(center.X, center.Y - 1, visited);
        if(working != null)
            neighbors.add(working);

        if(Diagonals){
            working = getNODE(center.X + 1, center.Y - 1, visited);
            if(working != null)
                neighbors.add(working);

            working = getNODE(center.X - 1, center.Y + 1, visited);
            if(working != null)
                neighbors.add(working);

            working = getNODE(center.X + 1, center.Y + 1, visited);
            if(working != null)
                neighbors.add(working);

            working = getNODE(center.X - 1, center.Y - 1, visited);
            if(working != null)
                neighbors.add(working);
        }

        NODE ret = neighbors.poll();

        if(ret != null && ret.stat == status.path)
            return neighbors.poll();
        else
            return ret;    // return smallest neighbor

    }

    // labels the shortest path as blue
    private void getPath(boolean Diagonals){
        NODE working = CURRENT; // start at stop node

        while (!(working.X == StartX && working.Y == StartY)){  // while not at start node
            working = getSmallestNeighbor(working, Diagonals);
            working.stat = status.path;
        }
        working.stat = status.start;        // set start back to start after we found it
    }

    // makes one 'step' through the array
    @Override
    public int step(boolean Diagonals) {
        for(int i = 0; i < SIZE_RUN; i++){
            CURRENT = calculated.poll();    // nodes that have their shortest distance calculated

            if(CURRENT != null && CURRENT.stat == status.stop) {
                visited.add(CURRENT);       // stop needs to be added somewhere
                getPath(Diagonals);         // labels shortest path as blue
                return 0;
            }
            ScoreSurrounding(Diagonals);    // give the surrounding nodes a score

            visited.add(CURRENT);           // current has been visited and won't need to be again
            //printDistanceScore();
            CURRENT = null;
        }

        return 1;   // step completed but stop NODE not found yet
    }

    // returns an array of strings
    @Override
    public String[][] makeStrings(){
        String[][] ret = new String[40][20];

        for(NODE n : unvisited)
            ret[n.X][n.Y] = ColorConversions.StatusToString(n.stat.toString());

        for(NODE n : visited)
            ret[n.X][n.Y] = ColorConversions.StatusToString(n.stat.toString());

        for(NODE n : calculated)
            ret[n.X][n.Y] = ColorConversions.StatusToString(n.stat.toString());

        return ret;
    }

    // clears the board but keeps the start and stop points
    @Override
    public void clear() {
        String[][] work = new String[40][20];       // new empty GRID

        for (String[] strings : work) {
            Arrays.fill(strings, "lightGrey");
        }
        work[StartX][StartY] = "orange";        // use old start, stop
        work[EndX][EndY] = "red";

        restart(work);
    }

    // restarts board based on new rhs
    @Override
    public void restart(String[][] rhs) {
        visited.clear();
        unvisited.clear();
        calculated.clear();
        init(rhs);
    }

    @Override
    public int getDistance() {
        NODE  ret = getNODE(EndX, EndY, unvisited);
        if(ret != null)
            return ret.Distance;
        else
            return 0;
    }

    // prints strings for debugging
    public void printStrings(){
        String[][] p = makeStrings();

        for (int i = 0; i < p[0].length; i++){
            System.out.println();
            for (String[] strings : p) {
                System.out.printf("%-10s, ", strings[i]);
            }
        }
    }

    // prints array of distances and scores
    public void printDistanceScore(){
        NODE[][] ret = new NODE[40][20];

        for(NODE n : unvisited)
            ret[n.X][n.Y] = n;

        for(NODE n : visited)
            ret[n.X][n.Y] = n;

        for(NODE n : calculated)
            ret[n.X][n.Y] = n;

        for (int i = 0; i < ret[0].length; i++){
            System.out.println();
            for (NODE[] nodes : ret) {
                System.out.printf("%-5d %-5d, ", nodes[i].Distance, nodes[i].Score);
            }
        }
    }

    private class NODE implements Comparable<NODE> {
        status stat;
        int X, Y;
        int Distance, Score;

        public NODE(int x, int y, status s, int dis) {
            X = Math.max(x, 0);

            Y = Math.max(y, 0);

            stat = s;

            if (dis >= 0)
                Distance = dis;

            Score = calcScore();
        }

        public void setDistance(int distance) {
            Distance = distance;
            Score = calcScore();
        }

        // estimates the distance of the shortest path assuming the shortest path uses this NODE
        private int calcScore(){
            return getHeuristic();// + Distance;       // estimated distance to go plus current distance
        }

        // returns estimated distance to the end from current NODE
        private int getHeuristic(){
            int XToGo = Math.abs(X - EndX);     // use absolute value to ignore direction
            int YToGo = Math.abs(Y - EndY);

            return XToGo + YToGo;
        }

        @Override
        public String toString(){
            return stat + " " + Distance + " " + Score + " " + X + " " + Y;
        }

        @Override
        public int compareTo(NODE rhs) {
//            if(Distance == rhs.Distance)
//                return Score - rhs.Score;
//            else
//                return Distance - rhs.Distance;
            if(Score - rhs.Score == 0)
                return Distance - rhs.Distance;
            else
                return Score - rhs.Score;
        }
    }
    class DistanceComparator implements Comparator<NODE> {
        @Override
        public int compare(NODE left, NODE right) {
            return left.Distance - right.Distance;
        }
    }
}
