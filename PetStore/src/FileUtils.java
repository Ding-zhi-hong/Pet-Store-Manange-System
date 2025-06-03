import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;


public class FileUtils {
    private static final String FILE_NAME = "pets.csv";
    private static final String DELIMITER = "‖";  // 使用竖线符号作为分隔符
    private static final String ESCAPE = "\\\\";    // 转义字符
    private static final Path USER_FILE = Paths.get("users.csv");

    public static boolean registerUser(User user) {
        try {
            // 确保文件存在
            if (!Files.exists(USER_FILE)) {
                Files.createFile(USER_FILE);
            }

            // 检查用户名是否已存在
            if (isUsernameTaken(user.getUsername())) {
                return false; // 用户名已被占用
            }

            // 追加新用户到文件
            try (BufferedWriter writer = Files.newBufferedWriter(USER_FILE,
                    java.nio.file.StandardOpenOption.APPEND)) {
                writer.write(user.getUsername() + "," + user.getPassword() + "\n");
            }

            return true;
        } catch (IOException ex) {
            System.err.println("用户注册失败: " + ex.getMessage());
            return false;
        }
    }

    // 检查用户名是否已存在
    private static boolean isUsernameTaken(String username) throws IOException {
        return loadUsersFromFile().stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    // 从文件加载所有用户
    public static List<User> loadUsersFromFile() {
        List<User> users = new ArrayList<>();

        try {
            if (Files.exists(USER_FILE)) {
                try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",", 2);
                        if (parts.length == 2) {
                            users.add(new User(parts[0], parts[1]));
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("加载用户数据失败: " + ex.getMessage());
        }

        return users;
    }
    // 注销用户（从文件中删除）
    public static boolean deleteUser(User userToDelete) {
        List<User> users = loadUsersFromFile();

        // 查找并移除对应用户
        boolean removed = users.removeIf(user ->
                user.getUsername().equals(userToDelete.getUsername()));

        if (removed) {
            // 重写整个文件
            try (BufferedWriter writer = Files.newBufferedWriter(USER_FILE)) {
                for (User user : users) {
                    writer.write(user.getUsername() + "," + user.getPassword() + "\n");
                }
                return true;
            } catch (IOException ex) {
                System.err.println("删除用户失败: " + ex.getMessage());
            }
        }

        return false;
    }









    public static void savePetsToFile(List<Pet> pets) {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            // 写入CSV表头
            writer.println("Name‖Gender‖Breed‖Age||classfication||price");

            for (Pet pet : pets) {
                // 处理字段中的特殊字符
                String safeName = escapeField(pet.getName());
                String safeGender = escapeField(pet.getGender());
                String safeBreed = escapeField(pet.getBreed());
                String safeclassfication = escapeField(pet.getClassification());

                String line = String.join(DELIMITER,
                        safeName,
                        safeGender,
                        safeBreed,
                        String.valueOf(pet.getAge()),
                        safeclassfication,
                        String.valueOf(pet.getPrice()));
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("保存文件失败: " + e.getMessage());
        }
    }


    // CSV字段转义方法
    private static String escapeField(String value) {
        if (value == null) return "";
        // 如果包含分隔符或换行符，则用引号包裹
        if (value.contains("‖") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }


    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;


    public static void saveOrderToFile(Order order) {
        final String DELIMITER = "‖";
        final Path path = Paths.get("orders.csv");
        final boolean fileExists = Files.exists(path);

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            // 写入表头（仅在文件不存在或为空时）
            if (!fileExists || Files.size(path) == 0) {
                writer.write("customName‖petname‖status‖time");
                writer.newLine();
            }

            // 构建数据行
            StringJoiner csvLine = new StringJoiner(DELIMITER);
            csvLine.add(escapeCsvField(order.getCustomerId()))
                    .add(escapeCsvField(order.getPet())) // 假设Order包含Pet对象
                    .add(escapeCsvField(order.getStatus()))
                    .add(escapeCsvField(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

            writer.write(csvLine.toString());
            writer.newLine();

        } catch (IOException e) {
            System.err.println("保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // CSV字段转义方法（符合RFC4180标准）
    private static String escapeCsvField(String value) {
        if (value == null) return "";
        if (value.contains("‖") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static List<Order> loadOrdersFromFile() {
        List<Order> orders = new ArrayList<>(); // 修复变量名称错误

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("orders.csv"))) {
            // 跳过CSV表头
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!" + ESCAPE + ")" + DELIMITER);
                if (parts.length == 4) {
                    try {
                        String name = unescapeField(parts[0]);
                        String gender = unescapeField(parts[1]);
                        String breed = unescapeField(parts[2]);
                        String age = unescapeField(parts[3]);
                        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        LocalDateTime dateTime = LocalDateTime.parse(age, customFormatter);
                        orders.add(new Order(name, gender, breed, dateTime));
                    } catch (NumberFormatException e) {
                        System.err.println("年龄格式错误: " + parts[3]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
        }
        return orders;
    }




    public static List<Pet> loadPetsFromFile() {
        List<Pet> pets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            // 跳过CSV表头
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("(?<!" + ESCAPE + ")" + DELIMITER);
                if (parts.length == 6) {
                    try {
                        String name = unescapeField(parts[0]);
                        String gender = unescapeField(parts[1]);
                        String breed = unescapeField(parts[2]);
                        int age = Integer.parseInt(parts[3]);
                        String classification = parts[4];
                        int price = Integer.parseInt(parts[5]);

                        if("Cat".equals(classification)) {
                        pets.add(new Cat(name, gender, breed, age, classification,price));}
                        else if("Dog".equals(classification))
                        {pets.add(new Dog(name, gender, breed, age, classification,price));}

                    } catch (NumberFormatException e) {
                        System.err.println("年龄格式错误: " + parts[3]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
        }
        return pets;
    }




/*
    private static String escapeField(String field) {
        return field.replace(ESCAPE, ESCAPE + ESCAPE)
                .replace(DELIMITER, ESCAPE + DELIMITER);
    }
*/
    private static String unescapeField(String field) {
        return field.replace(ESCAPE + DELIMITER, DELIMITER)
                .replace(ESCAPE + ESCAPE, ESCAPE);
    }


    public static void saveAllOrdersToFile(List<Order> orders) {
        Path path = Paths.get("orders.csv");

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) { // 关键：清空文件

            // 写入表头
            writer.write("customName‖petname‖status‖time");
            writer.newLine();

            for (Order order : orders) {
                StringJoiner csvLine = new StringJoiner(DELIMITER);
                csvLine.add(escapeCsvField(order.getCustomerId()))
                        .add(escapeCsvField(order.getPet())) // 假设Order包含Pet对象
                        .add(escapeCsvField(order.getStatus()))
                        .add(escapeCsvField(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

                writer.write(csvLine.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }






}