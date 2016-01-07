import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Service {
	//Object
	public WaveManager waveManager;
	private MulticastSocket sendingProcess;
	
	//Constructor
	public Service(WaveManager waveManager){
		this.waveManager=waveManager;
		try{
			sendingProcess = new MulticastSocket();
		}catch(Exception e){ }
	}
	
	//Class Methods
	public void sendMessage(String packetType, int messageID, String fromGroup, String toGroup, String data){
		try{
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(fromGroup);
			
			int hopCount = 0;
			
			//String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+waveManager.speed+"/"+waveManager.GPSlattitude+"/"+waveManager.GPSlongitude+"/"+data;
			
			/**Testing**/
			//General & Braking 
			String message = waveManager.CarID+"/"+messageID+"/"+fromGroup+"/"+hopCount+"/"+toGroup+"/"+waveManager.direction+"/"+60+"/"+45.3476235+"/"+-73.6597858+"/"+data;
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			sendingProcess.send(packet);
			
			String output = "-> Sent "+packetType+" message to "+fromGroup+": "+message;
			waveManager.userInterface.output(output);
			System.out.println(output);
		}catch(Exception e){ }
	}
	
	public double calculateDistance(double lat1, double lon1) {
		double theta = lon1 - waveManager.GPSlongitude;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(waveManager.GPSlattitude)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 1609.344;

		return (dist);
	}
	
	public double compareBearing(double lat1, double lon1) {
		double	dlonr = deg2rad(waveManager.GPSlongitude) - deg2rad(lon1);
		double	y = Math.sin(dlonr) * Math.cos(deg2rad(waveManager.GPSlattitude));
		double	x = Math.cos(deg2rad(lat1))*Math.sin(deg2rad(waveManager.GPSlattitude)) - Math.sin(deg2rad(lat1)) * Math.cos(deg2rad(waveManager.GPSlattitude))*Math.cos(dlonr);

		double	bearing = rad2deg(Math.atan2(y, x));

		System.out.println(""+bearing);
		return bearing;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	public boolean checkIfAhead(double lat1, double lon1) {
		double bearing = compareBearing(lat1, lon1);
		if(bearing<10 && bearing>150){		
			return true;
		}else{		
			return false;
		}
	}
	
	public boolean checkIfBehind(double lat1, double lon1) {
		double bearing = compareBearing(lat1, lon1);
		if(bearing>170 && bearing<190){		
			return true;
		}else{		
			return false;
		}
	}
}