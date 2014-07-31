package com.example.csonedatatransfer.partnerUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import com.example.csonedatatransfer.ObjectDisplayPanel;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
public class UploadUtil implements Receiver, SucceededListener {
	private File file;
	public static Set<String> uploadedSOject;
	@Override
	public void uploadSucceeded(SucceededEvent event) {
	
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			ReadFile(reader);
			ObjectDisplayPanel.updateObjectTable(uploadedSOject);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
        	String basepath = VaadinService.getCurrent()
	                  .getBaseDirectory().getAbsolutePath();
	
        	file = new File(basepath + "/WEB-INF/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                             e.getMessage(),
                             Notification.Type.ERROR_MESSAGE);
			Notification.show( e.getMessage());
            return null;
        }
        return fos; 
	}

	public Set<String> ReadFile(BufferedReader reader){
		uploadedSOject = new HashSet<String>();
		try {
			String sOjbect = reader.readLine(); 
			while(sOjbect != null){
				uploadedSOject.add(sOjbect);
				sOjbect = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return uploadedSOject;
		
	}
}
