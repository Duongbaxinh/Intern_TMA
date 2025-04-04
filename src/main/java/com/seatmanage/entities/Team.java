package com.seatmanage.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String code;
    @CreationTimestamp
    @Column(updatable = false)
    public LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "project", nullable = true,unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Project project;

    @UpdateTimestamp
    public LocalDateTime updatedAt;

}
