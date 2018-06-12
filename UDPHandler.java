import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/*
This handles UDP conenctions and drops those that are invalid. When uncommented,
allveh can maintain a Map of each vehicle and its most recent GPS location.

*/

public class UDPHandler extends myServer implements Runnable{
    
    DatagramSocket serverSocket;
    byte[] receiveData = new byte[100];  
   // Map original = new Hashtable(); //object will be GPS location + time stamp
    
    List<Object> list;

    UDPHandler(DatagramSocket serverSocket, List<Object> list) {
        this.serverSocket = serverSocket;
        this.list = list;
    }

    @Override
    public void run(){    
                 while(true)
               {
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        try {
                            serverSocket.receive(receivePacket);
                        } catch (IOException ex) {
                            Logger.getLogger(UDPHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                  String sentence = new String( receivePacket.getData());       
                                 
                  System.out.println("RECEIVED(UDP): " + sentence);

                  InetAddress IPAddress = receivePacket.getAddress();
                  
                  int port = receivePacket.getPort();
                             
                  for(int i = 0, n = sentence.length() ; i < n ; i++) { 

                            if(sentence.charAt(i)=='*') {

                                String checkme = sentence.substring(1, i); 
                                String smithcheck = sentence.substring(i+1, sentence.length());

                                boolean isvalid = checkSum(checkme, smithcheck);
                                
                                    if(!isvalid)                
                                      System.out.println("Your packet had an invalid checksum and was dropped");                  
                                    else                                  
                                        try {                                                                                     
                                            Reply(sentence, receivePacket, IPAddress, port, serverSocket);
                                      //      original = allveh(checkme, original); //pass string, original map (vehID, most recent GPS loc)
                                     //     System.out.println("list:" + original);     //this is maintaining vehicles with most recent GPS locations  
                                     
                                            int sen = checkme.length()-1;
                                            sen = sen-2;

                                            String vehID = checkme.substring(sen, sen+3);   
                                            String GPSloc = checkme.substring(0,sen-1);
                                            
                                            GPSLocations gpslocs = new GPSLocations(vehID, GPSloc, list);
                                            list = gpslocs.splitter();
                                      
                                } catch (IOException ex) {
                                    Logger.getLogger(UDPHandler.class.getName()).log(Level.SEVERE, null, ex);
                                } 
                         }                                                                         
        }

    }
    

    }
    public static boolean checkSum(String checkme, String smithcheck) {
        //his sends invalid even when its valid sometimes, ignore.
        
        int checks = 0;
        String mychecksum;
        boolean isvalid = false;

            for (int c = 0; c < checkme.length(); c++) {
                
                checks ^= checkme.charAt(c);
                mychecksum = Integer.toHexString(checks);
                
                isvalid = mychecksum.trim().equalsIgnoreCase(smithcheck.trim());    
             }        
           
            return isvalid;
         }  
             
        public static void Reply(String sentence, DatagramPacket receivePacket, InetAddress IPAddress, 
                                               int port, DatagramSocket serverSocket) throws IOException{     
               
            byte[] sendData = new byte[1024];
        
            String capitalizedSentence = sentence.toUpperCase();         
            sendData = capitalizedSentence.getBytes();
                 
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
                                                                IPAddress, port);              
            serverSocket.send(sendPacket);
                    
        }
        
        
        //THIS MAP CAN MAINTAIN VEHICLE ID + MOST RECENT GPS LOCATION
        
        /*
        
       public Map allveh(String checkme, Map original){   
           
            Map tempAll = new Hashtable();     
            tempAll = original;                       
            
            int sen = checkme.length()-1;
            sen = sen-2;

            String vehID = checkme.substring(sen, sen+3);   
            String GPSloc = checkme.substring(0,sen-1); 

            if(tempAll.containsKey(vehID)){
                
               tempAll.replace(vehID,GPSloc);   

            }
            else{
                
               tempAll.put(vehID,GPSloc); 
            } 
             
           // GPSLocations g = new GPSLocations(vehID, GPSloc, list); 
           //  g.splitter();
           
           //  list = g.splitter();
                              
             return tempAll; //returns updated veh(keys) and GPS locations + time stamps (values)
            }              

*/

  
       
        }         


            
        
        
        
  
    

