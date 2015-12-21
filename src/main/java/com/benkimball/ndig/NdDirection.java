package com.benkimball.ndig;

import java.util.HashMap;
import java.util.Map;

public enum NdDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTHEAST,
    SOUTHWEST,
    NORTHWEST,
    SOUTHEAST,
    UP,
    DOWN;

    private static final Map<NdDirection, NdDirection> opposites = new HashMap<>();

    static {
        opposites.put(NORTH, SOUTH);
        opposites.put(SOUTH, NORTH);
        opposites.put(EAST, WEST);
        opposites.put(WEST, EAST);
        opposites.put(NORTHEAST, SOUTHWEST);
        opposites.put(NORTHWEST, SOUTHEAST);
        opposites.put(SOUTHWEST, NORTHEAST);
        opposites.put(SOUTHEAST, NORTHWEST);
        opposites.put(UP, DOWN);
        opposites.put(DOWN, UP);
    }

    public NdDirection reverse(NdDirection direction) {
        return opposites.get(direction);
    }
}
