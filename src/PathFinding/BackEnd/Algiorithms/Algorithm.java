package PathFinding.BackEnd.Algiorithms;

public abstract class Algorithm {

    public abstract int step(boolean Diagonals);
    public abstract void clear();
    public abstract String[][] makeStrings();
    public abstract void restart(String[][] rhs);

}
