package PathFinding;
import PathFinding.GUI.*;
import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(900, 400);

        Grid grid = new Grid();

        frame.add(grid);
        frame.setResizable(false);

        frame.setVisible(true);
    }
}
