package com.hackathon.uob.entity;
/**
 * @author nikhilesh chaurasia
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dob;
    private String mobile;
    private String email;
    private String password;

    @Column(name = "created_ts", nullable = false, updatable = false)
    private LocalDateTime createdTs;

    @Column(name = "updated_ts", nullable = false)
    private LocalDateTime updatedTs;
}
