package dbi.jdbc;

import javax.swing.*;
import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    static final String USER = "c##dbi";
    static final String PW = "dbi2021";

    public static void main(String[] args) {
        init();
    }

    public static String sql(String query){
        StringBuilder s = new StringBuilder(query + "\n");
        // Open connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PW);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {
            // Retrieve data from ResultSet
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();

                int columnCount =  rsmd.getColumnCount();
                for(int i = 1; i < columnCount + 1; i++){
                    int type = rsmd.getColumnType(i);
                    s = new StringBuilder(switch (type) {
                        case Types.VARCHAR, Types.CHAR -> s + " " + rs.getString(i);
                        case Types.NUMERIC -> s + " " + rs.getInt(i);
                        default -> s + " " + "Type:" + Types.NUMERIC;
                    });

                }
                s.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public static void init() {
        JFrame mainFrame = new JFrame();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Database connection");

        JTextArea input = new JTextArea("Enter a valid SQL String. Using CAP-Database. \n Example: Select cid, city from customers");

        JButton commit = new JButton("Commit");

        JTextArea output = new JTextArea();
        output.setEditable(false);

        commit.addActionListener(e -> output.setText(sql(input.getText())));

        mainPanel.add(label);
        mainPanel.add(input);
        mainPanel.add(commit);
        mainPanel.add(output);

        mainFrame.add(mainPanel);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(720, 480);
        mainFrame.setVisible(true);
    }
}