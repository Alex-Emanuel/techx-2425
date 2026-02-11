package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import domain.Event;
import domain.Speaker;
import exception.DoubleBookingException;
import exception.DuplicateEventNameException;
import jakarta.validation.Valid;
import repository.EventRepository;
import repository.SpeakerRepository;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private SpeakerRepository speakerRepository;

	@Override
	public void saveImage(MultipartFile image, @Valid Event newevent) {
		if (!image.isEmpty()) {
            try {
            	newevent.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	public void makeEvent(Event event) {
		checkDouble(event, event.getId());
		List<Speaker> finalSpeakers = chosenSpeakers(event);		
	    event.setSpeakers(finalSpeakers);
	    eventRepository.save(event);
	}

	@Override
	public void editEvent(Event knownEvent, Event newevent, MultipartFile image) {
		checkDouble(newevent, knownEvent.getId());
		
        knownEvent.setCategory(newevent.getCategory());
        knownEvent.setName(newevent.getName());
        knownEvent.setDescription(newevent.getDescription());
        knownEvent.setDate(newevent.getDate());
        knownEvent.setTime(newevent.getTime());
        knownEvent.setBeamerCheck(newevent.getBeamerCheck());
        knownEvent.setBeamerCode(newevent.getBeamerCode());
        knownEvent.setPrice(newevent.getPrice());
        knownEvent.setAnnouncement(newevent.getAnnouncement());
        knownEvent.setRoom(newevent.getRoom());
        List<Speaker> finalSpeakers = chosenSpeakers(newevent);
        knownEvent.setSpeakers(finalSpeakers);
        
        if (image != null && !image.isEmpty()) {
            try {
                knownEvent.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        eventRepository.save(knownEvent);
	}
	
	private List<Speaker> chosenSpeakers(Event event) {
		List<Speaker> speakers = event.getSpeakers();
		
		List<Speaker> filteredSpeakers = speakers.stream()
			    .filter(speaker -> (speaker.getFirstname() == null || speaker.getFirstname().isBlank()) &&
			                       (speaker.getLastname() == null || speaker.getLastname().isBlank()) &&
			                       (speaker.getOrganization() == null || speaker.getOrganization().isBlank()))
			    .collect(Collectors.toList());
			
		speakers.removeAll(filteredSpeakers);
		
		List<Speaker> finalSpeakers = new ArrayList<>();

		for (Speaker speaker : speakers) {
			Optional<Speaker> existing = speakerRepository.findByFirstnameAndLastnameAndOrganization(
			            speaker.getFirstname(), speaker.getLastname(), speaker.getOrganization());

			if (existing.isPresent())
				finalSpeakers.add(existing.get());
			else
			    finalSpeakers.add(speakerRepository.save(speaker));
		}
		
		return finalSpeakers;
	}
	
	private void checkDouble(Event event, Long excludeEventId) {
	    
	    // Er mag geen dubbel event op hetzelfde tijdstip in hetzelfde lokaal zijn.
	    Optional<Event> optEvent = eventRepository.findByDateAndTimeAndRoom(
	            event.getDate(), event.getTime(), event.getRoom());
	    if(optEvent.isPresent() && optEvent.get().getId() != excludeEventId) {
	        throw new DoubleBookingException(event.getDate(), event.getTime(), event.getRoom());
	    }
	    
	    // Naam van de event op dezelfde dag mag nog niet voorkomen
	    optEvent = eventRepository.findByNameAndDate(event.getName(), event.getDate());
	    if(optEvent.isPresent() && optEvent.get().getId() != excludeEventId) {
	        throw new DuplicateEventNameException(event.getName(), event.getDate());
	    }
	}
}
