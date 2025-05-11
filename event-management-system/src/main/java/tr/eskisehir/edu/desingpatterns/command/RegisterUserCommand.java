package tr.eskisehir.edu.desingpatterns;

import java.util.Optional;

public class RegisterUserCommand implements Command {
    private final String eventId;
    private final String userId;
    
    public RegisterUserCommand(String eventId, String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }
    
    @Override
    public boolean undo(EventManager eventManager) {
        // Unregister the user from the event
        Optional<Event> eventOptional = eventManager.findEventById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.unregisterUser(userId);
        }
        return false;
    }
}