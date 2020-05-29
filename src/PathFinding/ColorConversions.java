package PathFinding;

import PathFinding.BackEnd.Algiorithms.Dijkstra;

import javax.swing.*;
import java.awt.*;

public class ColorConversions {

// turns string into a color object
    public static Color makeColor(String color) {
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

    // change Java Color to a string of that color
    public static String ColorToString(Color c) {
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

    public static String StatusToString(String s){
        switch (s) {
            case "unvisited":
                return "lightGrey";
            case "visited":
                return "darkGrey";
            case "wall":
                return "black";
            case "stop":
                return "red";
            case "start":
                return "orange";
            case "path":
                return "blue";
            default:
                return "white";
        }
    }

}   // end ColorConversions
