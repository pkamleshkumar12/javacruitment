package com.interview.template.model;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "deleted_token"}), @UniqueConstraint(columnNames = {"email", "deleted_token"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted = false")
public class UserEntity extends AuditEntity {

    private static final String ACTIVE = "ACTIVE";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq")
    private Long id;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Builder.Default
    @Column(name = "deleted_token")
    private String deletedToken = ACTIVE;
}
