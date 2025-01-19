package io.github.nhatbangle.sdp.product.controller.module;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleVersionCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleVersionUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ModuleVersionResponse;
import io.github.nhatbangle.sdp.product.mapper.ModuleMapper;
import io.github.nhatbangle.sdp.product.service.module.ModuleVersionService;
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
@RequestMapping(path = "/api/${app.version}/product/module/version")
public class ModuleVersionController {

    private final ModuleVersionService service;
    private final ModuleMapper mapper;

    @GetMapping("/{productId}/product")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleVersionResponse> getAllVersions(
            @PathVariable @UUID String productId,
            @RequestParam(required = false) String versionName,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "6") int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var version = service.queryAllVersions(productId, versionName, isUsed, pageable);
        return PagingWrapper.fromPage(version)
                .map(mapper::toResponse);
    }

    @GetMapping("/{versionId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleVersionResponse getVersion(
            @PathVariable @UUID String versionId
    ) {
        var version = service.getVersion(versionId);
        return mapper.toResponse(version);
    }

    @PostMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleVersionResponse createVersion(
            @PathVariable @UUID String moduleId,
            @RequestBody @Valid ModuleVersionCreatingRequest body
    ) {
        var version = service.createVersion(moduleId, body);
        return mapper.toResponse(version);
    }

    @PutMapping("/{versionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVersion(
            @PathVariable @UUID String versionId,
            @RequestBody ModuleVersionUpdatingRequest body
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
