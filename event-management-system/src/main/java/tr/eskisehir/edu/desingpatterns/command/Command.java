package tr.eskisehir.edu.desingpatterns;

public interface Command {
    boolean undo(EventManager eventManager);
}