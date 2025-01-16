package io.github.nhatbangle.sdp.product.controller.module;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ModuleResponse;
import io.github.nhatbangle.sdp.product.mapper.ModuleMapper;
import io.github.nhatbangle.sdp.product.service.module.ModuleService;
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
@RequestMapping(path = "/api/${app.version}/product/module")
public class ModuleController {

    private final ModuleService service;
    private final ModuleMapper mapper;

    @GetMapping("/{productVersionId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleResponse> getAllProductsByUserId(
            @PathVariable @UUID String productVersionId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "true") boolean isUsed,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "6") int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var modules = service.queryAllModules(productVersionId, name, isUsed, pageable);
        return PagingWrapper.fromPage(modules)
                .map(mapper::toResponse);
    }

    @GetMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleResponse getLabelById(
            @PathVariable @UUID String moduleId
    ) {
        var module = service.getModule(moduleId);
        return mapper.toResponse(module);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse createProduct(
            @RequestBody @Valid ModuleCreatingRequest body
    ) {
        var label = service.createModule(body);
        return mapper.toResponse(label);
    }

    @PutMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateModule(
            @PathVariable @UUID String moduleId,
            @RequestBody @Valid ModuleUpdatingRequest body
    ) {
        service.updateModule(moduleId, body);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModule(@PathVariable @UUID String moduleId) {
        service.deleteModule(moduleId);
    }

}
