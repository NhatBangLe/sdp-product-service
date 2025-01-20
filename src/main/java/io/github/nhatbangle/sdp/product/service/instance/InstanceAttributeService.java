package io.github.nhatbangle.sdp.product.service.instance;

import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceAttributeRequest;
import io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute;
import io.github.nhatbangle.sdp.product.repository.instance.InstanceAttributeRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "instance-attributes")
public class InstanceAttributeService {

    private final InstanceAttributeRepository repository;
    private final MessageSource messageSource;

    @NotNull
    public List<InstanceAttribute> getAllAttributes(@NotNull @UUID String instanceId) {
        var sort = Sort.by("numOrder").ascending();
        return repository.findAllByInstance_Id(instanceId, sort);
    }

    @NotNull
    @Cacheable(key = "#attributeId")
    public InstanceAttribute getAttribute(@NotNull @UUID String attributeId)
            throws IllegalArgumentException {
        return findAttribute(attributeId);
    }

    private InstanceAttribute findAttribute(String attributeId) throws IllegalArgumentException {
        return repository.findById(attributeId)
                .orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "instance_attribute.not_found",
                            new Object[]{attributeId},
                            Locale.getDefault()
                    );
                    return new IllegalArgumentException(message);
                });
    }

    @NotNull
    public InstanceAttribute createAttribute(
            @NotNull @Valid InstanceAttributeRequest request
    ) {
        var attribute = InstanceAttribute.builder()
                .key(request.key())
                .value(request.value())
                .numOrder(request.numOrder())
                .build();
        return repository.save(attribute);
    }

    @NotNull
    @CachePut(key = "#attributeId")
    public InstanceAttribute updateInstance(
            @NotNull @UUID String attributeId,
            @NotNull @Valid InstanceAttributeRequest request
    ) throws IllegalArgumentException {
        var attribute = findAttribute(attributeId);
        attribute.setKey(request.key());
        attribute.setValue(request.value());
        attribute.setNumOrder(request.numOrder());
        return repository.save(attribute);
    }

    @CacheEvict(key = "#attributeId")
    public void deleteAttribute(@NotNull @UUID String attributeId) {
        repository.deleteById(attributeId);
    }

}
