package com.seatmanage.dto.request;

import com.seatmanage.dto.response.SeatDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PUBLIC)
public class SaveDiagram {
    String roomId;
    String image;
    List<SeatDiagramUpdate> seats;
    String object;
}
