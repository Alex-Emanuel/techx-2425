package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RoomAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@Getter private final String roomcode;
}
