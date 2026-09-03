import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{	
    private DefaultTableModel tableModel;
    private JTable table;

    String cmd = null;
    // DB connection components
    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private Container content;

    private JPanel detailsPanel;
    private JScrollPane dbContentsPanel;

    private Border lineBorder;

    private JLabel IDLabel=new JLabel("ID:                 ");
    private JLabel NameLabel=new JLabel("Name:               ");
    private JLabel SquadLabel=new JLabel("Squad:      ");
    private JLabel LevelLabel=new JLabel("Level:      ");
    private JLabel PositionLabel=new JLabel("Position:        ");
    private JLabel AgeLabel=new JLabel("Age:                 ");
    private JLabel GenderLabel=new JLabel("Gender:               ");


    private JTextField IDTF= new JTextField(10);
    private JTextField NameTF=new JTextField(10);
    private JTextField SquadTF=new JTextField(10);
    private JTextField LevelTF=new JTextField(10);
    private JTextField PositionTF=new JTextField(10);
    private JTextField AgeTF=new JTextField(10);
    private JTextField GenderTF=new JTextField(10);

    // CRUD 
    private JButton getButton = new JButton("Get");
    private JButton deleteButton = new JButton("Delete");
    private JButton updateButton = new JButton("Put");
    private JButton insertButton = new JButton("Post");
    private JButton clearButton = new JButton("Clear");
    private JButton showAllButton = new JButton("Show All");
    
    // Export button
    private JButton ListAllPowers = new JButton("Export to Excel!");
    private JPanel exportButtonPanel;

    public JDBCMainWindowContent(String aTitle)
    {	
        super(aTitle, false,false,false,false);
        setEnabled(true);

        // Initialize database connection
        initiate_db_conn();
        createTableIfNotExists();

        // Setup UI layout
        content=getContentPane();
        content.setLayout(null);
        content.setBackground(Color.black);
        lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

        // Setup input form panel
        detailsPanel=new JPanel();
        detailsPanel.setLayout(new GridLayout(11,2));
        detailsPanel.setBackground(Color.pink);
        detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));
        
        // Add form components
        detailsPanel.add(IDLabel);			
        detailsPanel.add(IDTF);
        detailsPanel.add(NameLabel);		
        detailsPanel.add(NameTF);
        detailsPanel.add(SquadLabel);		
        detailsPanel.add(SquadTF);
        detailsPanel.add(LevelLabel);	
        detailsPanel.add(LevelTF);
        detailsPanel.add(PositionLabel);		
        detailsPanel.add(PositionTF);
        detailsPanel.add(AgeLabel);
        detailsPanel.add(AgeTF);
        detailsPanel.add(GenderLabel);
        detailsPanel.add(GenderTF);
    
        // Configure action buttons
        insertButton.setSize(80, 30);
        updateButton.setSize(80, 30);
        getButton.setSize(80, 30);
        deleteButton.setSize(80, 30);
        clearButton.setSize(80, 30);
        showAllButton.setSize(80, 30);
        
        // Position buttons
        insertButton.setLocation(380, 10);
        updateButton.setLocation(380, 60);
        deleteButton.setLocation(380, 110);
        getButton.setLocation(380, 160);
        clearButton.setLocation(380, 210);
        showAllButton.setLocation(380, 260);
        
        // Color code buttons by function
        getButton.setBackground(Color.green);
        deleteButton.setBackground(Color.red);
        updateButton.setBackground(Color.cyan);
        insertButton.setBackground(Color.blue);
        insertButton.setForeground(Color.white); 
        clearButton.setBackground(Color.gray);
        showAllButton.setBackground(Color.orange);

        // Add action listeners
        insertButton.addActionListener(this);
        updateButton.addActionListener(this);
        getButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        showAllButton.addActionListener(this);
        
        // Configure export section
        exportButtonPanel=new JPanel();
        exportButtonPanel.setLayout(new GridLayout(1,1));
        exportButtonPanel.setBackground(Color.pink);
        exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));

        ListAllPowers.setBackground(Color.green);
        ListAllPowers.setPreferredSize(new Dimension(200, 60));
        ListAllPowers.setFont(new Font("Arial", Font.BOLD, 14));
        
        exportButtonPanel.add(ListAllPowers);
        exportButtonPanel.setSize(400, 150);
        exportButtonPanel.setLocation(10, 420);

        ListAllPowers.addActionListener(this);

        content.add(insertButton);
        content.add(updateButton);
        content.add(getButton);
        content.add(deleteButton);
        content.add(clearButton);
        content.add(showAllButton);
    
        // Setup data table
        String[] columns = {"ID", "Name", "Squad", "Level", "Position", "Age", "Gender"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(900, 300));

        // Add table to scroll pane
        JScrollPane dbContentsPanel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dbContentsPanel.setBackground(Color.pink);
        dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Database Content"));
        
        
        detailsPanel.setSize(360, 400);
        detailsPanel.setLocation(3, 0);
        content.add(detailsPanel);
        content.add(exportButtonPanel);
        
        dbContentsPanel.setSize(700, 300);
        dbContentsPanel.setLocation(477, 0);
        content.add(dbContentsPanel);

        
        
        setSize(982, 645);
        
        setVisible(true);
        refreshDB();
    }
    
    private void createTableIfNotExists() {
        try {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "POWER", null);
            
            if (!tables.next()) {
                String createTableSQL = "CREATE TABLE power ("
                    + "id INTEGER PRIMARY KEY, "
                    + "name VARCHAR(255), "
                    + "squad VARCHAR(255), "
                    + "level INTEGER, "
                    + "position VARCHAR(255), "
                    + "age INTEGER, "
                    + "gender VARCHAR(10))";
                
                stmt.executeUpdate(createTableSQL);
                System.out.println("Created table 'power'");
            }
        } catch (SQLException e) {
            System.err.println("Error checking/creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Refreshes the table with current database contents
    public void refreshDB() {
        try {
            tableModel.setRowCount(0);
            String query = "SELECT * FROM power";
            rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("Error refreshing DB: " + e.getMessage());
        }
    }
    
    // Retrieves a single power by ID and displays it
    public void InitiateOnePower(int id) {
        try {
            tableModel.setRowCount(0);
            String query = "SELECT * FROM power WHERE id = " + id;
            rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            if (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving power: " + e.getMessage());
        }
    }
    
    public void DeleteTables() {
        try {
            String query = "DELETE FROM power";
            stmt.executeUpdate(query);
            refreshDB();
        } catch (Exception e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }
    
    public void FillTables() {
        try {
            DeleteTables();
            
            String query1 = "INSERT INTO power (id, name, squad, level, position, age, gender) " +
                "VALUES (1, 'Ichigo Kurosaki', 'Substitute', 300000000, 'Substitute Power', 15, 'M')";
            stmt.executeUpdate(query1);
            
            String query2 = "INSERT INTO power (id, name, squad, level, position, age, gender) " +
                "VALUES (2, 'Byakuya Kuchiki', 'Squad 6', 100000000, 'Captain', 150, 'M')";
            stmt.executeUpdate(query2);
            
            String query3 = "INSERT INTO power (id, name, squad, level, position, age, gender) " +
                "VALUES (3, 'Rukia Kuchiki', 'Squad 13', 500000, 'Lieutenant', 150, 'F')";
            stmt.executeUpdate(query3);
            
            refreshDB();
        } catch (Exception e) {
            System.err.println("Error filling tables: " + e.getMessage());
        }
    }

    // Initialize database connection
    public void initiate_db_conn() {
        try {
            // Load HSQLDB driver
            Class.forName("org.hsqldb.jdbcDriver");
            String url = "jdbc:hsqldb:hsql://localhost/oneDB";
            
            // Connect using default credentials
            con = DriverManager.getConnection(url, "SA", "Passw0rd");
            stmt = con.createStatement();
        } catch(Exception e) {
            System.out.println("Error: Failed to connect to database\n"+e.getMessage());
        }
    }
    
    // Handle button actions
    @SuppressWarnings("null")
    public void actionPerformed(ActionEvent e) {
        Object target = e.getSource();
        
        // Clear form fields
        if (target == clearButton) {
            IDTF.setText("");
            NameTF.setText("");
            SquadTF.setText("");
            LevelTF.setText("");
            PositionTF.setText("");
            AgeTF.setText("");
            GenderTF.setText("");
        }

        // Insert a new power
        if (target == insertButton) {		 
            try {
                String idtext = IDTF.getText();
                String nametext = NameTF.getText();
                String squadtext = SquadTF.getText();
                String LevelText = LevelTF.getText();
                String PositionText = PositionTF.getText();
                String AgeText = AgeTF.getText();
                String gendertext = GenderTF.getText();
                
                if(IDTF.getText().isEmpty() || NameTF.getText().isEmpty() || SquadTF.getText().isEmpty() || LevelTF.getText().isEmpty()
                        || PositionTF.getText().isEmpty() || AgeTF.getText().isEmpty() || GenderTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ensure all boxes have information in them");
                    return;
                }
                
                int id = Integer.parseInt(idtext);
                int levelInt = Integer.parseInt(LevelText);
                int ageint = Integer.parseInt(AgeText);
                
                String query = "INSERT INTO power (id, name, squad, level, position, age, gender) VALUES (" + 
                    id + ", '" + nametext + "', '" + squadtext + "', " + levelInt + ", '" + 
                    PositionText + "', " + ageint + ", '" + gendertext + "')";
                    
                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Record inserted successfully!");
            } catch (Exception ex) {
                System.err.println("Error inserting record: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error inserting record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                refreshDB();
            }
        }
        
        // Delete a power by ID
        if (target == deleteButton) {		 
            try {
                String idtext = IDTF.getText();
                if(IDTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "An ID is required to delete a member");
                    return;
                }
                int id = Integer.parseInt(idtext);
                
                String query = "DELETE FROM power WHERE id = " + id;
                int rowsAffected = stmt.executeUpdate(query);
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "No record found with ID: " + id);
                }
            } catch (Exception ex) {
                System.err.println("Error deleting record: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                refreshDB();
            }
        }

        if (target == showAllButton) {
            refreshDB();
        }
        // Update a power's details
        if (target == updateButton) {		 
            try {
                String idtext = IDTF.getText();
                if(IDTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "An ID is required to update a member");
                    return;
                }
                
                // Build dynamic update query based on filled fields
                StringBuilder updateQueryBuilder = new StringBuilder("UPDATE power SET ");
                boolean needComma = false;
                
                if(!NameTF.getText().isEmpty()) {
                    updateQueryBuilder.append("name = '").append(NameTF.getText()).append("'");
                    needComma = true;
                }
                
                if(!SquadTF.getText().isEmpty()) {
                    if(needComma) updateQueryBuilder.append(", ");
                    updateQueryBuilder.append("squad = '").append(SquadTF.getText()).append("'");
                    needComma = true;
                }
                
                if(!LevelTF.getText().isEmpty()) {
                    if(needComma) updateQueryBuilder.append(", ");
                    updateQueryBuilder.append("level = ").append(LevelTF.getText());
                    needComma = true;
                }
                
                if(!PositionTF.getText().isEmpty()) {
                    if(needComma) updateQueryBuilder.append(", ");
                    updateQueryBuilder.append("position = '").append(PositionTF.getText()).append("'");
                    needComma = true;
                }
                
                if(!AgeTF.getText().isEmpty()) {
                    if(needComma) updateQueryBuilder.append(", ");
                    updateQueryBuilder.append("age = ").append(AgeTF.getText());
                    needComma = true;
                }
                
                if(!GenderTF.getText().isEmpty()) {
                    if(needComma) updateQueryBuilder.append(", ");
                    updateQueryBuilder.append("gender = '").append(GenderTF.getText()).append("'");
                }
                
                updateQueryBuilder.append(" WHERE id = ").append(idtext);
                
                if(needComma) {
                    String query = updateQueryBuilder.toString();
                    int rowsAffected = stmt.executeUpdate(query);
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Record updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No record found with ID: " + idtext);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No fields to update!");
                }
            } catch (Exception ex) {
                System.err.println("Error updating record: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error updating record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                refreshDB();
            }
        }
        
        if(target == this.getButton) {
            try {					
                String idtext = IDTF.getText();
                if(IDTF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "An ID is required to retrieve a member");
                    return;
                }
                int id = Integer.parseInt(idtext);
                InitiateOnePower(id);
            } catch (Exception ex) {
                System.err.println("Error retrieving record: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error retrieving record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Export data to CSV
        if(target == this.ListAllPowers) {
            try {
                cmd = "select * from power";
                rs = stmt.executeQuery(cmd); 	
                writeToFile(rs);
                JOptionPane.showMessageDialog(null, "Data exported to MyOutput.csv successfully!");
            } catch(Exception e1) {
                System.err.println("Error exporting data: " + e1.getMessage());
                JOptionPane.showMessageDialog(null, "Error exporting data: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Export result set to CSV file
    private void writeToFile(ResultSet rs) {
        try {
            System.out.println("In writeToFile");
            FileWriter outputFile = new FileWriter("MyOutput.csv");
            PrintWriter printWriter = new PrintWriter(outputFile);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();

            // Write header row
            for(int i=0; i<numColumns; i++) {
                printWriter.print(rsmd.getColumnLabel(i+1) + ",");
            }
            printWriter.print("\n");
            
            // Write data rows
            while(rs.next()) {
                for(int i=0; i<numColumns; i++) {
                    printWriter.print(rs.getString(i+1) + ",");
                }
                printWriter.print("\n");
                printWriter.flush();
            }
            printWriter.close();
            System.out.println("File write complete");
        } catch(Exception e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}