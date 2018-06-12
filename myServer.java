import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

class myServer
        
{
   public static void main(String args[]) throws Exception      
   
   {      
          List<Object> list = Collections.synchronizedList(new ArrayList<>()); //contains veh information and is shared by threads

          final int port = 9099;    

          DatagramSocket serverSocket = new DatagramSocket(port);       
          UDPHandler udp = new UDPHandler(serverSocket, list);
          Thread t1 = new Thread(udp);
          t1.start();   //start UDP 
   
          ServerSocket welcomeSocket = new ServerSocket(port);   //TCP
           
          while (true) 
        {
            Socket s = null;   //per connection
            
            try
            {
                s = welcomeSocket.accept();
                s.setSoTimeout(30000);			
                
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                
                Thread t2 = new ClientHandler(s, dis, dos, list); 
                t2.start();
                 
            }
            catch (IOException e){
                s.close();
            }
           }

    
         }
}





      
     


