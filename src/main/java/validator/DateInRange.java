package validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = DateInRangeValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface DateInRange {

    String message() default "{validator.dateInRange}";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}