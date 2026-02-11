package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validator.CompleteSpeaker;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(exclude = "id")
@CompleteSpeaker
public class Speaker implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// empty string OR any character multiple times (\\s spaces here still allowed)
	@Pattern(regexp = "^$|^[A-Za-zÀ-ÿ\\s'-]+$", message = "{validation.firstname.Pattern.message}")
    private String firstname;
	
	@Pattern(regexp = "^$|^[A-Za-zÀ-ÿ\\s'-]+$", message = "{validation.lastname.Pattern.message}")
    private String lastname;
	
	@Pattern(regexp = "^$|^[A-Za-zÀ-ÿ\\s'-]+$", message = "{validation.organization.Pattern.message}")
    private String organization;
    
    public Speaker(String firstname, String lastname, String organization) {
    	this.firstname = firstname;
    	this.lastname = lastname;
    	this.organization = organization;
    }
    
    @Override
    public String toString() {
        StringBuilder initials = new StringBuilder();

        String[] parts = lastname.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                char initial = parts[i].charAt(0);
                initials.append(initial).append(".");
            }
        }

        return firstname + " " + initials.toString();
    }
    
    @ManyToMany(mappedBy = "speakers")
	@JsonIgnore
    private Set<Event> events = new HashSet<>();
}
