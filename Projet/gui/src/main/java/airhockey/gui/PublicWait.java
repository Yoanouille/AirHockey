package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class PublicWait extends BorderPane {

    private Label info;


    public PublicWait(MenuClient menu){
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        info = new Label("Connexion to the server");
        info.setPrefHeight(100);
        info.setStyle("-fx-font : 28 Ubuntu;");
        info.setTextFill(WHITE);        this.setCenter(info);

        Button back = new Button("Back");
        this.setAlignment(back,Pos.BASELINE_RIGHT);
        this.setBottom(back);



        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);
        back.setStyle("-fx-background-color: #565656;");
        back.setTextFill(WHITE);

        back.setOnMouseEntered((action)->{
            back.setStyle("-fx-background-color: #FFFFFF;");
            back.setTextFill(BLACK);
        });

        back.setOnMouseExited((action)->{
            back.setStyle("-fx-background-color: #565656;");
            back.setTextFill(WHITE);
        });

        back.setOnAction(value -> {
            menu.setScene("first");
            menu.closeClient();
        });

        this.setPadding(new Insets(30,50,50,50));

    }
    public void connected(){
        info.setText("Connected to the server, waiting for an other player");
    }

}
