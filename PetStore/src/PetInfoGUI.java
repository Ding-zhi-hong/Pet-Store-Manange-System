import javax.swing.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/*
class PetInfoGUI{
    private JFrame frame;
    private JList<String> petList;
    private List<Pet> pets;  // 缓存宠物列表

    public PetInfoGUI(User user) {
        frame = new JFrame("Pet Information");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 加载并缓存宠物数据
        pets = FileUtils.loadPetsFromFile();

        petList = new JList<>(getPetNames());
        petList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加鼠标点击事件监听
        petList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // 双击触发
                    int index = petList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        showPetDetail(pets.get(index),user);
                    }
                }
            }
        });

        frame.add(new JScrollPane(petList));
        frame.setVisible(true);

    }

    private String[] getPetNames() {
        return pets.stream()
                .map(Pet::getName)
                .toArray(String[]::new);
    }

    private void showPetDetail(Pet pet,User user) {
        new PetDetailGUI(pet,user);
    }


}*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.*;

public class PetInfoGUI {
    private JFrame frame;
    private JList<String> petList;
    private List<Pet> pets;  // 缓存宠物列表
    private User user;

    public PetInfoGUI(User user) {
        this.user = user;
        frame = new JFrame("Pet Information");
        frame.setSize(500, 400); // 增加高度以容纳查询面板
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 加载并缓存宠物数据
        pets = FileUtils.loadPetsFromFile();

        petList = new JList<>(getPetNames());
        petList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加鼠标点击事件监听
        petList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // 双击触发
                    int index = petList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        showPetDetail(pets.get(index), user);
                    }
                }
            }
        });

        // 创建查询面板
        JPanel searchPanel = createSearchPanel();

        // 设置主界面布局
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(petList), BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private String[] getPetNames() {
        return pets.stream()
                .map(Pet::getName)
                .toArray(String[]::new);
    }

    private void showPetDetail(Pet pet, User user) {
        new PetDetailGUI(pet, user);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 5, 5));
        panel.setBorder(new TitledBorder("宠物查询"));

        // 创建查询选项
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"按类别", "按价格"});
        JComboBox<String> animalTypeCombo = new JComboBox<>(new String[]{"全部", "Cat", "Dog"});
        JTextField minPriceField = new JTextField("最低价");
        JTextField maxPriceField = new JTextField("最高价");
        JButton searchButton = new JButton("查询");

        // 初始状态下隐藏价格输入框
        minPriceField.setVisible(false);
        maxPriceField.setVisible(false);

        // 查询类型改变监听
        searchTypeCombo.addActionListener(e -> {
            if ("按类别".equals(searchTypeCombo.getSelectedItem())) {
                animalTypeCombo.setVisible(true);
                minPriceField.setVisible(false);
                maxPriceField.setVisible(false);
            } else {
                animalTypeCombo.setVisible(false);
                minPriceField.setVisible(true);
                maxPriceField.setVisible(true);
            }
        });

        // 输入框焦点监听
        setupTextFieldFocusListener(minPriceField);
        setupTextFieldFocusListener(maxPriceField);

        // 查询按钮监听
        searchButton.addActionListener(e -> {
            List<Pet> results = new ArrayList<>();

            if ("按类别".equals(searchTypeCombo.getSelectedItem())) {
                String selectedType = (String) animalTypeCombo.getSelectedItem();
                results = searchByType(selectedType);
            } else {
                try {
                    int minPrice = parsePrice(minPriceField.getText());
                    int maxPrice = parsePrice(maxPriceField.getText());
                    results = searchByPrice(minPrice, maxPrice);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "请输入有效的价格数字", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "没有找到匹配的宠物", "查询结果", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new QueryResultGUI(results, user);
            }
        });

        // 添加到面板
        panel.add(searchTypeCombo);
        panel.add(animalTypeCombo);
        panel.add(minPriceField);
        panel.add(maxPriceField);
        panel.add(searchButton);

        return panel;
    }

    private void setupTextFieldFocusListener(JTextField field) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("最低价") || field.getText().equals("最高价")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(field.getName().equals("min") ? "最低价" : "最高价");
                }
            }
        });

        // 设置参考名称
        field.setName(field.getText().equals("最低价") ? "min" : "max");
        field.setForeground(Color.GRAY);
    }

    private int parsePrice(String text) throws NumberFormatException {
        if ("最低价".equals(text) || "最高价".equals(text)) {
            return text.equals("最低价") ? 0 : Integer.MAX_VALUE;
        }
        return Integer.parseInt(text);
    }

    private List<Pet> searchByType(String type) {
        List<Pet> results = new ArrayList<>();
        for (Pet pet : pets) {
            if ("全部".equals(type) || pet.getClassification().equals(type)) {
                results.add(pet);
            }
        }
        return results;
    }

    private List<Pet> searchByPrice(int minPrice, int maxPrice) {
        List<Pet> results = new ArrayList<>();
        for (Pet pet : pets) {
            int price = pet.getPrice();
            if (price >= minPrice && price <= maxPrice) {
                results.add(pet);
            }
        }
        return results;
    }

    // 新增查询结果展示窗口
    private static class QueryResultGUI {
        private JFrame resultFrame;

        public QueryResultGUI(List<Pet> results, User user) {
            resultFrame = new JFrame("查询结果");
            resultFrame.setSize(500, 300);
            resultFrame.setLocationRelativeTo(null);

            // 创建结果列表
            String[] petNames = results.stream()
                    .map(pet -> pet.getName() + " (" + pet.getClassification() + " - ¥" + pet.getPrice() + ")")
                    .toArray(String[]::new);

            JList<String> resultList = new JList<>(petNames);
            resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // 添加双击事件
            resultList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int index = resultList.locationToIndex(evt.getPoint());
                        if (index >= 0) {
                            new PetDetailGUI(results.get(index), user);
                        }
                    }
                }
            });

            // 添加关闭按钮
            JButton closeButton = new JButton("关闭");
            closeButton.addActionListener(e -> resultFrame.dispose());

            // 设置布局
            resultFrame.setLayout(new BorderLayout());
            resultFrame.add(new JScrollPane(resultList), BorderLayout.CENTER);
            resultFrame.add(closeButton, BorderLayout.SOUTH);
            resultFrame.setVisible(true);
        }
    }
}

