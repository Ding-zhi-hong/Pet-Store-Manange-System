import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
/*
public class PetInforGUIAdmin {
    private JFrame frame;
    private JList<String> orderList;
    private List<Order> orders;
    private JButton refreshButton;
    private JPanel infoPanel;

    public PetInforGUIAdmin() {
        initialize();
        loadData();
        setupUI();
    }

    private void initialize() {
        frame = new JFrame("订单管理系统");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
    }

    private void loadData() {
        orders = FileUtils.loadOrdersFromFile();
    }

    private void setupUI() {
        // 订单列表面板
        JPanel listPanel = new JPanel(new BorderLayout());
        orderList = new JList<>(getCustomerIds());
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加选择监听器
        orderList.addListSelectionListener(this::handleOrderSelection);

        listPanel.add(new JLabel("订单列表"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("刷新数据");
        JButton deleteButton = new JButton("删除订单");

        refreshButton.addActionListener(e -> refreshData());
        deleteButton.addActionListener(e -> deleteSelectedOrder());

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        // 详细信息面板
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // 布局组合
        frame.add(listPanel, BorderLayout.WEST);
        frame.add(new JScrollPane(infoPanel), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

private String[] getCustomerIds() {
    HashSet<String> uniqueIds = new HashSet<>();
    List<String> ids = new ArrayList<>();
    for (Order order : orders) {
        String id = order.getCustomerId();
        // 未将 id 添加到列表中
        if (uniqueIds.add(id)) {
            ids.add(id + " (" + getOrderCount(id) + "笔订单)");
        }
    }
    return ids.toArray(new String[0]);
}

    private int getOrderCount(String customerId) {
        return (int) orders.stream()
                .filter(o -> o.getCustomerId().equals(customerId))
                .count();
    }


    private void handleOrderSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = orderList.getSelectedIndex();
            if (index >= 0) {
                showOrderDetails(orders.get(index));
            }
        }
    }

    private void showOrderDetails(Order order) {
        infoPanel.removeAll();

        // 基本信息
        addDetailRow("下单时间：", order.getOrderDate().toString());
        addDetailRow("订单状态：", createStatusComboBox(order));

        // 宠物列表
        JPanel petsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        String pet =order.getPet();
        petsPanel.add(new JLabel(pet));
        infoPanel.add(new JLabel("包含宠物："));
        infoPanel.add(petsPanel);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private JComboBox<String> createStatusComboBox(Order order) {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"PENDING", "COMPLETED", "CANCELLED"});
        comboBox.setSelectedItem(order.getStatus());
        comboBox.addActionListener(e -> {
            order.setStatus((String) comboBox.getSelectedItem());
            JOptionPane.showMessageDialog(frame, "状态已更新！");
        });
        return comboBox;
    }

    private void addDetailRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(label));
        row.add(new JLabel(value));
        infoPanel.add(row);
    }

    private void addDetailRow(String label, JComponent component) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(label));
        row.add(component);
        infoPanel.add(row);
    }

    private void refreshData() {
        loadData();
        orderList.setListData(getCustomerIds());
        infoPanel.removeAll();
        JOptionPane.showMessageDialog(frame, "数据已刷新！");
    }

    private void deleteSelectedOrder() {
        int index = orderList.getSelectedIndex();
        if (index >= 0) {
            orders.remove(index);
            orderList.setListData(getCustomerIds());
            JOptionPane.showMessageDialog(frame, "订单已删除！");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetInforGUIAdmin::new);
    }
}*/
public class PetInforGUIAdmin {
    private JFrame frame;
    private JList<String> orderList;
    private List<Order> orders;
    private JButton refreshButton;
    private JPanel infoPanel;
    private final String ORDER_FILE = "orders.csv"; // 订单数据文件

    public PetInforGUIAdmin() {
        initialize();
        loadData();
        setupUI();
    }

    private void initialize() {
        frame = new JFrame("订单管理系统");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
    }

    private void loadData() {
        orders = FileUtils.loadOrdersFromFile();
    }

    private void setupUI() {
        // 订单列表面板
        JPanel listPanel = new JPanel(new BorderLayout());
        orderList = new JList<>(getOrderDisplayStrings());
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加选择监听器
        orderList.addListSelectionListener(this::handleOrderSelection);

        listPanel.add(new JLabel("订单列表"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("刷新数据");
        JButton deleteButton = new JButton("删除订单");

        refreshButton.addActionListener(e -> refreshData());
        deleteButton.addActionListener(e -> deleteSelectedOrder());

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        // 详细信息面板
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        // 布局组合
        frame.add(listPanel, BorderLayout.WEST);
        frame.add(new JScrollPane(infoPanel), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // 获取订单显示字符串（包含客户ID和订单数量）
    private String[] getOrderDisplayStrings() {
        Map<String, Long> orderCounts = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerId, Collectors.counting()));

        return orders.stream()
                .map(order -> order.getCustomerId() + " (" + orderCounts.get(order.getCustomerId()) + "笔订单)")
                .toArray(String[]::new);
    }

    private void handleOrderSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = orderList.getSelectedIndex();
            if (index >= 0 && index < orders.size()) {
                showOrderDetails(orders.get(index));
            }
        }
    }

    private void showOrderDetails(Order order) {
        infoPanel.removeAll();
        infoPanel.setLayout(new GridLayout(0, 2, 5, 5));

        // 基本信息
        addDetailRow("客户ID:", order.getCustomerId());
        addDetailRow("下单时间:", order.getOrderDate().toString());
        addDetailRow("订单状态:", createStatusComboBox(order));
        addDetailRow("宠物信息:", order.getPet());

        // 添加保存按钮
        JButton saveButton = new JButton("保存修改");
        saveButton.addActionListener(e -> saveOrderChanges(order));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        infoPanel.add(new JLabel(""));
        infoPanel.add(buttonPanel);

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private JComboBox<String> createStatusComboBox(Order order) {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"PENDING", "COMPLETED", "CANCELLED"});
        comboBox.setSelectedItem(order.getStatus());
        return comboBox;
    }

    private void addDetailRow(String label, String value) {
        infoPanel.add(new JLabel(label));
        infoPanel.add(new JLabel(value));
    }

    private void addDetailRow(String label, JComponent component) {
        infoPanel.add(new JLabel(label));
        infoPanel.add(component);
    }

    private void refreshData() {
        loadData();
        orderList.setListData(getOrderDisplayStrings());
        infoPanel.removeAll();
        JOptionPane.showMessageDialog(frame, "数据已刷新！");
    }

    private void deleteSelectedOrder() {
        int index = orderList.getSelectedIndex();
        if (index >= 0 && index < orders.size()) {
            Order selectedOrder = orders.get(index);
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "确定要删除" + selectedOrder.getCustomerId() + "的订单吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // 从内存中删除
                orders.remove(index);

                // 更新文件
                saveAllOrdersToFile();

                // 更新UI
                orderList.setListData(getOrderDisplayStrings());
                infoPanel.removeAll();
                JOptionPane.showMessageDialog(frame, "订单已删除！");
            }
        }
    }

    // 保存单个订单的修改
    private void saveOrderChanges(Order order) {
        // 更新内存中的数据
        for (int i = 0; i < infoPanel.getComponentCount(); i++) {
            Component comp = infoPanel.getComponent(i);
            if (comp instanceof JComboBox) {
                JComboBox<String> comboBox = (JComboBox<String>) comp;
                order.setStatus((String) comboBox.getSelectedItem());
            }
        }

        // 更新文件
        saveAllOrdersToFile();

        JOptionPane.showMessageDialog(frame, "订单修改已保存！");
    }

    // 将所有订单保存到文件
    private void saveAllOrdersToFile() {
        FileUtils.saveAllOrdersToFile(orders);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetInforGUIAdmin::new);
    }
}