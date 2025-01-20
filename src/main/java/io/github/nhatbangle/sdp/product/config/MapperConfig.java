package io.github.nhatbangle.sdp.product.config;

import io.github.nhatbangle.sdp.product.mapper.DocumentLabelMapper;
import io.github.nhatbangle.sdp.product.mapper.InstanceMapper;
import io.github.nhatbangle.sdp.product.mapper.ModuleMapper;
import io.github.nhatbangle.sdp.product.mapper.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public DocumentLabelMapper documentLabelMapper() {
        return new DocumentLabelMapper();
    }

    @Bean
    public ProductMapper productMapper(DocumentLabelMapper mapper) {
        return new ProductMapper(mapper);
    }

    @Bean
    public ModuleMapper moduleMapper(DocumentLabelMapper mapper) {
        return new ModuleMapper(mapper);
    }

    @Bean
    public InstanceMapper instanceMapper() {
        return new InstanceMapper();
    }

}
