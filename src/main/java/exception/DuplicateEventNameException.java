package exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DuplicateEventNameException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@Getter private final String name;
	@Getter private final LocalDate date;
}
