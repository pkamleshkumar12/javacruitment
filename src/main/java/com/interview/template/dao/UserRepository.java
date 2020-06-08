package com.interview.template.dao;

import com.interview.template.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByUsernameContainingIgnoreCase(String username);
}
