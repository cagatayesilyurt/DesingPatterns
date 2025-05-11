package tr.eskisehir.edu.desingpatterns;

import java.util.List;
import java.util.stream.Collectors;

public class CategorySearchStrategy implements SearchStrategy {
    @Override
    public List<Event> search(List<Event> events, String query) {
        return events.stream()
                .filter(event -> event.getCategories().stream()
                        .anyMatch(category -> category.toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }
}