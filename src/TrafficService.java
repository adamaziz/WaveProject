import java.text.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrafficService extends Service implements Runnable {
	//Class Variables
	private Thread trafficServiceThread;
	
	//Resources
	public int delay;
	public int messageID = 0;
	public String serviceGroup = "230.0.0.5";
	private String output;
	
	//Constructor
	public TrafficService(WaveManager waveManager){
		super(waveManager);
		delay = waveManager.delay;
	}
	
	//Class Methods
	public void start(){
		if(trafficServiceThread==null){
			trafficServiceThread = new Thread(this, "TrafficService");
			trafficServiceThread.start();
		}
	}
	
	public void sendControlMessage(){
		sendMessage(waveManager.controlGroup, serviceGroup, messageID, "");
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}
	
	public void sendServiceMessage(){
		sendMessage(serviceGroup, serviceGroup, messageID, ""+waveManager.trafficLevel);
		messageID++;
		waveManager.userInterface.updateTrafficServicePacketsSent(messageID);
	}
		
	public void run(){
		while(true){
			delay = waveManager.delay;
			System.out.println(""+waveManager.inTraffic);
			if(waveManager.inTraffic = true){
				if(checkTraffic()){
					
					sendControlMessage();
					//Wait 
					try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
					
					int count = 0;
					while(count<5){
						sendServiceMessage();
	
						delay = waveManager.delay;
						//Wait
						try{ TimeUnit.MILLISECONDS.sleep(delay); } catch(Exception e){ }
						count++;
					}
				}
			}
		}
	}

	public boolean checkTraffic(){
		if(waveManager.trafficLevel>2){			
			return true;
		}
		return false;
	}
	
	//TRAFFIC ROUTING ALOGORITHM*/
	
	public void computeData(List<VehicleInfo> vehicles){	
		
		String[] trafficWords = {"Low", "Limited", "Moderate","Mild", "Heavy", "Severe"};
		String[] directionWords = {"N","NNE", "NE", "NEE", "E", "SEE", "SE", "SSE", "S","SSW", "SW", "SWW","W", "NWW", "NW", "NNW"};
		int direction = waveManager.bearing;
		int speed = waveManager.speed;
		double speedDiff = 0;
		int trafficLevel = 0;
		String userMessage = "";
		
		int numVehicles = vehicles.size();
		int[] dir = new int[2];
		int[] dirPrv = new int[2];
		int[] spd = new int[2];
		
		String[] dirString = getAvgDir(vehicles, numVehicles).split("/"); 
		dir[0] = Integer.parseInt(dirString[0]);
		dir[1] = Integer.parseInt(dirString[1]);
		dirPrv[0] = Integer.parseInt(dirString[2]);			
		dirPrv[1] = Integer.parseInt(dirString[3]);
	
		String[] spdString = getAvgSpd(vehicles, numVehicles, dir).split("/");
		spd[0] = Integer.parseInt(spdString[0]);
		spd[1] = Integer.parseInt(spdString[1]);
		
		for(int out = 0; out<2; out++){
			System.out.println("o Calculated: There are " + dirPrv[out] + " vehicles travelling at " +spd[out]+ "km/h in the " + directionWords[dir[out]] + " direction");
			output = "o Calculated: There are " + dirPrv[out] + " vehicles travelling at " +spd[out]+ "km/h in the " + directionWords[dir[out]] + " direction";
			waveManager.userInterface.computedTrafficInfo(output);
		}
		
		//Approximate this vehicles direction
		
		if(direction != dir[0] && direction != dir[1]){
			if(direction-dir[1] > direction-dir[0]){direction = 0;
			}else{
				direction = 1;
				}
		}
		
		speedDiff = speedDifference(speed, spd[direction]);
	
		//ALGORITHM
		
		if(speed < spd[direction]){
				trafficLevel = 0;	
		}else{	

		if(dirPrv[direction]>9){
			
			if(speedDiff > 40){
				trafficLevel = 5;
			}else if(speedDiff > 30){
				trafficLevel = 5;
			}else if(speedDiff > 20){
				trafficLevel = 4;
			}else if(speedDiff > 10){
				trafficLevel = 3;
			}else{
				trafficLevel = 2;
			}
				
		}else if( dirPrv[direction]>7){
			
			if(speedDiff > 40){
				trafficLevel = 5;
			}else if(speedDiff > 30){
				trafficLevel = 4;
			}else if(speedDiff > 20){
				trafficLevel = 3;
			}else if(speedDiff > 10){
				trafficLevel = 2;
			}else{
				trafficLevel = 1;
			}
		
		}else if( dirPrv[direction]>5){
			
			if(speedDiff > 40){
				trafficLevel = 4;
			}else if(speedDiff > 30){
				trafficLevel = 3;
			}else if(speedDiff > 20){
				trafficLevel = 2;
			}else if(speedDiff > 10){
				trafficLevel = 1;
			}else{
				trafficLevel = 0;
			}

		}else if( dirPrv[direction]>3){
			
			if(speedDiff > 40){
				trafficLevel = 3;
			}else if(speedDiff > 30){
				trafficLevel = 2;
			}else if(speedDiff > 20){
				trafficLevel = 1;
			}else{
				trafficLevel = 0;
			}

		}else{
			
			if(speedDiff > 40){
				trafficLevel = 2;
			}else if(speedDiff > 30){
				trafficLevel = 1;
			}else{
				trafficLevel = 0;
			}
		}
	}
		
		System.out.println("o Calculated: Traffic ahead is: " + trafficWords[trafficLevel]);
		output = "o Calculated: Traffic ahead is: " + trafficWords[trafficLevel];
		waveManager.userInterface.computedTrafficInfo(output);
		
	if(trafficLevel > 3){
		userMessage = trafficWords[trafficLevel] + " traffic ahead. Please find an alternative route.";
	}else if(trafficLevel > 1){
		userMessage = trafficWords[trafficLevel] + " traffic ahead. Please excercise caution.";
	}else{
		userMessage = trafficWords[trafficLevel] + " traffic ahead.";
	}
	
	waveManager.userInterface.userInfo(userMessage);
		
	/*	To do: 	Distance to traffic cluster calc. -> perhaps handled by general info service?
				Possible convenience algorithm implementation -> general or traffic?					*/
	
	}

//METHODS
	
	public static double speedDifference(int s1,int s2){
		double d = (s1 - s2)/s1;
		d = d*100;
		return d;
	}
	
	/*return average flow of traffic in two directions
	Ie. Avg speed north,east,etc. (use vectors to approx. to a given direction
	approx. to the two most prevalent directions (ie. northeast, southwest)
	vehicle can check if it is heading in the same direction
	Average Vehicle direction in two directions
	To do: add two other directions for cross-traffic.*/
	
	public static String getAvgDir(List<VehicleInfo> vehicles, int vLength){
		int[] direction = new int[vLength];
		int[] prevalence = new int[16];
		Arrays.fill(prevalence, 0);
		int[] laneDir = new int[]{0,0};
		int[] dirPrv = new int[]{0,0};

		for(int i = 0; i<vLength; i++){
			direction[i] = vehicles.get(i).bearing;
			
			int j =0;
			if(direction[i] < 11.25 || direction[i] > 348.75){prevalence[0]++;}
			
			for(j = 1; j<16; j++){
				if(direction[i] < j*22.5 + 11.25 && direction[i] > j*22.5 - 11.25){
					prevalence[j]++;
					}
				}
		}
		//if the direction k has highest appearance, update the top count to the appearance amount;
		//set previous highest value to second, highest to first (two most used directions)
		
		int top_count = 0;
		for(int w = 0; w<2; w++){
			for(int k = 0; k<16; k++){
				
				if(prevalence[k] >= top_count){
					top_count = prevalence[k];
					laneDir[w] = k;
					dirPrv[w] = prevalence[k];
				}
			}
			prevalence[laneDir[0]] = 0;
			top_count = 0;
		}
		return laneDir[0]+"/"+laneDir[1]+"/"+dirPrv[0]+"/"+dirPrv[1];
	}
	
	//Average Vehicle Speed in two directions
	//To do: add two other directions for cross-traffic.
	
	public static String getAvgSpd(List<VehicleInfo> vehicles, int vLength, int laneDir[]){
		int speed = 0;
		int direction;
		int[] spd = new int[]{0,0};
		int[] count = new int[]{0,0};
		
		for(int i = 0; i<vLength; i++){
			
			speed = vehicles.get(i).speed;
			direction = vehicles.get(i).bearing;
			
			//Approx. direction of current vehicle to two lane directions
			if(direction != laneDir[0] && direction != laneDir[1]){
				if(direction-laneDir[1] > direction-laneDir[0]){direction = laneDir[0];
				}else{
					direction = laneDir[1];
					}
			}
			
			//Compare to the lanes (add to respective lane speed)
			if(direction == laneDir[0]){
				spd[0] = spd[0] + speed;
				count[0]++;
			}else{
				spd[1] = spd[1] + speed;
				count[1]++;
			}
		}
		spd[0] = spd[0]/count[0];
		spd[1] = spd[1]/count[1];
		
		return spd[0]+"/"+spd[1];
	}
	
}