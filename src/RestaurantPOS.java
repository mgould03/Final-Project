import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class RestaurantPOS extends JFrame {

    private Map<Integer, DefaultListModel<String>> orderListMap = new HashMap<>();
    private JList<String> orderList;
    private JTextField totalField = new JTextField(10);
    private JComboBox<Integer> tableSelector = new JComboBox<>();

    // Predefined menu items and their prices
    private String[] menuItems = {"Classic Burger", "Cheese Flatbread", "Cobb Salad", "Chicken Alfredo", "NY Strip", "Fish and Chips", "Nashville Chicken Sandwich", "Chef's Soup", "Sushi Stack", "Chicken Parmesean"};
    private int[] menuPrices = {1500, 1400, 1600, 2300, 3600, 1900, 1600, 600, 1600, 2500};

    public RestaurantPOS() {
        setTitle("Restaurant Point of Sale System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Table selector
        JPanel tablePanel = new JPanel();
        JLabel tableLabel = new JLabel("Select Table:");
        for (int i = 1; i <= 10; i++) {
            tableSelector.addItem(i);
        }
        tableSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableNumber = (int) tableSelector.getSelectedItem();
                DefaultListModel<String> orderListModel = orderListMap.getOrDefault(tableNumber, new DefaultListModel<>());
                orderList.setModel(orderListModel);
                updateTotal();
            }
        });
        tablePanel.add(tableLabel);
        tablePanel.add(tableSelector);
        panel.add(tablePanel, BorderLayout.NORTH);

        // Menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(menuItems.length, 2));
        for (int i = 0; i < menuItems.length; i++) {
            JButton addButton = new JButton(menuItems[i] + " - $" + menuPrices[i] / 100 + "." + String.format("%02d", menuPrices[i] % 100));
            int index = i;
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int tableNumber = (int) tableSelector.getSelectedItem();
                    DefaultListModel<String> orderListModel = orderListMap.getOrDefault(tableNumber, new DefaultListModel<>());
                    orderListMap.put(tableNumber, orderListModel);
                    String item = menuItems[index];
                    double price = menuPrices[index] / 100.0;
                    orderListModel.addElement(item + " - $" + String.format("%.2f", price));
                    orderList.setModel(orderListModel); // Update order list model
                    updateTotal();
                }
            });
            menuPanel.add(addButton);
        }

        // Order panel
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BorderLayout());
        JLabel orderLabel = new JLabel("Order:");
        orderList = new JList<>();
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        orderPanel.add(orderLabel, BorderLayout.NORTH);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Buttons for removing items and clearing order
        JButton removeItemButton = new JButton("Remove Item");
        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableNumber = (int) tableSelector.getSelectedItem();
                DefaultListModel<String> orderListModel = orderListMap.get(tableNumber);
                if (orderListModel != null) {
                    int selectedIndex = orderList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        orderListModel.remove(selectedIndex);
                        orderList.setModel(orderListModel); // Update order list model
                        updateTotal();
                    }
                }
            }
        });

        JButton clearButton = new JButton("Clear Order");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableNumber = (int) tableSelector.getSelectedItem();
                DefaultListModel<String> orderListModel = orderListMap.get(tableNumber);
                if (orderListModel != null) {
                    orderListModel.removeAllElements();
                    orderList.setModel(orderListModel); // Update order list model
                    updateTotal();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(removeItemButton);
        buttonPanel.add(clearButton);
        orderPanel.add(buttonPanel, BorderLayout.WEST);

        // Total panel
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new FlowLayout());
        JLabel totalLabel = new JLabel("Total:");
        totalField.setEditable(false);
        totalPanel.add(totalLabel);
        totalPanel.add(totalField);

        panel.add(menuPanel, BorderLayout.WEST);
        panel.add(orderPanel, BorderLayout.CENTER);
        panel.add(totalPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void updateTotal() {
        int tableNumber = (int) tableSelector.getSelectedItem();
        DefaultListModel<String> orderListModel = orderListMap.getOrDefault(tableNumber, new DefaultListModel<>());
        double total = 0.0;
        for (int i = 0; i < orderListModel.size(); i++) {
            String selectedItem = orderListModel.getElementAt(i);
            String[] parts = selectedItem.split(" - ");
            total += Double.parseDouble(parts[1].substring(1));
        }
        totalField.setText(String.format("$%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RestaurantPOS();
            }
        });
    }
}
