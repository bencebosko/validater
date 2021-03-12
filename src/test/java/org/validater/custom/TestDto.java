package org.validater.custom;

import org.validater.annotations.ValidatedBy;

@ValidatedBy(validator = TestDtoValidator.class)
public class TestDto {

    private String email = "john@gmail.com";
    private String userName = "John Doe";
    private int age = 32;

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }
}
