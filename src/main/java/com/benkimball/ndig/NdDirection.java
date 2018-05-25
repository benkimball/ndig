package com.benkimball.ndig;

import java.util.Arrays;
import java.util.List;

public class NdDirection {

    private static final List<String> pairs = Arrays.asList(
            "north", "south",
            "east", "west",
            "up", "down",
            "northeast", "southwest",
            "northwest", "southeast",
            "in", "out",
            "clockwise", "anticlockwise",
            "widdershins", "turnwise",
            "spinward", "trailing",
            "rimward", "hubward",
            "left", "right",
            "backward", "forward",
            "toward", "away",
            "over", "under");

    public static String reverse(String direction) {
        String reversed;
        int i = pairs.indexOf(direction.toLowerCase());
        if(i == -1) {
            reversed = "anti" + direction;
        } else {
            int j = (i % 2 == 0) ? i + 1 : i - 1;
            reversed = pairs.get(j);
        }
        return reversed;
    }

    public static String normalize(String direction) {
        String match = direction.toLowerCase();
        switch (match) {
            case "n":
                match = "north";
                break;
            case "s":
                match = "south";
                break;
            case "e":
                match = "east";
                break;
            case "w":
                match = "west";
                break;
            case "ne":
                match = "northeast";
                break;
            case "nw":
                match = "northwest";
                break;
            case "se":
                match = "southeast";
                break;
            case "sw":
                match = "southwest";
                break;
            case "u":
                match = "up";
                break;
            case "d":
                match = "down";
                break;
        }
        return match;
    }
}
