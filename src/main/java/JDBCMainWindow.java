import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class JDBCMainWindow extends JFrame implements ActionListener
{
    private JMenuItem exitItem;
    private JMenuItem infoItem;
    private JMenuItem fillItem;
    private JMenuItem clearItem;
    private JMenuItem powerItem;
    JDBCMainWindowContent aWindowContent = new JDBCMainWindowContent("Power");

    public JDBCMainWindow()
    {
        // Main window setup
        super("JDBC 2024 Assignment"); 
        
        // Menu configuration
        JMenuBar menuBar=new JMenuBar();
        JMenu fileMenu=new JMenu("File");
        exitItem = new JMenuItem("Exit");
        infoItem = new JMenuItem("Project Info");
        fillItem = new JMenuItem("Fill Table");
        clearItem = new JMenuItem("Clear Table");
        powerItem = new JMenuItem("Power");
        
        fileMenu.add(exitItem);
        fileMenu.add(infoItem);
        fileMenu.add(fillItem);
        fileMenu.add(clearItem);
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);
        
        // Add listeners
        exitItem.addActionListener(this);
        infoItem.addActionListener(this);
        fillItem.addActionListener(this);
        clearItem.addActionListener(this);
        
        // Add content
        getContentPane().add(aWindowContent);
        
        setSize(1200, 600);
        setVisible(true);
    }

    // Handle menu actions
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(exitItem)){
            this.dispose();
        }
        else if(e.getSource().equals(infoItem)){
            JOptionPane.showMessageDialog(null,
                "This information contained within the database in this project is based on the Power characters and their level in the fictional universe of Bleach. It displays the power's name, squad, level, position etc");
        }
        else if(e.getSource().equals(fillItem)){
            aWindowContent.FillTables();
        }
        else if(e.getSource().equals(clearItem)){
            aWindowContent.DeleteTables();
        }
    }
}