package com.downloader;

import com.helper.CommContainer;
import com.helper.CommTracker;
import com.helper.XMLParser;

public class TestTimer {
	public static void main(String[] args) {
		
		CommContainer cont = CommContainer.getInstance();
		
		if(cont != null){
			System.out.println("Container created...");
		}
		
		if(cont.getSize() == 0) {
			System.out.println("Container is empty");
		}
		
		CommTracker tracker = CommTracker.getInstance();
		
		

	}
}
