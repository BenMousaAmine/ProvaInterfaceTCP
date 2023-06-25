package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class CustomFrame extends JFrame {
    private JButton button1;
    private JTextArea textArea;
    private HandleFrameRequest handleFrameRequest;

    private void handleBtnRequest(List<Car> carList) {
        String result = handleFrameRequest.transformToJson(carList);
        updateTextArea(result);
    }

    private void updateTextArea(String text) {
        textArea.setText(text);
    }

    public CustomFrame(Socket clientSocket) {
        setTitle("Auto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        handleFrameRequest = new HandleFrameRequest(clientSocket);

        JButton btnExpensive = new JButton("More expensive");
        JButton btnAll = new JButton("All");
        JButton btnSorted = new JButton("Sorted by name");
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel("Data in Formato Json:");
        container.add(label, BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnExpensive);
        buttonPanel.add(btnAll);
        buttonPanel.add(btnSorted);

        container.add(buttonPanel, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);

        btnExpensive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Car> carList = handleFrameRequest.MostExpensiveCar();
                handleBtnRequest(carList);
            }
        });

        btnAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Car> carList = handleFrameRequest.getCarList();
                handleBtnRequest(carList);
            }
        });

        btnSorted.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Car> carList = handleFrameRequest.CarsSortedByName();
                handleBtnRequest(carList);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Socket clientSocket = new Socket("127.0.0.1", 1234);
                    CustomFrame frame = new CustomFrame(clientSocket);
                    frame.setSize(400, 300);
                    frame.setVisible(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
