package com.pharmacy.management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "soft_delete = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password; // In a real app, this would be hashed

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "soft_delete")
    private boolean softDelete = false;

    @PrePersist
    protected void onCreate() {
       createdAt = new Timestamp(System.currentTimeMillis());
       updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
       updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
