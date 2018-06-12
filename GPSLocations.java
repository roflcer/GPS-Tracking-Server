import java.time.LocalTime;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;


/* 
GPSLocations class takes in vehicle ID, GPS location string, and is supposed to be able to edit vehicle list..
It can perform all conversions as requested, 24hr time not yet tested. 

*/

public class GPSLocations {
    
    String locstring; 

    String ident;
    String status;
    float latitude;
    float longitude;
    float speed;
    float heading;
    String datetime; 
    
    String gpsColNames[] = {"ID", "A/V","Time","Lat","Lon","Speed","Heading"};
    DefaultTableModel dtm = new DefaultTableModel(gpsColNames, 0);
    Object [] rowData = new Object[8];
    
    List<Object> list;

    GPSLocations(String vehID, String GPSloc, List<Object> list) {
        ident = vehID;
        locstring = GPSloc;         
        this.list = list;
    }
    
 //   public JSONObject splitter(){
     public List<Object> splitter(){

    String[] values = locstring.split(",");        //puts values of GPS location into an array 
    
    getStatus(values);  //reads status of vehicle
    lat2dec(values);    //converts latitude to dec
    long2dec(values);   //convert longitude to dec
    toMPH(values);      //converts knots to mph
    to24hr();           //converts current reading time to 24 HR format
    
     rowData[0]  =   ident;
     rowData[1]  =   status;
     rowData[3]  =   datetime;
     rowData[4]  =   latitude;
     rowData[5]  =   longitude;
     rowData[6]  =   speed;
     rowData[7]  =   heading;
     
    //  THIS RETURNS FINAL JSON MESSAGE TO SEND BACK AS RESPONSE !!!!!!! why wont it work
      list.add(dtm);
      
      return list;
    // return rowData;
    
    }
    
    public void getStatus(String[] values){  
        String AV = values[2];
        status = AV;
    }
    
    public void getHeading(String[] values){
        float direction = Float.parseFloat(values[8]);
        heading = direction;       //updates heading to float
    }
    
    public float lat2dec(String[] values){

        String lat = values[3];
        String NS = values[4];
	float med = Float.parseFloat(lat.substring(2))/60.0f;
	med -= Float.parseFloat(lat.substring(0, 2));
		if(NS.startsWith("S")) {
                        med = -med;
		}
                
        latitude = med;        //update latitude 
        
	return med;          
    }
    
    public float long2dec(String[] values) {
        String lon = values[5];
        String WE = values[6];
        
	float med = Float.parseFloat(lon.substring(3))/60.0f;
	med -= Float.parseFloat(lon.substring(0, 3));
        
	if(WE.startsWith("W")) {
            med = -med;
	}
        longitude = med;       //update longitude
        
        return med;
    }

    public float toMPH(String[] values){
        float a = (float) 1.152; //1 knot is 1.152 mph
        float b = Float.parseFloat(values[7]); //the amount of knots in data received
        
        float mph = a*b;
        speed = mph;                //update speed to mph  
        
        return mph;
    }
    
    public String to24hr(){
        LocalTime t = LocalTime.now();
       // DateTimeFormatter tf = DateTimeFormatter.ofPattern("m/d/y");
      //  datetime = t.format(tf);    //24HR m/d/y format of current date and time
        datetime = "idk why this wont work";
        return datetime;
    }
    
  /*  public JSONObject sendBack(DefaultTableModel dtm){
        
         JSONObject json1 = new JSONObject(dtm);
         
         return json1;
    }
*/
    
    
}
