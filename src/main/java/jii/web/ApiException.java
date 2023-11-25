package jii.web;

import lombok.Getter;

import java.io.Serial;

/**
 * @author admin
 */
@Getter
public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4100309035717531908L;

    private int status = 400;
    private int code = 1;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public static ApiException a404() {
        return new ApiException("Not found.", 404);
    }

    public static ApiException a401() {
        return new ApiException("Unauthorized.", 401);
    }

}
