import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
class PetDetailGUI {
    private JFrame detailFrame;
    private Order order;
    public PetDetailGUI(Pet pet,User user) {
        detailFrame = new JFrame(pet.getName() + " Details");
        detailFrame.setSize(300, 250);
        detailFrame.setLayout(new BorderLayout(10, 10));
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        // 添加属性信息
        addDetailRow(infoPanel,"Name:",pet.getName());
        addDetailRow(infoPanel,"Classfication:",pet.getClassification());
        addDetailRow(infoPanel,"Age:", String.valueOf(pet.getAge()));
        addDetailRow(infoPanel,"Breed:", pet.getBreed());
        addDetailRow(infoPanel,"Gender:", pet.getGender());
        addDetailRow(infoPanel,"Price:",  String.valueOf(pet.getPrice()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buyButton = new JButton("购买");
        buyButton.addActionListener(e -> {
            handlePurchase(pet,user);
            detailFrame.dispose(); // 关闭详情窗口
        });

        buttonPanel.add(buyButton);

        // 添加组件到主窗口
        detailFrame.add(infoPanel, BorderLayout.CENTER);
        detailFrame.add(buttonPanel, BorderLayout.SOUTH);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        panel.add(new JLabel(label, SwingConstants.RIGHT));
        panel.add(new JLabel(value));
    }

    private void handlePurchase(Pet pet,User user) {
        // 实际购买逻辑示例
        String message = String.format(
                "成功购买 %s！\n品种：%s\n年龄：%d岁\n",
                pet.getName(), pet.getBreed(), pet.getAge()
        );

        order=createOrder(pet,user);
        // 保存到CSV文件
        FileUtils.saveOrderToFile(order);
        JOptionPane.showMessageDialog(
                detailFrame,
                message,
                "购买成功",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private Order createOrder(Pet pet,User user) {
        Order order=new Order(user.getUsername(),pet.getName(),"PENDING",LocalDateTime.now());
        return order;
    }



}
