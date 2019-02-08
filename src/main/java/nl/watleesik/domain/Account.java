package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Account implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	
	@OneToOne
	@JoinColumn(nullable = false)
	private AccountRole accountRole;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(accountRole.getRole()));
		return roles;
	}

	public String getUsername() {
		return email;
	}

	public boolean isAccountNonExpired() {
		// TODO: implement account expired
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO: implement account locking
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO: implement password expiring
		return true;
	}

	public boolean isEnabled() {
		// TODO: implement disable account
		return true;
	}
}
