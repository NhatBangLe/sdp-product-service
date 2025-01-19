package io.github.nhatbangle.sdp.product.controller.product;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.product.ProductUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.ProductResponse;
import io.github.nhatbangle.sdp.product.mapper.ProductMapper;
import io.github.nhatbangle.sdp.product.service.product.ProductService;
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
@RequestMapping(path = "/api/${app.version}/product")
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @GetMapping("/{userId}/user")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<ProductResponse> getAllProductsByUserId(
            @PathVariable @UUID String userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "6") int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").ascending());
        var products = service.queryAllProducts(userId, name, isUsed, pageable);
        return PagingWrapper.fromPage(products)
                .map(mapper::toResponse);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(
            @PathVariable @UUID String productId
    ) {
        var product = service.getProduct(productId);
        return mapper.toResponse(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestBody @Valid ProductCreatingRequest body
    ) {
        var product = service.createProduct(body);
        return mapper.toResponse(product);
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(
            @PathVariable @UUID String productId,
            @RequestBody @Valid ProductUpdatingRequest body
    ) {
        service.updateProduct(productId, body);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable @UUID String productId) {
        service.deleteProduct(productId);
    }

}
