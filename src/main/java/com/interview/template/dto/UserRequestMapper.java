package com.interview.template.dto;

import com.interview.template.model.UserEntity;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserRequestMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(UserRequest.class, UserEntity.class)
                .addMappings(mapper -> {
                            mapper.skip(UserEntity::setId);
                        }
                );
    }

    public UserEntity map(UserRequest dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity merge(UserRequest dto, UserEntity entity) {
        if (!Objects.equals(entity.getId(), dto.getId())) {

            throw new IllegalArgumentException("Invalid user id");
        }

        modelMapper.map(dto, entity);
        return entity;
    }
}
