package PathFinding;
import PathFinding.BackEnd.Templates;
import PathFinding.GUI.*;
import javafx.scene.control.RadioButton;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(900, 600);

        frame.setLayout(new BorderLayout());

        Templates temp = new Templates();
        JRadioButton Dijkstra = new JRadioButton("Dijkstra's", true);
        JRadioButton Astar = new JRadioButton("A*");;

        Grid grid = new Grid(temp);

        frame.add(grid, BorderLayout.CENTER);

        ToolBar t = new ToolBar(grid, temp, Dijkstra, Astar);
        frame.add(t, BorderLayout.NORTH);

        AlgorithmsSideBar algo = new AlgorithmsSideBar(Dijkstra, Astar);
        frame.add(algo, BorderLayout.WEST);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}
