package nl.watleesik.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import nl.watleesik.domain.Account;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
	@Value("${security.tokenPrefix}")
	private String tokenPrefix;
	
	@Value("${security.headerString}")
	private String headerString;
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final JWTTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;
	
	public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String token = getTokenFromRequest(request);
			if (token != null && jwtTokenProvider.validateToken(token)) {
				String email = jwtTokenProvider.getEmailFromToken(token);
				Account account = (Account) customUserDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						account, null, account.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			LOG.error("Could not set user authetication in security context");
		}
		filterChain.doFilter(request, response);
		
	}
	
	private String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(headerString);
		log.debug("HEADER: {}", headerString);
		log.debug("TOKEN: {}", token);
		if (token != null && token.startsWith(tokenPrefix)) {
			return token.replace(tokenPrefix, "");
		}
		return null;
	}

}
