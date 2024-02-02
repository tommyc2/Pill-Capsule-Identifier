module com.tommycondon.ca1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tommycondon.ca1 to javafx.fxml;
    exports com.tommycondon.ca1;
}