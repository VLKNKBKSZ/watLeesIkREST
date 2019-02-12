package nl.watleesik.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import nl.watleesik.domain.Account;

@Component
public class JWTTokenProvider {

	@Autowired 
	Environment environment;
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

	public String generateToken(Authentication authentication) {
		Account account = (Account) authentication.getPrincipal();
		LOG.debug("Generate token for {}", account);

		return Jwts.builder()
				.setSubject(account.getEmail())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() 
						+ Long.parseLong(environment.getProperty("security.expirationTime"))))
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("security.secret")).compact();
	}

	public String getEmailFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(environment.getProperty("security.secret"))
				.parseClaimsJws(token.replace(environment.getProperty("security.tokenPrefix"), "")).getBody()
				.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(environment.getProperty("security.secret"))
				.parseClaimsJws(token.replace(environment.getProperty("security.tokenPrefix"), ""));
			return true;
		} catch (SignatureException ex) {
			LOG.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOG.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOG.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOG.error("Unsuported JWT token");
		} catch (IllegalArgumentException ex) {
			LOG.error("JWT claims string is empty");
		}
		return false;
	}
}
