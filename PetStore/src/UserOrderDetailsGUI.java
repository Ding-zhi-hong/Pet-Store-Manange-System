import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
public class UserOrderDetailsGUI {
    private JFrame frame;
    private JTable orderTable;
    private List<Order> orders;
    private User user;

    private static final String[] COLUMN_NAMES = {"宠物名称", "订单状态", "下单时间", "操作"};
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

    public UserOrderDetailsGUI(User user) {
        this.user = user;
        List<Order> allOrders = FileUtils.loadOrdersFromFile();

        // 过滤出属于当前用户的订单
        this.orders = filterUserOrders(allOrders, user);

        createGUI();
    }

    private List<Order> filterUserOrders(List<Order> allOrders, User user) {
        List<Order> userOrders = new ArrayList<>();

        // 确保allOrders不为null
        if (allOrders != null) {
            for (Order order : allOrders) {
                // 检查订单是否属于当前用户
                if (order.getCustomerId().equals(user.getUsername())) {
                    userOrders.add(order);
                }
            }
        }
        return userOrders;
    }


    private void createGUI() {
        frame = new JFrame(user.getUsername() + "的订单详情");
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建表格模型（不可编辑）
        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 所有单元格不可编辑
            }
        };

        // 填充表格数据
        for (Order order : orders) {
            String formattedDate = order.getOrderDate().format(DATE_FORMATTER);
            model.addRow(new Object[]{
                    order.getPet(),
                    order.getStatus(),
                    formattedDate,
                    createCancelButton(order)
            });
        }

        // 创建表格
        orderTable = new JTable(model);
        orderTable.setRowHeight(35);
        orderTable.getTableHeader().setReorderingAllowed(false); // 禁止列重排序

        // 设置表格列宽
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        // 设置状态列渲染器，不同状态不同颜色
        orderTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;

                if ("PENDING".equals(status)) {
                    c.setBackground(new Color(255, 255, 200)); // 黄色
                    c.setForeground(Color.BLACK);
                } else if ("COMPLETED".equals(status)) {
                    c.setBackground(new Color(200, 255, 200)); // 绿色
                    c.setForeground(Color.BLACK);
                } else if ("CANCELLED".equals(status)) {
                    c.setBackground(new Color(255, 200, 200)); // 红色
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(orderTable);

        // 创建底部关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> frame.dispose());
        closeButton.setPreferredSize(new Dimension(120, 35));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(closeButton);

        // 添加组件到主面板
        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("您还没有任何订单", SwingConstants.CENTER);
            emptyLabel.setFont(emptyLabel.getFont().deriveFont(18f));
            emptyLabel.setForeground(Color.GRAY);
            mainPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JButton createCancelButton(Order order) {
        JButton cancelButton = new JButton("取消订单");
        cancelButton.setEnabled("PENDING".equals(order.getStatus()));

        if (cancelButton.isEnabled()) {
            cancelButton.addActionListener(e -> cancelOrder(order));
        } else {
            cancelButton.setToolTipText("只有待处理状态的订单可以取消");
            cancelButton.setBackground(new Color(240, 240, 240));
            cancelButton.setForeground(Color.GRAY);
        }

        cancelButton.setPreferredSize(new Dimension(120, 30));
        return cancelButton;
    }
    private void cancelOrder(Order order) {
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "确定要取消订单: " + order.getPet() + "?",
                "确认取消",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

                // 更新订单状态
                order.setStatus("CANCELLED");

                // 保存更改到文件
                List<Order> allOrders = FileUtils.loadOrdersFromFile();
                allOrders.replaceAll(o ->
                        o.getCustomerId().equals(order.getCustomerId()) &&
                                o.getPet().equals(order.getPet()) &&
                                o.getOrderDate().equals(order.getOrderDate()) ? order : o
                );

                FileUtils.saveAllOrdersToFile(allOrders);

                // 刷新表格
                refreshTable();

                JOptionPane.showMessageDialog(
                        frame,
                        "订单已成功取消",
                        "操作成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }





    private void refreshTable() {
        this.orders = FileUtils.loadOrdersFromFile();
        DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
        model.setRowCount(0); // 清空表格

        for (Order order : orders) {
            String formattedDate = order.getOrderDate().format(DATE_FORMATTER);
            model.addRow(new Object[]{
                    order.getPet(),
                    order.getStatus(),
                    formattedDate,
                    createCancelButton(order)
            });
        }
    }
}


