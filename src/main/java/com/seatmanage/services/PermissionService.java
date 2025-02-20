package com.seatmanage.services;

import com.seatmanage.dto.request.PermissionRequest;
import com.seatmanage.dto.response.PermissionDTO;
import com.seatmanage.entities.PermissionActive;
import com.seatmanage.exception.AppExceptionHandle;
import com.seatmanage.exception.ErrorCode;
import com.seatmanage.mappers.PermissionMapper;
import com.seatmanage.repositories.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionActive createPermission(PermissionRequest permissionRequest) {
        PermissionActive permissionActiveEx = permissionRepository.findById(permissionRequest.name).orElse(null);
        if (permissionActiveEx != null)  throw new AppExceptionHandle(ErrorCode.EXISTED_PERMISSION);
        return  permissionRepository.save(permissionMapper.toPermission(permissionRequest));
    }
    public List<PermissionActive> getPermissionList(List<String> ids) {
        return permissionRepository.findAllById(ids);
    }
    public PermissionActive getPermissionById(String id) {
        return  permissionRepository.findById(id).orElse(null);
    }
    public List<PermissionActive> getAllPermission() {
        return permissionRepository.findAll();
    }
}
