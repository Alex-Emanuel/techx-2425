package domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "roomcode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String roomcode;
	private int capacity;

	@Override
	public String toString() {
		return String.format("%s", roomcode);
	}
}
