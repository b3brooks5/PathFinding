package PathFinding.BackEnd.Algiorithms;

import PathFinding.ColorConversions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class AStar extends Algorithm {
    enum status {unvisited, visited, wall, stop, start, path}
    private int StartX, StartY, EndX, EndY;
    private PriorityQueue<NODE> calculated = new PriorityQueue<>();
    private ArrayList<NODE> visited = new ArrayList<>();
    private ArrayList<NODE> unvisited = new ArrayList<>();
    private NODE CURRENT;
    private int SIZE_RUN = 10;

    public AStar(String[][] strings){
        init(strings);
    }

    // initializes new unvisited list for searching
    private void init(String[][] strings) {
        for (int x = 0; x < strings.length; x++){
            for(int y = 0; y < strings[0].length; y++){

                if(strings[x][y].equals("orange")){      // save start and end points
                    StartX = x;
                    StartY = y;
                    calculated.add(new NODE(x, y, makeStatus(strings[x][y]), 0));    //start is always 0
                }
                else if(strings[x][y].equals("red")){
                    EndX = x;
                    EndY = y;
                    unvisited.add(new NODE(x, y, makeStatus(strings[x][y]), 1000));
                }
                else {
                    // always add to unvisited
                    unvisited.add(new NODE(x, y, makeStatus(strings[x][y]), 1000));
                }
            }
        }

        CURRENT = null;
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

    // retrieves one node
    private NODE getNODE(int x, int y){
        for(NODE n : unvisited){
            if(n.X == x && n.Y == y)
                return n;
        }
        return null;    // if node not in unvisited
    }

    public void score(int x, int y, int d){
        NODE working = getNODE(x, y);

        if(working != null) {
            working.setDistance(d + 1);
            if(working.stat != status.stop)
                working.stat = status.visited;
            calculated.add(working);
        }
    }

    // Scores the surrounding nodes around 'c' passed in
    private void ScoreSurrounding(boolean Diagonals){
        score(CURRENT.X + 1, CURRENT.Y, CURRENT.Distance);
        score(CURRENT.X - 1, CURRENT.Y, CURRENT.Distance);
        score(CURRENT.X, CURRENT.Y + 1, CURRENT.Distance);
        score(CURRENT.X, CURRENT.Y -1 , CURRENT.Distance);

        if(Diagonals){
            score(CURRENT.X + 1, CURRENT.Y -1, CURRENT.Distance);
            score(CURRENT.X - 1, CURRENT.Y + 1, CURRENT.Distance);
            score(CURRENT.X - 1 , CURRENT.Y + 1, CURRENT.Distance);
            score(CURRENT.X + 1, CURRENT.Y -1 , CURRENT.Distance);
        }
    }

    private NODE getSmallestNeighbor(NODE center, boolean Diagonals){
        PriorityQueue<NODE> neighbors = new PriorityQueue<NODE>(new DistanceComparator());
        NODE working;
        working = getNODE(center.X + 1, center.Y);
        if (working != null)
            neighbors.add(working);

        working = getNODE(center.X - 1, center.Y);
        if (working != null)
            neighbors.add(working);
        working = getNODE(center.X, center.Y + 1);
        if (working != null)
            neighbors.add(working);

        working = getNODE(center.X, center.Y - 1);
        if(working != null)
            neighbors.add(working);

        if(Diagonals){
            neighbors.add(getNODE(center.X + 1, center.Y - 1));
            neighbors.add(getNODE(center.X - 1, center.Y + 1));
            neighbors.add(getNODE(center.X - 1, center.Y + 1));
            neighbors.add(getNODE(center.X - 1, center.Y - 1));
        }

        System.out.println("smallest " +  neighbors.peek());
        NODE ret = neighbors.poll();
        if(ret.stat == status.path)
            return neighbors.poll();
        else
            return ret;    // return smallest neighbor

    }

    private void getPath(boolean Diagonals){
        // from the end node
        // get it's neighbor with the smallest distance
        // make it blue
        // set that one to the current node and reapeat until at start node
        NODE working = CURRENT;

        while (working.X != StartX && working.Y != StartY){
            working = getSmallestNeighbor(working, Diagonals);
            working.stat = status.path;
        }


    }

    @Override
    public int step(boolean Diagonals) {
        for(int i = 0; i < SIZE_RUN; i++){
            System.out.printf("run: %d\t", i);
            CURRENT = calculated.poll();
            if(CURRENT.stat == status.stop) {
                getPath(Diagonals);
                return 0;
            }
            System.out.println("Current: " + CURRENT);
            ScoreSurrounding(Diagonals);

            visited.add(CURRENT);
            CURRENT = null;
        }

        return 1;
    }

    // returns an array of strings
    @Override
    public String[][] makeStrings(){
        String[][] ret = new String[40][20];

        for(NODE n : unvisited)
            ret[n.X][n.Y] = ColorConversions.StatusToString(n.stat.toString());

        for(NODE n : visited)
            ret[n.X][n.Y] = ColorConversions.StatusToString(n.stat.toString());

        return ret;
    }

    @Override
    public void clear() {

    }

    // restarts board based on new rhs
    @Override
    public void restart(String[][] rhs) {
        visited.clear();
        unvisited.clear();
        init(rhs);
    }

    // prints strings for debugging
    public void printStrings(){
        String[][] p = makeStrings();

        for (int i = 0; i < p[0].length; i++){
            System.out.println();
            for(int j = 0; j < p.length; j++){
                System.out.printf("%-10s, ", p[j][i]);
            }
        }
    }

    private class NODE implements Comparable {
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
            return getHeuristic() + Distance;       // estimated distance to go plus current distance
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
        public int compareTo(Object o) {
            return this.Score - ((NODE)o).Score;
        }


    }
    class DistanceComparator implements Comparator{
        @Override
        public int compare(Object o, Object t1) {
            return ((NODE)o).Distance - ((NODE)t1).Distance;
        }
    }
}
