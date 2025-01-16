package io.github.nhatbangle.sdp.product.service;

import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final MessageSource messageSource;

    public boolean isFileExist(String fileId) throws ServiceUnavailableException {
        return true;
    }

    public void validateIds(Set<String> attachmentIds) {
        var notExistAttachments = attachmentIds.parallelStream()
                .filter(id -> !isFileExist(id))
                .toList();
        if (!notExistAttachments.isEmpty()) {
            var joiner = new StringJoiner("\n");
            notExistAttachments.forEach(joiner::add);
            var message = messageSource.getMessage(
                    "attachment.not_found",
                    new Object[]{joiner.toString()},
                    Locale.getDefault()
            );
            throw new IllegalArgumentException(message);
        }
    }

}
