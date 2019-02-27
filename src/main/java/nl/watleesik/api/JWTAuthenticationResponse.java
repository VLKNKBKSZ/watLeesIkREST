package nl.watleesik.api;

import lombok.Data;
import nl.watleesik.domain.AccountRole;

@Data
public class JWTAuthenticationResponse {

	private String accessToken;
	private String tokenType = "Bearer";
	private String email;
	private AccountRole role;
	
	public JWTAuthenticationResponse(String accessToken, String email, AccountRole role) {
		this.accessToken = accessToken;
		this.email = email;
		this.role = role;
	}
}
