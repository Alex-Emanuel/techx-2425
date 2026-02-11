package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Event;

public class BeamerValidator implements Validator {
	
	@Override
    public boolean supports(Class<?> klass) {
        return Event.class.isAssignableFrom(klass);
    }

	@Override
	public void validate(Object newevent, Errors errors) {
		Event registration = (Event) newevent;
    	
    	String beamercodeStr = registration.getBeamerCode();
        Integer beamercheck = registration.getBeamerCheck();

        if (beamercodeStr == null || beamercodeStr.trim().isEmpty() || beamercheck == null) {
            return;
        }
        
        int beamercode = Integer.parseInt(beamercodeStr);
        if (beamercode % 97 != beamercheck) {
            errors.rejectValue("beamerCheck", 
            		"invalid.beamercheck", 
            		"Beamercheck is niet correct (moet beamercode % 97 zijn).");
        }
	}
}
