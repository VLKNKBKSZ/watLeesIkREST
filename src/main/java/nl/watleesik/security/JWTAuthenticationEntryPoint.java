package nl.watleesik.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {
		LOG.debug("Authentication failed: {}", ex.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		
	}

}
