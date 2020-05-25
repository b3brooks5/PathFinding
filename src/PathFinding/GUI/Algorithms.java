package PathFinding.GUI;

import PathFinding.BackEnd.Dijkstra;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Algorithms extends JPanel implements ActionListener {
    private JRadioButton Dijkstra, AStar;

    public Algorithms(){
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Algorithms");
        add(title);

        Dijkstra = new JRadioButton("Dijkstra's", true);
        Dijkstra.addActionListener(this);
        add(Dijkstra);

        AStar = new JRadioButton("A*");
        AStar.addActionListener(this);
        add(AStar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == Dijkstra){
            System.out.printf("Selected Dijkestra");

        }
    }
}
