package nl.watleesik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Opslaan van de waardering is niet gelukt")
public class RatingNotUpdatedForBookListItemException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
