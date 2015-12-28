package com.benkimball.ndig;

public class NdException extends Exception {
    NdException() {
        super();
    }

    NdException(String message) {
        super(message);
    }

    NdException(Throwable throwable) {
        super(throwable);
    }

    NdException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
