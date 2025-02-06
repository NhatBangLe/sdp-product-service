package io.github.nhatbangle.sdp.product.exception;

import java.io.Serial;

public class InvalidKeyException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8397705973642617290L;

    public InvalidKeyException(String message) {
        super(message);
    }

}
