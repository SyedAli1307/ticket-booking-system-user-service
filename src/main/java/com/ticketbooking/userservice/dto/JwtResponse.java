package com.ticketbooking.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String userName;
    
    
	public JwtResponse() {
	
	}


	public JwtResponse(String accessToken, String refreshToken, String tokenType, String userName) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.tokenType = tokenType;
		this.userName = userName;
	}
    
    
}