package io.github.nhatbangle.sdp.product.exception;

import java.io.Serial;

public class DataConflictException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8525033168419514272L;

    public DataConflictException(String message) {
        super(message);
    }

}
