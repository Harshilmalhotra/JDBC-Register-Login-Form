import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
        setSize(400, 450); // Increase size to accommodate changes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
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
        // Clear the content pane
        getContentPane().removeAll();

        // Add a logo at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Replace with your logo path
        topPanel.add(logoLabel);

        // Panel to hold the form components
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the form
        formPanel.setBackground(new Color(242, 242, 242)); // Light background

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
        String[] roles = {"Student", "Teacher", "Professional"}; // Added Admin role
        roleBox = new JComboBox<>(roles);
        roleBox.setBackground(Color.WHITE);
        roleBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(66, 133, 244)); // Google-like blue button
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
        // Clear the content pane
        getContentPane().removeAll();

        // Panel for logo
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel(new ImageIcon("logo.png")); // Replace with your logo path
        topPanel.add(logoLabel);

        // Panel to hold the components
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        formPanel.setBackground(new Color(242, 242, 242)); // Light background

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

                        JOptionPane.showMessageDialog(null, "Sign Up successful! You can now sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        createSignInForm(); // Switch to sign-in form
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

            try {
                // Check if the user exists
                PreparedStatement ps = conn.prepareStatement("SELECT role FROM users WHERE username = ? AND password = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");

                    if (role.equals("Admin")) {
                        // Admin logged in, show database contents
                        showDatabaseContents();
                    } else {
                        // Non-admin user logged in
                        JOptionPane.showMessageDialog(null, "Sign In successful! Welcome, " + username + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error signing in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show database contents in a new window
    private void showDatabaseContents() {
        JFrame dataFrame = new JFrame("User Database");
        dataFrame.setSize(600, 400);
        dataFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dataFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String username = rs.getString("username");
                String role = rs.getString("role");
                textArea.append("ID: " + id + ", Name: " + firstName + " " + lastName + ", Username: " + username + ", Role: " + role + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        dataFrame.add(scrollPane);
        dataFrame.setVisible(true);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        new UserForm();
    }
}
