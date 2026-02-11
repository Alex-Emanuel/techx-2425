package exception;

import java.time.LocalDate;
import java.time.LocalTime;

import domain.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DoubleBookingException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@Getter private final LocalDate date;
	@Getter private final LocalTime time;
	@Getter private final Room room;
}
