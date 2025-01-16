package io.github.nhatbangle.sdp.product.controller.product;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductChangelogUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductChangelogCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ProductChangelogResponse;
import io.github.nhatbangle.sdp.product.mapper.ProductMapper;
import io.github.nhatbangle.sdp.product.service.product.ProductChangelogService;
import jakarta.validation.Valid;
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
@RequestMapping(path = "/api/${app.version}/product/changelog")
public class ProductChangelogController {

    private final ProductChangelogService service;
    private final ProductMapper mapper;

    @GetMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ProductChangelogResponse> queryAllChangelogs(
            @PathVariable @UUID String productVersionId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "6") int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        var changelogs = service.queryAllChangelogs(productVersionId, name, pageable);
        return PagingWrapper.fromPage(changelogs)
                .map(mapper::toResponse);
    }

    @GetMapping("/{changelogId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductChangelogResponse getChangelog(
            @PathVariable @UUID String changelogId
    ) {
        var changelog = service.getChangelog(changelogId);
        return mapper.toResponse(changelog);
    }

    @PostMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductChangelogResponse createChangelog(
            @PathVariable @UUID String productVersionId,
            @RequestBody @Valid ProductChangelogCreatingRequest body
    ) {
        var changelog = service.createChangelog(productVersionId, body);
        return mapper.toResponse(changelog);
    }

    @PutMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChangelog(
            @PathVariable @UUID String productVersionId,
            @RequestBody @Valid ProductChangelogUpdatingRequest body
    ) {
        service.updateChangelog(productVersionId, body);
    }

    @DeleteMapping("/{changelogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChangelog(@PathVariable @UUID String changelogId) {
        service.deleteChangelog(changelogId);
    }

}
