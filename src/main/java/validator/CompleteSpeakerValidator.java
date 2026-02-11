package validator;

import domain.Speaker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompleteSpeakerValidator implements ConstraintValidator<CompleteSpeaker, Speaker> {

    @Override
    public boolean isValid(Speaker speaker, ConstraintValidatorContext context) {
        boolean heeftIets = isNietLeeg(speaker.getFirstname()) ||
                             isNietLeeg(speaker.getLastname()) ||
                             isNietLeeg(speaker.getOrganization());

        boolean allesIngevuld = isNietLeeg(speaker.getFirstname()) &&
                                isNietLeeg(speaker.getLastname()) &&
                                isNietLeeg(speaker.getOrganization());

        if (heeftIets && !allesIngevuld) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{validator.completeSpeakerValidator}")
            	   .addPropertyNode("organization")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isNietLeeg(String s) {
        return s != null && !s.isBlank();
    }
}

