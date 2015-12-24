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
}
