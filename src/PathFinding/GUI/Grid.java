package PathFinding.GUI;
import PathFinding.BackEnd.Dijkstra;
import PathFinding.BackEnd.Templates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class Grid extends JPanel implements MouseMotionListener, MouseListener {
    private int width = 40, height = 20;               // size of the grid
    private JPanel[][] grid;                              // grid containing the panels for display
    private int prevX, prevY;                           // x, y coordinates of previous squares
    private Color prevC = Color.LIGHT_GRAY;             // color of square last one
    private boolean mouseDragged = false, mouseGone = false;               // if mouse is currently being dragged
    private Templates temp;

    private String[][] test = {
            {"lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"red", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "orange", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "black", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"},
            {"lightGrey", "lightGrey", "lightGrey", "black", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey", "lightGrey", "lightGrey", "lightGrey", "lightGrey","lightGrey"}};

    public Grid(Templates t) {
        super();

        setLayout(new GridLayout(height,width));
        addMouseMotionListener(this);
        addMouseListener(this);
        setSize(900, 400);

        temp = t;

        init();
    }

    public void init() {
        grid = new JPanel[width][height];

        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                JPanel working = new JPanel();
                working.setBackground(Color.LIGHT_GRAY);
                add(working);
                grid[j][i] = working;
            }

        startSquares();
    }

    // set start and stop squares
    private void startSquares(){        // create start and end squares
        Random r = new Random();
        int startX, startY, endX, endY;

        do {
            startX = r.nextInt(grid.length);
            startY = r.nextInt(grid[0].length);
            endX = r.nextInt(grid.length);
            endY = r.nextInt(grid[0].length);

        } while(startX == endX && startY == endY);      // run until they are different

        grid[startX][startY].setBackground(Color.ORANGE);
        grid[endX][endY].setBackground(Color.RED);
    }

    // change the board to the grid given by rhs
    public void update(String [][] rhs){        // changes the grid to the new layout
        for(int i = 0; i < width; i++)         // change sll panel colors to new colors
            for(int j = 0; j < height; j++) {
                grid[i][j].setBackground(makeColor(rhs[i][j]));
            }
        revalidate();
    }

    // reset the board to new configurations
    public void restart() {
        for (JPanel[] tiles : grid)
            for (JPanel t : tiles)
                t.setBackground(Color.LIGHT_GRAY);
        startSquares();
    }

    // turns string into a color object
    private Color makeColor(String color) {
        switch (color) {
            case "lightGrey":
                return Color.LIGHT_GRAY;
            case "black":
                return Color.BLACK;
            case "gray":
                return Color.GRAY;
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "darkGrey":
                return Color.darkGray;
            case "orange":
                return Color.ORANGE;
        }

        return Color.WHITE;
    }

    // paint the panel as java Color objects
    private void paintPanel() {
        for(JPanel[] row : grid) {
            System.out.println();
            for (JPanel c : row) {
                System.out.printf("%-10s, ", c.getBackground());
            }
        }
    }

    // makes the grid into a 2D array of strings
    public String[][] makeStrings() {
        String[][] ret = new String[grid.length][grid[0].length];

        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++) {
                ret[i][j] = color(grid[i][j]);
            }

        return ret;
    }

    // print the grid 2D array as strings to stdout
    public void printStrings() {
        String[][] p = makeStrings();

        for(int i = 0; i < height; i++) {
            System.out.println("},");
            for (int j = 0; j < width; j++) {
                System.out.printf("\"%s\", ", p[j][i]);
            }
        }
    }

    // change Java Color to a string of that color
    private String color(JPanel t) {
        Color c = t.getBackground();

        if(c.equals(Color.BLACK))
            return "black";
        else if(c.equals(Color.LIGHT_GRAY))
            return "lightGrey";
        else if(c.equals(Color.DARK_GRAY))
            return "darkgrey";
        else if(c.equals(Color.GRAY))
            return "gray";
        else if(c.equals(Color.RED))
            return "red";
        else if(c.equals(Color.BLUE))
            return "blue";
        else if(c.equals((Color.ORANGE)))
            return "orange";
        else return "empty";
    }

    // return true if you are in the panel
    private boolean inSquare(MouseEvent e){
        int width = grid[0][0].getWidth();
        int height = grid[0][0].getHeight();

        // if inside the frame
        return (e.getX() / width < grid.length && e.getY() > 0 && e.getY() / height < grid[0].length && e.getX() > 0);
    }

    // return true if you aren't over a start or stop square
    private boolean isStartStop(MouseEvent e){
        int width = grid[0][0].getWidth();
        int height = grid[0][0].getHeight();

        return !grid[e.getX() / width][e.getY() / height].getBackground().equals(Color.RED) && !grid[e.getX() / width][e.getY() / height].getBackground().equals(Color.ORANGE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // just need to change the color
        int width = grid[0][0].getWidth();
        int height = grid[0][0].getHeight();

        // if the square wasn't red or orange
        if (!prevC.equals(Color.RED) && !prevC.equals(Color.ORANGE)) {
            grid[e.getX() / width][e.getY() / height].setBackground(Color.BLACK);
            prevC = Color.BLACK;
        }
        temp.startOver();

        //B = new Dijkstra(makeStrings());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {       // dragging is done
        mouseDragged = false;
        //paintPanel();
        //printStrings();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseGone = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseGone = true;

        grid[prevX][prevY].setBackground(prevC);     // remove the grey square when mouse exits
    }

    @Override
    public void mouseDragged(MouseEvent e) {        // set every tile you drag over to black
        if(!mouseGone) {
            int width = grid[0][0].getWidth();
            int height = grid[0][0].getHeight();

            // if not over start stop square
            if (inSquare(e) && isStartStop(e) && !prevC.equals(Color.RED) && !prevC.equals(Color.ORANGE)) {
                grid[e.getX() / width][e.getY() / height].setBackground(Color.BLACK);
                prevC = Color.BLACK;

                mouseDragged = true;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {      // change the tile you mouse over to grey
        int width = grid[0][0].getWidth();
        int height = grid[0][0].getHeight();

        // if inside the frame
        if(inSquare(e)){
            if (e.getY() / height != prevY || e.getX() / width != prevX) {  // if mouse moved on to the next square
                grid[prevX][prevY].setBackground(prevC);    // reset previous square

                prevX = e.getX() / width;  // current square is now previous
                prevY = e.getY() / height;
                prevC = grid[prevX][prevY].getBackground();
            }

            if (!mouseDragged) {    // if moused over light_gray square set gray
                grid[e.getX() / width][e.getY() / height].setBackground(Color.GRAY);
            }
        }
    }
}

