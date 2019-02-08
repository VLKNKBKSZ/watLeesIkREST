package nl.watleesik.security;

public class SecurityConstants {
	public static final String SECRET = "gFRde6stY3hfdgBV3";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/login";
	public static final String REGISTER_URL = "/account/register";	
}
