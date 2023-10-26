import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;

import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;

public class MainFrame extends JFrame {

    public static final String API_KEY = "92c8655787msh7ea35a1c5b792ebp1e18dbjsne33e6f59238a";
    final private Font mainfont = new Font("Arial", Font.BOLD, 15);
    JTextField urlfield;
    JTextArea responsefield;

    public void initialize() {

        JFrame window = new JFrame();

        JLabel Url = new JLabel("URL: ");
        Url.setFont(mainfont);
        Url.setBounds(10, 88, 100, 20);

        urlfield = new JTextField("https://weatherapi-com.p.rapidapi.com/current.json?q=53.1%2C-0.13");
        urlfield.setFont(mainfont);
        urlfield.setBounds(100, 85, 450, 30);

        JLabel parameters = new JLabel("Parameters: ");
        parameters.setFont(mainfont);
        parameters.setBounds(10, 80, 200, 300);

        JLabel responseLabel = new JLabel("Response: ");
        responseLabel.setFont(mainfont);
        responseLabel.setBounds(10, 490, 100, 20);

        JButton sendbutton = new JButton("send");
        sendbutton.setFont(mainfont);
        sendbutton.setBounds(580, 84, 80, 30);
        sendbutton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URL url = new URL(urlfield.getText());

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "Bearer " + API_KEY);

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder responsemessage = new StringBuilder();
                    String inputline;
                    while ((inputline = in.readLine()) != null) {
                        responsemessage.append(inputline);
                    }
                    in.close();

                    responsefield.setText(responsemessage.toString());

                } catch (Exception ex) {
                    responsefield.setText("Error: " + ex.getMessage());

                }

            }

        });

        String[] columnnames = { "Name", "Value" };

        Object[][] data = new Object[15][2];

        DefaultTableModel model = new DefaultTableModel(data, columnnames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // tscrollPane.setPreferredScrollableViewportSize(new Dimension(100, 500));
        scrollPane.setBounds(100, 250, 850, 200);

        responsefield = new JTextArea(12, 40);
        // responsefield.setBounds(0, 550, 100, 100);
        JScrollPane responseScrollPane = new JScrollPane(responsefield);
        responseScrollPane.setBounds(0, 550, 100, 100);
        responseScrollPane.setVisible(true);

        window.add(sendbutton);
        window.add(Url);
        window.add(urlfield);
        window.add(parameters);
        window.add(scrollPane);
        window.add(responseLabel);
        window.add(responseScrollPane, BorderLayout.SOUTH); // responsefield --> Das ist unnötig aber anderseits(siehe
                                                            // oben mit setbounds()) sieht man es nicht.
        // window.pack();
        window.setVisible(true);
        window.setTitle("HTTP Requester");
        window.setSize(1000, 800);
        window.setMinimumSize(new Dimension(300, 400));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(window);

    }

    public static void main(String[] args) {
        MainFrame myframe = new MainFrame();
        myframe.initialize();
    }

}