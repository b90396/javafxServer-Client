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

public class testClient {

    public static void main(String[] args) throws IOException, ParseException
    {
        String IP = "127.0.0.1";
        int port = 12345; //Integer.parseInt(args[0]);   

        Socket socket = new Socket(IP, port);   //establishes connection with the server, accept function is resolved when this happens
        
        Scanner socketInput = new Scanner(socket.getInputStream()); //for reading what has been sent to the socket from client

        //BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));    // for reading in user input
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);     //for sending user input to the server


        JSONParser jsonParser = new JSONParser();

        System.out.println("Session begun \n");
        
        for(int j = 0; j < 1001; j++) //sends an open request and then 1000 messages
        {
            JSONObject userRequest = new JSONObject();

            if(j == 0)
            {
                //toServer.println("open - test1");
                userRequest.put("_class", "OpenRequest");
                userRequest.put("identity", "testClient");
                toServer.println(userRequest);
            }
            else
            {
                //toServer.println("publish - message " + j);
                JSONObject message = new JSONObject();
                message.put("_class", "Message");
                message.put("from", "testClient");
                message.put("when", (new Date()).toString());
                message.put("body", "message " + j);

                userRequest.put("_class", "PublishRequest");
                userRequest.put("identity", "testClient");
                userRequest.put("message", message);
                toServer.println(userRequest);

            }

            


            // Reads number of response lines sent before every message in client handler (and skips newline character)
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

                    for(int k=0; k<messages.size(); k++)
                    {

                        JSONObject tempMessage = (JSONObject) messages.get(k);

                        JSONObject tempMessage2 = (JSONObject) tempMessage.get("message"); 

                        System.out.println(tempMessage2.get("from") + " wrote: '" + tempMessage2.get("body") + "' at " + tempMessage2.get("when"));   
                         
                                                                

                        //System.out.println(output.get("From").toString() + " sent: '" + output.get("Body").toString() + "' on " + output.get("When").toString());
                
                    }

                }


            }
            else if(output.containsValue("ErrorResponse"))
            {
                for (int i = 0; i < n; i++)
                {

                    System.out.println(output.get("Body"));
                
                }

            }else
            {
                System.out.println("Please ensure you have opened a channel with 'open -' so you can then enter one of the following commands to interact with the server\n1.publish -\n2.subscribe -\n3.unsubscribe -\n4.get\n\nIf you wish to open another channel, please run another instance of the client program to do this");
            }
            
        

        }
        

        //socket.close();

    }


}
    

