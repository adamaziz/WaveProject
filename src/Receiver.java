import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver implements Runnable{
	//Class Variables
	private Thread receiverThread;
	private WaveManager waveManager;
	private BreakService breakService;
	private MulticastSocket listener;
	
	//Resources
<<<<<<< HEAD
	public int timeout = 1;
	private MulticastSocket passAlongProcess;
	private String currentGroup;
	private int maxHopCount = 5;
	private String[] groupsToListenTo={"230.0.0.1", "", ""};
=======
	private MulticastSocket passAlongProcess;
	private String currentGroup;
	private int maxHopCount = 5;
	private String[] groupsToListenTo={"230.0.0.1", ""};
>>>>>>> refs/remotes/origin/Adam
	private String[][] recentlyReceivedMessages={{"", "",}, {"", "",}, {"", "",}, {"", "",},
												{"", "",}, {"", "",}, {"", "",}, {"", "",}};
	
	//Constructor
	public Receiver(WaveManager waveManager, BreakService breakService){
		this.waveManager=waveManager;
		this.breakService=breakService;
		try{
			listener = new MulticastSocket(waveManager.port);			
			listener.joinGroup(InetAddress.getByName(waveManager.controlGroup));
			currentGroup = waveManager.controlGroup;
		}catch(Exception e){
			
		}
	}
	
	//Class Methods
	public void start(){
		if(receiverThread==null){
			receiverThread = new Thread(this, "Receiver");
			receiverThread.start();
		}
	}
	
	public void run(){
		while(true){
			for(int i=0; i<groupsToListenTo.length; i++){
<<<<<<< HEAD
				if(!(groupsToListenTo[i].equals(""))){
					switchGroups(groupsToListenTo[i]);
					getPacket();
				}
=======
				switchGroups(groupsToListenTo[i]);
				getPacket();
>>>>>>> refs/remotes/origin/Adam
			}
		}
	}
	
	public void getPacket(){
		try{
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			try{
<<<<<<< HEAD
				listener.setSoTimeout(timeout);
=======
				listener.setSoTimeout(100);
>>>>>>> refs/remotes/origin/Adam
				listener.receive(packet);
			}catch(Exception e){
				
			}
			
			byte[] message = packet.getData();
			int length = packet.getLength();
			
			String str = new String(message, 0, length);
			String[] strings = str.split("/");
			
			String fromCarID = strings[0];
			String messageID = strings[1];
			String fromGroup = strings[2];
			int hopCount = Integer.parseInt(strings[3]);
			String messageGroup = strings[4];
			String direction = strings[5];
			
			//Commented for testing purposes
			//if(!(strings[0].equals(waveManager.CarID))){
			if(fromCarID.equals(waveManager.CarID)){
				
				if(receivedMessagePreviously(fromCarID, messageID)){
					
					if(fromGroup.equals(breakService.serviceGroup)){
						System.out.println("Received messageID '"+messageID+"' on channel '"+fromGroup+"' from CarID '"+fromCarID+"' going direction '"+direction+"' saying '"+strings[6]+"' from "+currentGroup+" with hopCount = "+hopCount);
						breakService.computeData(strings[6]);

					}else{
						System.out.println("Received messageID '"+messageID+"' on channel '"+fromGroup+"' from CarID '"+fromCarID+"' advertising '"+messageGroup+"'");
						
						boolean alreadyListening = false;
						for(int i=0; i<groupsToListenTo.length; i++){
							if(groupsToListenTo[i].equals(messageGroup)){
<<<<<<< HEAD
								System.out.println("Group '"+messageGroup+"' is already in groupsToListenTo");
=======
>>>>>>> refs/remotes/origin/Adam
								alreadyListening = true;
							}
						}
						if(!alreadyListening){
							groupsToListenTo[1] = messageGroup;
							System.out.println("Added '"+messageGroup+"' to groupsToListenTo");
							
						}
					}
					
					if(hopCount < maxHopCount){
						passAlongMessage(fromCarID, messageID, hopCount, messageGroup, strings[5]);
					}
				}
			}else{
				System.out.println("Omitted own message");
			}
			
		}catch(Exception e){
			
		}		
	}

	private void passAlongMessage(String fromCarID, String messageID, int hopCount, String messageGroup, String data){
		try{
			passAlongProcess = new MulticastSocket();
			
			hopCount++;
			
			//Preparing packet envelope
			InetAddress InetDestination = InetAddress.getByName(currentGroup);
			
			String message = fromCarID+"/"+messageID+"/"+currentGroup+"/"+hopCount+"/"+messageGroup+"/"+waveManager.direction+"/"+data;
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetDestination, waveManager.port);
			
			//Send packet
			passAlongProcess.send(packet);
		
			System.out.println("Passed messageID '"+messageID+"' along on "+currentGroup+" with hopCount '"+hopCount+"': "+message);
		}catch(Exception e){
			
		}
	}
	
	public void switchGroups(String group){
		try{
			listener.joinGroup(InetAddress.getByName(group));
			System.out.println("Switched to group "+currentGroup);
		}catch(Exception e){
			
		}
		currentGroup = group;
	}
	
	private boolean receivedMessagePreviously(String fromCarID, String messageID){
		for(int i=0; i<8; i++){
			if(recentlyReceivedMessages[i][0].equals(fromCarID)&&recentlyReceivedMessages[i][1].equals(messageID)){
				System.out.println("Message '"+messageID+"' from carID '"+fromCarID+"' has recently been received. Omit message");
				return false;
			}
		}
		
		for(int i=7; i>0; i--){
			recentlyReceivedMessages[i][0] = recentlyReceivedMessages[i-1][0];
			recentlyReceivedMessages[i][1] = recentlyReceivedMessages[i-1][1];
		}
		recentlyReceivedMessages[0][0] = fromCarID;	
		recentlyReceivedMessages[0][1] = messageID;	
		return true;
	}
}
