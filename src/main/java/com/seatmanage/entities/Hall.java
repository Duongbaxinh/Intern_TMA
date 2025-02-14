package com.seatmanage.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NamedQuery(name = "Hall.findByNameHall",
        query = "select h from Hall h where h.name = ?1")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column( nullable = false)
    String name;

    String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "floor_id",referencedColumnName = "id",nullable = true)
    Floor floor;

    @OneToMany(mappedBy = "hall")
    private List<Room> roomList;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
