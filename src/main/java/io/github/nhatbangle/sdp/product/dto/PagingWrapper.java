package io.github.nhatbangle.sdp.product.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record PagingWrapper<T>(
        Integer totalPages,
        Long totalElements,
        Integer number,
        Integer size,
        Integer numberOfElements,
        Boolean first,
        Boolean last,
        List<T> content
) {

    @NotNull
    public <R> PagingWrapper<R> map(@NotNull Function<T, R> mapper) {
        List<T> currentContent = Objects.requireNonNullElse(this.content, Collections.emptyList());
        List<R> newContent = currentContent.stream().map(mapper).toList();
        return new PagingWrapper<>(
                this.totalPages,
                this.totalElements,
                this.number,
                this.size,
                this.numberOfElements,
                this.first,
                this.last,
                newContent
        );
    }

    @NotNull
    public static <T> PagingWrapper<T> fromPage(@NotNull Page<T> page) {
        return new PagingWrapper<>(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.isFirst(),
                page.isLast(),
                page.toList()
        );
    }

}
