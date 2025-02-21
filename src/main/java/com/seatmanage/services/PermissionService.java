package com.seatmanage.services;

import com.seatmanage.config.SecurityUtil;
import com.seatmanage.dto.request.PermissionRequest;
import com.seatmanage.entities.PermissionActive;
import com.seatmanage.exception.AppExceptionHandle;
import com.seatmanage.exception.ErrorCode;
import com.seatmanage.mappers.PermissionMapper;
import com.seatmanage.repositories.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionActive createPermission(PermissionRequest permissionRequest) {
        System.out.println("check permissionRequest" + permissionRequest.name);
        PermissionActive permissionActiveEx =
                permissionRepository.findByName(SecurityUtil.PermissionAuth.valueOf(permissionRequest.name)).orElse(null);
        if (permissionActiveEx != null)  throw new AppExceptionHandle(ErrorCode.EXISTED_PERMISSION);
        System.out.println("check permissionRequest ##" + permissionRequest.name);
        PermissionActive permissionActive = new PermissionActive();
        permissionActive.setName(SecurityUtil.PermissionAuth.valueOf(permissionRequest.name));
        permissionActive.setDescription("");

        return  permissionRepository.save(permissionActive);
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
