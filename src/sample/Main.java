package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends Application {

    Stage window; //stage = window
    Scene LogIn, Home; //scene = inside of window
    String name;

    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));



    public static void main(String[] args) {

        launch(args); //launches java application and calls start function
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        window = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        //LOGIN SCENE

        TextField usernameInput = new TextField();

        Button button1 = new Button("Begin account"); //create button and set text
        button1.setOnAction(e -> {
            name = usernameInput.getText();
            window.setScene(Home);


        }); // sets what button click does



        VBox layout1 = new VBox(20);//create layout to display objects (like buttons, text boxes etc)
        layout1.getChildren().addAll(usernameInput, button1);
        LogIn = new Scene(layout1, 300, 300); //displaying the layout in a scene


        //HOME SCENE

        Button button2 = new Button("Back"); //create button and set text
        button2.setOnAction(e -> window.setScene(LogIn)); // sets what button click does

        Button button3 = new Button("get pop-up");
        button3.setOnAction(e -> popUp.box("pop-up", "This is a pop-up"));

        String[] argsForServer = new String[0];

        Button button4 = new Button("Start instance of client");
        button4.setOnAction(e -> {
            assignment3Client.main(argsForServer);
        });

        VBox layout2 = new VBox(20);//create layout to display objects (like buttons, text boxes etc)
        layout2.getChildren().addAll(button2, button3, button4);
        Home = new Scene(layout2, 300, 350); //displaying the layout in a scene

        window.setTitle("NSD Assignment 3");
        window.setScene(LogIn); //telling what scene to use
        window.show();          //displays it to the user
    }

}
