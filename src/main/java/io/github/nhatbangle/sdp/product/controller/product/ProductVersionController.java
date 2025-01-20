package io.github.nhatbangle.sdp.product.controller.product;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductVersionCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductVersionUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ProductVersionResponse;
import io.github.nhatbangle.sdp.product.mapper.ProductMapper;
import io.github.nhatbangle.sdp.product.service.product.ProductVersionService;
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
@RequestMapping(path = "/api/${app.version}/product/version")
public class ProductVersionController {

    private final ProductVersionService service;
    private final ProductMapper mapper;

    @GetMapping("/{productId}/product")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ProductVersionResponse> getAllVersionsByProductId(
            @PathVariable @UUID String productId,
            @RequestParam(required = false) String versionName,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var version = service.queryAllVersions(productId, versionName, isUsed, pageable);
        return PagingWrapper.fromPage(version)
                .map(mapper::toResponse);
    }

    @GetMapping("/{versionId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductVersionResponse getVersion(
            @PathVariable @UUID String versionId
    ) {
        var version = service.getVersion(versionId);
        return mapper.toResponse(version);
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVersionResponse createVersion(
            @PathVariable @UUID String productId,
            @RequestBody @Valid ProductVersionCreatingRequest body
    ) {
        var version = service.createVersion(productId, body);
        return mapper.toResponse(version);
    }

    @PutMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVersion(
            @PathVariable @UUID String versionId,
            @RequestBody ProductVersionUpdatingRequest body
    ) {
        service.updateVersion(versionId, body);
    }

    @DeleteMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVersion(
            @PathVariable @UUID String versionId
    ) {
        service.deleteVersion(versionId);
    }

}
