package tr.eskisehir.edu.desingpatterns;

import java.util.List;

public interface SearchStrategy {
    List<Event> search(List<Event> events, String query);
}