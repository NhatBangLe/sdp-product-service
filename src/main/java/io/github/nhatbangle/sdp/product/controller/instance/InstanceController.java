package io.github.nhatbangle.sdp.product.controller.instance;

import io.github.nhatbangle.sdp.product.dto.PagingWrapper;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceAlertRequest;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceUpdatingRequest;
import io.github.nhatbangle.sdp.product.dto.response.InstanceResponse;
import io.github.nhatbangle.sdp.product.mapper.InstanceMapper;
import io.github.nhatbangle.sdp.product.service.instance.InstanceService;
import io.github.nhatbangle.sdp.product.util.KeyEncryption;
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
@RequestMapping(path = "/api/${app.version}/product/instance")
public class InstanceController {

    private final InstanceService service;
    private final InstanceMapper mapper;

    @GetMapping("/{moduleVersionId}/module")
    @ResponseStatus(HttpStatus.OK)
    public PagingWrapper<InstanceResponse> getAllInstances(
            @PathVariable @UUID String moduleVersionId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(required = false, defaultValue = "6") @Min(1) @Max(50) int pageSize
    ) {
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        var instances = service.getAllInstances(moduleVersionId, name, isUsed, pageable);
        return PagingWrapper.fromPage(instances)
                .map(mapper::toResponse);
    }

    @GetMapping("/{instanceId}")
    @ResponseStatus(HttpStatus.OK)
    public InstanceResponse getInstance(@PathVariable @UUID String instanceId) {
        var instance = service.getInstance(instanceId);
        return mapper.toResponse(instance);
    }

    @GetMapping("/{instanceId}/secret")
    @ResponseStatus(HttpStatus.OK)
    public String getInstanceSecret(
            @PathVariable @UUID String instanceId
    ) {
        return KeyEncryption.crypt(instanceId);
    }

    @PostMapping("/alert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alertInstance(
            @RequestBody @Valid InstanceAlertRequest body
    ) {
        service.alertInstance(body);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstanceResponse createInstance(
            @RequestBody @Valid InstanceCreatingRequest body
    ) {
        var instance = service.createInstance(body);
        return mapper.toResponse(instance);
    }

    @PutMapping("/{instanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInstance(
            @PathVariable @UUID String instanceId,
            @RequestBody @Valid InstanceUpdatingRequest body
    ) {
        service.updateInstance(instanceId, body);
    }

    @DeleteMapping("/{instanceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstance(@PathVariable @UUID String instanceId) {
        service.deleteInstance(instanceId);
    }

}
