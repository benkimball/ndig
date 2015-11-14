package com.benkimball.ndig;

import java.time.Duration;

public class NdDurationFormatter {

    public static String format(Duration d) {
        String formatted;
        long s = d.getSeconds();
        long days = s / 86400; /* ignores DST and I don't care */
        long hours = (s % 86400) / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        if(days > 0) {
            formatted = String.format("%dd %dh", days, hours);
        } else if(hours > 0) {
            formatted = String.format("%dh %dm", hours, minutes);
        } else if(minutes > 0) {
            formatted = String.format("%dm %ds", minutes, seconds);
        } else {
            formatted = String.format("%ds", seconds);
        }
        return formatted;
    }
}
