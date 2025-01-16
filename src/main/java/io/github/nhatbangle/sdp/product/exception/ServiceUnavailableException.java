package io.github.nhatbangle.sdp.product.exception;

import java.io.Serial;

public class ServiceUnavailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8397705973642617290L;

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
