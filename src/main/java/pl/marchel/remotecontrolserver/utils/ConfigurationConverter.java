package pl.marchel.remotecontrolserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pl.marchel.remotecontrolserver.model.Configuration;

import javax.persistence.AttributeConverter;
import java.util.List;

@Slf4j
public class ConfigurationConverter implements AttributeConverter<List<Configuration>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Configuration> o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("On stringify {} : {}", o, e.getMessage());
        }
        return null;
    }

    @Override
    public List<Configuration> convertToEntityAttribute(String s) {
        if(s != null) {
            try {
                return mapper.readValue(s, new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("On parsing {} : {}", s, e.getMessage());
            }
        }
        return null;
    }
}
