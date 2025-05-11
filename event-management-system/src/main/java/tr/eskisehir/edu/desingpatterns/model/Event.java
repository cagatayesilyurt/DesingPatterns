package tr.eskisehir.edu.desingpatterns;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Event {
    private String id;
    private String name;
    private String location;
    private LocalDateTime dateTime;
    private String organizer;
    private String description;
    private Set<String> categories = new HashSet<>();
    private Set<String> tags = new HashSet<>();
    private List<String> registeredUsers = new ArrayList<>();
    
    public Event(String name, String location, LocalDateTime dateTime, String organizer, String description) {
        this.id = generateId();
        this.name = name;
        this.location = location;
        this.dateTime = dateTime;
        this.organizer = organizer;
        this.description = description;
    }
    
    private String generateId() {
        return System.currentTimeMillis() + "-" + Math.round(Math.random() * 1000);
    }
    
    // Add category (max 3)
    public boolean addCategory(String category) {
        if (categories.size() < 3) {
            return categories.add(category);
        }
        return false;
    }
    
    // Add tag (max 3)
    public boolean addTag(String tag) {
        if (tags.size() < 3) {
            return tags.add(tag);
        }
        return false;
    }
    
    // Registration methods
    public boolean registerUser(String userId) {
        return registeredUsers.add(userId);
    }
    
    public boolean unregisterUser(String userId) {
        return registeredUsers.remove(userId);
    }
    
    public int getRegistrationCount() {
        return registeredUsers.size();
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public String getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<String> getCategories() {
        return new HashSet<>(categories);
    }
    
    public void setCategories(Set<String> categories) {
        if (categories.size() <= 3) {
            this.categories = new HashSet<>(categories);
        }
    }
    
    public Set<String> getTags() {
        return new HashSet<>(tags);
    }
    
    public void setTags(Set<String> tags) {
        if (tags.size() <= 3) {
            this.tags = new HashSet<>(tags);
        }
    }
    
    public List<String> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers);
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", dateTime=" + dateTime +
                ", organizer='" + organizer + '\'' +
                ", categories=" + categories +
                ", tags=" + tags +
                ", registeredUsers=" + registeredUsers.size() +
                '}';
    }
}