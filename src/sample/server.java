package sample;

import java.net.ServerSocket;
import java.net.Socket;



public class server{


    public static void main(String[] args) throws Exception 
    {
        int portNumber = 12345;            //sets port number

        ServerSocket serverListener = new ServerSocket(portNumber);     //creates SERVER SOCKET that listens to a port

        try
        {
            

            while(true) // loop makes sure sample.server is constantly listening for connections
            {
                System.out.println("Connecting to client...");

                Socket client = serverListener.accept();                       //waits for sample.client to connect and assigns it to this socket

                System.out.println("Connected to client");


                Thread thread = new Thread();
                thread = new clientHandler(client);
                thread.start();


            }
        }
        finally 
        {
            serverListener.close();
            System.out.println("Server Closed");
        }

    }
}
