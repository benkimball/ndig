package com.benkimball.ndig;

import java.time.Duration;

public class DurationFormatter {

    public static String format(Duration d) {
        String formatted = "";
        long s = d.getSeconds();
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        formatted = String.format("%ds", seconds);
        if(minutes > 0) formatted = String.format("%dm %s", minutes, formatted);
        if(hours > 0) formatted = String.format("%dh %s", hours, formatted);
        return formatted;
    }
}
