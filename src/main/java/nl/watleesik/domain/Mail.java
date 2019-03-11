package nl.watleesik.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {

    private String from;
    private String to;
    private String subject;
    private String content;

}