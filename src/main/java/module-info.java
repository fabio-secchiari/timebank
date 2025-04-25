module org.fabiojava.timebank.timebank {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.fabiojava.timebank.timebank to javafx.fxml;
    exports org.fabiojava.timebank.timebank;
}