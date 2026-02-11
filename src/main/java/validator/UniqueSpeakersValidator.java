package validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import domain.Event;
import domain.Speaker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueSpeakersValidator implements ConstraintValidator<UniqueSpeakers, Event> {
	
	@Override
	public boolean isValid(Event event, ConstraintValidatorContext context) {		
		List<Speaker> geldigeSprekers = event.getSpeakers().stream()
	            .filter(s -> s.getFirstname() != null && !s.getFirstname().isBlank()
	                      && s.getLastname() != null && !s.getLastname().isBlank()
	            		  && s.getOrganization() != null && !s.getOrganization().isBlank())
	            .collect(Collectors.toList());
		
		Set<Speaker> uniqueSpeakers = new HashSet<>(geldigeSprekers);
		
		return uniqueSpeakers.size() == geldigeSprekers.size();
	}
}
