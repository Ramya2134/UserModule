package com.zettamine.boot.service;

import java.util.List;
import java.util.Map;

import com.zettamine.boot.dto.LoginDto;
import com.zettamine.boot.dto.UserDto;
import com.zettamine.boot.dto.UserRoleDto;

public interface IUserService {

	Boolean save(UserDto userDto);
	
	Boolean checkValidUser(String email);
	
	Map<String, Object> checkValidPassword(LoginDto dto);
	
	Boolean updatePassword(LoginDto dto);

	Integer forgotPassword(String email);
	
	List<UserDto> getAllUsers();
	
	Boolean deleteUserById(Integer Id);

	List<UserRoleDto> getAllEmployees();
	
	UserRoleDto getEmployeeByEmail(String email);
	
	Boolean update(UserDto userDto);
	
	UserRoleDto getUserById(Integer id);
	
	String getPasswordById(String email);
}
