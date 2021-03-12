package org.validater;

public class ValidationError {

    private final String message;

    public ValidationError(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        ValidationError that = (ValidationError) o;

        return message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "message='" + message + '\'' +
                '}';
    }
}
