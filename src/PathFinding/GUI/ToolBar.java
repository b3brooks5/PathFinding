package PathFinding.GUI;

import PathFinding.BackEnd.Dijkstra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JPanel implements ActionListener {
    Grid GRID;      // same grid in grid.java
    boolean RUNNING = false, DIAGONALS = false, RUN = false;
    double distance;
    String speed;

    // GUI buttons
    JButton refresh, clear, play, template;
    Checkbox check;
    Dijkstra Dij;

    public ToolBar(Grid grid) {
        super();

        GridBagLayout g = new GridBagLayout();
        setLayout(g);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;        // starting coordinates
        c.gridy = 0;
        c.gridwidth = 1;
        check = new Checkbox("Allow diagonals", false); // allow diagonals
        g.setConstraints(check, c);
        add(check);

        c.gridx = 1;
        c.gridy = 0;
        JLabel distnace = new JLabel("Diagonal distance");      // set diagonal distance
        g.setConstraints(distnace, c);
        add(distnace);

        Double[] sizeOptions = {1.0, 1.5, 2.0};
        JComboBox<Double> diagonal = new JComboBox<Double>(sizeOptions);
        diagonal.setSelectedIndex(0);
        c.gridx = 2;
        c.gridwidth = 1;
        g.setConstraints(diagonal, c);
        add(diagonal);

        play = new JButton("Play");                     // play button
        c.gridx = 3;
        g.setConstraints(play, c);
        play.addActionListener(this);
        add(play);
        // end of row 1

        JLabel s = new JLabel("Speed");
        c.gridx = 0;
        c.gridy = 1;
        g.setConstraints(s, c);
        add(s);

        String[] speedOptions = {"slow", "normal", "fast", "finish"};   // drop box for speed
        JComboBox<String> speed = new JComboBox<String>(speedOptions);
        diagonal.setSelectedIndex(0);
        c.gridx = 1;
        c.gridy = 1;
        g.setConstraints(speed, c);
        add(speed);

        refresh = new JButton("Refresh");           // get an all new board
        c.gridx = 2;
        g.setConstraints(refresh, c);
        refresh.addActionListener(this);
        add(refresh);


        clear = new JButton("Clear");               // clear the board with the same start, stop
        c.gridx = 3;
        g.setConstraints(clear, c);
        clear.addActionListener(this);
        add(clear);

        template = new JButton("Template");         // preset design to run
        c.gridx = 4;
        g.setConstraints(template, c);
        template.addActionListener(this);
        add(template);

        GRID = grid;
    }

    private void clear(){       // general function used to clear the board
        RUN = false;
        if(Dij != null) {
            Dij.clear();
            GRID.update(Dij.makeStrings());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refresh){           // restart the board
            GRID.restart();
            if(Dij != null)
                Dij.restart(GRID.makeStrings());
            RUN = false;
        }
        else if (e.getSource() == clear){       // clears the board with the same start, stop
            clear();
        }
        else if(e.getSource() == play && !RUN){
            int stepping = 1;
            RUNNING = true;         // true if currently finding path, needs to block other buttons from executing
            Dij = new Dijkstra(GRID.makeStrings());

            while(stepping == 1){                       // while you can still step
                stepping = Dij.step(DIAGONALS, distance);

                if(stepping == -1){     // the stop tile cannot be reached
                    clear();
                    return;
                }
                GRID.update(Dij.makeStrings());
            }

            RUN = true;

            Dij.getShortestPath();
            GRID.update(Dij.makeStrings());

            RUNNING = false;            // program is now no longer running
        }
        else if (e.getSource() == check){       // if diagonals are allowed
            RUNNING = true;
        }
    }
}
