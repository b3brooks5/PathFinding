package PathFinding.GUI;

import PathFinding.BackEnd.Dijkstra;
import PathFinding.BackEnd.Templates;

import javax.swing.*;
import javax.tools.Diagnostic;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class ToolBar extends JPanel implements ActionListener {
    Grid GRID;      // same grid in grid.java
    boolean RUNNING = false, DIAGONALS = false, RUN = false;
    double distance;
    String speed;
    Templates temp;

    // GUI buttons
    JButton refresh, clear, play, template;
    JCheckBox check;
    Dijkstra Dij;
    JComboBox<Double> Dbox;

    public ToolBar(Grid grid, Templates t) {
        super();

        setLayout(new GridLayout(3, 3));
        GridBagConstraints c = new GridBagConstraints();

        check = new JCheckBox("Allow Diagonals", false);
        check.addActionListener(this);
        add(check, c);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));

        JLabel l = new JLabel("Diagonal Distance");

        Double[] sizeOptions = {1.0, 1.5, 2.0, 2.1, 2.5, 5.0};
        Dbox = new JComboBox<Double>(sizeOptions);
        Dbox.setSelectedIndex(0);
        Dbox.addActionListener(this);

        p.add(l);
        p.add(Dbox);

        //add(p, c);

        template = new JButton("Template");
        template.addActionListener(this);
        add(template, c);


        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1, 2));

        String[] speedOptions = {"slow", "normal", "fast", "finish"};   // drop box for speed
        JComboBox<String> speed = new JComboBox<String>(speedOptions);
        speed.setSelectedIndex(3);

        JLabel s = new JLabel("Speed");

        p2.add(s);
        p2.add(speed);

        add(p2, c);

        refresh = new JButton("Refresh");
        refresh.addActionListener(this);
        add(refresh, c);

        clear = new JButton("clear");
        clear.addActionListener(this);
        add(clear, c);

        play = new JButton("Play");
        play.addActionListener(this);
        add(play, c);

        GRID = grid;
        distance = 1.0;
        temp = t;
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
                    JOptionPane.showMessageDialog(null, "The red square cannot be reached");
                    clear();
                    return;
                }
                GRID.update(Dij.makeStrings());     // make string and update board

//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
            }

            RUN = true;

            System.out.println();
            Dij.printDistances();

            Dij.getShortestPath(DIAGONALS);
            GRID.update(Dij.makeStrings());

            RUNNING = false;            // program is now no longer running
        }
        else if (e.getSource() == check && !RUNNING){       // if diagonals are allowed
            DIAGONALS = check.isSelected();     // set diagonals to the value of the checkbox
        }
        else if(e.getSource() == Dbox && !RUNNING){
            distance = (double)Dbox.getSelectedItem();
        }
        else if(e.getSource() == template && !RUNNING){
            GRID.update(temp.getNext());
        }
    }
}
