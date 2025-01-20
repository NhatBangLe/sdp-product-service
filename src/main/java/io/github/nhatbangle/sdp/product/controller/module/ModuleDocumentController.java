package io.github.nhatbangle.sdp.product.controller.module;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleDocumentCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.module.ModuleDocumentUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ModuleDocumentResponse;
import io.github.nhatbangle.sdp.product.mapper.ModuleMapper;
import io.github.nhatbangle.sdp.product.service.module.ModuleDocumentService;
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
@RequestMapping(path = "/api/${app.version}/product/module/document")
public class ModuleDocumentController {

    private final ModuleDocumentService service;
    private final ModuleMapper mapper;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ModuleDocumentResponse> getAllProductsByUserId(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String moduleName,
            @RequestParam(required = false) String documentTitle,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var products = service.queryAllDocuments(userId, moduleName, documentTitle, pageable);
        return PagingWrapper.fromPage(products)
                .map(mapper::toResponse);
    }

    @GetMapping("/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    public ModuleDocumentResponse getDocument(
            @PathVariable @UUID String documentId
    ) {
        var product = service.getDocument(documentId);
        return mapper.toResponse(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDocumentResponse createDocument(
            @RequestBody @Valid ModuleDocumentCreatingRequest body
    ) {
        var product = service.createDocument(body);
        return mapper.toResponse(product);
    }

    @PutMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDocument(
            @PathVariable @UUID String documentId,
            @RequestBody @Valid ModuleDocumentUpdatingRequest body
    ) {
        service.updateDocument(documentId, body);
    }

    @DeleteMapping("/{documentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable @UUID String documentId) {
        service.deleteDocument(documentId);
    }

}
