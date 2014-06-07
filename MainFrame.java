package com.DanielNorman.CSVPractice;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable recordTable;
	private JTextField idField;
	private JTextField dobField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JButton deleteRecordButton;
	private JButton editRecordButton;
	private JTextField idAEField;
	private JTextField dobAEField;
	private JTextField lastNameAEField;
	private JTextField firstNameAEField;
	private JTabbedPane tabbedPane;
	private JButton saveEditButton;
	private JButton addRecordButton;
	
	
	public static List<Record> recordList = new ArrayList<Record>();
	public int lastEditingID;
	private JButton clearButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					readCSVFile("records.csv");
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	        	generateCSVFile("records.csv");
	        }
	    }, "Shutdown-thread"));
	}
	
	private static void readCSVFile(String fileName)
	{
		BufferedReader br = null;
		String line = "";
	 
		try
		{
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null)
			{
				String[] data = line.split(",");
				String[] dateInfo = data[3].split("/");
				Record rec = new Record(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(dateInfo[0]), Integer.parseInt(dateInfo[1]), Integer.parseInt(dateInfo[2]));
				recordList.add(rec);
			}
	 
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { } finally {
			if (br != null)
			{
				try { br.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
		}
		
	}
	
	private static void generateCSVFile(String fileName)
	{
		try
		{
		    FileWriter writer = new FileWriter(fileName);
	 
		    for (Record rec : recordList)
		    {
		    	String line = rec.id + "," + rec.firstName + "," + rec.lastName + "," + rec.getDobString() + "\n";
		    	writer.append(line);
		    }
		    writer.flush();
		    writer.close();
		}
		catch(IOException e) { }
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MainFrame() {
		setTitle("Record Keeper");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 479, 379);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel searchPanel = new JPanel();
		tabbedPane.addTab("Search", null, searchPanel, null);
		searchPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 87, 428, 171);
		searchPanel.add(scrollPane);
		
		recordTable = new JTable();

		scrollPane.setViewportView(recordTable);
		recordTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		recordTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				"ID", "First Name", "Last Name", "DOB (mm/dd/yyyy)"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		recordTable.getColumnModel().getColumn(0).setPreferredWidth(44);
		recordTable.getColumnModel().getColumn(3).setPreferredWidth(102);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblId.setBounds(70, 11, 46, 14);
		searchPanel.add(lblId);
		
		idField = new JTextField();
		idField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		idField.setBounds(126, 8, 86, 20);
		searchPanel.add(idField);
		idField.setColumns(10);
		
		JLabel lblDobmmddyyyy = new JLabel("DOB (mm/dd/yyyy):");
		lblDobmmddyyyy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDobmmddyyyy.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDobmmddyyyy.setBounds(222, 11, 120, 14);
		searchPanel.add(lblDobmmddyyyy);
		
		dobField = new JTextField();
		dobField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dobField.setColumns(10);
		dobField.setBounds(352, 8, 86, 20);
		searchPanel.add(dobField);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFirstName.setBounds(10, 39, 106, 14);
		searchPanel.add(lblFirstName);
		
		firstNameField = new JTextField();
		firstNameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		firstNameField.setColumns(10);
		firstNameField.setBounds(126, 36, 86, 20);
		searchPanel.add(firstNameField);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblLastName.setBounds(236, 39, 106, 14);
		searchPanel.add(lblLastName);
		
		lastNameField = new JTextField();
		lastNameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lastNameField.setColumns(10);
		lastNameField.setBounds(352, 36, 86, 20);
		searchPanel.add(lastNameField);
		
		deleteRecordButton = new JButton("Delete Record");
		deleteRecordButton.setEnabled(false);
		deleteRecordButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		deleteRecordButton.setBounds(222, 269, 120, 23);
		searchPanel.add(deleteRecordButton);
		
		editRecordButton = new JButton("Edit Record");
		editRecordButton.setEnabled(false);
		editRecordButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		editRecordButton.setBounds(92, 269, 120, 23);
		searchPanel.add(editRecordButton);
		
		JPanel addEditPanel = new JPanel();
		tabbedPane.addTab("Add/Edit", null, addEditPanel, null);
		addEditPanel.setLayout(null);
		
		idAEField = new JTextField();
		idAEField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		idAEField.setColumns(10);
		idAEField.setBounds(126, 64, 86, 20);
		addEditPanel.add(idAEField);
		
		dobAEField = new JTextField();
		dobAEField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dobAEField.setColumns(10);
		dobAEField.setBounds(352, 64, 86, 20);
		addEditPanel.add(dobAEField);
		
		lastNameAEField = new JTextField();
		lastNameAEField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lastNameAEField.setColumns(10);
		lastNameAEField.setBounds(352, 109, 86, 20);
		addEditPanel.add(lastNameAEField);
		
		firstNameAEField = new JTextField();
		firstNameAEField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		firstNameAEField.setColumns(10);
		firstNameAEField.setBounds(126, 109, 86, 20);
		addEditPanel.add(firstNameAEField);
		
		JLabel label = new JLabel("First Name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setBounds(10, 112, 106, 14);
		addEditPanel.add(label);
		
		JLabel label_1 = new JLabel("Last Name:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_1.setBounds(236, 112, 106, 14);
		addEditPanel.add(label_1);
		
		JLabel label_2 = new JLabel("DOB (mm/dd/yyyy):");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_2.setBounds(222, 67, 120, 14);
		addEditPanel.add(label_2);
		
		JLabel label_3 = new JLabel("ID:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(70, 67, 46, 14);
		addEditPanel.add(label_3);
		
		saveEditButton = new JButton("Save Edit");
		saveEditButton.setEnabled(false);
		saveEditButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		saveEditButton.setBounds(10, 196, 120, 23);
		addEditPanel.add(saveEditButton);
		
		addRecordButton = new JButton("Add Record");
		addRecordButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addRecordButton.setBounds(164, 196, 120, 23);
		addEditPanel.add(addRecordButton);
		
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		clearButton.setBounds(318, 196, 120, 23);
		addEditPanel.add(clearButton);
		addEditPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{firstNameAEField, dobAEField, lastNameAEField, saveEditButton, addRecordButton, clearButton, idAEField, label, label_1, label_2, label_3}));
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tabbedPane, addEditPanel, searchPanel, scrollPane, recordTable, lblId, idField, lblDobmmddyyyy, dobField, lblFirstName, firstNameField, lblLastName, lastNameField, deleteRecordButton, editRecordButton, idAEField, dobAEField, lastNameAEField, firstNameAEField, label, label_1, label_2, label_3, saveEditButton, addRecordButton, clearButton}));
		
		
		//Field listeners
		idField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { updateTable(); }
			public void removeUpdate(DocumentEvent e) { updateTable(); }
			public void insertUpdate(DocumentEvent e) { updateTable(); }
		});
		firstNameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { updateTable(); }
			public void removeUpdate(DocumentEvent e) { updateTable(); }
			public void insertUpdate(DocumentEvent e) { updateTable(); }
		});
		lastNameField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { updateTable(); }
			public void removeUpdate(DocumentEvent e) { updateTable(); }
			public void insertUpdate(DocumentEvent e) { updateTable(); }
		});
		dobField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) { updateTable(); }
			public void removeUpdate(DocumentEvent e) { updateTable(); }
			public void insertUpdate(DocumentEvent e) { updateTable(); }
		});
		
		//Table selection listener
		recordTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e)
			{
				deleteRecordButton.setEnabled(recordTable.getSelectedRowCount() > 0);
				editRecordButton.setEnabled(recordTable.getSelectedRowCount() > 0);
			}
		});
		
		//Edit button listener
		editRecordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				tabbedPane.setSelectedIndex(1);
				saveEditButton.setEnabled(true);
				addRecordButton.setText("Cancel Edit");
				
				int row = recordTable.getSelectedRow();
				for (Record rec : recordList)
				{
					if (rec.id == (Integer) recordTable.getModel().getValueAt(row, 0)) //Populate the text fields on the Add/Edit view
					{
						idAEField.setText("" + rec.id);
						firstNameAEField.setText(rec.firstName);
						lastNameAEField.setText(rec.lastName);
						dobAEField.setText(rec.getDobString());
						lastEditingID = rec.id;
						break;
					}
				}
			}
		});
		
		//Delete button listener
		deleteRecordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int row = recordTable.getSelectedRow();
				for (Record rec : recordList)
				{
					if (rec.id == (Integer) recordTable.getModel().getValueAt(row, 0)) //Populate the text fields on the Add/Edit view
					{
						recordList.remove(rec);
						break;
					}
				}
				updateTable();
			}
		});
		
		//Save edit button listener
		saveEditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				for (Record rec : recordList)
				{
					if (rec.id == lastEditingID) //Update the record we chose to edit
					{
						rec.id = Integer.parseInt(idAEField.getText());
						rec.firstName = firstNameAEField.getText();
						rec.lastName = lastNameAEField.getText();
						rec.dob = rec.new SimpleDate(dobAEField.getText());
						break;
					}
				}
				
				addRecordButton.setText("Add Record");
				saveEditButton.setEnabled(false);
				idAEField.setText(null);
				firstNameAEField.setText(null);
				lastNameAEField.setText(null);
				dobAEField.setText(null);
				
				//Sort the record list
				Collections.sort(recordList, new Comparator<Record>() {
					public int compare(Record r1, Record r2)
					{
						if (r1.id != r2.id) return r1.id - r2.id;
						if (!r1.lastName.equals(r2.lastName)) return r1.lastName.compareTo(r2.lastName);
						return r1.firstName.compareTo(r2.firstName);
					}
				});
				
				updateTable();
			}
		});
		
		//Add record/Cancel edit button listener
		addRecordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (addRecordButton.getText().equals("Add Record")) //Create and add a new Record to the list
				{
					Record rec = new Record(Integer.parseInt(idAEField.getText()), firstNameAEField.getText(), lastNameAEField.getText(), null);
					rec.dob = rec.new SimpleDate(dobAEField.getText());
					recordList.add(rec);
					
					//Sort the record list
					Collections.sort(recordList, new Comparator<Record>() {
						public int compare(Record r1, Record r2)
						{
							if (r1.id != r2.id) return r1.id - r2.id;
							if (!r1.lastName.equals(r2.lastName)) return r1.lastName.compareTo(r2.lastName);
							return r1.firstName.compareTo(r2.firstName);
						}
					});
				}
				
				addRecordButton.setText("Add Record");
				saveEditButton.setEnabled(false);
				idAEField.setText(null);
				firstNameAEField.setText(null);
				lastNameAEField.setText(null);
				dobAEField.setText(null);
				
				updateTable();
			}
		});
		
		//Clear button listener
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				addRecordButton.setText("Add Record");
				saveEditButton.setEnabled(false);
				idAEField.setText(null);
				firstNameAEField.setText(null);
				lastNameAEField.setText(null);
				dobAEField.setText(null);
			}
		});
		
		//Sort the record list
		Collections.sort(recordList, new Comparator<Record>() {
			public int compare(Record r1, Record r2)
			{
				if (r1.id != r2.id) return r1.id - r2.id;
				if (!r1.lastName.equals(r2.lastName)) return r1.lastName.compareTo(r2.lastName);
				return r1.firstName.compareTo(r2.firstName);
			}
		});

		updateTable();
	}

	public void updateTable()
	{
		DefaultTableModel model = (DefaultTableModel) recordTable.getModel();
		model.setRowCount(0);
		
		for (Record rec : recordList)
		{
			if (idField.getText().isEmpty() &&
				firstNameField.getText().isEmpty() &&
				lastNameField.getText().isEmpty() &&
				dobField.getText().isEmpty())
			{
				model.addRow(new Object[]{rec.id, rec.firstName, rec.lastName, rec.getDobString()});
			}
			else if (("" + rec.id).startsWith(idField.getText()) &&
				rec.firstName.toLowerCase().startsWith(firstNameField.getText().toLowerCase()) &&
				rec.lastName.toLowerCase().startsWith(lastNameField.getText().toLowerCase()) &&
				rec.getDobString().startsWith(dobField.getText()))
			{
				model.addRow(new Object[]{rec.id, rec.firstName, rec.lastName, rec.getDobString()});
			}
		}
	}
}
