package validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateInRangeValidator implements ConstraintValidator<DateInRange, LocalDate> {
    private static final LocalDate START_DATE = LocalDate.of(2025, 5, 22);
    private static final LocalDate END_DATE = LocalDate.of(2025, 5, 25);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) 
        	return true;
        return !value.isBefore(START_DATE) && !value.isAfter(END_DATE);
    }
}
