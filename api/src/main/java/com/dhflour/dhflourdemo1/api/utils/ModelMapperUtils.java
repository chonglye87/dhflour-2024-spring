package com.dhflour.dhflourdemo1.api.utils;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ModelMapperUtils {

    @Autowired
    private ModelMapper modelMapper;

    public <S, T> void map(S source, T target, PropertyMap<S, T> propertyMap) {
        modelMapper.addMappings(propertyMap);
        modelMapper.map(source, target);
    }

    public <S, T> void mapSkippingFields(S source, T target, String... skippingFields) {
        Set<String> fieldsToSkip = new HashSet<>(Arrays.asList(skippingFields));

        Condition<Object, Object> skipFieldsCondition = ctx -> {
            String fieldName = ctx.getMapping().getLastDestinationProperty().getName();
            return !fieldsToSkip.contains(fieldName);
        };

        modelMapper.getConfiguration().setPropertyCondition(skipFieldsCondition);
        modelMapper.map(source, target);
    }
}