package com.benkimball.ndig;

import net.jcip.annotations.Immutable;

@Immutable
public class NdResult {
    private final String message;

    public NdResult(String message, Object... args) {
        this.message = String.format(message, args);
    }

    public String getMessage() {
        return message;
    }
}
