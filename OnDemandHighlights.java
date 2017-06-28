import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.component.EmbeddedMediaListPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.medialist.MediaList;

import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class OnDemandHighlights {

    private final JFrame frame;
    private final EmbeddedMediaListPlayerComponent mediaPlayerComponent;
    private final MediaList mediaList;
    private final JButton previousButton;
    private final JButton rewindButton;
    private final JButton pauseButton;
    private final JButton slowButton;
    private final JButton ffButton;
    private final JButton nextButton;
	private boolean isSlow = false;

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OnDemandHighlights(args);
            }
        });
    }

    public OnDemandHighlights(String[] args) {
	    //how long to play clip before and after event
	    final int beforeTime = 10;
	    final int afterTime = 45;
	    final String filePath = "E:\\Kyle\\Videos\\EPL\\2015_to_2016\\";
	    final String fileExtension = ".flv";
	    
	    int playerStartTime;
	    int playerStopTime;
	    String fullFilePath;
	    String fileName;
	    String mediaOptionStart;
	    String mediaOptionStop;
	    
	    HashMap<String,Integer> firstHalfStart = new HashMap<String,Integer>();
	    HashMap<String,Integer> secondHalfStart = new HashMap<String,Integer>();
	    
		SendDataToSQL sdts = new SendDataToSQL();
		String query;
		String database = "SoccerDFS";
		ResultSet rs;
		ResultSet rs1;
		StringBuilder errorLog = new StringBuilder();
		String myGID;
		int wsGID;
		int firstHalfMin;
		int firstHalfSec;
		int secondHalfMin;
		int secondHalfSec;
		int firstHalfStartTime;		//contains number of seconds into recording that first half starts
		int secondHalfStartTime;	//contains number of seconds into recording that second half starts
		
		//contains event start times in milliseconds
		ArrayList<HighlightEvent> events = new ArrayList<HighlightEvent>();
		int eventHalf;
		int eventMin;
		int eventSec;
		int eventTime;		//represented in seconds
		HighlightEvent he = new HighlightEvent();
		
		//variables for query to get select highlights
		String team1;
		String team2;
		String player;
		
		BufferedWriter bw;		//to print out query being used
		String queryFilePath = "C:\\Kyle\\Gambling\\DFS\\Code\\SiteParser\\ProgramFiles\\OnDemandHighlights\\query.txt";
		
		query = "Select * from GameTimes";
		rs = sdts.getQuery(query, database, errorLog);
		try{
			while(rs.next()){
				myGID = rs.getString("myGID");
				wsGID = rs.getInt("wsGID");
				firstHalfMin = rs.getInt("firstHalfMin");
				firstHalfSec = rs.getInt("firstHalfSec");
				secondHalfMin = rs.getInt("secondHalfMin");
				secondHalfSec = rs.getInt("secondHalfSec");
				
				firstHalfStartTime = firstHalfMin * 60 + firstHalfSec;
				secondHalfStartTime = secondHalfMin * 60 + secondHalfSec;
				
				firstHalfStart.put(myGID,firstHalfStartTime);
				secondHalfStart.put(myGID,secondHalfStartTime);
			}
			rs.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		//query to get highlights to view
		myGID = "AstonVillaNewcastleUnited07-05-2016";
		player = "Bertrand Traore";
		team1 = "liverpool";
		//***********************************************************************************************************************************
		//all shots on target by a certain team
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.myGID like ";
		//query = query + "'%" + team1 + "%' and dkdesc like '%sot%' order by wsmce.myGID desc,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all shots on target in a certain game
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.myGID like ";
		//query = query + "'%" + myGID + "%' and dkdesc like '%sot%' order by wsmce.myGID desc,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all shots on target in a certain game by a certain team
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.myGID like ";
		//query = query + "'%" + myGID + "%' and wsmce.mygid like '%" + team1 + "%' and dkdesc like '%sot%' order by wsmce.myGID desc,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all events involving points for a particular player
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.player like ";
		//query = query + "'%" + player + "%' and isPoint=1 order by wsmce.myGID desc,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all crosses for a particular player
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.player ";
		//query = query + "like '%" + player + "%' and c=1 order by wsmce.myGID desc,wsmce.half,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all crosses for a particular player in a particular game
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.player ";
		//query = query + "like '%" + player + "%' and wsmce.myGID = '" + myGID + "' and c=1 order by wsmce.myGID desc,wsmce.half,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all corners taken in games for a particular team
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.myGID like ";
		//query = query + "'%" + team1 + "%' and wsdesc like '%cornertaken%' order by wsmce.myGID desc,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all shots for a particular player
		//query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.player ";
		//query = query + "like '%" + player + "%' and s=1 order by wsmce.myGID desc,wsmce.half,wsmce.min,wsmce.sec";
		//***********************************************************************************************************************************
		//all goals or assists for a particular player
		query = "Select wsmce.myGID,wsmce.half,wsmce.min,wsmce.sec from wsmcEventsNew wsmce inner join GameTimes gt on wsmce.myGID = gt.myGID where wsmce.player ";
		query = query + "like '%" + player + "%' and (g=1 or a=1) order by wsmce.myGID desc,wsmce.half,wsmce.min,wsmce.sec";
		
		//save query to file
		try{
			bw = new BufferedWriter(new FileWriter(queryFilePath));
			bw.write(query);
			bw.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		rs1 = sdts.getQuery(query, database, errorLog);
		try{
			while(rs1.next()){
				myGID = rs1.getString("myGID");
				eventHalf = rs1.getInt("half");
				eventMin = rs1.getInt("min");
				eventSec = rs1.getInt("sec");
				
				he = new HighlightEvent(myGID,eventHalf,eventMin,eventSec);
				
				events.add(he);
			}
			rs1.close();
		}catch(Exception e){
			System.out.println(e);
		}
	    
        frame = new JFrame("My First Media Player");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        
        mediaPlayerComponent = new EmbeddedMediaListPlayerComponent();
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

        JPanel controlsPane = new JPanel();
        previousButton = new JButton("Previous");
        controlsPane.add(previousButton);
        rewindButton = new JButton("Rewind");
        controlsPane.add(rewindButton);
        pauseButton = new JButton("Pause");
        controlsPane.add(pauseButton);
        slowButton = new JButton("Slow");
        controlsPane.add(slowButton);
        ffButton = new JButton("Fast Forward");
        controlsPane.add(ffButton);
        nextButton = new JButton("Next");
        controlsPane.add(nextButton);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaListPlayer().playPrevious();
            }
        });
        
        rewindButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().skip(-10000);
            }
        });
        
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaListPlayer().pause();
            }
        });
        
        slowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
	            if(isSlow){
                	mediaPlayerComponent.getMediaPlayer().setRate(1);
                	isSlow = false;
            	}else{
	            	mediaPlayerComponent.getMediaPlayer().setRate(.5f);
	            	isSlow = true;
            	}
            }
        });
        
        ffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaPlayer().skip(10000);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayerComponent.getMediaListPlayer().playNext();
            }
        });
        
        //frame.setContentPane(mediaPlayerComponent);
        frame.setContentPane(contentPane);
        frame.setVisible(true);
        mediaList = mediaPlayerComponent.getMediaList();
	    //fileName = "ArsenalSwansea02-03-2016.flv";
	    //myGID = "ArsenalSwansea02-03-2016";
	    //add each event time to playlist
	    for(int i = 0; i < events.size(); i++){
	        he = events.get(i);
	        myGID = he.getMyGID();
	        fileName = myGID + fileExtension;
    		firstHalfStartTime = firstHalfStart.get(myGID);
    		secondHalfStartTime = secondHalfStart.get(myGID);
	    	eventHalf = he.getHalf();
	        eventTime = 60*he.getMin() + he.getSec();
	        if(eventHalf == 1){
	    		playerStartTime = firstHalfStartTime + eventTime - beforeTime;
    		}else{
	    		playerStartTime = secondHalfStartTime + eventTime - 2700 - beforeTime;
    		}
    		playerStopTime = playerStartTime + beforeTime + afterTime;
    		fullFilePath = filePath + fileName;
    		mediaOptionStart = ":start-time=" + playerStartTime;
    		mediaOptionStop = ":stop-time=" + playerStopTime;
    		mediaList.addMedia(fullFilePath,mediaOptionStart,mediaOptionStop);
		}
        //mediaList.addMedia("E:\\Kyle\\Videos\\EPL\\2015_to_2016\\ArsenalSwansea02-03-2016.flv", ":start-time=60", ":stop-time=70");
        //mediaList.addMedia("E:\\Kyle\\Videos\\EPL\\2015_to_2016\\ArsenalSwansea02-03-2016.flv", ":start-time=1000", ":stop-time=1010");
        //mediaList.addMedia("E:\\Kyle\\Videos\\EPL\\2015_to_2016\\ArsenalSwansea02-03-2016.flv", ":start-time=919000", ":stop-time=949000");
        mediaPlayerComponent.getMediaListPlayer().play();
        //attempt to speed up time it takes to load highlights. Play every clip, then rewind to back to first
        
        /*
        for(int i = 0; i < events.size() - 1; i++){
	        try{
	        	Thread.sleep(10000);
        	}catch(Exception e){
	        	System.out.println(e);
        	}
	        mediaPlayerComponent.getMediaListPlayer().playNext();
        }
        for(int i = 0; i < events.size() - 1; i++){
	        try{
	        	Thread.sleep(10000);
        	}catch(Exception e){
	        	System.out.println(e);
        	}
	        mediaPlayerComponent.getMediaListPlayer().playPrevious();
        }
        */
    }
}
