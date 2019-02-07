package nl.watleesik.utility;

import lombok.Data;

@Data
public class ApiRespone<T> {

    private int status;
    private String message;
    private T result;

}
