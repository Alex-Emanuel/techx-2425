package validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = BeamerCorrectValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface BeamerCorrect {

	String message() default "{invalid.beamercheck}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
