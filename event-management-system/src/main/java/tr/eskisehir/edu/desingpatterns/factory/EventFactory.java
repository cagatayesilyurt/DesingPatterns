package tr.eskisehir.edu.desingpatterns;

import java.time.LocalDateTime;

public class EventFactory {
    // Factory method for creating different types of events
    public static Event createEvent(String type, String name, String location, 
                                   LocalDateTime dateTime, String organizer, 
                                   String description) {
        
        Event event = new Event(name, location, dateTime, organizer, description);
        
        // Set default category based on type
        event.addCategory(type);
        
        // Add specific tags based on event type
        switch (type.toLowerCase()) {
            case "concert":
                event.addTag("Music");
                break;
            case "seminar":
                event.addTag("Educational");
                break;
            case "workshop":
                event.addTag("Interactive");
                break;
            case "conference":
                event.addTag("Professional");
                break;
            case "exhibition":
                event.addTag("Art");
                break;
            default:
                event.addTag("General");
                break;
        }
        
        return event;
    }
}