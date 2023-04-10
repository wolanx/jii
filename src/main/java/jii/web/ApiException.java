package jii.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * @author admin
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4100309035717531908L;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
