package Errors;

import java.io.Serial;

public class InvalidDistance extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDistance(String reason) {
        super(reason);
    }
}
