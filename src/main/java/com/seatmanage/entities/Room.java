package com.seatmanage.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    String name;

    String description;

    String image;
    int capacity;

    @Column(length = 3000)
    String object;
    @ManyToOne
    @JoinColumn(name = "hallId")
    Hall hall;

    @OneToOne
    @JoinColumn(name = "chief")
    User chief;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Seat> seatList;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
