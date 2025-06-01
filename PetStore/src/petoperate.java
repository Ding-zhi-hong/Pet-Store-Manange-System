import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class petoperate {
    private JFrame detailFrame;
    private Pet pet;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField breedField;
    private JTextField genderField;
    private JTextField priceField;
    public petoperate(Pet pet) {
        this.pet = pet;
        initializeUI();
        setupComponents();
    }

    private void initializeUI() {
        detailFrame = new JFrame(pet.getName() + " 详情");
        detailFrame.setSize(350, 250);
        detailFrame.setLayout(new BorderLayout(10, 10));
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupComponents() {
        // 信息输入面板
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        nameField = createEditableField(pet.getName());
        ageField = createEditableField(String.valueOf(pet.getAge()));
        breedField = createEditableField(pet.getBreed());
        genderField = createEditableField(pet.getGender());
        priceField=createEditableField(String.valueOf(pet.getPrice()));
        addDetailRow(infoPanel, "名称:", nameField);
        addDetailRow(infoPanel, "年龄:", ageField);
        addDetailRow(infoPanel, "品种:", breedField);
        addDetailRow(infoPanel, "性别:", genderField);
        addDetailRow(infoPanel, "价格:", priceField);
        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(this::handleSave);

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> detailFrame.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // 组装界面
        detailFrame.add(infoPanel, BorderLayout.CENTER);
        detailFrame.add(buttonPanel, BorderLayout.SOUTH);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setVisible(true);
    }

    private JTextField createEditableField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return field;
    }

    private void addDetailRow(JPanel panel, String label, JComponent field) {
        JLabel titleLabel = new JLabel(label);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(titleLabel);
        panel.add(field);
    }

    private void handleSave(ActionEvent e) {
        try {
            // 输入验证
            String newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                throw new IllegalArgumentException("名称不能为空");
            }

            int newAge = Integer.parseInt(ageField.getText().trim());
            if (newAge < 0) {
                throw new IllegalArgumentException("年龄不能为负数");
            }

            // 更新宠物对象
            pet.setName(newName);
            pet.setAge(newAge);
            pet.setBreed(breedField.getText().trim());
            pet.setGender(genderField.getText().trim());
            pet.setPrice(Integer.parseInt(priceField.getText().trim()));

            // 保存到文件
            List<Pet> pets = FileUtils.loadPetsFromFile();

            FileUtils.savePetsToFile(pets);

            // 更新窗口标题
            detailFrame.setTitle(pet.getName() + " 详情");
            JOptionPane.showMessageDialog(detailFrame, "保存成功！");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(detailFrame, "年龄必须是整数",
                    "输入错误", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(detailFrame, ex.getMessage(),
                    "输入错误", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(detailFrame, "保存失败: " + ex.getMessage(),
                    "系统错误", JOptionPane.ERROR_MESSAGE);
        }
    }


}
