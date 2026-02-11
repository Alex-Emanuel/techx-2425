package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BeamerCorrectValidator implements ConstraintValidator<BeamerCorrect, Event> {

	@Override
	public boolean isValid(Event event, ConstraintValidatorContext context) {
		if (event == null || event.getBeamerCode() == null || event.getBeamerCheck() == null) {
            return true;
        }

        try {
            int beamercode = Integer.parseInt(event.getBeamerCode());
            boolean isValid = beamercode % 97 == event.getBeamerCheck();
            if(!isValid) {
            	context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{invalid.beamercheck}")
                	   .addPropertyNode("beamerCheck")
                       .addConstraintViolation();
                return false;
            }
            return isValid;
        } catch (NumberFormatException e) {
            return false;
        }
	}

}
