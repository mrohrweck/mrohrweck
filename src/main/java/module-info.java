module com.example.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    exports snake;
    exports snake.Controller;
    opens snake.Controller to javafx.fxml;

}