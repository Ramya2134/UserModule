package com.zettamine.boot.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class UserCreateRequest {
    private UserDto userDto;
    private MultipartFile image;

   
}