package domain;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "username")
public class User implements Serializable {

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
    private Set<Event> favoriteEvents = new HashSet<>();
	
	public User(String username, String password, Role role) {
		this.username = username;
		setPassword(password);
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
	
	private void setPassword(String plainTextPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            this.password = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
	
	private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
