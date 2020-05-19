package PathFinding;
import PathFinding.GUI.*;
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(900, 400);
        frame.setLayout(new BorderLayout());

        Grid grid = new Grid();

        frame.add(grid, BorderLayout.CENTER);

        JLabel l = new JLabel("Hello");
        frame.add(l, BorderLayout.NORTH);

        frame.setResizable(false);

        frame.setVisible(true);
    }
}
