package tr.eskisehir.edu.desingpatterns;

import java.util.Optional;

public class UnregisterUserCommand implements Command {
    private final String eventId;
    private final String userId;
    
    public UnregisterUserCommand(String eventId, String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }
    
    @Override
    public boolean undo(EventManager eventManager) {
        // Re-register the user to the event
        Optional<Event> eventOptional = eventManager.findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.registerUser(userId);
        }
        return false;
    }
}