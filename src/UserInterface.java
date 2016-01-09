import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface implements Runnable{
	//Objects
	public WaveManager waveManager;
	private Thread userInterfaceThread;    
	
	//UI Objects
	private JFrame mainFrame;
    
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel buttonPanel;
    private JPanel outputPanel;
    private JPanel packetInfoPanel;
    private JPanel sentPacketInfoPanel;
    private JPanel receivedPacketInfoPanel;
    private JPanel topComputedDataPanel;
    private JPanel leftComputedDataPanel;
    private JPanel rightComputedDataPanel;
    
    private JLabel leftPanelLabel;
    private JLabel centerPanelLabel;
    private JLabel rightPanelLabel;
    private JLabel carID;
    private JLabel gps;
    private JLabel bearing;
    private JLabel speed;
    private JLabel brakeAmount;
    private JLabel vehicleType;
    private JLabel generalInfo;
    private JLabel suggestedBrakeAmount;
    private JLabel suggestedBrakeSpeed;
    private JLabel generalInfoPacketsSent;
    private JLabel brakeServicePacketsSent;
    private JLabel emergencyServicePacketsSent;
    private JLabel numPacketsReceived;
    private JLabel numPacketsPassed;
    private JLabel numPacketsOmitted;
    private JLabel sender;
    private JLabel receiver;
    
    private JButton button1;
    private JButton button2;
    private JButton button3;

    private JTextArea output;
    private JScrollPane scroll;
    
    private JTextArea computedGeneralInfo;
    private JScrollPane computedGeneralInfoScroll;
    private JTextArea computedBrakeInfo;
    private JScrollPane computedBrakeInfoScroll;
    private JTextArea computedEmergencyInfo;
    private JScrollPane computedEmergencyInfoScroll;

	//Class Methods
    public UserInterface(WaveManager waveManager){
    	this.waveManager=waveManager;
	   mainFrame = new JFrame();
	   
	   mainPanel = new JPanel();
	   topPanel = new JPanel();
	   leftPanel = new JPanel();
	   centerPanel = new JPanel();
	   rightPanel = new JPanel();
	   buttonPanel = new JPanel();
	   outputPanel = new JPanel();
	   outputPanel = new JPanel();       
	   packetInfoPanel = new JPanel();
	   sentPacketInfoPanel = new JPanel();
	   receivedPacketInfoPanel = new JPanel();
	   topComputedDataPanel = new JPanel();
	   leftComputedDataPanel = new JPanel();
	   rightComputedDataPanel = new JPanel();
	   
	   //Make buttons increase gas and brake
	   button1 = new JButton("  Gas  ");
	   button2 = new JButton("Brake");
	   button3 = new JButton("Siren");
	   
	   //Labels
	   leftPanelLabel = new JLabel("<html><u>Vehicle Info</u></html>");
	   centerPanelLabel = new JLabel("<html><u>Variable Data</u></html>");
	   rightPanelLabel = new JLabel("<html><u>Calculated Info</u></html>");
	   carID = new JLabel("Car ID: xxx-xxx-xxx-xxx");
	   gps = new JLabel("GPS: -, -");
	   bearing = new JLabel("Bearing: - degrees");
	   speed = new JLabel("Speed: -");
	   brakeAmount = new JLabel("Brake Amount: -");
	   vehicleType = new JLabel("Vehicle Type: -");
	   generalInfo = new JLabel("General Info:");
	   suggestedBrakeAmount = new JLabel("Speed Adjustment:");
	   suggestedBrakeSpeed = new JLabel("Speed of Brake Applied:");
	   sender = new JLabel("<html><u>Sender</u></html>");
	   generalInfoPacketsSent = new JLabel("GeneralInfoService Packets Sent: 0 ");
	   brakeServicePacketsSent = new JLabel("BreakService Packets Sent: 0 ");
	   emergencyServicePacketsSent = new JLabel("EmergencyService Packets Sent: 0 ");
	   receiver = new JLabel("<html><u>Receiver</u></html>");
	   numPacketsReceived = new JLabel("Received Packets: 0 ");
	   numPacketsPassed = new JLabel("Packets Passed: 0 ");
	   numPacketsOmitted = new JLabel("Omitted Packets: 0 ");
	
	   leftPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   centerPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   rightPanelLabel.setHorizontalAlignment(JLabel.CENTER);
	   carID.setHorizontalAlignment(JLabel.CENTER);
	   carID.setFont(new Font("Open Sans", Font.BOLD, 13));
	   gps.setHorizontalAlignment(JLabel.CENTER);
	   gps.setFont(new Font("Open Sans", Font.BOLD, 13));
	   bearing.setHorizontalAlignment(JLabel.CENTER);
	   bearing.setFont(new Font("Open Sans", Font.BOLD, 13));
	   speed.setHorizontalAlignment(JLabel.CENTER);
	   speed.setFont(new Font("Open Sans", Font.BOLD, 13));
	   brakeAmount.setHorizontalAlignment(JLabel.CENTER);
	   brakeAmount.setFont(new Font("Open Sans", Font.BOLD, 13));
	   vehicleType.setHorizontalAlignment(JLabel.CENTER);
	   vehicleType.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   generalInfo.setHorizontalAlignment(JLabel.CENTER);
	   generalInfo.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   suggestedBrakeAmount.setHorizontalAlignment(JLabel.CENTER);
	   suggestedBrakeAmount.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   suggestedBrakeSpeed.setHorizontalAlignment(JLabel.CENTER);   
	   suggestedBrakeSpeed.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   sender.setHorizontalAlignment(JLabel.CENTER);  
	   receiver.setHorizontalAlignment(JLabel.CENTER);  
	   generalInfoPacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   generalInfoPacketsSent.setFont(new Font("Open Sans", Font.BOLD, 13));
	   brakeServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   brakeServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   emergencyServicePacketsSent.setHorizontalAlignment(JLabel.CENTER);   
	   emergencyServicePacketsSent.setFont(new Font("Open Sans", Font.BOLD, 13));  
	   numPacketsReceived.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsReceived.setFont(new Font("Open Sans", Font.BOLD, 13));  
	   numPacketsPassed.setHorizontalAlignment(JLabel.CENTER);   
	   numPacketsPassed.setFont(new Font("Open Sans", Font.BOLD, 13)); 
	   numPacketsOmitted.setHorizontalAlignment(JLabel.CENTER);    
	   numPacketsOmitted.setFont(new Font("Open Sans", Font.BOLD, 13));       
	   
	   //Output boxes
	   JLabel outputLabel = new JLabel("<html><u>Sending/Receiving</u></html>");
	   output = new JTextArea();
	   output.setFont(new Font("Open Sans", Font.PLAIN, 12));
	   output.setEditable(false);
	   scroll = new JScrollPane (output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   scroll.setPreferredSize(new Dimension(1100,200));
	
	   JLabel generalInfoServiceOutputLabel = new JLabel("<html><u>General Info Service Computed Information</u></html>");
	   computedGeneralInfo = new JTextArea();
	   computedGeneralInfo.setFont(new Font("Open Sans", Font.PLAIN, 12));
	   computedGeneralInfo.setEditable(false);
	   computedGeneralInfoScroll = new JScrollPane (computedGeneralInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedGeneralInfoScroll.setPreferredSize(new Dimension(540,150));
	
	   JLabel brakeServiceOutputLabel = new JLabel("<html><u>Brake Service Computed Information</u></html>");
	   computedBrakeInfo = new JTextArea();
	   computedBrakeInfo.setFont(new Font("Open Sans", Font.PLAIN, 12));
	   computedBrakeInfo.setEditable(false);
	   computedBrakeInfoScroll = new JScrollPane (computedBrakeInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedBrakeInfoScroll.setPreferredSize(new Dimension(540,150));
	   
	   JLabel emergencyServiceOutputLabel = new JLabel("<html><u>Emergency Service Computed Information</u></html>");
	   computedEmergencyInfo = new JTextArea();
	   computedEmergencyInfo.setFont(new Font("Open Sans", Font.PLAIN, 12));
	   computedEmergencyInfo.setEditable(false);
	   computedEmergencyInfoScroll = new JScrollPane (computedEmergencyInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   computedEmergencyInfoScroll.setPreferredSize(new Dimension(540,150));
	   
	   topPanel.setLayout(new GridLayout(1,3));
	   leftPanel.setLayout(new GridLayout(6,1));
	   centerPanel.setLayout(new GridLayout(6,1));
	   rightPanel.setLayout(new GridLayout(6,1));
	   packetInfoPanel.setLayout(new GridLayout(1,2));
	   sentPacketInfoPanel.setLayout(new GridLayout(4,1));
	   receivedPacketInfoPanel.setLayout(new GridLayout(4,1));
	   topComputedDataPanel.setLayout(new GridLayout(1,2));
	   
	   topPanel.setPreferredSize(new Dimension(700,150));
	   leftPanel.setPreferredSize(new Dimension(700,150));
	   centerPanel.setPreferredSize(new Dimension(700,150));
	   rightPanel.setPreferredSize(new Dimension(700,150));
	   buttonPanel.setPreferredSize(new Dimension(1100,50));
	   outputPanel.setPreferredSize(new Dimension(1125,750));
	   packetInfoPanel.setPreferredSize(new Dimension(1125,80));
	   sentPacketInfoPanel.setPreferredSize(new Dimension(500,40));
	   receivedPacketInfoPanel.setPreferredSize(new Dimension(500,100));
	   topComputedDataPanel.setPreferredSize(new Dimension(1125,1000));
	   leftComputedDataPanel.setPreferredSize(new Dimension(550,700));
	   rightComputedDataPanel.setPreferredSize(new Dimension(550,700));
	   
	   //Add components to panels
	   topPanel.add(leftPanel);
	   topPanel.add(centerPanel);
	   topPanel.add(rightPanel);
	   leftPanel.add(leftPanelLabel);
	   leftPanel.add(carID);
	   leftPanel.add(vehicleType);
	   centerPanel.add(centerPanelLabel);
	   centerPanel.add(gps);
	   centerPanel.add(bearing);
	   centerPanel.add(speed);
	   centerPanel.add(brakeAmount);
	   rightPanel.add(rightPanelLabel);
	   rightPanel.add(generalInfo);
	   rightPanel.add(suggestedBrakeAmount);
	   rightPanel.add(suggestedBrakeSpeed);
	   buttonPanel.add(button1);
	   buttonPanel.add(button2);
	   buttonPanel.add(button3);
	   outputPanel.add(packetInfoPanel);
	   outputPanel.add(outputLabel);
	   outputPanel.add(scroll);
	   outputPanel.add(topComputedDataPanel);
	   packetInfoPanel.add(sentPacketInfoPanel);
	   packetInfoPanel.add(receivedPacketInfoPanel);
	   sentPacketInfoPanel.add(sender);
	   sentPacketInfoPanel.add(generalInfoPacketsSent);
	   sentPacketInfoPanel.add(brakeServicePacketsSent);
	   sentPacketInfoPanel.add(emergencyServicePacketsSent);
	   receivedPacketInfoPanel.add(receiver);
	   receivedPacketInfoPanel.add(numPacketsReceived);
	   receivedPacketInfoPanel.add(numPacketsOmitted);
	   receivedPacketInfoPanel.add(numPacketsPassed);
	   topComputedDataPanel.add(leftComputedDataPanel);
	   topComputedDataPanel.add(rightComputedDataPanel);
	   leftComputedDataPanel.add(generalInfoServiceOutputLabel);
	   leftComputedDataPanel.add(computedGeneralInfoScroll);
	   rightComputedDataPanel.add(brakeServiceOutputLabel);
	   rightComputedDataPanel.add(computedBrakeInfoScroll);
	   leftComputedDataPanel.add(emergencyServiceOutputLabel);
	   leftComputedDataPanel.add(computedEmergencyInfoScroll);
	   
	   mainPanel.add(topPanel);
	   mainPanel.add(buttonPanel);
	   mainPanel.add(outputPanel);
	   
	   mainFrame.add(mainPanel); 
	   mainFrame.setTitle("WAVE Interface");
	   mainFrame.setSize(1150,1000);
	   mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   mainFrame.setVisible(true);
    }
    
    public void start(){
		if(userInterfaceThread==null){
			userInterfaceThread = new Thread(this, "UserInterfaceThread");
			userInterfaceThread.start();
		}
	}
    
    public void run(){
    	while(true){
    		 try{
    			 output.setCaretPosition(output.getDocument().getLength());
    			 computedGeneralInfo.setCaretPosition(computedGeneralInfo.getDocument().getLength());
    			 computedBrakeInfo.setCaretPosition(computedBrakeInfo.getDocument().getLength());
    			 computedEmergencyInfo.setCaretPosition(computedEmergencyInfo.getDocument().getLength());
    			 
    			 waveManager.checkSpeed();
    			 waveManager.checkBearing();
    			 waveManager.checkBrake();
    			 waveManager.checkGPS();
    		 }catch(Exception e){ }
    	}
    }

    public void output(String outputText){
    	output.append(outputText+"\n");
    }
    
    public void computedGeneralInfo(String outputText){
    	computedGeneralInfo.append(outputText+"\n");
    }

    public void computedBrakeInfo(String outputText){
    	computedBrakeInfo.append(outputText+"\n");
    }
    
    public void writeCarID(String outputText){
    	carID.setText("CarID: "+outputText);
    }
    
    public void writeVehicleType(String outputText){
    	vehicleType.setText("Vehicle Type: "+outputText);
    }
    
    public void writeBearing(int outputText){
    	bearing.setText("Bearing: "+outputText+" degrees");
    }
    
    public void writeGPS(double lattitude, double longitude){
    	gps.setText("GPS: "+lattitude+", "+longitude);
    }
    
    public void writeSpeed(int outputText){
    	speed.setText("Speed: "+outputText);
    }
    
    public void writeBrakeAmount(int outputText){
    	brakeAmount.setText("Brake Amount: "+outputText);
    }
    
    public void writeGeneralInfo(String outputText){
    	generalInfo.setText("General Info: "+outputText);
    }
    
    public void writeSuggestedSpeedAdjustment(String outputText){
    	suggestedBrakeAmount.setText("Speed Adjustment: "+outputText);
    }
    
    public void writeSpeedBrakeApplied(String outputText){
    	suggestedBrakeSpeed.setText("Speed of Brake Applied: "+outputText);
    }
    
    public void updateNumPacketsReceived(int output){
    	numPacketsReceived.setText("Received Packets: "+output+" ");
    }

    public void updateNumPacketsOmitted(int output){
    	numPacketsOmitted.setText("Omitted Packets: "+output+" ");
    }
    
    public void updateNumPacketsPassed(int output){
    	numPacketsPassed.setText("Packets Passed: "+output+" ");
    }

    public void updateGeneralInfoServicePacketsSent(int output){
    	generalInfoPacketsSent.setText("GeneralInfoService Packets Sent: "+output+"");
    }
    
    public void updateBrakeServicePacketsSent(int output){
    	brakeServicePacketsSent.setText("BrakeService Packets Sent: "+output+"");
    }
    
    public void updateEmergencyServicePacketsSent(int output){
    	numPacketsPassed.setText("EmergencyService Packets Sent: "+output+"");
    }
    
    public void actionPerformed(ActionEvent e) {
    		
    }
    
}
