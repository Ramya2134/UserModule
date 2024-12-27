package com.zettamine.boot.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

import com.zettamine.boot.dto.LoginDto;
import com.zettamine.boot.dto.UserDto;
import com.zettamine.boot.dto.UserRoleDto;
import com.zettamine.boot.email.EmailService;
import com.zettamine.boot.entity.User;
import com.zettamine.boot.mapper.UserMapper;
import com.zettamine.boot.mapper.UserRoleMapper;
import com.zettamine.boot.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements IUserService {

	private UserRepository userRepo;
	
	private IRoleService roleService;
	
	private EmailService emailService;
	
	@Override
	@Transactional
	public Boolean save(UserDto userDto) {
		
		Optional<User> optUser = userRepo.findByEmail(userDto.getEmail());
		if (optUser.isPresent()) {
			return null; 
		}
		User user = UserMapper.userDtoToUser(userDto, new User());
		user.setTempPwd(generatePassayPassword());
		user.setAccountLock(false);
		user.setLoginStatus(0);
		user.setAttempts(0);
		user.setRole(roleService.getByRoleId(userDto.getRoleId()));
		userRepo.save(user);
		try {
			emailService.sendEmail(user, false);
		}
		catch(Exception ex){
			return false;
		}        
		 return true;
	}

	private static String generatePassayPassword() {

		PasswordGenerator gen = new PasswordGenerator();
		CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
		CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
		lowerCaseRule.setNumberOfCharacters(2);

		CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
		CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
		upperCaseRule.setNumberOfCharacters(2);

		CharacterData digitChars = EnglishCharacterData.Digit;
		CharacterRule digitRule = new CharacterRule(digitChars);
		digitRule.setNumberOfCharacters(2);

		CharacterData specialChars = new CharacterData() {
			public String getErrorCode() {
				return "cannot generate special characters";
			}

			public String getCharacters() {
				return "!@#$%^&*()_+";
			}
		};
		CharacterRule splCharRule = new CharacterRule(specialChars);
		splCharRule.setNumberOfCharacters(2);

		String password = gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);

		return password;
	}

	@Override
	@Transactional
	public Boolean checkValidUser(String email) {
		
		Optional<User> optUser = userRepo.findByEmail(email);
		
		if(optUser.isPresent()) {
		
			return true;
		}
		return false;
	}
	@Override
	@Transactional
	public Map<String,Object> checkValidPassword(LoginDto dto) {
		Map<String, Object> logInStatus = new HashMap();
		User user = userRepo.findByEmail(dto.getEmail()).get();
		if (user.isAccountLock() || user.getAttempts()>=3) {
			return null; 
		}
		Integer status = user.getLoginStatus();
		if (status==0) {
			if (!dto.getPassword().equals(user.getTempPwd())) {
				user.setAttempts(user.getAttempts()+1);
				if (user.getAttempts()>=3) {
					user.setAccountLock(true);
				}
				if(user.getRole().getRoleId()==1) {
					user.setAttempts(0);
				}
				userRepo.save(user);
				logInStatus.put("attempts",user.getAttempts().toString());
				
				return logInStatus;
			}
		}else if(status==1) {
			if (!dto.getPassword().equals(user.getPassword())) {
				user.setAttempts(user.getAttempts()+1);
				if (user.getAttempts()>=3) {
					user.setAccountLock(true);
				}
				if(user.getRole().getRoleId()==1) {
					user.setAttempts(0);
				}
				userRepo.save(user);
				logInStatus.put("attempts",user.getAttempts().toString());
				
				return logInStatus;
			}
		}
		logInStatus.put("firstName", user.getFirstName());
		logInStatus.put("lastName", user.getLastName());
		logInStatus.put("role", user.getRole().getRoleName());
		logInStatus.put("designation", user.getDesignation());
		logInStatus.put("logInStatus", user.getLoginStatus().toString());
		logInStatus.put("email", user.getEmail());
		//System.out.println("User Image Data: " + user.getImage());
		//System.out.println("User Image Data Length: " + user.getImage().length);
		logInStatus.put("image", user.getImage());
		
		user.setAttempts(0);
		userRepo.save(user);

		return logInStatus;
	}
	@Override
	@Transactional
	public Boolean updatePassword(LoginDto dto) {
		
		User user = userRepo.findByEmail(dto.getEmail()).get();
		
		user.setPassword(dto.getPassword());
		user.setLoginStatus(1);
		try {
			userRepo.save(user);
			return true;
		}
		catch(Exception ex){
			
			return false;
		}
		
		
	}
	
	@Override
	@Transactional
	public Integer forgotPassword(String email) {
		
		User user = userRepo.findByEmail(email).get();
	
		Integer otp  = new Random().nextInt(100000,999999);
		System.out.println("OTP ================ "+otp);
		if(user.isAccountLock()) {
			return null;
		}
		String tempPwd = user.getTempPwd();
		user.setTempPwd(otp.toString());
		user.setLoginStatus(0);
		user.setAttempts(0);
		user.setPassword(null);
		Boolean isExistingUser = true;
		
		 try {
	            if (!emailService.sendEmail(user, isExistingUser)) {
	            	return otp;
				}
	           
	        } catch (Exception e) {
	            log.error("Failed to send otp : {}", e.getMessage());
	            return otp;
	        }
		 user.setTempPwd(tempPwd);
		 userRepo.save(user);
		 return otp;
	}
	@Override	
	public List<UserDto> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserDto> userDtoList = users.stream().map(m ->UserMapper.userToUserDto(m, new UserDto())).toList();
		return userDtoList;
	}
	
	@Override	
	public List<UserRoleDto> getAllEmployees() {
		List<User> all = userRepo.findAll();
		List<UserRoleDto> userDtoList = all.stream().map(m -> UserRoleMapper.userToUserRoleDto(m, new UserRoleDto())).toList();
		return userDtoList;
	}
	@Override
	@Transactional
	public Boolean deleteUserById(Integer Id) {
		 Optional<User> optional = userRepo.findById(Id);
		 if(optional.isEmpty()) {
			 return false;
		 }
		 userRepo.delete(optional.get());
		return true;
	}

	@Override
	public UserRoleDto getEmployeeByEmail(String email) {
		Optional<User> optUser = userRepo.findByEmail(email);
		if(optUser.isEmpty()) {
			return null;
		}
		User user = optUser.get();
		UserRoleDto userRoleDto = UserRoleMapper.userToUserRoleDto(user, new UserRoleDto());
		return userRoleDto;
	}
	
	@Override
	@Transactional
	public Boolean update(UserDto userDto) {
			
		Optional<User> optUser = userRepo.findByEmail(userDto.getEmail());
		
		User user = UserMapper.userDtoToUser(userDto, optUser.get());
		user.setRole(roleService.getByRoleId(userDto.getRoleId()));
		
		System.out.println(user);
		userRepo.save(user);
		
		return true;
	
	}
	
	@Override
	@Transactional
	public UserRoleDto getUserById(Integer id) {
		
		Optional<User> optionalUser = userRepo.findById(id);
		if(optionalUser.isEmpty()) {
			return null;
		}
		User user = optionalUser.get();
		UserRoleDto userRoleDto = UserRoleMapper.userToUserRoleDto(user, new UserRoleDto());
		return userRoleDto;
	}

	@Override
	public String getPasswordById(String email) {
		User user = userRepo.findByEmail(email).get();
		if (user.getPassword()==null) {
			return null;
		}
		return user.getPassword();
	}
	
	
}
