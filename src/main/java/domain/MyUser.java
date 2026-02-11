package domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "username")
@Builder
@AllArgsConstructor
@Table(name = "users")
public class MyUser implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_EVENTS = 5;
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter(AccessLevel.NONE)
	private long id;
	
	private String username;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@ManyToMany
	@Builder.Default
    private Set<Event> favoriteEvents = new HashSet<>();
	
	public MyUser(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public Set<Event> getFavoriteEvents(){
        return Collections.unmodifiableSet(favoriteEvents);
    }
    
    public void addFavoriteEvent(Event event) {
    	if(favoriteEvents.size() >= MAX_EVENTS)
    		return;
    	favoriteEvents.add(event);
    }

    public void removeFavoriteEvent(Event event) {
        favoriteEvents.remove(event);
    }
}
