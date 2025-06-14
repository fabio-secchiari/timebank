package org.fabiojava.timebank.gui.components;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class RatingEvent extends Event {
    public static final EventType<RatingEvent> RATING_CHANGED =
            new EventType<>(Event.ANY, "RATING_CHANGED");

    private final int rating;

    public RatingEvent(EventType<? extends Event> eventType, int rating) {
        super(eventType);
        this.rating = rating;
    }

}