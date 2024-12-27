package com.zettamine.boot.utils;

import java.util.Base64;

public class ImageConverter {
   
    	public static String convertToBase64(byte[] data) {
            if (data != null && data.length > 0) {
                return Base64.getEncoder().encodeToString(data);
            }
            return null; 
        }
    
}