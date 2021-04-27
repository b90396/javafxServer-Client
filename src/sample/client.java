package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class client {

    public static void main(String[] args) throws IOException, ParseException
    {
        String IP = "127.0.0.1";
        int port = 12345; //Integer.parseInt(args[0]);

        String identity = null;

        boolean successfulInput;

        Socket socket = new Socket(IP, port);   //establishes connection with the sample.server, accept function is resolved when this happens
        
        Scanner socketInput = new Scanner(socket.getInputStream()); //for reading what has been sent to the socket from client

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));    // for reading in user input
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);     //for sending user input to the sample.server


        JSONParser jsonParser = new JSONParser();

        System.out.println("Session begun \n");
        
        while(true)
        {
            
        successfulInput = false;






        String userInputString = userInput.readLine();

        JSONObject userRequest = new JSONObject();

        if(identity == null)
        {
            if(userInputString.contains("open -"))
            {

                String[] splitInput = userInputString.split("-");
                identity = splitInput[1];

                userRequest.put("_class", "OpenRequest");
                userRequest.put("identity", identity);
                toServer.println(userRequest);

                successfulInput = true;

            }


            

        } else if(userInputString.contains("publish -"))
        {
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

            successfulInput = true;

        } else if(userInputString.contains("unsubscribe -"))
        {
            String[] splitInput = userInputString.split("-");

            userRequest.put("_class", "UnsubscribeRequest");
            userRequest.put("identity", identity);
            userRequest.put("channel", splitInput[1]);
            toServer.println(userRequest);

            successfulInput = true;
        } else if(userInputString.contains("subscribe -"))
        {
            String[] splitInput = userInputString.split("-");

            userRequest.put("_class", "SubscribeRequest");
            userRequest.put("identity", identity);
            userRequest.put("channel", splitInput[1]);
            toServer.println(userRequest);

            successfulInput = true;
        } else if(userInputString.contains("get"))
        {

            userRequest.put("_class", "GetRequest");
            userRequest.put("identity", identity);
            toServer.println(userRequest);
            
            successfulInput = true;
        }

        
        
        




            if(successfulInput == true)
            {
            // Reads number of response lines sent before every message in sample.client handler (and skips newline character)
			int n = socketInput.nextInt();
			socketInput.nextLine();

            JSONObject output = (JSONObject) jsonParser.parse(socketInput.nextLine());

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
            }


        }
    
    }
}
