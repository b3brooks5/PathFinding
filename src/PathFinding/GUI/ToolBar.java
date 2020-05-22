package PathFinding.GUI;

import PathFinding.BackEnd.Dijkstra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JPanel implements ActionListener {
    Grid GRID;
    boolean RUNNING = false, DIAGONALS = false, RUN = false;
    double distance;
    String speed;
    JButton refresh, clear, play;
    Checkbox check;
    Dijkstra Dij;

    public ToolBar(Grid grid) {
        super();

        GridBagLayout g = new GridBagLayout();
        setLayout(g);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        check = new Checkbox("Allow diagonals", false);
        g.setConstraints(check, c);
        add(check);

        c.gridx = 1;
        c.gridy = 0;
        JLabel distnace = new JLabel("Diagonal distance");
        g.setConstraints(distnace, c);
        add(distnace);

        Double[] sizeOptions = {1.0, 1.5, 2.0};
        JComboBox<Double> diagonal = new JComboBox<Double>(sizeOptions);
        diagonal.setSelectedIndex(0);
        c.gridx = 2;
        c.gridwidth = 1;
        g.setConstraints(diagonal, c);
        add(diagonal);

        //c.gridwidth = GridBagConstraints.REMAINDER;     // end of line
        play = new JButton("Play");
        c.gridx = 3;
        g.setConstraints(play, c);
        play.addActionListener(this);
        add(play);

        //c.gridwidth = GridBagConstraints.RELATIVE;
        JLabel s = new JLabel("Speed");
        c.gridx = 0;
        c.gridy = 1;
        g.setConstraints(s, c);
        add(s);

        String[] speedOptions = {"slow", "normal", "fast", "finish"};
        JComboBox<String> speed = new JComboBox<String>(speedOptions);
        diagonal.setSelectedIndex(0);
        //c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 1;
        c.gridy = 1;
        g.setConstraints(speed, c);
        add(speed);

        refresh = new JButton("Refresh");
        c.gridx = 2;
        g.setConstraints(refresh, c);
        refresh.addActionListener(this);
        add(refresh);


        clear = new JButton("Clear");
        c.gridx = 3;
        g.setConstraints(clear, c);
        clear.addActionListener(this);
        add(clear);

        GRID = grid;
        //Dij = new Dijkstra(GRID.makeStrings());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refresh){
            GRID.restart();
            Dij.restart(GRID.makeStrings());
            RUN = false;
        }
        else if (e.getSource() == clear){
            System.out.printf("In clear\n");
            RUN = false;
            Dij.clear();
            GRID.update(Dij.makeStrings());
        }
        else if(e.getSource() == play && !RUN){
            //System.out.printf("Play was called\n");
            RUNNING = true;
            Dij = new Dijkstra(GRID.makeStrings());

            while(!Dij.step(DIAGONALS, distance)){
                GRID.update(Dij.makeStrings());
            }

            RUN = true;
            System.out.println();
            Dij.getShortestPath();
            GRID.update(Dij.makeStrings());

            RUNNING = false;
        }
        else if (e.getSource() == check){
            RUNNING = true;
        }
    }
}
