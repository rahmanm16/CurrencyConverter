module org.example.currencyconverter {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;


    opens org.example.currencyconverter to javafx.fxml;
    exports org.example.currencyconverter;
}