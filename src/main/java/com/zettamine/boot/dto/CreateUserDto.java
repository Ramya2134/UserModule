package com.zettamine.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateUserDto {
	
	private String firstName;
	private String lastName;
	private String email;
	private String designation;
	private Integer roleId;

}
