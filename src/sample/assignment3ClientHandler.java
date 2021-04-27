package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class assignment3ClientHandler extends Thread {

    private Socket client;
    private PrintWriter out; // for sending info to sample.client
    private BufferedReader in; // for receiving info from the sample.client

    private int messagesRead = 0; // counter so program only prints messages that haven't been printed before

    private static Vector<JSONObject> messageBoard = new Vector<JSONObject>(); // message board shared across all
                                                                               // clients

    private Vector<String> subscribedList = new Vector<String>(); // list for storing subscriptions

    FileWriter messageLogWriter = new FileWriter("messageBoardLog.txt", true);

    JSONParser jsonParser = new JSONParser();

    public assignment3ClientHandler(Socket clientSocket) throws IOException // constructor for this class - accepts sample.client socket
                                                                 // as input
    {
        client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);

    }

    @Override
    public void run() // runs when calling class
    {

        try {
            while (true) {

                String clientInput = in.readLine(); //waits to read line from client
                JSONObject clientInputJson = (JSONObject) jsonParser.parse(clientInput);

                
                if (clientInput.contains("OpenRequest")) 
                {
                        JSONObject response = new JSONObject();
                        response.put("_class", "SuccessResponse");
                        subscribedList.add(clientInputJson.get("identity").toString());
                        out.println(1);
                        out.println(response);
                } 
                else if (clientInput.contains("GetRequest")) {

                    boolean foundMessage = false;

                    JSONObject response = new JSONObject();
                    response.put("_class", "MessageListResponse");

                    Vector<JSONObject> messages = new Vector<JSONObject>();

                    for (int i = messagesRead; i < messageBoard.size(); i++) 
                    {
                        
                        if (subscribedList.contains(messageBoard.get(i).get("identity"))) 
                        {
                            foundMessage = true;
                            messages.add(messageBoard.get(i));
                            
                        }

                        

                    }
                    if (foundMessage == false) {
                        response = new JSONObject();
                        response.put("_class", "ErrorResponse");
                        response.put("Body", "no new messages");
                        out.println(1);
                        out.println(response);

                    }
                    else
                    {
                        response.put("messages", messages);

                        out.println(1);
                        out.println(response);
                    }

                    messagesRead = messageBoard.size();

                } else if (clientInput.contains("PublishRequest")) {


                    messageBoard.add(clientInputJson);

                    messageLogWriter.write(clientInputJson.toJSONString() + "\n");
                    messageLogWriter.flush();

                    JSONObject response = new JSONObject();
                    response.put("_class", "SuccessResponse");

                    out.println(1);
                    out.println(response);

                } else if (clientInput.contains("SubscribeRequest") && !clientInput.contains("UnsubscribeRequest")) {

                    boolean foundUser = false;

                    for (int i = 0; i < messageBoard.size(); i++) {
                        String tempUser = messageBoard.get(i).get("identity").toString();

                        if (clientInputJson.get("channel").toString().contains(tempUser)) {

                            subscribedList.add(tempUser);
                            JSONObject response = new JSONObject();
                            response.put("_class", "SuccessResponse");
                            out.println(1);
                            out.println(response);
                            foundUser = true;
                            break;

                        }
                    }
                    if (foundUser == false) {
                        JSONObject response = new JSONObject();
                        response.put("_class", "ErrorResponse");
                        response.put("Body", "Could not find user " + clientInputJson.get("channel").toString());
                        out.println(1);
                        out.println(response);
                    }

                } else if (clientInput.contains("UnsubscribeRequest")) {
                    boolean userFound = false;
                    

                    for (int i = 0; i < subscribedList.size(); i++) {
                        if (subscribedList.get(i).contains(clientInputJson.get("channel").toString())) {
                            JSONObject response = new JSONObject();
                            response.put("_class", "SuccessResponse");
                            out.println(1);
                            out.println(response);
                            subscribedList.remove(i);
                            userFound = true;

                            break;
                        }
                    }

                    if (userFound == false) {
                        JSONObject response = new JSONObject();
                        response.put("_class", "ErrorResponse");
                        response.put("Body", "Could not find user " + clientInputJson.get("channel").toString());
                        out.println(1);
                        out.println(response);

                    }

                } else if (clientInput.contains("quit")) {
                    JSONObject response = new JSONObject();
                    response.put("_class", "ErrorResponse");
                    response.put("Body", "Disconnected from Server");
                    out.println(1);
                    out.println(response);
                    break;

                } else {
                    JSONObject response = new JSONObject();
                    response.put("_class", "ErrorResponse");
                    response.put("Body", clientInput);
                    out.println(1);
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.err.println("sample.client disconected - sample.clientHandler ln66");
        } catch (ParseException e) {
            System.out.println("could not parse json");
        }
        finally
        {
            try 
            {
                in.close();
            } catch (IOException e) 
            {
                System.out.println("error when terminating sample.clientHandler");
            }

            out.close();

        }

    }



    
}
