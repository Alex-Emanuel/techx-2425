package service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import domain.Event;
import jakarta.validation.Valid;

public interface EventService {

	@Transactional
	public void makeEvent(Event event);
	
	@Transactional
	public void editEvent(Event existingevent, Event newevent, MultipartFile image);

	@Transactional
	public void saveImage(MultipartFile image, @Valid Event newevent);
}
