package com.zettamine.boot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zettamine.boot.dto.RoleDto;
import com.zettamine.boot.entity.Role;
import com.zettamine.boot.exception.ResourceNotFoundException;
import com.zettamine.boot.mapper.RoleMapper;
import com.zettamine.boot.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements IRoleService {

	private RoleRepository repository;
	
	@Override
	@Transactional
	public void saveRole(RoleDto roleDto) {
		Role role = RoleMapper.roleDtoToRole(roleDto, new Role());
		repository.save(role);
	}

	@Override
	@Transactional
	public Role getByRoleId(Integer roleId) {
		Role role = repository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "role Id", roleId.toString()));
		return role;
	}

	@Override
	@Transactional
	public List<RoleDto> fetchAllRoles() {
		
		List<Role> roles = repository.findAll();
		
		List<RoleDto> rolesDto = roles.stream().map(role->RoleMapper.roleToRoleDto(role, new RoleDto())).collect(Collectors.toList());
		
		return rolesDto;
	}
	
	

}
