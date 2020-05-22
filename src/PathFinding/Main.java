package PathFinding;
import PathFinding.GUI.*;
import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        Grid grid = new Grid();

        frame.add(grid, BorderLayout.CENTER);

        ToolBar t = new ToolBar(grid);
        frame.add(t, BorderLayout.NORTH);

        Algorithms algo = new Algorithms();
        frame.add(algo, BorderLayout.WEST);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}
