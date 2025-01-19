package io.github.nhatbangle.sdp.product.mapper;

import jakarta.validation.constraints.NotNull;

public interface IEntityMapper<T, R> {
    @NotNull
    R toResponse(@NotNull T entity);
}
