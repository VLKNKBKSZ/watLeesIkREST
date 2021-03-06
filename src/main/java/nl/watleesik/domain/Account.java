package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @OneToOne
    @MapsId
    private Profile profile;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private AccountRole role = AccountRole.USER;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private LocalDateTime lastLogin;
    
	public Account(long id, String email, Profile profile, String password, AccountRole role) {
		this.id = id;
		this.email = email;
		this.profile = profile;
		this.password = password;
		this.role = role;
	}
	
	public Account(long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}	

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authority;
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