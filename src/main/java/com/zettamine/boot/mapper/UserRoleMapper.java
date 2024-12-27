package com.zettamine.boot.mapper;

import com.zettamine.boot.dto.UserRoleDto;
import com.zettamine.boot.entity.User;

public class UserRoleMapper {

	public static UserRoleDto userToUserRoleDto(User user, UserRoleDto userRoleDto) {
		userRoleDto.setId(user.getUserId());
		userRoleDto.setFirstName(user.getFirstName());
		userRoleDto.setLastName(user.getLastName());
		userRoleDto.setEmail(user.getEmail());
		userRoleDto.setDesignation(user.getDesignation());
		userRoleDto.setAccountLock(user.isAccountLock());
		userRoleDto.setRoleName(user.getRole().getRoleName());
		if(user.getImage()!=null) {
			userRoleDto.setImage(user.getImage());
		}	
		return userRoleDto;
	}
}