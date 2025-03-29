package com.seatmanage.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class DiagramDraft {
    String image;
    boolean draft;
    String roomId;
    List<SeatDiagramUpdate> seats;
    String object;
}
