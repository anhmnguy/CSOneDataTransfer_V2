package com.example.csonedatatransfer;

import com.example.csonedatatransfer.partnerUtil.PartnerDataTransferUtil;
import com.example.csonedatatransfer.partnerUtil.PartnerMetadataLoginUtil;
import com.sforce.soap.partner.PartnerConnection;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
@Theme("csonedatatransfer_v2")
public class DestinationLoginPanel extends Panel {

	private static VerticalLayout layout;
	private static TextField usernameField;
	private static PasswordField passwordField;
	private static Button loginButton;
	public static PartnerConnection destinationConnection; 

	/**
	 * @param sourceSoapAddress
	 *            : the soap address of the destination environment. Please change the
	 *            soap address accordingly. You can find the soap address in the
	 *            partner wsdl
	 */
	private static final String sourceSoapAddress = "https://test.salesforce.com/services/Soap/u/31.0";

	/**
	 * Displays a panel for login credentials for the source environment  
	 */
	public DestinationLoginPanel() {
		layout = new VerticalLayout();
		setContent(layout);
		buildLayout();

		loginButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				login();
			}
		});

	}

	/**
	 * Handles the login for salesforce authentication 
	 */
	private static void login() {

		String username = usernameField.getValue();
		String password = passwordField.getValue();

		try {
			PartnerMetadataLoginUtil.login(username, password,
					sourceSoapAddress);
			destinationConnection = PartnerMetadataLoginUtil.getConnection(); 
			System.out.println("Authentication Successful!");
			Notification.show("Authentication Successful!");
			PartnerDataTransferUtil objs = new PartnerDataTransferUtil(); 
			objs.querySOjbects(); 
			DataTransferCompletePanel panel = new DataTransferCompletePanel();
			CsonedatatransferUI.mainLayout.removeAllComponents(); 
			CsonedatatransferUI.mainLayout.addComponent(panel);
		
		} catch (Exception e) {
			System.out.println("Partner Authentication unsuccessful");
			Notification.show("Wrong username and/or password");
		}
	
		
	}
	
	/**
	 * Method that handles enter key on fields
	 */
	private static void addEnterKeyActionToPasswordField(final PasswordField password) {
	        password.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
	            //What will be done when your Text Field is active is Enter Key is pressed
	            public void handleAction(Object sender, Object target) {
	                login(); 
	            }
	        });
	    }

	
	/**
	 * Builds the main layout of the login panel
	 */
	public static void buildLayout() {
		usernameField = new TextField("Username");
		usernameField.setValue("anhmnguy@cisco.fts.ts4svc");
		passwordField = new PasswordField("Password");
		loginButton = new Button("Login");
		Label message = new Label ("Last Step: Please sign into your destination environment.");
		message.addStyleName("label");
		addEnterKeyActionToPasswordField(passwordField); 
		
		layout.addStyleName("layoutCenter");
		layout.addComponent(message);
		layout.addComponent(usernameField);
		layout.addComponent(passwordField);
		layout.addComponent(loginButton);

	}
}