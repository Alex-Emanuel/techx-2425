package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("Gebruiker"), ADMIN("Administrator");
	
	private final String displayRole;
}
