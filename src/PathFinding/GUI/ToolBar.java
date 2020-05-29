package PathFinding.GUI;

import PathFinding.BackEnd.Algiorithms.Algorithm;
import PathFinding.BackEnd.Algiorithms.AStar;
import PathFinding.BackEnd.Algiorithms.Dijkstra;
import PathFinding.BackEnd.Templates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JPanel implements ActionListener {
    Grid GRID;      // same grid in grid.java
    boolean RUNNING = false, DIAGONALS = false, RUN = false;
    double distance;
    String speed;
    Templates temp;

    // GUI buttons
    JRadioButton Dijkstra, AStar;
    JButton refresh, clear, play, template;
    JCheckBox check;
    //Dijkstra Dij;
    Algorithm algo;
    JComboBox<Double> Dbox;

    public ToolBar(Grid grid, Templates t, JRadioButton Dij, JRadioButton As) {
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
        Dijkstra = Dij;
        AStar = As;
    }

    private void clear(){       // general function used to clear the board
        RUN = false;
        if(algo != null) {
            algo.clear();
            GRID.update(algo.makeStrings());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refresh){           // restart the board
            GRID.restart();
            if(algo != null)
                algo.restart(GRID.makeStrings());
            RUN = false;
            temp.startOver();
        }
        else if (e.getSource() == clear){       // clears the board with the same start, stop
            clear();
            temp.startOver();       // templates will now start from first template
        }
        else if(e.getSource() == play && !RUN){
            int stepping = 1;

            RUNNING = true;         // true if currently finding path, needs to block other buttons from executing

            if(Dijkstra.isSelected()){
                System.out.println("Creating Dikjkastras");
                algo = new Dijkstra(GRID.makeStrings());
            }
            else if (AStar.isSelected())
                algo = new AStar(GRID.makeStrings());
            else{
                System.out.println("Their was a problem initializing");
                return;
            }


            while(stepping == 1){                       // while you can still step
                stepping = algo.step(DIAGONALS);

                if(stepping == -1){     // the stop tile cannot be reached
                    JOptionPane.showMessageDialog(null, "The red square cannot be reached");
                    clear();
                    return;
                }

                GRID.update(algo.makeStrings());     // make string and update board

//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
            }
            temp.startOver();
            RUN = true;

            //algo.getShortestPath(DIAGONALS);
            GRID.update(algo.makeStrings());

            RUNNING = false;            // program is now no longer running
        }
        else if (e.getSource() == check && !RUNNING){       // if diagonals are allowed
            DIAGONALS = check.isSelected();     // set diagonals to the value of the checkbox
        }
        else if(e.getSource() == Dbox && !RUNNING){
            distance = (double)Dbox.getSelectedItem();
        }
        else if(e.getSource() == template && !RUNNING){
            clear();
            GRID.update(temp.getNext());
        }
    }
}
