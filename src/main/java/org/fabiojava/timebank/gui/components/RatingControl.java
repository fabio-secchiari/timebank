package org.fabiojava.timebank.gui.components;

import javafx.scene.layout.HBox;
import lombok.Getter;

public class RatingControl extends HBox {
    private final RatingStar[] stars;
    @Getter
    private int rating;

    public RatingControl() {
        stars = new RatingStar[5];
        getStyleClass().add("rating-control");
        setSpacing(5);

        for (int i = 0; i < 5; i++) {
            final RatingStar star = new RatingStar(i + 1);
            stars[i] = star;
            getChildren().add(star);

            star.addEventHandler(RatingEvent.RATING_CHANGED, event -> setRating(event.getRating()));
        }
    }

    public void setRating(int newRating) {
        rating = newRating;
        for (int i = 0; i < stars.length; i++) {
            stars[i].setSelected(i < newRating);
        }
    }

}