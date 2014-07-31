package com.example.csonedatatransfer.partnerUtil;

import com.example.csonedatatransfer.ObjectDisplayPanel;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.ws.ConnectionException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class PartnerObjectsUtil {

	/**
	 * Utility to help with populating a table to display all of the objects
	 * available
	 */
	private static String[] objectNames;
	private static CheckBox objCheckbox;

	/**
	 * Gets a list of all the available objects
	 * @return an array of object names 
	 */
	public static String[] getObjects() {

		try {
			// Make the describeGlobal() call
			DescribeGlobalResult describeGlobalResult = PartnerMetadataLoginUtil.connection
					.describeGlobal();
			// Get the sObjects from the describe global result
			DescribeGlobalSObjectResult[] sobjectResults = describeGlobalResult
					.getSobjects();
			objectNames = new String[sobjectResults.length];
			// Save the object names into an array
			for (int i = 0; i < sobjectResults.length; i++) {
				objectNames[i] = sobjectResults[i].getName();
			}

		} catch (ConnectionException ce) {
			ce.printStackTrace();
		}
		return objectNames;

	}
	

	/**
	 * Populate the table with checkboxes for each of the objects
	 * 
	 * @param objectTable
	 *            table to populate
	 * @param objects
	 *            an array of all the objects
	 * @return objectTable
	 */
	public static Table populateObjectTable(Table objectTable,
			final String[] objects) {

		objectTable.addContainerProperty("Object Name", CheckBox.class, null);
		objectTable.addContainerProperty("Number of Records to Import",
				TextField.class, null);
		// populate the table with checkboxes and objects
		for (int i = 0; i < objects.length; i++) {
			objCheckbox = new CheckBox(objects[i]);
			if(UploadUtil.uploadedSOject != null && UploadUtil.uploadedSOject.contains(objects[i])){
				objCheckbox.setValue(true);
				ObjectDisplayPanel.selectedObjects.add(objects[i]);
				HandleSelectLabel();
			}
			final TextField numbOfRecords = new TextField();
			objectTable.addItem(new Object[] { objCheckbox, numbOfRecords }, i);
			handleCheckBox(objCheckbox, objects, i);
		}
		return objectTable;
	}

	/**
	 * Handles the logic for the checkboxes
	 * 
	 * @param checkBox
	 *            checkbox associated with each object
	 * @param objects
	 *            an array of objects
	 * @param index
	 *            index of the object in the object array
	 */
	@SuppressWarnings("serial")
	public static void handleCheckBox(final CheckBox checkBox,
			final String objects[], final int index) {
		// Add a listener for each of the checkboxes
		checkBox.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				if (checkBox.getValue()) {
					checkBox.setValue(true);
					ObjectDisplayPanel.selectedObjects.add(objects[index]);
				} else {
					checkBox.setValue(false);
					ObjectDisplayPanel.selectedObjects.remove(objects[index]);
				}
				HandleSelectLabel();
				System.out.println("Selected: " + checkBox.getCaption()
						+ " Value = " + checkBox.getValue());
			}
		});
		
	}
	
	private static void HandleSelectLabel(){
		// Print out the number of selected
				// If the number of selected exceeds 100, then a caution
				// text will appear
				if (ObjectDisplayPanel.selectedObjects.size() <= 100) {
					ObjectDisplayPanel.numberSelectedMessage.setStyleName("layoutCenter");
					ObjectDisplayPanel.numberSelectedMessage
							.setValue("Number Selected: " + ObjectDisplayPanel.selectedObjects.size());
				} else {
					ObjectDisplayPanel.numberSelectedMessage
							.setStyleName("redText");
					ObjectDisplayPanel.numberSelectedMessage
							.setValue("Number Selected: "
									+ ObjectDisplayPanel.selectedObjects.size()
									+ " Caution! Cannot import more than 100 objects at one time.");
				}
	}

}
