package PathFinding;
import PathFinding.BackEnd.Templates;
import PathFinding.GUI.*;
import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(900, 600);

        frame.setLayout(new BorderLayout());

        Templates temp = new Templates();

        Grid grid = new Grid(temp);

        frame.add(grid, BorderLayout.CENTER);

        ToolBar t = new ToolBar(grid, temp);
        frame.add(t, BorderLayout.NORTH);

        Algorithms algo = new Algorithms();
        frame.add(algo, BorderLayout.WEST);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}
