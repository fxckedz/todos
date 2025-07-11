package br.com.medeiros.api.todo.v1.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Date timestamp;
    private HttpStatus message;
    private String details;

    public ExceptionResponse(Date timestamp, HttpStatus message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public HttpStatus getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
