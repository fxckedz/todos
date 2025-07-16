package br.com.medeiros.api.todo.v1.serialization.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public YamlJackson2HttpMessageConverter() {
        super(
                new YAMLMapper().
                        registerModule(new JavaTimeModule()).
                        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).
                        setSerializationInclusion(
                                JsonInclude.Include.NON_NULL),
                                MediaType.parseMediaType("application/x-yaml"));
    }
}
