package com.seatmanage.mappers;

import com.seatmanage.dto.request.DiagramDraft;
import com.seatmanage.dto.request.SaveDiagram;
import com.seatmanage.dto.request.SeatDiagramUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SeatDiagramUpdate.class})
public interface DiagramMapper {

    @Mapping(target = "image",ignore = true)
    @Mapping(target = "seats",ignore = true)
    DiagramDraft toDiagramDraft(SaveDiagram saveDiagram);
}
