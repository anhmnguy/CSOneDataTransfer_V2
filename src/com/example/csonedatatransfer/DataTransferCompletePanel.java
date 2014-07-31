package com.example.csonedatatransfer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class DataTransferCompletePanel extends Panel{
	
	private VerticalLayout layout; 
	private Button importMore; 
	public DataTransferCompletePanel(){
		layout = new VerticalLayout();
		setContent(layout);
		buildLayout();
		
		importMore.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				SourceLoginPanel loginPanel = new SourceLoginPanel(); 
				CsonedatatransferUI.mainLayout.removeAllComponents();
				CsonedatatransferUI.mainLayout.addComponent(loginPanel);
			}
		});
		
	}
	public void buildLayout(){
		Label congrats = new Label("Congrats, your data have been successfully imported");
		importMore = new Button("Import More"); 
		importMore.addStyleName("moreButton");
		layout.setHeight("150px");
		congrats.addStyleName("label");
		layout.addComponent(congrats);
		layout.addComponent(importMore);
	}

}
