package nl.watleesik.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static nl.watleesik.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JWTTokenProvider jwtTokenProvider;
	
	public WebSecurity(CustomUserDetailsService customUserDetailsService,
			JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint, JWTTokenProvider jwtTokenProvider) {
		this.customUserDetailsService = customUserDetailsService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// TODO: This method needs to be fine-tuned later on
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors()
		.and()
		.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		.authorizeRequests().antMatchers(SIGN_UP_URL).permitAll()
		.antMatchers(REGISTER_URL).permitAll()
		.antMatchers("/account/**").hasRole("ADMIN")
		.anyRequest().authenticated();

	// Add our custom JWT authentication filter		
	http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
	}

}
