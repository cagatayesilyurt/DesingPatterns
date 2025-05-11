package tr.eskisehir.edu.desingpatterns;

import java.util.Optional;

public class UpdateEventCommand implements Command {
    private final String eventId;
    private final Event oldState;
    private final Event newState;
    
    public UpdateEventCommand(String eventId, Event oldState, Event newState) {
        this.eventId = eventId;
        this.oldState = oldState;
        this.newState = newState;
    }
    
    @Override
    public boolean undo(EventManager eventManager) {
        // Restore the event to its previous state
        Optional<Event> eventOptional = eventManager.findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.setName(oldState.getName());
            event.setLocation(oldState.getLocation());
            event.setDateTime(oldState.getDateTime());
            event.setOrganizer(oldState.getOrganizer());
            event.setDescription(oldState.getDescription());
            event.setCategories(oldState.getCategories());
            event.setTags(oldState.getTags());
            return true;
        }
        return false;
    }
}