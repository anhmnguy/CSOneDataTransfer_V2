package com.example.csonedatatransfer.partnerUtil;

import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Login utility.
 */
public class PartnerMetadataLoginUtil {

	public static PartnerConnection connection; 

	/**
	 * Attempt to establishes a partner connection
	 * @param username username
	 * @param password password
	 * @param soapAddress soap address of salesforce connection 
	 * @return The metadata connection 
	 * @throws ConnectionException
	 */
	public static MetadataConnection login(String username, String password,
			String soapAddress) throws ConnectionException {
		final String USERNAME = username;
		final String PASSWORD = password;
		final String URL = soapAddress;
		final LoginResult loginResult = loginToSalesforce(USERNAME, PASSWORD,
				URL);
		
		// Reset Destination URL. The URL returned from login must be set in the SforceService
		 ConnectorConfig newConfig = new ConnectorConfig();
         newConfig.setSessionId(
                 loginResult.getSessionId());
         newConfig.setServiceEndpoint(
                 loginResult.getServerUrl());
         connection = Connector.newConnection(newConfig);
		
		return createMetadataConnection(loginResult);
	}

	/**
	 * Creates a metadata connection 
	 * @param loginResult login result
	 * @return the metadata connection 
	 * @throws ConnectionException
	 */
	private static MetadataConnection createMetadataConnection(
			final LoginResult loginResult) throws ConnectionException {
		ConnectorConfig metaDataConfig = new ConnectorConfig();
		metaDataConfig.setServiceEndpoint(loginResult.getMetadataServerUrl());
		metaDataConfig.setSessionId(loginResult.getSessionId());
		return new MetadataConnection(metaDataConfig);
	}
	/**
	 * Attempt to login into salesforce 
	 * @param username username
	 * @param password password
	 * @param loginUrl soap address of salesforce environment
	 * @return the login result
	 * @throws ConnectionException
	 */
	private static LoginResult loginToSalesforce(final String username,
			final String password, final String loginUrl)
			throws ConnectionException {
		ConnectorConfig loginConfig = new ConnectorConfig();
		loginConfig.setAuthEndpoint(loginUrl);
		loginConfig.setServiceEndpoint(loginUrl);
		loginConfig.setManualLogin(true);
		connection = new PartnerConnection(loginConfig);
		return (connection.login(username, password));
	}
	
	/**
	 * Get the partner connection 
	 * @return partner connection 
	 */
	public static PartnerConnection getConnection() {
		return connection; 
	}
}