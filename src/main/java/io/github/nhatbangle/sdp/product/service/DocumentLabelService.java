package io.github.nhatbangle.sdp.product.service;

import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelCreatingRequest;
import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.repository.DocumentLabelRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class DocumentLabelService {

    private final MessageSource messageSource;
    private final DocumentLabelRepository repository;
    private final UserService userService;

    /**
     * Query all {@link DocumentLabel} by user id
     *
     * @param userId    the id of the user
     * @param labelName the versionName of the label
     * @return the page of labels
     */
    @NotNull
    public Page<DocumentLabel> queryAllLabels(
            @NotNull @UUID String userId,
            @Nullable String labelName,
            @NotNull Pageable pageable
    ) {
        return repository.findAllByUser_IdAndNameContainsIgnoreCase(
                userId,
                Objects.requireNonNullElse(labelName, ""),
                pageable
        );
    }

    /**
     * Get a {@link DocumentLabel} by id
     *
     * @param labelId the id of the label
     * @return the label
     * @throws IllegalArgumentException if the label is not found
     */
    @NotNull
    public DocumentLabel getLabel(@NotNull @UUID String labelId)
            throws IllegalArgumentException {
        return repository.findById(labelId).orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "document_label.not_found",
                            new Object[]{labelId},
                            Locale.getDefault()
                    );
                    return new IllegalArgumentException(message);
                }
        );
    }

    /**
     * Create a new {@link DocumentLabel}
     *
     * @param request the information of the label
     * @return the created label
     * @throws IllegalArgumentException     if the user is not found
     * @throws ConstraintViolationException if the information is invalid
     */
    @NotNull
    public DocumentLabel createLabel(@NotNull @Valid DocumentLabelCreatingRequest request)
            throws IllegalArgumentException, ConstraintViolationException {
        var userId = request.userId();
        User user;
        try {
            user = userService.getUserById(userId);
        } catch (IllegalArgumentException e) {
            user = User.builder().id(userId).build();
        }
        var label = DocumentLabel.builder()
                .name(request.name())
                .description(request.description())
                .user(user)
                .build();
        return repository.save(label);
    }

    /**
     * Update entire a {@link DocumentLabel}
     *
     * @param labelId the id of the label
     * @param body    the new information of the label
     * @throws IllegalArgumentException     if the label is not found
     * @throws ConstraintViolationException if the new information is invalid
     */
    public void updateLabel(
            @NotNull @UUID String labelId,
            @NotNull @Valid DocumentLabelUpdatingRequest body
    ) throws IllegalArgumentException, ConstraintViolationException {
        var label = getLabel(labelId);
        label.setName(body.name());
        label.setDescription(body.description());
        repository.save(label);
    }

    /**
     * Delete a label by id
     *
     * @param labelId the id of the label
     */
    public void deleteLabel(@NotNull @UUID String labelId) {
        repository.deleteById(labelId);
    }

}
