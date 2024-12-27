package com.zettamine.boot.mapper;

import com.zettamine.boot.dto.CreateUserDto;
import com.zettamine.boot.dto.UserDto;
import com.zettamine.boot.entity.User;

public class UserMapper {

	public static User userDtoToUser( UserDto userDto,User user) {
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setDesignation(userDto.getDesignation());
		user.setImage(userDto.getImage());
		return user;
	}
	
	public static UserDto userToUserDto(User user,UserDto userDto) {
		if(user.getUserId()!=null) {
			userDto.setUserId(user.getUserId());
		}
		
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setDesignation(user.getDesignation());
		if(user.getImage()!=null) {
			userDto.setImage(user.getImage());
		}
		return userDto;
	}
	
	public static UserDto CreateUserDtoToUserDto(CreateUserDto user,UserDto userDto) {
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setDesignation(user.getDesignation());
		
		return userDto;
	}
}
