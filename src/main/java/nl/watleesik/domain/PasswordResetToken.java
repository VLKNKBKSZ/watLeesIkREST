package nl.watleesik.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@OneToOne
	@JoinColumn(nullable = false, name = "profile_id")
	private Account account;
	
	@Column(nullable = false)
	private LocalDateTime expireDate;
	
	public void setExpireTime(long minutes) {
		expireDate = LocalDateTime.now().plusMinutes(minutes);
	}
	
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expireDate);
	}
}
