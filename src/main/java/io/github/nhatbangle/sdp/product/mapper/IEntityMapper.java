package io.github.nhatbangle.sdp.product.mapper;

public interface IEntityMapper<T, R> {
    R toResponse(T entity);
}
