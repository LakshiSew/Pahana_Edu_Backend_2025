package com.Pahana_Edu_Backend.Dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    

    private String token;
    private String role;
    private String userId;

    //    public LoginResponse(String token, String role, String userId) {
    //     this.token = token;
    //     this.role = role;
    //     this.userId = userId;
        
    // }
}
