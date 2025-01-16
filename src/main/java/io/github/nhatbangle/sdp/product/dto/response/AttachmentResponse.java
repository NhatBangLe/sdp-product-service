package io.github.nhatbangle.sdp.product.dto.response;

import java.io.Serializable;

public record AttachmentResponse(
        String attachmentId,
        long createdAtMillis
) implements Serializable {
}
