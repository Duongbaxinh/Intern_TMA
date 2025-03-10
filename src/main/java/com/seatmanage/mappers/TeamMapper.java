package com.seatmanage.mappers;

import com.seatmanage.dto.request.RoleRequest;
import com.seatmanage.dto.request.TeamRequest;
import com.seatmanage.dto.response.RoleDTO;
import com.seatmanage.dto.response.TeamDTO;
import com.seatmanage.entities.Role;
import com.seatmanage.entities.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team toTeam(TeamRequest teamRequest);

    TeamDTO toTeamDTO(Team team);
}
