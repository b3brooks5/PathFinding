package PathFinding.BackEnd.Algiorithms;

import java.util.Comparator;

public class AStar extends Algorithm {
    enum status {unvisited, visited, wall}
    private int StartX, StartY, EndX, EndY;

    public AStar(String[][] strings){
        init(strings);
    }

    private void init(String[][] strings) {
        for (int x = 0; x < strings.length; x++){
            for(int y = 0; y < strings[0].length; y++){
                if(strings[x][y].equals("start")){
                    StartX = x;
                    StartY = y;
                }
                else if(strings[x][y].equals("stop")){
                    EndX = x;
                    EndY = y;
                }
            }
        }
    }


    @Override
    public int step(boolean Diagonals) {
        return 0;
    }

    @Override
    public void clear() {

    }

    public String[][] makeStrings(){
        return new String[1][2];
    }

    @Override
    public void restart(String[][] rhs) {

    }


    private class NODE implements Comparator {
        status stat;
        int X, Y;
        int Distance, Score;

        public NODE(int x, int y, status s, int dis) {
            if(x < 0 )
                X = x;

            if(y < 0)
                Y = y;

            stat = s;

            if (dis > 0)
                Distance = dis;

            Score = calcScore();
        }

        // estimates the distance of the shortest path assuming it uses this NODE
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
        public int compare(Object o, Object t1) {
            return ((NODE)o).Score - ((NODE)t1).Score;
        }
    }
}
