## Description
A **thread-safe Validation framework** for validating java objects. Validations can be added to the fields
of an object with any type, or we can map validators to a specified type of object. Although the lib
contains some built-in field validations, more specific validations can be implemented by implementing
the `FieldValidator` or `Validator` interface.

**Validations are cached for each type of object**, so objects are only scanned for the first time
when their type is unknown. Some condition checks are also can be saved this way, so the validations run pretty fast.

Furthermore, validations **can be loaded to the cache eagerly** by providing packages to scan. Objects from these packages will be 
scanned at initialization.

## Examples
### Configuration
``` java
@Configuration
public class BeanCfg {

    @Bean
    public ValidationRunner validationRunner() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        return factory.getValidationRunner();
    }
}
```
Or with eager loading of the types to be validated:
``` java

    @Bean
    public ValidationRunner validationRunner() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        factory.setPackagesToScan("org.example.dto")
        return factory.getValidationRunner();
    }
}

```

### ValidationRunner
Run validations through the `ValidationRunner` class. The result of the validation is returned in `ValidationResult` containing
an immutable map which maps field names to error messages.

``` java

// ..

@Autowired
private ValidationRunner validationRunner;

@PostMapping("/example/create")
public ResponseEntity<ValidationResult> createExample(@RequestBody ExampleDto exampleDto) {

    ValidationResult result = validationRunner.validate(exampleDto);

    if (result.isValid()) {
        // ..

    } else {
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
```
### Field validations
Fields can be validated by annotating them with a validation matching their type. Each validations must be implemented once and reusable in the entire app.

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
### Creating a Field validation
Here's how the `Matches` built-in validation implemented.

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = MatchesValidator.class)
public @interface Matches {
    String regex();
    String message() default "not matches the pattern";
}


public class MatchesValidator implements FieldValidator<String, Matches> {

    @Override
    public Class<String> fieldType() {
        return String.class;
    }

    @Override
    public void validate(@Nullable String value, Matches annotation, List<ValidationError> errors) {
        if(value == null) {
            errors.add(new ValidationError("value is null"));
        }
        else if(!value.matches(annotation.regex()))
            errors.add(new ValidationError(annotation.message()));
    }
}

```
### Creating Validator for specific objects

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
    
      errorsByField.put("fieldName", List.of(new ValidationError("error message"));
    }
}

```
