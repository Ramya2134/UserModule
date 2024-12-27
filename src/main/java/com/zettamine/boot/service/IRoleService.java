package com.zettamine.boot.service;

import java.util.List;

import com.zettamine.boot.dto.RoleDto;
import com.zettamine.boot.entity.Role;

public interface IRoleService {

	void saveRole(RoleDto roleDto);
	
	Role getByRoleId(Integer roleId);
	
	List<RoleDto> fetchAllRoles();
}
