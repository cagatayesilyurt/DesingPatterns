package tr.eskisehir.edu.desingpatterns;

public class AddEventCommand implements Command {
    private final Event event;
    
    public AddEventCommand(Event event) {
        this.event = event;
    }
    
    @Override
    public boolean undo(EventManager eventManager) {
        // Remove the added event
        eventManager.getAllEvents().removeIf(e -> e.getId().equals(event.getId()));
        return true;
    }
}