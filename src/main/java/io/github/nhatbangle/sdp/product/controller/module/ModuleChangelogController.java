package io.github.nhatbangle.sdp.product.controller.module;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleChangelogCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleChangelogUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ModuleChangelogResponse;
import io.github.nhatbangle.sdp.product.mapper.ModuleMapper;
import io.github.nhatbangle.sdp.product.service.module.ModuleChangelogService;
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
@RequestMapping(path = "/api/${app.version}/product/module/changelog")
public class ModuleChangelogController {

    private final ModuleChangelogService service;
    private final ModuleMapper mapper;

    @GetMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleChangelogResponse> queryAllChangelogs(
            @PathVariable @UUID String productVersionId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        var changelogs = service.queryAllChangelogs(productVersionId, name, pageable);
        return PagingWrapper.fromPage(changelogs)
                .map(mapper::toResponse);
    }

    @GetMapping("/{changelogId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleChangelogResponse getChangelog(
            @PathVariable @UUID String changelogId
    ) {
        var changelog = service.getChangelog(changelogId);
        return mapper.toResponse(changelog);
    }

    @PostMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleChangelogResponse createChangelog(
            @PathVariable @UUID String productVersionId,
            @RequestBody @Valid ModuleChangelogCreatingRequest body
    ) {
        var changelog = service.createChangelog(productVersionId, body);
        return mapper.toResponse(changelog);
    }

    @PutMapping("/{productVersionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChangelog(
            @PathVariable @UUID String productVersionId,
            @RequestBody @Valid ModuleChangelogUpdatingRequest body
    ) {
        service.updateChangelog(productVersionId, body);
    }

    @DeleteMapping("/{changelogId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable @UUID String changelogId) {
        service.deleteChangelog(changelogId);
    }

}
