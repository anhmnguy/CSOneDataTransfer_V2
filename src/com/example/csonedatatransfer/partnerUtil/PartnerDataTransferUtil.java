package com.example.csonedatatransfer.partnerUtil;

import java.time.format.DateTimeFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.example.csonedatatransfer.DestinationLoginPanel;
import com.example.csonedatatransfer.ObjectDisplayPanel;
import com.example.csonedatatransfer.SourceLoginPanel;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.Error;

public class PartnerDataTransferUtil {

	private PartnerConnection sourceConnection;
	private PartnerConnection destinationConnection;
	private Field[] fields;

	public PartnerDataTransferUtil() {
		sourceConnection = SourceLoginPanel.sourceConnection;
		destinationConnection = DestinationLoginPanel.destinationConnection;

	}

	public void querySOjbects() throws ConnectionException {
		DescribeSObjectResult[] describeSObjectResults = sourceConnection
				.describeSObjects(ObjectDisplayPanel.arrayOfSelectedOjbects);
		for (int i = 0; i < describeSObjectResults.length; i++) {
			DescribeSObjectResult desObj = describeSObjectResults[i];

			desObj.getChildRelationships();
			// For each described sObject, get the fields
			fields = desObj.getFields();
			// Build the queryString
			String queryString = "SELECT ";
			for (int j = 0; j < fields.length; j++) {
				queryString += fields[j].getName();
				if (j != fields.length - 1) {
					queryString += ", ";
				}
			}
			queryString += " FROM ";
			queryString += desObj.getName();
			System.out.println(queryString);
			queryString(queryString);
		}
	}

	public void queryString(String queryString) {
		try {
			QueryResult qResult = new QueryResult();
			qResult = sourceConnection.query(queryString);
			boolean done = false;
			if (qResult.getSize() > 0) {
				System.out.println("\nLogged-in user can see a total of "
						+ qResult.getSize() + " records.");
				while (!done) {
					transferData(qResult);
					if (qResult.isDone()) {
						done = true;
					} else {
						qResult = sourceConnection.queryMore(qResult
								.getQueryLocator());
					}
				}
			} else {
				System.out.println("No records found.\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void transferData(QueryResult qResult) throws ConnectionException {
		SObject[] records = qResult.getRecords();
		for (int i = 0; i < records.length; ++i) {
			SObject obj = new SObject();
			obj.setType(records[i].getType());
			updateFields(obj, records[i]);
			obj.setName(records[i].getName());
			SaveResult[] results = destinationConnection
					.create(new SObject[] { obj });
			isUpdated(results);

		}

	}

	public SObject updateFields(SObject obj, SObject records) {

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String recordValue = (String) records.getField(fieldName);
			String fieldType = field.getType().toString();

			if (field.isCreateable() && recordValue != null
					&& !fieldName.contains("Id") && !fieldName.contains("id")) {

				if (fieldType.equals("_double")) {
					obj.setField(fieldName, Double.parseDouble(recordValue));
				} else if (fieldType.equals("_boolean")) {
					obj.setField(fieldName, Boolean.parseBoolean(recordValue));
				} else if (fieldType.equals("datetime")){
//					DateTime dt = DateTime.parse(recordValue, 
//			                  DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss.zzz")); 
//					obj.setField(fieldName, dt);
					
					//Don't do anything for now.
				}
				else {
					obj.setField(fieldName, recordValue);
				}
				System.out.println("\n\n"+fieldName + " was created. Value is:  " + recordValue + "\n\n");
			}
			System.out.println("Field is: " + field.getName() + "Record value is: " + recordValue);
		
		}

		return obj;
	}

	public boolean isUpdated(SaveResult[] saveResults) {
		for (int j = 0; j < saveResults.length; j++) {
			if (saveResults[j].isSuccess()) {
				System.out.println("\nSObject with an ID of "
						+ saveResults[j].getId() + " was imported.");
				System.out.println("Query succesfully executed.\n");
				return true;
			} else {
				for (int i = 0; i < saveResults[j].getErrors().length; i++) {
					Error err = saveResults[j].getErrors()[i];
					System.out.println("Errors were found on item " + j);
					System.out.println("Error code: "
							+ err.getStatusCode().toString());
					System.out.println("Error message: " + err.getMessage()
							+ "\n");
				}
			}
		}
		return false;
	}
}