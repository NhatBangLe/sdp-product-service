package io.github.nhatbangle.sdp.product.service;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final MessageSource messageSource;
    private final UserRepository repository;

    /**
     * Get a {@link User} by id
     * @param userId the id of the user
     * @return the user
     * @throws IllegalArgumentException if the user is not found
     * @throws ServiceUnavailableException if the authentication service is unavailable
     */
    public User getUserById(@NotNull @UUID String userId)
            throws NoSuchElementException, ServiceUnavailableException {
        validateUserId(userId);
        return repository.findById(userId).orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "user.not_found",
                            new Object[]{userId},
                            Locale.getDefault()
                    );
                    return new NoSuchElementException(message);
                }
        );
    }

    public boolean isUserAvailable(@NotNull @UUID String userId) throws ServiceUnavailableException {
        return true;
    }

    /**
     * Validate the user id
     * @param userId the id of the user
     * @throws IllegalArgumentException if the user is not found
     * @throws ServiceUnavailableException if the authentication service is unavailable
     */
    public void validateUserId(@NotNull @UUID String userId)
            throws IllegalArgumentException, ServiceUnavailableException {
        if (!isUserAvailable(userId)) {
            var message = messageSource.getMessage(
                    "user.not_found",
                    new Object[]{userId},
                    Locale.getDefault()
            );
            throw new IllegalArgumentException(message);
        }
    }

}
