import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;

public class PetAdminGUI {
    private JFrame frame;
    private JList<String> petList;
    private List<Pet> pets;
    private DefaultListModel<String> listModel;

    public PetAdminGUI() {
        initializeUI();
        loadData();
        setupUIComponents();
    }

    private void initializeUI() {
        frame = new JFrame("宠物管理系统");
        frame.setSize(700, 500);  // 增大窗口尺寸以容纳更多按钮
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
    }

    private void loadData() {
        pets = FileUtils.loadPetsFromFile();
    }

    private void setupUIComponents() {
        // 初始化列表模型
        listModel = new DefaultListModel<>();
        updateListModel();

        // 主列表区域
        petList = new JList<>(listModel);
        petList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(petList);
        listScroll.setBorder(BorderFactory.createTitledBorder("宠物列表"));

        // 底部按钮面板 - 使用GridLayout实现按钮分组
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 第一组：宠物操作按钮
        JPanel petButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        petButtonPanel.setBorder(BorderFactory.createTitledBorder("宠物操作"));

        // 删除按钮
        JButton deleteButton = new JButton("删除宠物");
        deleteButton.addActionListener(this::handleDeletePet);

        // 新增按钮
        JButton addButton = new JButton("新增宠物");
        addButton.addActionListener(this::handleAddPet);

        petButtonPanel.add(deleteButton);
        petButtonPanel.add(addButton);

        // 第二组：订单管理按钮
        JPanel orderButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        orderButtonPanel.setBorder(BorderFactory.createTitledBorder("订单管理"));

        // 订单管理按钮
        JButton orderButton = new JButton("订单管理");
        orderButton.addActionListener(this::handleNewWindow);

        orderButtonPanel.add(orderButton);

        // 第三组：其他功能按钮（预留）
        JPanel otherButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        otherButtonPanel.setBorder(BorderFactory.createTitledBorder("其他功能"));

        // 添加按钮到主面板
        bottomPanel.add(petButtonPanel);
        bottomPanel.add(orderButtonPanel);
        bottomPanel.add(otherButtonPanel);

        // 双击查看详情
        petList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showSelectedPetDetails();
                }
            }
        });

        frame.add(listScroll, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // 更新列表数据
    private void updateListModel() {
        listModel.clear();
        pets.stream()
                .map(Pet::getName)
                .forEach(listModel::addElement);
    }

    // 删除选中宠物
    private void handleDeletePet(ActionEvent e) {
        int index = petList.getSelectedIndex();
        if (index >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "确定要删除宠物 '" + pets.get(index).getName() + "' 吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                pets.remove(index);
                FileUtils.savePetsToFile(pets);
                updateListModel();
                JOptionPane.showMessageDialog(frame, "删除成功！");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "请先选择要删除的宠物",
                    "提示", JOptionPane.WARNING_MESSAGE);
        }
    }

    // 打开新增宠物界面
    private void handleAddPet(ActionEvent e) {
        AddPetDialog dialog = new AddPetDialog(frame);
        if (dialog.showDialog()) {
            Pet newPet = dialog.getPet();
            pets.add(newPet);
            FileUtils.savePetsToFile(pets);
            updateListModel();
            JOptionPane.showMessageDialog(frame, "成功添加宠物: " + newPet.getName());
        }
    }

    // 打开订单管理界面
    private void handleNewWindow(ActionEvent e) {
        new PetInforGUIAdmin();
    }

    // 刷新数据
    private void refreshData() {
        pets = FileUtils.loadPetsFromFile();
        updateListModel();
    }

    // 查看详情
    private void showSelectedPetDetails() {
        int index = petList.getSelectedIndex();
        if (index >= 0) {
            new petoperate(pets.get(index));
        }
    }

    // 新增宠物对话框（简化版）
    private static class AddPetDialog extends JDialog {
        private JTextField nameField = new JTextField(20);
        private JTextField breedField = new JTextField(20);
        private JComboBox<String> genderCombo = new JComboBox<>(new String[]{"公", "母"});
        private JComboBox<String> classanimal = new JComboBox<>(new String[]{"Cat", "Dog"});
        private JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        private boolean confirmed = false;
        private JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 50));

        public AddPetDialog(JFrame parent) {
            super(parent, "添加新宠物", true);
            setLayout(new GridLayout(7, 2, 10, 10));

            add(new JLabel("名称:"));
            add(nameField);
            add(new JLabel("品种:"));
            add(breedField);
            add(new JLabel("性别:"));
            add(genderCombo);
            add(new JLabel("年龄:"));
            add(ageSpinner);
            add(new JLabel("动物类别："));
            add(classanimal);
            add(new JLabel("价格："));
            add(priceSpinner);

            JButton confirm = new JButton("确认");
            confirm.addActionListener(e -> {
                confirmed = true;
                dispose();
            });

            JButton cancel = new JButton("取消");
            cancel.addActionListener(e -> dispose());

            add(confirm);
            add(cancel);

            pack();
            setLocationRelativeTo(parent);
        }

        public boolean showDialog() {
            setVisible(true);
            return confirmed;
        }

        public Pet getPet() {
            if ("Cat".equals((String)classanimal.getSelectedItem())){
                return new Cat(
                        nameField.getText().trim(),
                        (String) genderCombo.getSelectedItem(),
                        breedField.getText().trim(),
                        (int) ageSpinner.getValue(),
                        (String) classanimal.getSelectedItem(),
                        (Integer) priceSpinner.getValue()


                );}
            else {
                return new Dog(
                        nameField.getText().trim(),
                        (String) genderCombo.getSelectedItem(),
                        breedField.getText().trim(),
                        (int) ageSpinner.getValue(),
                        (String) classanimal.getSelectedItem(),
                        (Integer) priceSpinner.getValue()

                );
            }

    }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetAdminGUI::new);
    }
}


