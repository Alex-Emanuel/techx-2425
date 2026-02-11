package com.springBoot_IT_Conferentie;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import domain.Event;
import domain.Room;
import domain.Speaker;
import domain.User;
import repository.EventRepository;
import repository.RoomRepository;
import repository.SpeakerRepository;
import repository.UserRepository;
import static domain.Role.*;
import static domain.Category.*;

@Component
public class initDataConfig implements CommandLineRunner {

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private SpeakerRepository speakerRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void run(String... args) {

		//ROOMS
		//=====
		Room room1 = new Room("A001", 25);
		Room room2 = new Room("B002", 40);
		Room room3 = new Room("C003", 30);
		Room room4 = new Room("D004", 15);
		Room room5 = new Room("E005", 50);
		Room room6 = new Room("F006", 1);
		
		//SPEAKERS
		//========
		Speaker speaker1 = new Speaker("Jan", "Jansen", "Google");
		Speaker speaker2 = new Speaker("Emma", "de Vries", "Microsoft");
		Speaker speaker3 = new Speaker("Mark", "Peters", "Google");
		Speaker speaker4 = new Speaker("Sophie", "Bosch", "Microsoft");
		Speaker speaker5 = new Speaker("Liam", "Janssen", "Microsoft");
		Speaker speaker6 = new Speaker("Olivia", "Vermeulen", "Google");
		Speaker speaker7 = new Speaker("Lucas", "Martens", "HeroDevs");
		Speaker speaker8 = new Speaker("Mia", "Dekker", "Google");
		Speaker speaker9 = new Speaker("Daan", "Bakker", "HeroDevs");
		Speaker speaker10 = new Speaker("Lotte", "Smit", "HeroDevs");
		
		//EVENTS
		//======
		Event event1 = new Event("Lezing over Duurzame Technologie", room1, LocalDate.of(2025, 5, 22), LocalTime.of(14, 0), "1234", 70, 25.50, IT_MANAGEMENT);
		event1.addSpeaker(speaker1);  
		event1.addSpeaker(speaker3);  
		event1.imageChange("static/img/event-images/e1.jpg", "e1.jpg");
		//------
		Event event2 = new Event("Workshop Creatief Schrijven in de Digitale Wereld", room2, LocalDate.of(2025, 5, 23), LocalTime.of(10, 30), "6666", 70, 40.50, DIVERSITY_IN_TECH);
		event2.addSpeaker(speaker2);
		//------
		Event event3 = new Event("Marketinginnovatie en Data-analyse", room3, LocalDate.of(2025, 5, 24), LocalTime.of(11, 0), "8910", 83, 45.00, DATA_ANALYTICS);
		event3.addSpeaker(speaker5);
		event3.addSpeaker(speaker6);
		event3.addSpeaker(speaker7);
		//------
		Event event4 = new Event("Seminar: Kunstmatige Intelligentie in de Praktijk", room4, LocalDate.of(2025, 5, 22), LocalTime.of(15, 30), "1112", 45, 50.00, AI_ML);
		event4.addSpeaker(speaker8);
		event4.imageChange("static/img/event-images/e3.jpg", "e3.jpg");
		//------
		Event event5 = new Event("Netwerkborrel voor Innovatieve Startups", room5, LocalDate.of(2025, 5, 23), LocalTime.of(17, 0), "1314", 53, 20.00, IT_MANAGEMENT);
		event5.addSpeaker(speaker10);
		event5.addSpeaker(speaker2);
		event5.imageChange("static/img/event-images/e5.jpg", "e5.jpg");
		//------
		Event event6 = new Event("Tech Talk: AI en de Toekomst van Werk", room2, LocalDate.of(2025, 5, 24), LocalTime.of(13, 0), "1516", 61, 35.00, AI_ML);
		event6.addSpeaker(speaker3);
		event6.addSpeaker(speaker6);
		//------
		Event event7 = new Event("Deep Dive: Cloud Native Architectuur", room1, LocalDate.of(2025, 5, 22), LocalTime.of(9, 30), "1234", 70, 50.00, CLOUD_INFRASTRUCTURE);
		event7.addSpeaker(speaker4);
		//------
		Event event8 = new Event("Training: Veilige Code Schrijven", room2, LocalDate.of(2025, 5, 23), LocalTime.of(13, 0), "6666", 70, 45.00, CYBERSECURITY);
		event8.addSpeaker(speaker6);  
		event8.addSpeaker(speaker7);  
		event8.addSpeaker(speaker8);
		event8.imageChange("static/img/event-images/e7.jpg", "e7.jpg");
		//------
		Event event9 = new Event("Data Storytelling met Power BI", room3, LocalDate.of(2025, 5, 24), LocalTime.of(10, 15), "8910", 83, 42.50, DATA_ANALYTICS);
		event9.addSpeaker(speaker7);
		event9.imageChange("static/img/event-images/e8.jpg", "e8.jpg");
		//------
		Event event10 = new Event("AI Workshop: Machine Learning Basics", room4, LocalDate.of(2025, 5, 22), LocalTime.of(14, 0), "1112", 45, 45.00, AI_ML);
		event10.addSpeaker(speaker3);  
		event10.addSpeaker(speaker1);  
		event10.addSpeaker(speaker4);
		event10.imageChange("static/img/event-images/e9.jpg", "e9.jpg");
		//------
		Event event11 = new Event("Innovaties in IT-beheer", room5, LocalDate.of(2025, 5, 23), LocalTime.of(16, 45), "1314", 53, 30.00, IT_MANAGEMENT);
		event11.addSpeaker(speaker5);
		//------
		Event event12 = new Event("Cloudbeveiliging: Best Practices", room1, LocalDate.of(2025, 5, 24), LocalTime.of(11, 30), "1516", 61, 48.00, CYBERSECURITY);
		event12.addSpeaker(speaker2); 
		//------
		Event event13 = new Event("Hackathon voor Jonge Developers", room2, LocalDate.of(2025, 5, 22), LocalTime.of(10, 0), "1234", 70, 15.00, DEVELOPMENT);
		event13.addSpeaker(speaker9);  
		event13.addSpeaker(speaker4);  
		event13.addSpeaker(speaker10); 
		//------
		Event event14 = new Event("Panelgesprek: Vrouwen in Tech", room3, LocalDate.of(2025, 5, 23), LocalTime.of(14, 15), "6666", 70, 25.00, DIVERSITY_IN_TECH);
		event14.addSpeaker(speaker8);  
		event14.addSpeaker(speaker1); 
		//------
		Event event15 = new Event("Deep Learning & Neural Nets", room4, LocalDate.of(2025, 5, 25), LocalTime.of(9, 45), "8910", 83, 45.00, AI_ML);
		event15.addSpeaker(speaker2); 
		//------
		Event event16 = new Event("Scrum en Agile voor Teams", room5, LocalDate.of(2025, 5, 25), LocalTime.of(13, 30), "1112", 45, 35.00, IT_MANAGEMENT);
		event16.addSpeaker(speaker5);  
		event16.addSpeaker(speaker7); 
		event16.imageChange("static/img/event-images/e12.jpg", "e12.jpg");
		//------
		Event event17 = new Event("Scrum en Agile voor Teams", room5, LocalDate.of(2025, 5, 24), LocalTime.of(13, 30), "1112", 45, 35.00, IT_MANAGEMENT);
		event17.addSpeaker(speaker5);  
		event17.addSpeaker(speaker7); 
		//------
		Event event18 = new Event("Scrum en Agile voor Teams", room5, LocalDate.of(2025, 5, 23), LocalTime.of(13, 30), "1112", 45, 35.00, IT_MANAGEMENT);
		event18.addSpeaker(speaker5);  
		event18.addSpeaker(speaker7); 
		event18.imageChange("static/img/event-images/e14.jpg", "e14.jpg");
		
		event1.setAnnouncement("Keynote door Prof. Vermeulen over ethiek in AI");
		event2.setAnnouncement("In samenwerking met UGent - Lokaal B105");
		event3.setAnnouncement("Panelgesprek over duurzaamheid in ondernemen");
		event4.setAnnouncement("Inclusief expo van studentenprojecten");
		event5.setAnnouncement("Live demo: Quantum computing uitgelegd");
		event6.setAnnouncement("Gastcollege met certificaat van deelname");
		event7.setAnnouncement("Netwerkmoment met drankjes na afloop");
		event8.setAnnouncement("Opname beschikbaar na registratie");
		event9.setAnnouncement("Boekvoorstelling: 'De Toekomst van Onderwijs'");
		event10.setAnnouncement("Early bird kortingen tot 20 april");
		event11.setAnnouncement("Workshop: Hoe ChatGPT in je lessen gebruiken");
		event12.setAnnouncement("Bereikbaar met tram 1 – halte Bijlokehof");
		event13.setAnnouncement("Vergeet je badge niet mee te nemen");
		event14.setAnnouncement("Stiltecabines en oortjes beschikbaar");
		event15.setAnnouncement("Thema: Brein en besluitvorming in marketing");
		
		event1.setDescription("Een diepgaande lezing over de rol van technologie in duurzame bedrijfsvoering. Ontdek hoe slimme software, sensornetwerken en AI bijdragen aan een circulaire economie en ecologische voetafdruk verkleinen. We bekijken praktijkvoorbeelden uit de industrie en bespreken strategieën waarmee bedrijven duurzaamheid integreren in hun IT-infrastructuur. Deze sessie richt zich tot iedereen die zich wil inzetten voor een groenere toekomst via technologische innovatie.");
		event3.setDescription("Leer hoe moderne data-analyse marketingstrategieën vormgeeft en optimaliseert. Deze sessie toont hoe A/B testing, predictive analytics en klantsegmentatie het gedrag van je doelgroep inzichtelijk maken. We behandelen tools als Google Analytics en Power BI en bespreken real-life campagnes die dankzij data-inzichten enorm succesvol waren. Een must voor marketeers die hun resultaten willen onderbouwen met harde cijfers.");
		event4.setDescription("Seminar over praktische toepassingen van kunstmatige intelligentie in diverse sectoren zoals gezondheidszorg, retail en mobiliteit. Aan de hand van concrete cases zie je hoe AI beslissingen ondersteunt, processen automatiseert en klantbeleving verbetert. Verwacht inspirerende sprekers, live demonstraties en een kritische kijk op ethiek en data privacy. Deze sessie brengt theorie en praktijk moeiteloos samen.");
		event5.setDescription("Een informele gelegenheid om te netwerken met jonge ondernemers, investeerders en tech-enthousiastelingen. In een ontspannen setting maak je kennis met opkomende startups, wissel je ervaringen uit en vind je misschien wel je volgende co-founder of klant. We voorzien drankjes, fingerfood, achtergrondmuziek en speednetworking-momenten. Voor iedereen die innovatie en connectie op één avond wil combineren.");
		event6.setDescription("Een interactieve sessie over hoe AI de werkvloer transformeert. Van geautomatiseerde klantendiensten tot slimme assistenten die agenda’s beheren: AI dringt door in alle domeinen. We bespreken wat dit betekent voor werkzekerheid, samenwerking, productiviteit en ethiek. Inclusief open panel met experten, korte workshops én tijd voor vragen en discussie met het publiek.");
		event7.setDescription("Verdiep je in cloud-native architectuur en ontdek hoe dit softwareontwikkeling fundamenteel verandert. Begrijp de principes van microservices, containerisatie via Docker, orkestratie met Kubernetes en continuous deployment. Ontdek hoe je sneller innoveert, betrouwbaarder schaaft en veiliger werkt in complexe omgevingen. Deze sessie combineert theorie, praktijkvoorbeelden en technische inzichten voor ontwikkelaars en IT-managers.");
		event8.setDescription("Leer hoe je veilige code schrijft die bestand is tegen moderne bedreigingen. We behandelen OWASP top 10 kwetsbaarheden, inputvalidatie, veilige authenticatie en tooling zoals SonarQube en Snyk. Met hands-on oefeningen, live demo’s en checklisten die je meteen kunt toepassen in je eigen projecten. Ideaal voor developers, QA en security-minded teams die hun software willen hardenen.");
		event9.setDescription("Ontdek hoe je met Power BI overtuigende datavisualisaties bouwt en complexe inzichten eenvoudig presenteert. We bespreken best practices voor storytelling, dashboards op maat, KPI-rapporten en het delen van rapporten met stakeholders. Je leert omgaan met DAX-formules, slicers en interactieve elementen. De sessie eindigt met een use case waarin cijfers echt het verhaal vertellen.");
		event10.setDescription("Volg een workshop en leer stap voor stap hoe machine learning werkt. Je leert over supervised en unsupervised learning, bouwt zelf modellen in een no-code omgeving en ontdekt algoritmes zoals decision trees, random forests en regressie. We behandelen tools zoals Teachable Machine, Google Colab en scikit-learn. Geen voorkennis vereist – enkel nieuwsgierigheid naar de toekomst van slimme software.");
		event11.setDescription("Krijg inzicht in de laatste innovaties binnen IT-beheer: van hybride cloudbeheer tot automated patching en AIOps. We bespreken monitoring tools, self-healing systemen en de integratie van security in elke beheerlaag. Deze sessie geeft je praktische tips én strategische inzichten voor het efficiënter beheren van complexe IT-omgevingen in groeiende organisaties.");
		event12.setDescription("Leer hoe je cloudbeveiliging structureel aanpakt. Van identity en access management (IAM), encryptie van data-at-rest en -in-transit tot multi-factor authenticatie. We bespreken compliance (zoals GDPR en ISO 27001) en incidentrespons. Deze sessie is bedoeld voor IT-professionals die hun cloudarchitectuur waterdicht willen maken zonder gebruiksgemak te verliezen.");
		event13.setDescription("Een dynamische hackathon voor jonge developers om ideeën snel om te zetten in functionerende prototypes. Gedurende één dag werk je in een team aan een concrete challenge binnen een thema zoals duurzaamheid, mobiliteit of inclusie. Onder begeleiding van coaches bouw je, test je en pitch je jouw oplossing aan een vakjury. Spannend, leerzaam en vooral: fun.");
		event14.setDescription("Een inspirerend panelgesprek met vrouwen uit de techwereld die openhartig vertellen over hun carrièrepad, obstakels en drijfveren. Onderwerpen zijn onder meer representatie, rolmodellen, het doorbreken van stereotypen en inclusieve bedrijfsculturen. Het event is inclusief Q&A en netwerkmoment, en biedt zowel inspiratie als concrete handvatten voor verandering.");
		event17.setDescription("Een diepgaande workshop over de toepassing van Scrum en Agile in real-life projecten. We simuleren sprints, bespreken valkuilen en hoe je team ownership en samenwerking bevordert. Je leert hoe je Agile methodes kunt afstemmen op de cultuur van jouw organisatie, en hoe metrics zoals velocity en burndown charts helpen om bij te sturen.");
		event18.setDescription("Ontdek hoe Scrum en Agile je team helpen sneller en effectiever resultaten te boeken. Via concrete voorbeelden leer je hoe je Agile governance structureert, welke rollen verantwoordelijkheden hebben en hoe je draagvlak opbouwt. Ook ideaal voor organisaties die op zoek zijn naar continue verbetering en wendbaarheid in een snel veranderende markt.");

		
		//USERS
		//=====
		User user1 = new User("admin@example.com", "kaas", ADMIN);
		User user2 = new User("user@example.com", "kaas", USER);
		user2.addFavoriteEvent(event1);
		user2.addFavoriteEvent(event3);
		user2.addFavoriteEvent(event5);
		
		//ADD TO REPO
		//===========
		roomRepository.saveAll(List.of(room1, room2, room3, room4, room5, room6));
		speakerRepository.saveAll(List.of(
				speaker1, speaker2, speaker3, speaker4, speaker5, 
				speaker6, speaker7, speaker8, speaker9, speaker10
		));
		eventRepository.saveAll(List.of(
				event1, event2, event3, event4, event5, event6, 
				event7, event8, event9, event10, event11, event12, 
				event13, event14, event15, event16, event17, event18
		));
		userRepository.saveAll(List.of(user1, user2));
	}
}
