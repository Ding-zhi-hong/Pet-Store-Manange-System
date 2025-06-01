import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class PetStoreGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton deleteButton;
    private JComboBox<String> userTypeCombo;
    private List<Pet> pets;
    private List<User> users;

    public PetStoreGUI() {
        frame = new JFrame("Pet Store Management");
        pets = FileUtils.loadPetsFromFile();

        // Sample admin and user for demonstration

        // 修改此处：如果加载失败则创建默认列表
        if(pets.isEmpty()) { // 添加默认数据
            pets = new ArrayList<>();
            pets.add(new Dog("Buddy", "Male", "Golden Retriever", 2,"Dog",200));
            pets.add(new Cat("Whiskers", "Female", "Siamese", 1,"Cat",100));
            FileUtils.savePetsToFile(pets);
        }

        users = new ArrayList<>();
        users.add(new Admin("admin", "admin123"));
        users.add(new User("user1", "password"));
        users.add(new User("user2", "password"));
        users.add(new User("user3", "password"));
        users.add(new User("user4", "password"));
        users.add(new User("user5", "password"));

        initialize();
    }

    private void initialize() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        deleteButton = new JButton("Delete");
        userTypeCombo = new JComboBox<>(new String[]{"Admin", "User"});

        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(new JLabel("User Type:"));
        frame.add(userTypeCombo);
        frame.add(loginButton);
        frame.add(registerButton);
        frame.add(deleteButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userType = (String) userTypeCombo.getSelectedItem();

                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        if (userType.equals("Admin") && user instanceof Admin) {
                            JOptionPane.showMessageDialog(frame, "Welcome, Admin!");
                            Admin admin = (Admin) user;
                            admin.managePets();
                            admin.viewOrders();
                            // 登录成功后关闭当前窗口，打开宠物管理界面
                            frame.dispose();
                            new PetAdminGUI(); // 打开宠物信息界面
                            return;
                        } else if (userType.equals("User") && user instanceof User) {
                            JOptionPane.showMessageDialog(frame, "Welcome, User!");
                            // 登录成功后关闭当前窗口，打开宠物信息界面
                            frame.dispose();
                            new PetInfoGUI(user); // 打开宠物信息界面
                            return;
                        }
                    }
                }
                JOptionPane.showMessageDialog(frame, "Invalid login credentials.");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    users.add(new User(username, password));
                    JOptionPane.showMessageDialog(frame, "Registration successful!");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    for (User use : users) {
                        if (use.getUsername().equals(username) && use.getPassword().equals(password) ) {
                            users.remove(use);
                            JOptionPane.showMessageDialog(frame, "Deletion successful!");
                        }
                    }


                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new PetStoreGUI();
    }
}

