package dev.shvetsova.ewmc.stats.exeption;

public class ValidateException extends RuntimeException {

    public ValidateException(String message) {
        super(message);
    }
}