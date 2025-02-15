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
public class Seat {
    public enum TypeSeat {
        TEMPORARY ,PERMANENT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    String name;
    String description;
    double posX;
    double posY;

    @Column( unique = false, nullable = false)
    @Enumerated(EnumType.STRING)
    TypeSeat typeSeat = TypeSeat.TEMPORARY;

    @ManyToOne
    @JoinColumn(name = "roomId")
    Room room;

    @OneToOne
    @JoinColumn(name = "userId", nullable = true,unique = true)
    User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
