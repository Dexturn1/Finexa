package com.finexa.finexa.role.entity;


import com.finexa.finexa.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.boot.models.annotations.internal.CascadeAnnotation;

import java.util.List;

@Entity
@Data
@Builder
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Role Name is required")
    private String name; // ROLE NAME eg. CUSTOMER, AUDITOR, ADMIN



}
