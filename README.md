## Description
A thread-safe Validation framework for validating java objects. Validations can be added to the fields
of an object with any type, or we can add validators to a specified type of object. Although the lib
contains some built-in field validations, more specific validations can be implemented by implementing
the `FieldValidator` or `Validator` interface, and mapping the annotation labels to these implementations.

Validations mapped to `FieldValidator` validates single fields of objects of any type, while Objects
mapped to their corresponding Validator validate the whole object at once.

Field validations are cached for each type of object, so objects are only scanned for the first time
when their type is unknown. Some condition checks are also can be saved this way, so the validations run pretty fast.

The validator implementations can be loaded lazily while scanning annotations or eagerly from Spring's `ApplicationContext`.

## Examples

### Validating fields of any object

``` java
public class ExampleDto {

    @Matches(regex = "John.*")
    private String userName;

    @Required
    @EmailAvailable
    private String email;

    @Min(value = 18, message = "under 18")
    @Max(value = 35, message = "over 35")
    private int age;
    
    // setters, getters ..

```

### Validating specific objects

``` java
@ValidatedBy(validator = ExampleDtoValidator.class)
public class ExampleDto {

    private String userName;
    private String email;
    private int age;
    
    // .. 
}

public class ExampleDtoValidator implements Validator<ExampleDto> {

    @Override
    public Class<ExampleDto> type() {
        return ExampleDto.class;
    }

    @Override
    public void validate(ExampleDto value, Map<String, List<ValidationError>> errorsByField) { 
      // validation logic
    }
}

```
