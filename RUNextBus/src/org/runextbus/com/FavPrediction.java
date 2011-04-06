package org.runextbus.com;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class FavPrediction extends Activity implements OnClickListener {
	
 Button ButtonUpdate;
 ServerInterface sobj = new ServerInterface();
 XmlParser xobj= new XmlParser();
 String timeXml=null;
 private TextView output;
 

 
public void onCreate(Bundle savedInstanceState) {

	 
	super.onCreate(savedInstanceState);
	 setContentView(R.layout.showfavtime);
		
	 getFavTime(Global.favRouteTag,Global.favRoute,Global.favStopTag,Global.favStop);
	 
 }
 

 void getFavTime(String favrTag, String favrTitle, String favsTag, String favsTitle){

	 String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+favrTag+"&s="+favsTag;
	 timeXml = sobj.retrieve(timeUrl);
	 
	 
	 System.out.println("RouteTag : "+favrTag+"\n");
	 System.out.println("StopTag : "+favsTag+"\n");
	 ArrayList<String> timeMinutes = xobj.parseTimeResponse(timeXml);
	  
	  int count = timeMinutes.size();
	 
	  if(count!=0){
	  // pass the entire arrayList
		  Global.time=timeMinutes;
		  displayTime(timeMinutes);
	  }
	  
	  else
	  {
		  displayError();
		  
	  }

	 
 }	 // end of getFavTime
	 
	 
		  
 void displayTime(ArrayList <String> time){
		
		output = (TextView)findViewById(R.id.out_text);    	        
		String times= null;
		StringBuilder sb = new StringBuilder();
		times = " ROUTE	: "+Global.favRoute+"\n";
		sb.append(times);
		times = " at "+Global.favStop+"\n";
		sb.append(times);
		
		
		//display only the first two times 
		
		times="  Next Bus Arrival in \n\n";
		sb.append(times);
		
		times="					"+(String)time.get(0)+" min"+"\n";
		sb.append(times);
		//check if ne more prediction available to display
		
		if(time.size()>1){
		if((String)time.get(1)!=null){
		
			times="					"+(String)time.get(1)+" min"+"\n";
		sb.append(times);
		}
		}
		
		if(time.size()>2){
		if((String)time.get(2)!=null){
			//String timeThree = (String)time.get(2);
			times="					"+(String)time.get(2)+" min"+"\n";
			sb.append(times);
		}
		}
		
		this.output.setText(sb.toString());
		
		ButtonUpdate=(Button)findViewById(R.id.ButtonUpdate);
		ButtonUpdate.setOnClickListener(this);
		

	} // end of displayTime

 
 void displayError(){
		
		output = (TextView)findViewById(R.id.out_text);    	        
		String times= null;
		StringBuilder sb = new StringBuilder();
		times = "  ROUTE : "+Global.favRoute+"\n";
		sb.append(times);
		times = "  at "+Global.favStop+"\n";
		sb.append(times);
		times= "\n\n	PREDICTION\n " +
			   "	CURRENTLY\n " +
			   "	UNAVAILABLE\n";
			sb.append(times);
				
		this.output.setText(sb.toString());
		
		//wait for update to be clicked 
		//can make it auto update 
		
		ButtonUpdate=(Button)findViewById(R.id.ButtonUpdate);
		ButtonUpdate.setOnClickListener(this);

	} // end of displayError

 
 
// Wait for click on one of the buttons 
public void onClick(View v) {	
	    		
		
	    		Intent i;
	    		// Auto-generated method stub
	    		switch(v.getId())
	    		{
	    		
	    		/**
	    		 * Case statements to branch control to correct class 
	    		 */
	    		case R.id.ButtonUpdate:
	    		// call update with database returned values
	    				
	    					updateTime(Global.favRouteTag,Global.favStopTag);
	    					break;
	    					

	    		}// end of case statements
	
	
}//end of onClick


public void updateTime(String route, String stop){

   /** All the URL's for invoking API on nextBus.com 
 	*	Make http request with stop and route given*/
    
  String timeUrl = "https://www.cs.rutgers.edu/lcsr/research/nextbus/feed.php?command=predictions&a="+Global.agency+"&r="+route+"&s="+stop;
  timeXml = sobj.retrieve(timeUrl);
  ArrayList<String> timeMinutes = xobj.parseTimeResponse(timeXml);
  int count = timeMinutes.size();
  if(count!=0){
  // pass the entire arrayList
	  displayTime(timeMinutes);
	  }
  
  else{
	  displayError();
  }

}//end of update

}