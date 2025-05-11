package tr.eskisehir.edu.desingpatterns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventManager {
    // Singleton instance
    private static EventManager instance;
    
    // Event storage
    private final List<Event> events = new ArrayList<>();
    
    // Command pattern - command history for undo
    private final List<Command> commandHistory = new ArrayList<>();
    
    // Private constructor for Singleton
    private EventManager() {}
    
    // Singleton getter
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
    
    // Add a new event
    public void addEvent(Event event) {
        events.add(event);
        commandHistory.add(new AddEventCommand(event));
    }
    
    // Get all events
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    // Find event by ID
    public Optional<Event> findEventById(String id) {
        return events.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
    
    // Search events by name
    public List<Event> searchEventsByName(String name) {
        return events.stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Search events by category
    public List<Event> searchEventsByCategory(String category) {
        return events.stream()
                .filter(e -> e.getCategories().stream()
                        .anyMatch(c -> c.toLowerCase().contains(category.toLowerCase())))
                .collect(Collectors.toList());
    }
    
    // Search events by tag
    public List<Event> searchEventsByTag(String tag) {
        return events.stream()
                .filter(e -> e.getTags().stream()
                        .anyMatch(t -> t.toLowerCase().contains(tag.toLowerCase())))
                .collect(Collectors.toList());
    }
    
    // Sort events by name (ascending or descending)
    public List<Event> sortEventsByName(boolean ascending) {
        List<Event> sortedEvents = new ArrayList<>(events);
        if (ascending) {
            sortedEvents.sort(Comparator.comparing(Event::getName));
        } else {
            sortedEvents.sort(Comparator.comparing(Event::getName).reversed());
        }
        return sortedEvents;
    }
    
    // Update event
    public boolean updateEvent(String eventId, Event updatedEvent) {
        Optional<Event> eventOptional = findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event originalEvent = eventOptional.get();
            Event oldState = cloneEvent(originalEvent);
            
            // Update the event properties
            originalEvent.setName(updatedEvent.getName());
            originalEvent.setLocation(updatedEvent.getLocation());
            originalEvent.setDateTime(updatedEvent.getDateTime());
            originalEvent.setOrganizer(updatedEvent.getOrganizer());
            originalEvent.setDescription(updatedEvent.getDescription());
            originalEvent.setCategories(updatedEvent.getCategories());
            originalEvent.setTags(updatedEvent.getTags());
            
            // Add command to history for undo
            commandHistory.add(new UpdateEventCommand(eventId, oldState, cloneEvent(originalEvent)));
            return true;
        }
        return false;
    }
    
    // Helper method to clone an event
    private Event cloneEvent(Event event) {
        Event clone = new Event(
                event.getName(),
                event.getLocation(),
                event.getDateTime(),
                event.getOrganizer(),
                event.getDescription()
        );
        clone.setCategories(event.getCategories());
        clone.setTags(event.getTags());
        return clone;
    }
    
    // Register user to event
    public boolean registerUserToEvent(String eventId, String userId) {
        Optional<Event> eventOptional = findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            boolean result = event.registerUser(userId);
            if (result) {
                commandHistory.add(new RegisterUserCommand(eventId, userId));
            }
            return result;
        }
        return false;
    }
    
    // Unregister user from event
    public boolean unregisterUserFromEvent(String eventId, String userId) {
        Optional<Event> eventOptional = findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            boolean result = event.unregisterUser(userId);
            if (result) {
                commandHistory.add(new UnregisterUserCommand(eventId, userId));
            }
            return result;
        }
        return false;
    }
    
    // Undo last action
    public boolean undoLastAction() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.remove(commandHistory.size() - 1);
            return lastCommand.undo(this);
        }
        return false;
    }
}