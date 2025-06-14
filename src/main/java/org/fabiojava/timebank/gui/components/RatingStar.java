package org.fabiojava.timebank.gui.components;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

public class RatingStar extends Label {
    private boolean selected;
    @Getter
    private final int position;

    public RatingStar(int position) {
        this.position = position;
        this.selected = false;
        updateDisplay();

        // Stile di base
        getStyleClass().add("rating-star");

        // Eventi mouse
        setOnMouseEntered(this::handleMouseEntered);
        setOnMouseExited(this::handleMouseExited);
        setOnMouseClicked(this::handleMouseClicked);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateDisplay();
    }

    private void updateDisplay() {
        setText(selected ? "★" : "☆");
    }

    private void handleMouseEntered(MouseEvent event) {
        if (!selected) {
            setText("★");
            getStyleClass().add("star-hover");
        }
    }

    private void handleMouseExited(MouseEvent event) {
        if (!selected) {
            setText("☆");
            getStyleClass().remove("star-hover");
        }
    }

    private void handleMouseClicked(MouseEvent event) {
        fireEvent(new RatingEvent(RatingEvent.RATING_CHANGED, position));
    }
}