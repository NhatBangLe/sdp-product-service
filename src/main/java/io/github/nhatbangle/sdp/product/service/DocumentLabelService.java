package io.github.nhatbangle.sdp.product.service;

import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.DocumentLabel;
import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelCreatingRequest;
import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.DocumentLabelRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "document-labels")
public class DocumentLabelService {

    private final MessageSource messageSource;
    private final DocumentLabelRepository repository;
    private final UserService userService;

    /**
     * Query all {@link DocumentLabel} by user id
     *
     * @param userId    the id of the user
     * @param labelName the versionName of the label
     * @return the page of labelIds
     */
    @NotNull
    public Page<DocumentLabel> queryAllLabels(
            @NotNull @UUID String userId,
            @Nullable String labelName,
            @NotNull Pageable pageable
    ) {
        return repository.findAllByUser_IdAndNameContainsIgnoreCase(userId, labelName, pageable);
    }

    /**
     * Get a {@link DocumentLabel} by id
     *
     * @param labelId the id of the label
     * @return the label
     * @throws NoSuchElementException if the label is not found
     */
    @NotNull
    @Cacheable(key = "#labelId")
    public DocumentLabel getLabel(@NotNull @UUID String labelId) throws NoSuchElementException {
        return findLabel(labelId);
    }

    private DocumentLabel findLabel(String labelId) throws NoSuchElementException {
        return repository.findById(labelId).orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "document_label.not_found",
                            new Object[]{labelId},
                            Locale.getDefault()
                    );
                    return new NoSuchElementException(message);
                }
        );
    }

    /**
     * Create a new {@link DocumentLabel}
     *
     * @param request the information of the label
     * @return the created label
     * @throws NoSuchElementException if the user is not found
     */
    @NotNull
    public DocumentLabel createLabel(@NotNull @Valid DocumentLabelCreatingRequest request)
            throws NoSuchElementException, ServiceUnavailableException {
        var userId = request.userId();
        User user;
        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException e) {
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
     * @param request the new information of the label
     * @return the updated label
     * @throws NoSuchElementException if the label is not found
     */
    @NotNull
    @CachePut(key = "#labelId")
    public DocumentLabel updateLabel(
            @NotNull @UUID String labelId,
            @NotNull @Valid DocumentLabelUpdatingRequest request
    ) throws NoSuchElementException {
        var label = findLabel(labelId);
        label.setName(request.name());
        label.setDescription(request.description());
        return repository.save(label);
    }

    /**
     * Delete a label by id
     *
     * @param labelId the id of the label
     */
    @CacheEvict(key = "#labelId")
    public void deleteLabel(@NotNull @UUID String labelId) {
        repository.deleteById(labelId);
    }

}
