package io.github.nhatbangle.sdp.product.service.instance;

import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceAlertRequest;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceAttributeRequest;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceCreatingRequest;
import io.github.nhatbangle.sdp.product.dto.request.instance.InstanceUpdatingRequest;
import io.github.nhatbangle.sdp.product.entity.instance.Instance;
import io.github.nhatbangle.sdp.product.entity.instance.InstanceAttribute;
import io.github.nhatbangle.sdp.product.exception.DataConflictException;
import io.github.nhatbangle.sdp.product.exception.InvalidKeyException;
import io.github.nhatbangle.sdp.product.repository.instance.InstanceRepository;
import io.github.nhatbangle.sdp.product.service.module.ModuleVersionService;
import io.github.nhatbangle.sdp.product.util.KeyEncryption;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
@CacheConfig(cacheNames = "instances")
public class InstanceService {

    private final InstanceRepository repository;
    private final MessageSource messageSource;
    private final ModuleVersionService moduleVersionService;

    @NotNull
    public Page<Instance> getAllInstances(
            @NotNull @UUID String moduleVersionId,
            @Nullable String instanceName,
            @Nullable Boolean isUsed,
            @NotNull PageRequest pageable
    ) {
        return repository.findAllByModuleVersion_IdAndNameContainsIgnoreCaseAndIsUsed(
                moduleVersionId,
                instanceName,
                isUsed,
                pageable
        );
    }

    @NotNull
    @Cacheable(key = "#instanceId")
    public Instance getInstance(@NotNull @UUID String instanceId) throws NoSuchElementException {
        return findInstance(instanceId);
    }

    private Instance findInstance(String instanceId) throws NoSuchElementException {
        return repository.findById(instanceId)
                .orElseThrow(() -> {
                    var message = messageSource.getMessage(
                            "instance.not_found",
                            new Object[]{instanceId},
                            Locale.getDefault()
                    );
                    return new NoSuchElementException(message);
                });
    }

    @NotNull
    public Instance createInstance(
            @NotNull @Valid InstanceCreatingRequest request
    ) throws NoSuchElementException {
        var moduleVersion = moduleVersionService.getVersion(request.moduleVersionId());
        var instance = repository.save(Instance.builder()
                .name(request.name())
                .description(request.description())
                .moduleVersion(moduleVersion)
                .secretKey(java.util.UUID.randomUUID().toString())
                .build());

        var attRqs = request.attributes();
        if (!attRqs.isEmpty()) {
            instance.setAttributes(convertToInstanceAttribute(instance, attRqs));
            return repository.save(instance);
        }

        return instance;
    }

    @NotNull
    @CachePut(key = "#instanceId")
    public Instance updateInstance(
            @NotNull @UUID String instanceId,
            @NotNull @Valid InstanceUpdatingRequest request
    ) throws NoSuchElementException {
        var instance = findInstance(instanceId);
        instance.setName(request.name());
        instance.setDescription(request.description());
        return repository.save(instance);
    }

    @CacheEvict(key = "#instanceId")
    public void deleteInstance(@NotNull @UUID String instanceId) {
        repository.deleteById(instanceId);
    }

    public String generateSecretKey(@NotNull @UUID String instanceId) throws NoSuchElementException {
        var instance = findInstance(instanceId);
        var key = KeyEncryption.crypt(java.util.UUID.randomUUID().toString());

        instance.setSecretKey(key);
        repository.save(instance);

        return key;
    }

    @Transactional
    public void alertInstance(@NotNull @Valid InstanceAlertRequest request)
            throws InvalidKeyException, NoSuchElementException {
        var instanceId = request.instanceId();
        var instance = findInstance(instanceId);

        if (!KeyEncryption.compare(request.secretKey(), KeyEncryption.crypt(instance.getSecretKey()))) {
            var message = messageSource.getMessage(
                    "instance.invalid_key",
                    new Object[]{instanceId},
                    Locale.getDefault()
            );
            throw new InvalidKeyException(message);
        }

        var moduleVersion = instance.getModuleVersion();
        if (!moduleVersion.getIsUsed()) moduleVersion.setIsUsed(true);

        var module = moduleVersion.getModule();
        if (!module.getIsUsed()) module.setIsUsed(true);

        var productVersion = module.getProductVersion();
        if (!productVersion.getIsUsed()) productVersion.setIsUsed(true);

        var product = productVersion.getProduct();
        if (!product.getIsUsed()) product.setIsUsed(true);

        repository.save(instance);
    }

    private Set<InstanceAttribute> convertToInstanceAttribute(
            @NotNull Instance instance,
            @NotNull Set<InstanceAttributeRequest> requests
    ) throws DataConflictException {
        var instanceId = instance.getId();
        if (instanceId == null) {
            var message = messageSource.getMessage(
                    "instance.not_saved",
                    null,
                    Locale.getDefault()
            );
            throw new DataConflictException(message);
        }

        return requests.stream()
                .map(attRequest -> InstanceAttribute.builder()
                        .key(attRequest.key())
                        .value(attRequest.value())
                        .numOrder(attRequest.numOrder())
                        .instance(instance)
                        .build())
                .collect(Collectors.toSet());
    }

}
