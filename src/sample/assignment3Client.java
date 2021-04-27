package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

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

public class assignment3Client extends Application {

    Stage window; //stage = window
    Scene LogIn, Home; //scene = inside of window
    String textInput;

    String identity = null;
    boolean successfulInput;

    public static void main(String[] args) {

        launch(args); //launches java application and calls start function
    }

    public void start(Stage primaryStage) throws IOException, ParseException
    {
        String IP = "127.0.0.1";
        int port = 12345;   //Integer.parseInt(args[0]);





        Socket socket = new Socket(IP, port);   //establishes connection with the server, accept function is resolved when this happens
        
        Scanner socketInput = new Scanner(socket.getInputStream()); //for reading what has been sent to the socket from clientHandler?

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));    // for reading in user input
        //BufferedReader inputFromGui = new BufferedReader()
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);     //for sending user input to the sample.server



        JSONParser jsonParser = new JSONParser();

        System.out.println("Session begun \n");
        
        //while(true)
        //{
            


            //INPUT MENU

            window = primaryStage;

            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


            //INPUT SCENE

            TextField usernameInput = new TextField();

            Button button1 = new Button("Set Command"); //create button and set text
            button1.setOnAction(e -> {
                textInput = usernameInput.getText();



            }); // sets what button click does

                Button button2 = new Button("Login");
                button2.setOnAction(e -> {

                    //THE ELSE IFS CAN BE REPLACED WITH INDIVIDUAL BUTTON FUNCTIONS AND SETTING
                    // THE TEXT INPUT VARIABLE

                    successfulInput = false;

                    textInput = usernameInput.getText();

                    String userInputString = "open - " + textInput; //needs to be changed to get input from a button/text field

                    JSONObject userRequest = new JSONObject();


                    String[] splitInput = userInputString.split("-");
                    identity = splitInput[1];

                    userRequest.put("_class", "OpenRequest");
                    userRequest.put("identity", identity);
                    toServer.println(userRequest);

                    successfulInput = true;

                    // Reads number of response lines sent before every message in sample.client handler (and skips newline character)
                    int n = socketInput.nextInt();
                    socketInput.nextLine();

                    JSONObject output = null;
                    try {
                        output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    if(output.containsValue("SuccessResponse"))
                        {
                            System.out.println("Success");
                        }else
                        {
                            System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the sample.server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the sample.client program to do this");
                        }
                });

                Button unsubscribeToUserButton = new Button("unsubscribe");   //will have to open pop-up window
                unsubscribeToUserButton.setOnAction(e -> {

                    textInput = usernameInput.getText();

                    String userInputString = "unsubscribe -" + textInput; //needs to be changed to get input from a button/text field

                    JSONObject userRequest = new JSONObject();

                        String[] splitInput = userInputString.split("-");

                        userRequest.put("_class", "UnsubscribeRequest");
                        userRequest.put("identity", identity);
                        userRequest.put("channel", splitInput[1]);
                        toServer.println(userRequest);

                        successfulInput = true;

                    // Reads number of response lines sent before every message in sample.client handler (and skips newline character)
                    int n = socketInput.nextInt();
                    socketInput.nextLine();

                    JSONObject output = null;
                    try {
                        output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    if(output.containsValue("SuccessResponse"))
                    {
                        System.out.println("Success");
                    }
                    else if(output.containsValue("ErrorResponse"))
                    {
                        for (int i = 0; i < n; i++)
                        {
                            System.out.println(output.get("Body"));
                        }
                    }
                    else
                    {
                        System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the sample.server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the sample.client program to do this");
                    }


                });
                Button subscribeToUserButton = new Button("subscribe"); //will have to open pop-up window/use a drop down
                subscribeToUserButton.setOnAction(e ->{

                    textInput = usernameInput.getText();

                    String userInputString = "subscribe -" + textInput; //needs to be changed to get input from a button/text field

                    JSONObject userRequest = new JSONObject();

                    String[] splitInput = userInputString.split("-");

                    userRequest.put("_class", "SubscribeRequest");
                    userRequest.put("identity", identity);
                    userRequest.put("channel", splitInput[1]);
                    toServer.println(userRequest);

                    successfulInput = true;

                    // Reads number of response lines sent before every message in sample.client handler (and skips newline character)
                    int n = socketInput.nextInt();
                    socketInput.nextLine();

                    JSONObject output = null;
                    try {
                        output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    if(output.containsValue("SuccessResponse"))
                    {
                        System.out.println("Success");
                    }
                    else if(output.containsValue("ErrorResponse"))
                    {
                        for (int i = 0; i < n; i++)
                        {
                            System.out.println(output.get("Body"));
                        }
                    }
                    else
                    {
                        System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the sample.server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the sample.client program to do this");
                    }
                });

                Button getMessagesButton = new Button("Get");
                getMessagesButton.setOnAction(e -> {

                    JSONObject userRequest = new JSONObject();

                    textInput = usernameInput.getText();

                    String userInputString = "get" + textInput;

                    userRequest.put("_class", "GetRequest");
                    userRequest.put("identity", identity);
                    toServer.println(userRequest);

                    successfulInput = true;

                    // Reads number of response lines sent before every message in sample.client handler (and skips newline character)
                    int n = socketInput.nextInt();
                    socketInput.nextLine();

                    JSONObject output = null;
                    try {
                        output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    if(output.containsValue("MessageListResponse"))
                    {
                        for (int i = 0; i < n; i++)
                        {
                            JSONArray messages = (JSONArray) output.get("messages");

                            for(int j=0; j<messages.size(); j++)
                            {

                                JSONObject tempMessage = (JSONObject) messages.get(j);

                                JSONObject tempMessage2 = (JSONObject) tempMessage.get("message");

                                System.out.println(tempMessage2.get("from") + " wrote: '" + tempMessage2.get("body") + "' at " + tempMessage2.get("when")); //SEND THIS TO GUI INSTEAD OF/AS WELL AS SYS.OUT
                            }
                        }
                    }
                });

                /*if(successfulInput == true)
                {
                    // Reads number of response lines sent before every message in client handler (and skips newline character)
                    int n = socketInput.nextInt();
                    socketInput.nextLine();

                    JSONObject output = null;
                    try {
                        output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    if(output.containsValue("SuccessResponse"))
                    {
                        System.out.println("Success");
                    }
                    else if(output.containsValue("MessageListResponse"))
                    {
                        for (int i = 0; i < n; i++)
                        {
                            JSONArray messages = (JSONArray) output.get("messages");

                            for(int j=0; j<messages.size(); j++)
                            {
                                JSONObject tempMessage = (JSONObject) messages.get(j);

                                JSONObject tempMessage2 = (JSONObject) tempMessage.get("message");

                                System.out.println(tempMessage2.get("from") + " wrote: '" + tempMessage2.get("body") + "' at " + tempMessage2.get("when"));
                            }
                        }
                    }
                    else if(output.containsValue("ErrorResponse"))
                    {
                        for (int i = 0; i < n; i++)
                        {
                            System.out.println(output.get("Body"));
                        }
                    }
                }
                else
                {
                    System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the sample.server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the sample.client program to do this");
                }*/


            Button publishMessageButton = new Button("send message");   //BUTTON FOR SENDING MESSAGES - gets text from the input field
            publishMessageButton.setOnAction(e ->
            {
                textInput = usernameInput.getText();

                JSONObject userRequest = new JSONObject();
                String userInputString = "publish -" + textInput;

                String[] splitInput = userInputString.split("-");

                JSONObject message = new JSONObject();
                message.put("_class", "Message");
                message.put("from", identity);
                message.put("when", (new Date()).toString());
                message.put("body", splitInput[1]);

                userRequest.put("_class", "PublishRequest");
                userRequest.put("identity", identity);
                userRequest.put("message", message);
                toServer.println(userRequest);

                        //MAY NOT NEED BELOW AS USER HAS TO INTERACT VIA BUTTONS
                        //SHOULD NOT GET AN ERROR MESSAGE, ONLY SUCCESS

                //successfulInput = true;

                // Reads number of response lines sent before every message in client handler (and skips newline character)
                int n = socketInput.nextInt();
                socketInput.nextLine();

                JSONObject output = null;
                try {
                    output = (JSONObject) jsonParser.parse(socketInput.nextLine());
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                if(output.containsValue("SuccessResponse"))
                {
                    System.out.println("Success");
                }
                else if(output.containsValue("ErrorResponse"))
                {
                    for (int i = 0; i < n; i++)
                    {

                        System.out.println(output.get("Body"));

                    }

                }
                else
                {
                    System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the sample.server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the sample.client program to do this");
                }
            });


            VBox layout1 = new VBox(20);//create layout to display objects (like buttons, text boxes etc)
            layout1.getChildren().addAll(usernameInput, button1, button2, publishMessageButton, getMessagesButton, subscribeToUserButton, unsubscribeToUserButton);
            LogIn = new Scene(layout1, 300, 300); //displaying the layout in a scene

            window.setTitle("NSD Assignment 3");
            window.setScene(LogIn); //telling what scene to use
            window.show();          //displays it to the user






        //}
    
    }
}
