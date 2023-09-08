module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.test to javafx.fxml;
    exports com.example.test;
    exports com.example.test.controllers;
    opens com.example.test.controllers to javafx.fxml;
}