module ru.nsu.gorin.networks.lab3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp;
    requires com.google.gson;


    opens ru.nsu.gorin.networks.lab3 to javafx.fxml;
    exports ru.nsu.gorin.networks.lab3;
}