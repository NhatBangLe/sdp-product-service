package io.github.nhatbangle.sdp.product.service;

import io.github.nhatbangle.sdp.product.entity.User;
import io.github.nhatbangle.sdp.product.exception.ServiceUnavailableException;
import io.github.nhatbangle.sdp.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
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
    public User getUserById(String userId) throws IllegalArgumentException, ServiceUnavailableException {
        validateUserId(userId);
        return repository.findById(userId).orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "user.not_found",
                            new Object[]{userId},
                            Locale.getDefault()
                    );
                    return new IllegalArgumentException(message);
                }
        );
    }

    public boolean isUserAvailable(String userId) throws ServiceUnavailableException {
        return true;
    }

    /**
     * Validate the user id
     * @param userId the id of the user
     * @throws IllegalArgumentException if the user is not found
     * @throws ServiceUnavailableException if the authentication service is unavailable
     */
    private void validateUserId(String userId)
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
