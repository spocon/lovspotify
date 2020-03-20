package me.haga.librespot.spotifi.exception;

public class GeneralException extends RuntimeException{

    private Integer code;
    private String message;

    public GeneralException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
