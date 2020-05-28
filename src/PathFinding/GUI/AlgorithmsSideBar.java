package PathFinding.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlgorithmsSideBar extends JPanel implements ActionListener {
    private JRadioButton Dijkstra, AStar;

    public AlgorithmsSideBar(JRadioButton dij, JRadioButton ast){
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Algorithms");
        add(title);

        Dijkstra = dij;
        Dijkstra.addActionListener(this);
        add(Dijkstra);

        AStar = ast;
        AStar.addActionListener(this);
        add(AStar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == Dijkstra){      // only one can be selected at a time
            AStar.setSelected(false);
        }
        else if(e.getSource() == AStar){
            Dijkstra.setSelected(false);
        }
    }
}
