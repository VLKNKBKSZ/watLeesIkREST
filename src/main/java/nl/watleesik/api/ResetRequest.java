package nl.watleesik.api;

import lombok.Data;

@Data
public class ResetRequest {

	private String password;
	private String token;
}
