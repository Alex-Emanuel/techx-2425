package domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validator.DateInRange;
import validator.UniqueSpeakers;
import validator.ValidSpeakers;

@Entity
@Getter
@Setter
@UniqueSpeakers
@ValidSpeakers
@EqualsAndHashCode(of = "name")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Event implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private long id;
	
	@NotNull(message = "{validation.category.NotNull.message}")
	@Enumerated(EnumType.STRING)
    private Category category;
	
	@NotBlank(message = "{validation.name.NotBlank.message}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{validation.name.Pattern.message}")
	private String name;
	
	@Size(max = 1000, message = "{validation.description.Size.message}")
	@Column(length = 1000)
	private String description;
	
	@NotNull(message = "{validation.date.NotNull.message}")
	@DateInRange
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	@NotNull(message = "{validation.time.NotNull.message}")
	@DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;
	
	@NotBlank(message = "{validation.beamerCode.NotBlank.message}")
	@Pattern(regexp = "^[0-9]{4}$", message = "{validation.beamerCode.Pattern.message}")
    private String beamerCode;
	
	@NotNull(message = "{validation.beamerCheck.NotNull.message}")
	@Min(value = 10, message = "{validation.beamerCheck.Min.message}")
	@Max(value = 99, message = "{validation.beamerCheck.Max.message}")
	private Integer beamerCheck;
	
	@DecimalMin(value = "9.99", inclusive = true, message = "{validation.price.DecimalMin.message}")
	@DecimalMax(value = "100", inclusive = false, message = "{validation.price.DecimalMax.message}")
	@NotNull(message = "{validation.price.NotNull.message}")
	@NumberFormat(pattern="#0.00")
    private Double price;
    
	@Size(max = 255, message = "{validation.announcement.Size.message}")
    private String announcement;
    
    @ManyToOne
    @NotNull(message = "{validation.room.NotNull.message}")
    @JoinColumn(name = "roomcode")
    private Room room;
    
	@Size(min = 1, max = 3, message = "{validation.speakers.Size.message}")
    @Valid
	@ManyToMany
	private List<Speaker> speakers = new ArrayList<>();
	
	@Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    
    @Column(name="image_name")
    private String imageName;
	
    public Event(String name, Room room, LocalDate date, LocalTime time, String beamerCode, int beamerCheck, double price, Category category) {
		this.name = name;
		this.room = room;
		this.date = date;
		this.time = time;
		this.beamerCode = beamerCode;
		this.beamerCheck = beamerCheck;
		this.price = price;
		this.category = category;
		// Set placeholder image
		imageChange("static/img/placeholder.jpg", "placeholder.jpg");
	}
    
    public void addSpeaker(Speaker speaker) {
    	if(speakers.size() < 3)
    		speakers.add(speaker);
    }

    public void removeSpeaker(Speaker speaker) {
        speakers.remove(speaker);
    }
    
    public void imageChange(String path, String name) {
		try {
	        InputStream img = getClass().getClassLoader().getResourceAsStream(path);
            if (img != null) {
                this.image = img.readAllBytes();
                setImageName(name);
            }            
	    } catch (IOException e) {
	        throw new RuntimeException("Could not load image", e);
	    }
	}
    
    public void setImage(MultipartFile image) throws IOException {
    	if (!image.isEmpty()) {
    		this.image = image.getBytes();
    	    this.imageName = image.getOriginalFilename();
    	} else {
    		imageChange("static/img/placeholder.jpg", "placeholder.jpg");
    	}
    }
}
