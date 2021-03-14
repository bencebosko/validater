package org.validater.exceptions;

/**
 * Thrown when a validator has no default or public nullary constructor.
 */

public class ValidatorInstantiationException extends ValidationException {

    private final Class<?> validatorType;

    public ValidatorInstantiationException(Class<?> validatorType) {
        super("Validator cannot be instantiated: " + validatorType.getSimpleName());
        this.validatorType = validatorType;
    }

    public Class<?> getValidatorType() {
        return validatorType;
    }
}
