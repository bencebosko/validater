package org.validater.exceptions;

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
