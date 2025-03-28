package com.seatmanage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NamedQuery(name = "User.findByEmailAddress",
        query = "select u from User u where u.firstName = ?1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String firstName;
    String lastName;
    String username;

    String roomId;
    @Size(min = 5)
    String password;

    @ManyToOne
    @JoinColumn(name = "roleId",referencedColumnName = "name",nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Role role;

    @OneToOne
    @JoinColumn(name = "seatId",nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Seat seat;

    @ManyToOne
    @JoinColumn(name = "teamId")
    Team team;

    @Column(nullable = false,columnDefinition = "BOOLEAN DEFAULT false")
    boolean deleted = false;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
