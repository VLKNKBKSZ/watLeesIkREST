package nl.watleesik.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {
	
	private String from;
	private String to;
	private String subject;
	private String content;
	
	
}
