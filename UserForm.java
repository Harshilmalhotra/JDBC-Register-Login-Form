import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.JTableHeader;

public class UserForm extends JFrame {
    // Fields for Sign-Up / Sign-In
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JComboBox<String> roleBox;
    private JPasswordField passwordField;

    // Database Connection
    private Connection conn;

    // Constructor for creating the GUI and initializing the database
    public UserForm() {
        // Set up the database
        initDatabase();

        // Create UI elements
        createSignupForm();

        // Set frame properties
        setTitle("Online Compiler");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Initialize SQLite Database
    private void initDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:user_data.db");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT NOT NULL, " +
                    "last_name TEXT NOT NULL, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "role TEXT NOT NULL, " +
                    "password TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create Sign-Up form UI
    private void createSignupForm() {
        getContentPane().removeAll();

        // Add a logo at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Replace with your logo path
        topPanel.add(logoLabel);

        // Panel to hold the form components
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(242, 242, 242));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(15);
        firstNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(15);
        lastNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel roleLabel = new JLabel("Are you a?");
        String[] roles = {"Student", "Teacher", "Professional"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBackground(Color.WHITE);
        roleBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(66, 133, 244));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);

        JButton switchToSignInButton = new JButton("Already have an account? Sign In");
        switchToSignInButton.setBackground(Color.LIGHT_GRAY);
        switchToSignInButton.setFocusPainted(false);

        // Add action listeners
        signUpButton.addActionListener(new SignUpAction());
        switchToSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSignInForm();
            }
        });

        // Add components to the panel
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(roleLabel);
        formPanel.add(roleBox);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signUpButton);
        formPanel.add(switchToSignInButton);

        // Combine panels and set the layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Create Sign-In form UI
    private void createSignInForm() {
        getContentPane().removeAll();

        // Panel for logo
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Replace with your logo path
        topPanel.add(logoLabel);

        // Panel to hold the components
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(242, 242, 242));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton signInButton = new JButton("Sign In");
        signInButton.setBackground(new Color(66, 133, 244));
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);

        JButton switchToSignUpButton = new JButton("Don't have an account? Sign Up");
        switchToSignUpButton.setBackground(Color.LIGHT_GRAY);
        switchToSignUpButton.setFocusPainted(false);

        // Add action listeners
        signInButton.addActionListener(new SignInAction());
        switchToSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSignupForm();
            }
        });

        // Add components to the panel
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(signInButton);
        formPanel.add(switchToSignUpButton);

        // Combine panels and set the layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Action for sign-up
    class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String role = (String) roleBox.getSelectedItem();
            String password = new String(passwordField.getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Check if the username already exists
                    PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                    checkUser.setString(1, username);
                    ResultSet rs = checkUser.executeQuery();

                    if (rs.next()) {
                        // Username already exists
                        JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Insert new user into the database
                        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (first_name, last_name, username, role, password) VALUES (?, ?, ?, ?, ?)");
                        ps.setString(1, firstName);
                        ps.setString(2, lastName);
                        ps.setString(3, username);
                        ps.setString(4, role);
                        ps.setString(5, password);
                        ps.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Sign-up successful! You can now sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        createSignInForm(); // Redirect to the sign-in form after successful sign-up
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error signing up: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Action for sign-in
    class SignInAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("root")) {
                // Show database table for admin
                showDatabaseTable();
            } else {
                try {
                    // Verify user credentials
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        showDatabaseTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error logging in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Display the database table
    private void showDatabaseTable() {
        getContentPane().removeAll();

        try {
            // Get data from the database
            String[] columnNames = {"ID", "First Name", "Last Name", "Username", "Role"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("role")
                };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setRowHeight(25); // Set row height for better visibility
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            table.setFillsViewportHeight(true);

            // Set table header
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Arial", Font.BOLD, 16));
            header.setBackground(new Color(66, 133, 244));
            header.setForeground(Color.WHITE);
            header.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Wrap the table in a scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Set layout and add components
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            setTitle("User Database");
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserForm());
    }
}
