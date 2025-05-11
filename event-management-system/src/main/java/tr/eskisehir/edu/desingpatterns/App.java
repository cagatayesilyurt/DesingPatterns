package tr.eskisehir.edu.desingpatterns;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EventManager eventManager = EventManager.getInstance();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Map<String, SearchStrategy> searchStrategies = new HashMap<>();
    
    static {
        // Initialize search strategies
        searchStrategies.put("name", new NameSearchStrategy());
        searchStrategies.put("category", new CategorySearchStrategy());
        searchStrategies.put("tag", new TagSearchStrategy());
    }
    
    public static void main(String[] args) {
        boolean running = true;
        
        System.out.println("Event Management System");
        System.out.println("======================");
        
        // Sample events for testing
        createSampleEvents();
        
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    createEvent();
                    break;
                case "2":
                    searchEvent();
                    break;
                case "3":
                    registerToEvent();
                    break;
                case "4":
                    modifyEvent();
                    break;
                case "0":
                    running = false;
                    System.out.println("Thank you for using Event Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void printMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Create Event");
        System.out.println("2. Search Event");
        System.out.println("3. Register to Event");
        System.out.println("4. Modify Event");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static void createEvent() {
        System.out.println("\n=== Create Event ===");
        
        // Get event details
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter event location: ");
        String location = scanner.nextLine();
        
        LocalDateTime dateTime = null;
        while (dateTime == null) {
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            try {
                dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm format.");
            }
        }
        
        System.out.print("Enter organizer name: ");
        String organizer = scanner.nextLine();
        
        System.out.print("Enter event description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter event type (Concert, Seminar, Workshop, etc.): ");
        String type = scanner.nextLine();
        
        // Create event using factory pattern
        Event event = EventFactory.createEvent(type, name, location, dateTime, organizer, description);
        
        // Add additional categories
        System.out.println("\nYou can add up to 2 more categories.");
        for (int i = 0; i < 2; i++) {
            System.out.print("Enter category " + (i + 2) + " (or press Enter to skip): ");
            String category = scanner.nextLine();
            if (category.isEmpty()) {
                break;
            }
            event.addCategory(category);
        }
        
        // Add additional tags
        System.out.println("\nYou can add up to 2 more tags.");
        for (int i = 0; i < 2; i++) {
            System.out.print("Enter tag " + (i + 2) + " (or press Enter to skip): ");
            String tag = scanner.nextLine();
            if (tag.isEmpty()) {
                break;
            }
            event.addTag(tag);
        }
        
        // Save the event
        eventManager.addEvent(event);
        System.out.println("Event created successfully: " + event.getName());
    }
    
    private static void searchEvent() {
        System.out.println("\n=== Search Event ===");
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Category");
        System.out.println("3. Tag");
        System.out.print("Enter your choice: ");
        String searchChoice = scanner.nextLine();
        
        SearchStrategy strategy;
        String searchType;
        
        switch (searchChoice) {
            case "1":
                searchType = "name";
                strategy = searchStrategies.get("name");
                break;
            case "2":
                searchType = "category";
                strategy = searchStrategies.get("category");
                break;
            case "3":
                searchType = "tag";
                strategy = searchStrategies.get("tag");
                break;
            default:
                System.out.println("Invalid choice. Using name search by default.");
                searchType = "name";
                strategy = searchStrategies.get("name");
        }
        
        System.out.print("Enter search keyword: ");
        String query = scanner.nextLine();
        
        List<Event> searchResults = strategy.search(eventManager.getAllEvents(), query);
        
        if (searchResults.isEmpty()) {
            System.out.println("No events found matching your search criteria.");
            return;
        }
        
        // Display search results
        System.out.println("\nSearch Results:");
        System.out.println("Sort order:");
        System.out.println("1. Ascending by name");
        System.out.println("2. Descending by name");
        System.out.print("Enter your choice: ");
        String sortChoice = scanner.nextLine();
        
        boolean ascending = !sortChoice.equals("2");
        
        List<Event> sortedResults;
        if (ascending) {
            sortedResults = searchResults.stream()
                    .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                    .toList();
        } else {
            sortedResults = searchResults.stream()
                    .sorted((e1, e2) -> e2.getName().compareToIgnoreCase(e1.getName()))
                    .toList();
        }
        
        displayEvents(sortedResults);
        
        // View event details
        System.out.print("\nEnter event number to view details (or 0 to go back): ");
        try {
            int eventNumber = Integer.parseInt(scanner.nextLine());
            if (eventNumber > 0 && eventNumber <= sortedResults.size()) {
                Event selectedEvent = sortedResults.get(eventNumber - 1);
                displayEventDetails(selectedEvent);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    private static void registerToEvent() {
        System.out.println("\n=== Register to Event ===");
        
        List<Event> events = eventManager.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available for registration.");
            return;
        }
        
        displayEvents(events);
        
        System.out.print("Enter event number to register (or 0 to go back): ");
        try {
            int eventNumber = Integer.parseInt(scanner.nextLine());
            if (eventNumber > 0 && eventNumber <= events.size()) {
                Event selectedEvent = events.get(eventNumber - 1);
                
                System.out.print("Enter your user ID: ");
                String userId = scanner.nextLine();
                
                boolean success = eventManager.registerUserToEvent(selectedEvent.getId(), userId);
                if (success) {
                    System.out.println("Successfully registered for: " + selectedEvent.getName());
                    System.out.println("Current registration count: " + selectedEvent.getRegistrationCount());
                } else {
                    System.out.println("Registration failed. You may already be registered.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    private static void modifyEvent() {
        System.out.println("\n=== Modify Event ===");
        
        List<Event> events = eventManager.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available to modify.");
            return;
        }
        
        displayEvents(events);
        
        System.out.print("Enter event number to modify (or 0 to go back): ");
        try {
            int eventNumber = Integer.parseInt(scanner.nextLine());
            if (eventNumber > 0 && eventNumber <= events.size()) {
                Event selectedEvent = events.get(eventNumber - 1);
                modifySelectedEvent(selectedEvent);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
    
    private static void modifySelectedEvent(Event event) {
        System.out.println("\nModifying event: " + event.getName());
        System.out.println("1. Update event details");
        System.out.println("2. Modify categories and tags");
        System.out.println("3. Undo last modification");
        System.out.print("Enter your choice: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                updateEventDetails(event);
                break;
            case "2":
                modifyCategoriesAndTags(event);
                break;
            case "3":
                boolean undoSuccess = eventManager.undoLastAction();
                if (undoSuccess) {
                    System.out.println("Last action undone successfully.");
                } else {
                    System.out.println("Nothing to undo or undo failed.");
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void updateEventDetails(Event event) {
        // Create a copy of the original event
        Event updatedEvent = new Event(
                event.getName(),
                event.getLocation(),
                event.getDateTime(),
                event.getOrganizer(),
                event.getDescription()
        );
        updatedEvent.setCategories(event.getCategories());
        updatedEvent.setTags(event.getTags());
        
        System.out.print("Enter new name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            updatedEvent.setName(name);
        }
        
        System.out.print("Enter new location (or press Enter to keep current): ");
        String location = scanner.nextLine();
        if (!location.isEmpty()) {
            updatedEvent.setLocation(location);
        }
        
        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm) (or press Enter to keep current): ");
        String dateTimeStr = scanner.nextLine();
        if (!dateTimeStr.isEmpty()) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
                updatedEvent.setDateTime(dateTime);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Date and time not updated.");
            }
        }
        
        System.out.print("Enter new organizer (or press Enter to keep current): ");
        String organizer = scanner.nextLine();
        if (!organizer.isEmpty()) {
            updatedEvent.setOrganizer(organizer);
        }
        
        System.out.print("Enter new description (or press Enter to keep current): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            updatedEvent.setDescription(description);
        }
        
        boolean updated = eventManager.updateEvent(event.getId(), updatedEvent);
        if (updated) {
            System.out.println("Event updated successfully.");
        } else {
            System.out.println("Failed to update event.");
        }
    }
    
    private static void modifyCategoriesAndTags(Event event) {
        System.out.println("\nCurrent categories: " + event.getCategories());
        System.out.println("Current tags: " + event.getTags());
        
        // Create a copy of the original event
        Event updatedEvent = new Event(
                event.getName(),
                event.getLocation(),
                event.getDateTime(),
                event.getOrganizer(),
                event.getDescription()
        );
        updatedEvent.setCategories(event.getCategories());
        updatedEvent.setTags(event.getTags());
        
        System.out.println("\nModify categories? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            updatedEvent.getCategories().clear(); // Clear existing categories
            
            System.out.println("Enter up to 3 categories (press Enter after each, empty to finish):");
            for (int i = 0; i < 3; i++) {
                System.out.print("Category " + (i + 1) + ": ");
                String category = scanner.nextLine();
                if (category.isEmpty()) {
                    break;
                }
                updatedEvent.addCategory(category);
            }
        }
        
        System.out.println("\nModify tags? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            updatedEvent.getTags().clear(); // Clear existing tags
            
            System.out.println("Enter up to 3 tags (press Enter after each, empty to finish):");
            for (int i = 0; i < 3; i++) {
                System.out.print("Tag " + (i + 1) + ": ");
                String tag = scanner.nextLine();
                if (tag.isEmpty()) {
                    break;
                }
                updatedEvent.addTag(tag);
            }
        }
        
        boolean updated = eventManager.updateEvent(event.getId(), updatedEvent);
        if (updated) {
            System.out.println("Categories and tags updated successfully.");
        } else {
            System.out.println("Failed to update categories and tags.");
        }
    }
    
    private static void displayEvents(List<Event> events) {
        System.out.println("\n=== Events ===");
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            System.out.println((i + 1) + ". " + event.getName() + " - " + 
                              event.getDateTime().format(dateFormatter) + " - " + 
                              event.getLocation());
        }
    }
    
    private static void displayEventDetails(Event event) {
        System.out.println("\n=== Event Details ===");
        System.out.println("Name: " + event.getName());
        System.out.println("Location: " + event.getLocation());
        System.out.println("Date and Time: " + event.getDateTime().format(dateFormatter));
        System.out.println("Organizer: " + event.getOrganizer());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Categories: " + String.join(", ", event.getCategories()));
        System.out.println("Tags: " + String.join(", ", event.getTags()));
        System.out.println("Registration Count: " + event.getRegistrationCount());
    }
    
    // Create some sample events for testing
    private static void createSampleEvents() {
        Event concert = EventFactory.createEvent(
            "Concert", 
            "Summer Rock Festival", 
            "City Park", 
            LocalDateTime.now().plusDays(30), 
            "Rock Productions", 
            "Annual rock music festival featuring local and international bands."
        );
        concert.addTag("Outdoor");
        concert.addTag("Music");
        
        Event seminar = EventFactory.createEvent(
            "Seminar", 
            "Introduction to AI", 
            "University Hall", 
            LocalDateTime.now().plusDays(10), 
            "Tech Institute", 
            "Learn the basics of artificial intelligence and machine learning."
        );
        seminar.addTag("Educational");
        seminar.addTag("Technology");
        
        Event workshop = EventFactory.createEvent(
            "Workshop", 
            "Creative Writing Workshop", 
            "Community Center", 
            LocalDateTime.now().plusDays(5), 
            "Writers Guild", 
            "Hands-on workshop to improve your creative writing skills."
        );
        workshop.addCategory("Arts");
        workshop.addTag("Interactive");
        
        eventManager.addEvent(concert);
        eventManager.addEvent(seminar);
        eventManager.addEvent(workshop);
    }
}