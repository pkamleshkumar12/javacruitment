package com.interview.template.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.interview.template.model.UserEntity;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.beans.factory.annotation.Value;

@Projection(name = "userProjection", types = {UserEntity.class})
public interface UserProjection {

    @Value("#{target.id}")
    String getId();

    @JsonProperty(value = "username")
    String getUsername();

    @JsonProperty(value = "email")
    String getEmail();

}
