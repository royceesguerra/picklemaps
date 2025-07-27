package com.picklemaps.picklemaps_app.exceptions;

public class CourtNotFoundException extends BaseException {

    public CourtNotFoundException() {}

    public CourtNotFoundException(String message) {
        super(message);
    }

    public CourtNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourtNotFoundException(Throwable cause) {
        super(cause);
    }
}
