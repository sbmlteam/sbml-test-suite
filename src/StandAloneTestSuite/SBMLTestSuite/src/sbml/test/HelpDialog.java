package sbml.test;
//
//  Created by Jan Hettenhausen on 25/07/07.
//  
//
// @file    HelpDialog.java
// @brief   HelpDialog class for SBML Standalone application
// @edited by   Kimberly Begley
// 

//
//<!---------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// 
// Copyright 2008      California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available at
// http://sbml.org/Software/SBML_Test_Suite/license.html
//------------------------------------------------------------------------- -->
// Generate help dialog for SBML Test Suite - Standalone application.
//


import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.util.Properties;
import java.awt.color.*;
import java.util.Random;
import java.awt.event.*;
import javax.swing.border.*;
import java.net.URL;
import java.io.*;


public class HelpDialog extends JDialog implements ActionListener{
    
/**
 * This class provides help dialog for the Standalone SBML Test Suite.
 * 
 * @author Kimberly Begley
 * @version 2.0
 */

    /**
     * HelpDialog has one constructor to create and fill the content of the frame that contains the help dialog.
     * @param parent the parent JFrame of the calling class.
     */
	public HelpDialog(JFrame parent) {
		super(parent, "Help", false);
		setVisible(true);
		setSize(new Dimension(800,600));
		//setBorder(BorderFactory.createEmptyBorder(10,100,10,100));
		getContentPane().setLayout(new BorderLayout());
		add(new JLabel("help"), BorderLayout.CENTER);
		JPanel backbuttonPanel = new JPanel();
		backbuttonPanel.setLayout(new BoxLayout(backbuttonPanel,BoxLayout.PAGE_AXIS));
		JButton backButton = new JButton("Close");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backbuttonPanel.add(backButton);
		add(backbuttonPanel, BorderLayout.SOUTH);		
		
		URL helpURL = getClass().getResource("resources/about.html");
		try {
			JEditorPane helpDoc = new JEditorPane(helpURL);
			scrollPane = new JScrollPane(helpDoc);
		} 
		catch (IOException ex) {
			scrollPane = new JScrollPane(new JLabel("Help File not found"));
		}
			
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		
		add(scrollPane,BorderLayout.CENTER);

		setLocationRelativeTo(getParent());

		backButton.addActionListener(this);
	}
	
	
/*	public HelpDialog(JDialog parent) {
		super(parent, "Help", false);
		setVisible(false);
		setSize(new Dimension(800,600));
		//		setBorder(BorderFactory.createEmptyBorder(10,100,10,100));
		getContentPane().setLayout(new BorderLayout());
		add(new JLabel("help"), BorderLayout.CENTER);
		JPanel backbuttonPanel = new JPanel();
		backbuttonPanel.setLayout(new BoxLayout(backbuttonPanel,BoxLayout.PAGE_AXIS));
		JButton backButton = new JButton("Close");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backbuttonPanel.add(backButton);
		add(backbuttonPanel, BorderLayout.SOUTH);		
		
		URL helpURL = getClass().getResource("help.html");
		try {
			JEditorPane helpDoc = new JEditorPane(helpURL);
			scrollPane = new JScrollPane(helpDoc);
		} 
		catch (IOException ex) {
			scrollPane = new JScrollPane(new JLabel("Help File not found"));
		}
		
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		
		add(scrollPane,BorderLayout.CENTER);
		
		setLocationRelativeTo(getParent());
		
		backButton.addActionListener(this);
	} */
/**
 * actionPerformed Listener that listens for the close action of the help dialog 
 * @param e the actionEvent called when the close button is selected.
 */
	
	public void actionPerformed (ActionEvent e) {
	//	this.hide();
		this.dispose();
	}
	
	JScrollPane scrollPane;

	
	
	
}
