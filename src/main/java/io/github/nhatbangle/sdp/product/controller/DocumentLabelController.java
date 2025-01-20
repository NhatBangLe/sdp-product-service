package io.github.nhatbangle.sdp.product.controller;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.DocumentLabelResponse;
import io.github.nhatbangle.sdp.product.dto.request.DocumentLabelCreatingRequest;
import io.github.nhatbangle.sdp.product.mapper.DocumentLabelMapper;
import io.github.nhatbangle.sdp.product.service.DocumentLabelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/${app.version}/document/label")
public class DocumentLabelController {

    private final DocumentLabelService service;
    private final DocumentLabelMapper mapper;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<DocumentLabelResponse> getAllLabels(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String labelName,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var labels = service.queryAllLabels(userId, labelName, pageable);
        return PagingWrapper.fromPage(labels)
                .map(mapper::toResponse);
    }

    @GetMapping("/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public DocumentLabelResponse getLabel(
            @PathVariable @UUID String labelId
    ) {
        var label = service.getLabel(labelId);
        return mapper.toResponse(label);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentLabelResponse createLabel(
            @RequestBody @Valid DocumentLabelCreatingRequest body
    ) {
        var label = service.createLabel(body);
        return mapper.toResponse(label);
    }

    @PutMapping("/{labelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(
            @PathVariable @UUID String labelId,
            @RequestBody @Valid DocumentLabelUpdatingRequest body
    ) {
        service.updateLabel(labelId, body);
    }

    @DeleteMapping("/{labelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable @UUID String labelId) {
        service.deleteLabel(labelId);
    }

}
