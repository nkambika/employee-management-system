package com.myproject.services;

import com.myproject.dtos.RoleRequestDto;
import com.myproject.models.Role;

import java.util.List;

public interface RoleService {
    Role create(RoleRequestDto roleRequestDto);
    List<Role> getAllRoles();
    Role getRoleByName(String name);
}
