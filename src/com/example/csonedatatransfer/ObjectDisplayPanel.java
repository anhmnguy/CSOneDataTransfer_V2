package com.example.csonedatatransfer;

import java.util.HashSet;
import java.util.Set;

import com.example.csonedatatransfer.partnerUtil.PartnerObjectsUtil;
import com.example.csonedatatransfer.partnerUtil.UploadUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("csonedatatransfer_v2")
/**
 * Panel that will display a table of all the available objects in the saleforce environment 
 * @author anhmnguy
 *
 */
public class ObjectDisplayPanel extends Panel {

	public static Label numberSelectedMessage;
	public static Set<String> selectedObjects;
	public static String[] arrayOfSelectedOjbects; 
	private static VerticalLayout objectPanel;
	private static Table objectTable;
	private static String[] arrayOfObjects; 
	private static Button nextButton;

	public ObjectDisplayPanel() {
		objectPanel = new VerticalLayout();
		setContent(objectPanel);
		buildLayout();

		selectedObjects = new HashSet<String>();

		arrayOfObjects = PartnerObjectsUtil.getObjects();
		objectTable = PartnerObjectsUtil.populateObjectTable(objectTable, arrayOfObjects);

		nextButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				arrayOfSelectedOjbects = new String[selectedObjects
						.size()];
				int i = 0;
				for (String x : selectedObjects) {
					arrayOfSelectedOjbects[i] = x;
					i++;
				}
				CsonedatatransferUI.mainLayout.removeAllComponents();
				DestinationLoginPanel destination = new DestinationLoginPanel();
				destination.setWidth("300px");
				destination.setStyleName("panelLayout");
				CsonedatatransferUI.mainLayout.addComponent(destination);
			}
		});

	}

	public static void updateObjectTable(Set<String> updateObjects){
		objectTable.removeAllItems(); 
		objectTable = PartnerObjectsUtil.populateObjectTable(objectTable, arrayOfObjects);
	}

	/**
	 * Build the main layout of the object display panel
	 */
	public static void buildLayout() {
		Label pickObjMessage = new Label(
				"Step 2: Please pick what objects you would like to import. Please note that a max of 100 objects can only be imported at one time.");
		Label orMessage = new Label("-Or-");
		
		pickObjMessage.addStyleName("layoutCenter");
		orMessage.addStyleName("layoutCenter");
		
		
		UploadUtil receiver = new UploadUtil(); 
		Upload upload = new Upload("Upload an txt file and click apply.", receiver);
		upload.setButtonCaption("Apply");
		upload.setWidth("500px");
		upload.addSucceededListener(receiver);

		numberSelectedMessage = new Label("Number Selected: 0");	
		numberSelectedMessage.addStyleName("layoutCenter");
	
		objectTable = new Table();
		// Send changes in selection immediately to server.
		objectTable.setImmediate(true);
		objectTable.setHeight("300px");
		objectTable.setWidth("500px");
		
		
		nextButton = new Button("Next");
		nextButton.addStyleName("button");
		
		objectPanel.addStyleName("layoutCenter");
		objectPanel.addComponent(pickObjMessage);
		objectPanel.addComponent(orMessage);
		objectPanel.addComponent(upload);
		objectPanel.addComponent(numberSelectedMessage);
		objectPanel.addComponent(objectTable);
		objectPanel.addComponent(nextButton);
	}

}