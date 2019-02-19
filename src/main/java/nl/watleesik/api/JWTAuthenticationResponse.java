package nl.watleesik.api;

import lombok.Data;

@Data
public class JWTAuthenticationResponse {

	private String accessToken;
	private String tokenType = "Bearer";
	private String email;
	private String role;
	private long profileId;
	
	public JWTAuthenticationResponse(String accessToken, String email, String role, long profileId) {
		this.accessToken = accessToken;
		this.email = email;
		this.role = role;
		this.profileId = profileId;
	}
}
