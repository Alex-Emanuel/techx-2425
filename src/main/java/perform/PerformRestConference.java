package perform;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.reactive.function.client.WebClient;

import domain.Event;
import reactor.core.publisher.Mono;

public class PerformRestConference {

	private final String SERVER_URI = "http://localhost:8080/rest";
	private WebClient webClient = WebClient.create();

	public PerformRestConference() {
		
		String[] dates = { "22-05-2025", "23-05-2025" };
		for (String date : dates) {
		    getEventsOnDate(date);
		    System.out.println("==============================================================================================================================================");
		}
		
		IntStream.rangeClosed(1, 3).forEach(number -> {
			try
			{
				System.out.printf("\n--------- GET CAPACITY OF ROOM %d ---------%n", number);
				getCapacityOfRoom(number);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		});
	}
	
	private void getEventsOnDate(String date) {   
		System.out.printf("\n------- GET EVENTS ON %s -------%n%n", date);
	    
	    webClient.get()
	    .uri(SERVER_URI + "/events/" + date)
	    .retrieve()
        .bodyToFlux(Event.class)
        .flatMap(event -> {
            printEventData(event);
            return Mono.empty();
        })
        .blockLast();
	}

	private void getCapacityOfRoom(int id) {
	    Integer capacity = webClient.get()
	        .uri(SERVER_URI + "/rooms/" + id + "/capacity")
	        .retrieve()
	        .bodyToMono(Integer.class)
	        .block();

	    System.out.println("Capacity of Room with id " + id + ": " + capacity);
	}
		
	private void printEventData(Event event) {
		DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		System.out.println(String.format("""
		        ------ %s 
		        ID           : %d
		        Category     : %s
		        Date         : %s
		        Time         : %s
		        Price        : €%.2f
		        Beamer Code  : %s
		        Beamer Check : %d
		        Room Code    : %s
		        Capacity     : %d
		        Announcement : %s
		        Image Name   : %s
		        Description:
		        %s
		        Speakers:
		        %s
		        """,
		        event.getName(), event.getId(), event.getCategory(), event.getDate().format(form),
		        event.getTime(), event.getPrice(), event.getBeamerCode(), event.getBeamerCheck(),
		        event.getRoom() != null ? event.getRoom().getRoomcode() : "-",
		        event.getRoom() != null ? event.getRoom().getCapacity() : 0,
		        event.getAnnouncement() != null ? event.getAnnouncement() : "-",
		        event.getImageName() != null ? event.getImageName() : "-",
		        event.getDescription() != null ? event.getDescription() : "-",
		        event.getSpeakers() != null && !event.getSpeakers().isEmpty()
		            ? event.getSpeakers().stream()
		            	    .map(s -> String.format("  - %s %s (%s)", s.getFirstname(), s.getLastname(), s.getOrganization()))
		            	    .collect(Collectors.joining("\n"))
		            : "-"
		));
	}
}
