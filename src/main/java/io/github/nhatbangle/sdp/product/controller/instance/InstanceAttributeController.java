package io.github.nhatbangle.sdp.product.controller.instance;

import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceAttributeRequest;
import io.github.nhatbangle.sdp.product.dto.response.InstanceAttributeResponse;
import io.github.nhatbangle.sdp.product.mapper.InstanceMapper;
import io.github.nhatbangle.sdp.product.service.instance.InstanceAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/${app.version}/product/instance/attribute")
public class InstanceAttributeController {

    private final InstanceAttributeService service;
    private final InstanceMapper mapper;

    @GetMapping("/{instanceId}/instance")
    @ResponseStatus(HttpStatus.OK)
    public List<InstanceAttributeResponse> getAllAttributes(@PathVariable @UUID String instanceId) {
        return service.getAllAttributes(instanceId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{attributeId}")
    @ResponseStatus(HttpStatus.OK)
    public InstanceAttributeResponse getAttribute(@PathVariable @UUID String attributeId) {
        var instance = service.getAttribute(attributeId);
        return mapper.toResponse(instance);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstanceAttributeResponse createInstance(
            @RequestBody @Valid InstanceAttributeRequest body
    ) {
        var instance = service.createAttribute(body);
        return mapper.toResponse(instance);
    }

    @PutMapping("/{attributeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInstance(
            @PathVariable @UUID String attributeId,
            @RequestBody @Valid InstanceAttributeRequest body
    ) {
        service.updateInstance(attributeId, body);
    }

    @DeleteMapping("/{attributeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstance(@PathVariable @UUID String attributeId) {
        service.deleteAttribute(attributeId);
    }

}
