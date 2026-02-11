package domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "roomcode")
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "roomcode", "capacity"})
public class Room implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Pattern(regexp = "^[A-Za-z]{1}\\d{3}$", message = "{validation.roomcode.Pattern.message}")
	private String roomcode;
	
	@Range(min = 1, max = 50, message = "{validation.capacity.Range.message}")
    private int capacity;

	@Override
	public String toString() {
		return String.format("%s", roomcode);
	}
}
