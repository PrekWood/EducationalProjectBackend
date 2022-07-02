package com.unpi.educationalprojectbackend.Security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTSecret {
    private static final String JWT_SECRET = "2vB3shAJsf754kAWF9N#E58wUWQp!zZqNq48H3QMA&gvN2D#H9qtDWBhkEs3q";

    public static String getJWTSecret(){
        return JWT_SECRET;
    }
}
