package com.example.csonedatatransfer;

import java.io.File;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("csonedatatransfer_v2")
public class CsonedatatransferUI extends UI {
	
	/**
	 * Main layout and entry point to the application 
	 * @author anhmnguy
	 *
	 */

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = CsonedatatransferUI.class)
	public static class Servlet extends VaadinServlet {
	}
	public static final VerticalLayout mainLayout = new VerticalLayout();

	@Override
	protected void init(VaadinRequest request) {
		mainLayout.removeAllComponents();
		mainLayout.setMargin(true);
		setContent(mainLayout);
		
		// Find the application directory
		String basepath = VaadinService.getCurrent()
		                  .getBaseDirectory().getAbsolutePath();
		
		Image logo = new Image("", new FileResource(new File(basepath+"/WEB-INF/images/Logo.png"))); 
		logo.addStyleName("logo");

		SourceLoginPanel login = new SourceLoginPanel(); 
		login.setStyleName("panelLayout");
		login.setWidth("300px");
		mainLayout.addComponent(logo);
		mainLayout.addComponent(login);
	}

}