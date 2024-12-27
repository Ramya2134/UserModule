package com.zettamine.boot.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.boot.constants.AppConstants;
import com.zettamine.boot.dto.ResponseDto;
import com.zettamine.boot.dto.RoleDto;
import com.zettamine.boot.dto.UserDto;
import com.zettamine.boot.dto.UserRoleDto;
import com.zettamine.boot.service.IRoleService;
import com.zettamine.boot.service.IUserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin("http://localhost:3000")
public class UserController {

	private IUserService userService;
	
	private IRoleService roleService;
	
	@PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> saveUser(@RequestPart("userDto") String userJson,
			@RequestPart("image") MultipartFile image) {
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 UserDto userDto = null;
		 try {
			 userDto = objectMapper.readValue(userJson,UserDto.class);
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}

		System.out.println("Image"+image);
	   
	    if (image == null ) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty image file provided");
	    }
	   

	    byte[] imageData;
	    try {
	        if (image != null) {
	            imageData = image.getBytes();
	            System.out.println(imageData);
	           
	            userDto.setImage(imageData);
	        }
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to read image file");
	    }

	    Boolean saved = userService.save(userDto);
	    if (saved == null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
	    } else if (!saved) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed. Please check the user data.");
	    }

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(new ResponseDto(AppConstants.STATUS_201, AppConstants.MESSAGE_201));
	}

	
	@GetMapping("/fetch-all/roles")
	public ResponseEntity<?> fetchAllRoles(){
		
		List<RoleDto> allRoles = roleService.fetchAllRoles();
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(allRoles);
	}
	@PutMapping("/forgot-password/{email}")
	public ResponseEntity<?> sendMailForOtpForNewPassword(@PathVariable ("email") String email){
		Integer otp = userService.forgotPassword(email);
		if(otp == null) {
			return ResponseEntity 
					.status(HttpStatus.LOCKED)
					.body("Account Locked");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(otp);
		}
	}
	
	@GetMapping("/fetch-all/users")
	public ResponseEntity<?> getAllEmployees(){
		List<UserDto> allEmployees = userService.getAllUsers();
		if(allEmployees == null) {
			return ResponseEntity 
					.status(HttpStatus.BAD_REQUEST)
					.body("No employees found");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(allEmployees);
		}
	}
	
	@GetMapping("/fetch-all")
	public ResponseEntity<?> getAllEmps(){
		List<UserRoleDto> allEmployees = userService.getAllEmployees();
		if(allEmployees == null) {
			return ResponseEntity 
					.status(HttpStatus.BAD_REQUEST)
					.body("No employees found");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(allEmployees);
		}
	}
	
	
	@GetMapping("/fetch-by-email/{email}")
	public ResponseEntity<?> getEmployeeByEmail(@PathVariable ("email") String email){
		UserRoleDto employeeByEmail = userService.getEmployeeByEmail(email);
		if(employeeByEmail==null) {
			return ResponseEntity 
					.status(HttpStatus.BAD_REQUEST)
					.body("Employee data not found");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(employeeByEmail);
		}
	}
	
	@PostMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> updateUser(
	        @RequestPart("userDto") String userJson,
	        @RequestPart(name = "image", required = false) MultipartFile image) {

	    ObjectMapper objectMapper = new ObjectMapper();
	    UserDto userDto = null;

	    try {
	        userDto = objectMapper.readValue(userJson, UserDto.class);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format for userDto");
	    }

	    if (image == null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty image file provided");
	    }

	    byte[] imageData;
	    try {
	        imageData = image.getBytes();
	        userDto.setImage(imageData);
	        System.out.println("Controller "+userDto);
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to read image file");
	    }

	    Boolean saved = userService.update(userDto); 

	    if (saved == null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
	    } else if (!saved) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed. Please check the user data.");
	    }

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(new ResponseDto(AppConstants.STATUS_201, AppConstants.MESSAGE_201));
	}
	
	@GetMapping("/fetch-user/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
		Integer userId = Integer.parseInt(id);
		UserRoleDto userRoleDto = userService.getUserById(userId);
		if(userRoleDto == null) {
			return ResponseEntity 
					.status(HttpStatus.BAD_REQUEST)
					.body("No employees found");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(userRoleDto);
		}
	}
	
	@GetMapping("/get-pwd/{email}")
	public ResponseEntity<?> getPasswordById(@PathVariable ("email") String email){
		String employeePwd = userService.getPasswordById(email);
		if(employeePwd==null) {
			return ResponseEntity 
					.status(HttpStatus.ACCEPTED)
					.body(employeePwd);
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body(employeePwd);
		
		}
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployeeById(@PathVariable ("id") Integer id){
		Boolean status = userService.deleteUserById(id);
		if(status==false) {
			return ResponseEntity 
					.status(HttpStatus.BAD_REQUEST)
					.body("Employee data not deleted");
		}else {
			return ResponseEntity 
					.status(HttpStatus.OK)
					.body("Employee data deleted");
		}
	}
}
