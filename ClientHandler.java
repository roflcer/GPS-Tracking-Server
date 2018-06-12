import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/* 
This class handles each connection. It stops since my json returns null :( , but it can be persistent if you send back a simple string instead. 
Uses gson for parsing. 
*/

public class ClientHandler extends Thread {
    
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    
    final String validReq1 = "<AVL><vehicles>all</vehicles></AVL>";
    final String validReq2 = "<AVL><vehicles>active</vehicles></AVL>";
    
    List<Object> list;
    
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, List<Object> list)  { 
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        
            }
    
    @Override
    public void run() 
    {
        String received;
        String sendme;    
       //String trythis = sendme.toString();
        
        while (true) 
        {
            try{
 
             BufferedReader inFromClient = new BufferedReader(new InputStreamReader(dis)); 
             received = inFromClient.readLine();
             System.out.println("Received(TCP): " + received);
             DataOutputStream outToClient = new DataOutputStream(dos);
             
             //TRY TO LOAD THE PAYLOAD WITH LIST DATA
             Gson gson = new Gson();
             String jsonPayload = gson.toJson(list); 
             
             
                if(received.equals(validReq1)||received.equals(validReq2)){        
                    //  sendme = received.toUpperCase() + '\n';         // ------> You can use this if you want to see a persistent connection
                      //outToClient.writeBytes(sendme);
                      
                      System.out.println("Trying to send json.." +jsonPayload);
                      outToClient.writeBytes(jsonPayload);
                      
               /*     switch(received){
                        
                        case validReq1: //return JSON for all vehicles(V) break;

                        outToClient.writeBytes(list.toString());
                        break;
                         
                        case validReq2: //return JSON for all vehicles flagged active(A) break;
                        outToClient.writeBytes(trythis);
                        break;
      
                        default: 
                            System.out.println("FFFFUUUUU!!1! WHY GOD");
                         }
                    */
             } 
                else
                    System.out.println(received + "is not valid sry."); //discards invalid requests

           }
            //TOCHANGE
            catch (IOException ex) {
                            Logger.getLogger(UDPHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
        }
    }

}

