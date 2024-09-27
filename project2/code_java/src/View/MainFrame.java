package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

import Update.*;

public class MainFrame extends JFrame implements ActionListener {
    final int WIDTH = 1200;
    final int HEIGHT = 720;
    BufferedImage originalImage;
    Image scaledImage;

    public MainFrame() {
        loadImage();
        scaleImage();
        initJMenuBar();

        this.setLocationRelativeTo(null); // Center the window.
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setLayout(null);
        initFrame();
        this.setVisible(true);


    }

    private void loadImage() {
        try {
            originalImage = ImageIO.read(new File("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\picture\\0504.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scaleImage() {
        scaledImage = originalImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    }

    public void initFrame() {
        this.setTitle("Shenzhen Subway");

        this.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);//
        this.setResizable(false);

        addStationButton();
        deleteStationButton();
        modifyStationButton();
        addLineButton();
        deleteLineButton();
        modifyLineButton();
        PSOLButton();
        PSOMButton();
        DSFLButton();
        QueryForwardButton();
        QueryBackwardButton();
        upSubButton();
        addCurrentPane();
        addCurrentCPane();
        addPasButton();
        addCardButton();
        addChargeButton();
        downSubButton();
        addTimeLabel();

        JLabel bg = new JLabel(new ImageIcon(scaledImage));
        bg.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);//这里裁剪的是原来的图片
        this.getContentPane().add(bg);
        this.setVisible(true);

        this.setAlwaysOnTop(false);
        this.setVisible(true);
    }

    JMenu showJMenu = new JMenu("show");
    JMenu NavJMenu = new JMenu("Navigation");
    JMenu askJMenu = new JMenu("Ask");
    JMenu state = new JMenu("State");
    JMenuItem showMap = new JMenuItem("show map");
    JMenuItem NaviJ = new JMenuItem("Navi way");
    JMenuItem askway = new JMenuItem("ask rides");
    JMenuItem cs = new JMenuItem("check state");
    JMenuItem sta = new JMenuItem("start open");
    JMenuItem sto = new JMenuItem("emergency close");

    private void initJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        showJMenu.add(showMap);
        NavJMenu.add(NaviJ);
        askJMenu.add(askway);
        state.add(cs);
        state.add(sta);
        state.add(sto);
        showMap.addActionListener(this);
        NaviJ.addActionListener(this);
        askway.addActionListener(this);
        cs.addActionListener(this);
        sta.addActionListener(this);
        sto.addActionListener(this);

        jMenuBar.add(showJMenu);
        jMenuBar.add(NavJMenu);
        jMenuBar.add(askJMenu);
        jMenuBar.add(state);
        this.setJMenuBar(jMenuBar);
    }

    public void addStationButton() {
        JButton button = new JButton("add a station");
        add(button);
        Color originalColor = Color.ORANGE;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);

        button.setLocation(WIDTH - 260, 70);
        //X:距离左边的距离
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JTextField nameField = new JTextField(15);
            JTextField districtField = new JTextField(15);
            JTextField introField = new JTextField(15);
            JTextField chineseNameField = new JTextField(15);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("District:"));
            panel.add(districtField);
            panel.add(new JLabel("Intro:"));
            panel.add(introField);
            panel.add(new JLabel("Chinese Name:"));
            panel.add(chineseNameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input new station information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String district = districtField.getText();
                String intro = introField.getText();
                String chineseName = chineseNameField.getText();
                stationAMD s = new stationAMD();
                s.add_a_station(name, district, intro, chineseName);
            }
        });
    }

    public void deleteStationButton() {
        JButton button = new JButton("delete a station");
        Color originalColor = Color.ORANGE;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 110);
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(button);
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField nameField = new JTextField(15);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input station you want to delete", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                stationAMD s = new stationAMD();
                s.delete_a_station(name);
            }
        });

    }

    public void modifyStationButton() {
        JButton button = new JButton("modify a station");
        add(button);
        Color originalColor = Color.ORANGE;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 150);
        //X:距离左边的距离
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField oldName = new JTextField(15);
            JTextField nameField = new JTextField(15);
            JTextField districtField = new JTextField(15);
            JTextField introField = new JTextField(15);
            JTextField chineseNameField = new JTextField(15);
            panel.add(new JLabel("OldName:"));
            panel.add(oldName);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("District:"));
            panel.add(districtField);
            panel.add(new JLabel("Intro:"));
            panel.add(introField);
            panel.add(new JLabel("Chinese Name:"));
            panel.add(chineseNameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input new station information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String district = districtField.getText();
                String intro = introField.getText();
                String chineseName = chineseNameField.getText();
                String old = oldName.getText();
                stationAMD s = new stationAMD();
                s.modify_a_station(old, name, district, intro, chineseName);
            }
        });
    }

    public void addLineButton() {
        JButton button = new JButton("add a line");
        add(button);
        Color originalColor = Color.CYAN;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 240);
        //X:距离左边的距离
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(8, 2));
            JTextField lineNameField = new JTextField(15);
            JTextField startTimeField = new JTextField(15);
            JTextField endTimeField = new JTextField(15);
            JTextField introField = new JTextField(15);
            JTextField mileageField = new JTextField(15);
            JTextField colorField = new JTextField(15);
            JTextField firstOpeningField = new JTextField(15);
            JTextField urlField = new JTextField(15);

            panel.add(new JLabel("Line Name:"));
            panel.add(lineNameField);
            panel.add(new JLabel("Start Time:"));
            panel.add(startTimeField);
            panel.add(new JLabel("End Time:"));
            panel.add(endTimeField);
            panel.add(new JLabel("Intro:"));
            panel.add(introField);
            panel.add(new JLabel("Mileage:"));
            panel.add(mileageField);
            panel.add(new JLabel("Color:"));
            panel.add(colorField);
            panel.add(new JLabel("First Opening:"));
            panel.add(firstOpeningField);
            panel.add(new JLabel("URL:"));
            panel.add(urlField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input new line information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String lineName = lineNameField.getText();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();
                String intro = introField.getText();
                String mileage = mileageField.getText();
                String color = colorField.getText();
                String firstOpening = firstOpeningField.getText();
                String url = urlField.getText();

                lineAMD l = new lineAMD();
                l.add_a_line(lineName, startTime, endTime, intro, mileage, color, firstOpening, url);
            }

        });
    }

    public void deleteLineButton() {
        JButton button = new JButton("delete a line");
        Color originalColor = Color.CYAN;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 280);
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(button);
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField nameField = new JTextField(15);
            panel.add(new JLabel("Name:"));
            panel.add(nameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input station you want to delete", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                lineAMD l = new lineAMD();
                l.delete_a_line(name);
            }
        });
    }

    public void modifyLineButton() {
        JButton button = new JButton("modify a line");
        add(button);
        Color originalColor = Color.CYAN;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 320);
        //X:距离左边的距离
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(9, 2));

            JTextField old = new JTextField(15);
            JTextField lineNameField = new JTextField(15);
            JTextField startTimeField = new JTextField(15);
            JTextField endTimeField = new JTextField(15);
            JTextField introField = new JTextField(15);
            JTextField mileageField = new JTextField(15);
            JTextField colorField = new JTextField(15);
            JTextField firstOpeningField = new JTextField(15);
            JTextField urlField = new JTextField(15);

            panel.add(new JLabel("Old Line:"));
            panel.add(old);
            panel.add(new JLabel("Line Name:"));
            panel.add(lineNameField);
            panel.add(new JLabel("Start Time:"));
            panel.add(startTimeField);
            panel.add(new JLabel("End Time:"));
            panel.add(endTimeField);
            panel.add(new JLabel("Intro:"));
            panel.add(introField);
            panel.add(new JLabel("Mileage:"));
            panel.add(mileageField);
            panel.add(new JLabel("Color:"));
            panel.add(colorField);
            panel.add(new JLabel("First Opening:"));
            panel.add(firstOpeningField);
            panel.add(new JLabel("URL:"));
            panel.add(urlField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input new line information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String lineName = lineNameField.getText();
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();
                String intro = introField.getText();
                String mileage = mileageField.getText();
                String color = colorField.getText();
                String firstOpening = firstOpeningField.getText();
                String url = urlField.getText();
                String oldL = old.getText();

                lineAMD l = new lineAMD();
                l.modify_a_line(oldL, lineName, startTime, endTime, intro, mileage, color, firstOpening, url);
            }

        });
    }

    public void PSOLButton() {
        JButton button = new JButton("put 1");
        Color originalColor = Color.PINK;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 440);
        button.setSize(75, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 12));
        add(button);
        //String lineName, String stationName, int stationNo
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField lnameField = new JTextField(15);
            JTextField snameField = new JTextField(15);
            JTextField stationNo = new JTextField(15);

            panel.add(new JLabel("lineName:"));
            panel.add(lnameField);
            panel.add(new JLabel("stationName:"));
            panel.add(snameField);
            panel.add(new JLabel("stationNo:"));
            panel.add(stationNo);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input station you want to put on line", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = lnameField.getText();
                String sname = snameField.getText();
                String stationNo1 = stationNo.getText(); // 获取用户输入的文本

                // 将字符串转换为整数
                int no = Integer.parseInt(stationNo1);
                changeConn c = new changeConn();
                int rlt = c.putStationOnLine(name, sname, no);

                if (rlt == 0) {
                    JOptionPane.showMessageDialog(this, "Station added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else if (rlt == 1) {
                    JOptionPane.showMessageDialog(this, "The station doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (rlt == 2) {
                    JOptionPane.showMessageDialog(this, "The station is already in line.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to insert new station.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void PSOMButton() {
        JButton button = new JButton("put n");
        Color originalColor = Color.PINK;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 180, 440);
        button.setSize(75, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 12));
        add(button);

        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 2));
            JTextField lnameField = new JTextField(15);
            JTextField nField = new JTextField(15);
            JTextField xField = new JTextField(15);
            panel.add(new JLabel("lineName:"));
            panel.add(lnameField);
            panel.add(new JLabel("Number of stations:"));
            panel.add(nField);
            panel.add(new JLabel("Position on line:")); // 新增提示文本
            panel.add(xField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input line name and number of stations", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String lineName = lnameField.getText();
                int numOfStations = Integer.parseInt(nField.getText());
                if (numOfStations <= 0) {
                    JOptionPane.showMessageDialog(this, "Number of stations must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int positionOnLine = Integer.parseInt(xField.getText());

                JPanel stationsPanel = new JPanel(new GridLayout(numOfStations, 1));
                for (int i = 0; i < numOfStations; i++) {
                    JTextField stationField = new JTextField(15);
                    stationsPanel.add(new JLabel("Station " + (i + 1) + ":"));
                    stationsPanel.add(stationField);
                }

                int result2 = JOptionPane.showConfirmDialog(this, stationsPanel, "Please input station names", JOptionPane.OK_CANCEL_OPTION);
                if (result2 == JOptionPane.OK_OPTION) {
                    changeConn c = new changeConn();
                    ArrayList<String> stationNames = new ArrayList<>();
                    for (Component component : stationsPanel.getComponents()) {
                        if (component instanceof JTextField) {
                            JTextField textField = (JTextField) component;
                            stationNames.add(textField.getText());
                        }
                    }
                    for (int i = 0; i < numOfStations; i++) {
                        int rlt = c.putStationOnLine(lineName, stationNames.get(i), positionOnLine);
                        if (rlt == 1) {
                            JOptionPane.showMessageDialog(this, "At least one station doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else if (rlt == 2) {
                            JOptionPane.showMessageDialog(this, "At least one station is already in the line.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                        JOptionPane.showMessageDialog(this, "Station added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }


    public void DSFLButton() {
        JButton button = new JButton("deleteStaFromLine");
        Color originalColor = Color.PINK;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 480);
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 12));
        add(button);
        //String lineName, String stationName, int stationNo
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField lnameField = new JTextField(15);
            JTextField snameField = new JTextField(15);

            panel.add(new JLabel("lineName:"));
            panel.add(lnameField);
            panel.add(new JLabel("stationName:"));
            panel.add(snameField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input station you want to delete on line", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = lnameField.getText();
                String sname = snameField.getText();

                changeConn c = new changeConn();
                int rlt = c.deleteStationFromLine(sname, name);

                if (rlt == 0) {
                    JOptionPane.showMessageDialog(this, "Station delete from line successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else if (rlt == 1) {
                    JOptionPane.showMessageDialog(this, "The station doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (rlt == 2) {
                    JOptionPane.showMessageDialog(this, "The line doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (rlt == 3) {
                    JOptionPane.showMessageDialog(this, "The station is not in the line.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Fail to delete", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void QueryForwardButton() {
        JButton button = new JButton("Query Forward");
        Color originalColor = Color.BLACK;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 580);
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 15));
        add(button);

        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField lnameField = new JTextField(15);
            panel.add(new JLabel("lineName:"));
            panel.add(lnameField);
            JTextField nameField = new JTextField(15);
            panel.add(new JLabel("stationName:"));
            panel.add(nameField);
            JTextField n = new JTextField(15);
            panel.add(new JLabel("n:"));
            panel.add(n);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input the station you want to query", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("num");
                tableModel.addColumn("station_No");
                tableModel.addColumn("station_n");
                String name = nameField.getText();
                String lname = lnameField.getText();
                String n1 = n.getText();
                int n11 = Integer.parseInt(n1);
                // 调用查询方法并获取返回值
                QueryStation q = new QueryStation();
                String queryResult = q.forwardQuery(name, lname, n11, tableModel);

                JTable table = new JTable();
                table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame frame = new JFrame("records Ask");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(500, 300);
                frame.setLocationRelativeTo(null);
                frame.add(scrollPane);
                frame.toFront();
                frame.setVisible(true);

                // 显示对话框
                //JOptionPane.showMessageDialog(this, scrollPane, "Query Result", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void QueryBackwardButton() {
        JButton button = new JButton("Query Backward");
        Color originalColor = Color.black;
        Color transparentColor = new Color(originalColor.getRed(), originalColor.getGreen(),
                originalColor.getBlue(), 200);
        button.setBackground(transparentColor);
        button.setForeground(Color.WHITE);
        button.setLocation(WIDTH - 260, 620);
        button.setSize(150, 30);
        button.setFont(new Font("Rockwell", Font.BOLD, 13));
        add(button);

        button.addActionListener(e -> {

            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField lnameField = new JTextField(15);
            panel.add(new JLabel("lineName:"));
            panel.add(lnameField);
            JTextField nameField = new JTextField(15);
            panel.add(new JLabel("stationName:"));
            panel.add(nameField);

            JTextField n2 = new JTextField(15);
            panel.add(new JLabel("n:"));
            panel.add(n2);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input the station you want to query", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("line_No");
                tableModel.addColumn("station_No");
                tableModel.addColumn("station_n");
                String name = nameField.getText();
                String lname = lnameField.getText();
                String n22 = n2.getText();
                int n222 = Integer.parseInt(n22);
                // 调用查询方法并获取返回值
                QueryStation q = new QueryStation();
                String queryResult = q.backwardQuery(name, lname, n222, tableModel);
                JTable table = new JTable();
                table = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame frame = new JFrame("records Ask");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(500, 300);
                frame.setLocationRelativeTo(null);
                frame.add(scrollPane);
                frame.toFront();
                frame.setVisible(true);


            }
        });
    }

    public void upSubButton() {
        JButton upSubButton = new JButton("Up Subway");
        upSubButton.setSize(130, 30);
        upSubButton.setLocation(570, 100);
        upSubButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(upSubButton);
        upSubButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Passenger", "Card"});
            JTextField idField = new JTextField(15);
            JTextField stationField = new JTextField(15);
            JComboBox<String> typeCombo2 = new JComboBox<>(new String[]{"common", "business"});
            panel.add(new JLabel("Type:"));
            panel.add(typeCombo);
            panel.add(new JLabel("ID:"));
            panel.add(idField);
            panel.add(new JLabel("Station:"));
            panel.add(stationField);
            panel.add(new JLabel("Carriage:"));
            panel.add(typeCombo2);

            int result = JOptionPane.showConfirmDialog(this, panel, "Input Information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String type = (String) typeCombo.getSelectedItem();
                String id = idField.getText();
                String station = stationField.getText();
                String type2 = (String) typeCombo2.getSelectedItem();
                int miao = 0;
                if(type2.equals("common")){
                    miao = 3;
                }else {
                    miao = 7;
                }
                TakeARide t = new TakeARide();
                int ans = 100;
                if (type.equals("Passenger")) {
                    ans = t.UpSubwayPass(id, station, miao);
                } else if (type.equals("Card")) {
                    ans = t.UpSubwayCard(id, station, miao);
                }
                if (ans == 1) {
                    JOptionPane.showMessageDialog(this, "The station doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (ans == 2) {
                    JOptionPane.showMessageDialog(this, "The passenger doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (ans == 3) {
                    JOptionPane.showMessageDialog(this, "The card doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (ans == 4) {
                    JOptionPane.showMessageDialog(this, "The station is not operating", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (ans == 0) {
                    JOptionPane.showMessageDialog(this, "upup!", "Success", JOptionPane.OK_CANCEL_OPTION);
                } else if (ans == 65) {
                    JOptionPane.showMessageDialog(this, "不在当前地铁运营时间内", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void downSubButton() {
        JButton downSubButton = new JButton("Down Subway");
        downSubButton.setSize(130, 30);
        downSubButton.setLocation(570, 460);
        downSubButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(downSubButton);

        downSubButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Passenger", "Card"});
            JTextField idField = new JTextField(15);
            JTextField stationField = new JTextField(15);
            panel.add(new JLabel("Type:"));
            panel.add(typeCombo);
            panel.add(new JLabel("ID:"));
            panel.add(idField);
            panel.add(new JLabel("Station:"));
            panel.add(stationField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Input Information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String type = (String) typeCombo.getSelectedItem();
                String id = idField.getText();
                String station = stationField.getText();
                TakeARide t = new TakeARide();
                int ans = 1000;
                if (type.equals("Passenger")) {
                    ans = t.DownSubwayPass(id, station);
                    if (ans == 1) {
                        JOptionPane.showMessageDialog(this, "The station doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 2) {
                        JOptionPane.showMessageDialog(this, "The passenger doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 3) {
                        JOptionPane.showMessageDialog(this, "The passenger is not on the subway.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 4) {
                        JOptionPane.showMessageDialog(this, "The station is not operating", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "The fare for this ride is: " + ans, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (type.equals("Card")) {
                    ans = t.DownSubwayCard(id, station);
                    if (ans == 1) {
                        JOptionPane.showMessageDialog(this, "The station doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 2) {
                        JOptionPane.showMessageDialog(this, "The card doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 3) {
                        JOptionPane.showMessageDialog(this, "The card is not on the subway.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ans == 9) {
                        JOptionPane.showMessageDialog(this, "The card have no money, you should charge first.", "Error", JOptionPane.ERROR_MESSAGE);
                    }else if (ans == 4) {
                        JOptionPane.showMessageDialog(this, "The station is not operating", "Error", JOptionPane.ERROR_MESSAGE);
                    }  else {
                        JOptionPane.showMessageDialog(this, "The fare for this ride is: " + ans, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });
    }


    public void addCurrentPane() {
        JButton showButton = new JButton("currentPas");
        showButton.setSize(130, 30);
        showButton.setLocation(30, 60);
        showButton.setFont(new Font("Arial", Font.BOLD, 14));
        showButton.addActionListener(e -> {
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Start Station");
            tableModel.addColumn("Start Time");
            tableModel.addColumn("type");
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Current Passengers");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            frame.add(scrollPane);
            frame.toFront();
            frame.setVisible(true);
            // 在新窗口中显示表格数据
            TakeARide t = new TakeARide();
            t.currentPass(tableModel);
        });
        // 在主窗口中添加按钮
        add(showButton);
    }

    public void addCurrentCPane() {
        JButton showButton = new JButton("currentCard");
        showButton.setSize(130, 30);
        showButton.setLocation(230, 30);
        showButton.setFont(new Font("Arial", Font.BOLD, 14));
        showButton.addActionListener(e -> {
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Start Station");
            tableModel.addColumn("Start Time");
            tableModel.addColumn("type");
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Current Card");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            frame.add(scrollPane);
            frame.toFront();
            frame.setVisible(true);
            // 在新窗口中显示表格数据
            TakeARide t = new TakeARide();
            t.currentCard(tableModel);
        });
        // 在主窗口中添加按钮
        add(showButton);
    }

    public void addPasButton() {
        JButton button = new JButton("PassNew");
        add(button);

        button.setLocation(760, 260);
        //X:距离左边的距离
        button.setSize(70, 19);
        button.setFont(new Font("Rockwell", Font.BOLD, 8));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField nameField = new JTextField(15);
            JTextField Id = new JTextField(15);
            JTextField Phone = new JTextField(15);

            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Id:"));
            panel.add(Id);
            panel.add(new JLabel("Phone number:"));
            panel.add(Phone);
            panel.add(new JLabel("genger:"));
            JComboBox<String> typeCombo2 = new JComboBox<>(new String[]{"男", "女"});
            panel.add(typeCombo2);
            panel.add(new JLabel("District:"));
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Chinese Mainland", "Chinese Hong Kong", "Chinese Taiwan", "Chinese Macao"});
            panel.add(typeCombo);


            int result = JOptionPane.showConfirmDialog(this, panel, "Please input new passenger information", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String id = Id.getText();
                String ph = Phone.getText();
                String gen = "";
                if (typeCombo2.getSelectedItem().equals("男")) {
                    gen = "男";
                } else {
                    gen = "女";
                }
                String dis = "";
                String selectedDistrict = (String) typeCombo.getSelectedItem();
                switch (selectedDistrict) {
                    case "Chinese Mainland":
                    case "Chinese Hong Kong":
                    case "Chinese Taiwan":
                    case "Chinese Macao":
                        dis = selectedDistrict;
                        break;
                    default:
                        // 如果选项不在预期范围内，默认设置为 "Chinese Mainland"
                        dis = "Chinese Mainland";
                        break;
                }
                PasCas pc = new PasCas();
                pc.BeAPassenger(name, id, ph, gen, dis);
            }

        });
    }

    public void addCardButton() {
        JButton button = new JButton("CardNew");
        add(button);

        button.setLocation(760, 290);
        //X:距离左边的距离
        button.setSize(70, 19);
        button.setFont(new Font("Rockwell", Font.BOLD, 8));
        button.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField moneyField = new JTextField(15);

            panel.add(new JLabel("Money Initial"));
            panel.add(moneyField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input money amount", JOptionPane.OK_CANCEL_OPTION);
            String moneyStr = moneyField.getText();
            double money = Double.parseDouble(moneyStr);
            if (result == JOptionPane.OK_OPTION) {
                PasCas pc = new PasCas();
                String c = pc.TakeACard(money);
                JTextArea textArea = new JTextArea(c);
                textArea.setFont(new Font("Arial", Font.BOLD, 32));
                // 创建一个滚动窗格以容纳文本区域
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(50, 50));

                JOptionPane.showMessageDialog(this, scrollPane, "Your card number is:", JOptionPane.INFORMATION_MESSAGE);

                ImageIcon paymentCodeIcon = new ImageIcon("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\picture\\pay1.jpg");
                Image paymentCodeImage = paymentCodeIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); // 将图像缩放为300x300
                showPaymentCode(paymentCodeImage);
            }

        });
    }

    private void showPaymentCode(Image paymentCodeImage) {
        JFrame frame = new JFrame("Payment Code");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel(new ImageIcon(paymentCodeImage));
        frame.add(label);

        JButton paidButton = new JButton("Paid");
        paidButton.addActionListener(e -> {
            frame.dispose();
        });
        frame.add(paidButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void addChargeButton() {
        JButton button = new JButton("charge");
        add(button);

        button.setLocation(760, 320);
        //X:距离左边的距离
        button.setSize(70, 19);
        button.setFont(new Font("Rockwell", Font.BOLD, 8));
        button.addActionListener(e -> {

            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField codeField = new JTextField(15);
            panel.add(new JLabel("code"));
            panel.add(codeField);
            JTextField moneyField = new JTextField(15);
            panel.add(new JLabel("Money Charge"));
            panel.add(moneyField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Please input money amount", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String code = codeField.getText();
                String moneyStr = moneyField.getText();
                try {
                    double money = Double.parseDouble(moneyStr);
                    PasCas pc = new PasCas();
                    int q = pc.recharge(code, money);
                    if (q == 1) {
                        JOptionPane.showMessageDialog(this, "The card doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ImageIcon paymentCodeIcon = new ImageIcon("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\picture\\pay1.jpg");
                    Image paymentCodeImage = paymentCodeIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); // 将图像缩放为300x300
                    showPaymentCode(paymentCodeImage);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid money amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void addTimeLabel() {
        JLabel label = new JLabel();
        label.setSize(300, 50);
        label.setLocation(600, 5);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        updateTime(label); // 初始化时间
        add(label);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime(label); // 每秒更新时间
            }
        }, 0, 1000); // 每秒执行一次

    }

    private void updateTime(JLabel timeLabel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());
        timeLabel.setText(currentTime);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == showMap) {
            System.out.println("show map");
            Map.showMap();
        } else if (obj == NaviJ) {
            System.out.println("导航");
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField codeField = new JTextField(15);
            panel.add(new JLabel("start station"));
            panel.add(codeField);
            JTextField moneyField = new JTextField(15);
            panel.add(new JLabel("end station"));
            panel.add(moneyField);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input two station", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String code = codeField.getText();
                String moneyStr = moneyField.getText();
                try {
                    DefaultTableModel tableModel = new DefaultTableModel();
                    DefaultTableModel tableModel2 = new DefaultTableModel();
                    tableModel.addColumn("station_name");
                    tableModel.addColumn("line_name");
                    tableModel2.addColumn("初始站");
                    tableModel2.addColumn("线路1");
                    tableModel2.addColumn("站数1");
                    tableModel2.addColumn("换乘站1");
                    tableModel2.addColumn("线路2");
                    tableModel2.addColumn("站数2");
                    tableModel2.addColumn("换乘站2");
                    tableModel2.addColumn("线路3");
                    tableModel2.addColumn("站数3");
                    tableModel2.addColumn("目的地");
                    tableModel2.addColumn("总站数");
                    Naviga naviga = new Naviga();
                    int r = naviga.Nav(code, moneyStr, tableModel, tableModel2);
                    JTable table = new JTable();
                    if (r == 1) {
                        JOptionPane.showMessageDialog(this, "The stations doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (r == 3) {
                        table = new JTable(tableModel);
                    } else if (r == 4) {
                        table = new JTable(tableModel2);
                    }
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Navigation");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(800, 300);
                    frame.setLocationRelativeTo(null);
                    frame.add(scrollPane);
                    frame.toFront();
                    frame.setVisible(true);
                    // 在新窗口中显示表格数据

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid money amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (obj == askway) {
            System.out.println("查ride 1-n");
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Passenger", "Card"});
            panel.add(new JLabel("ask for"));
            panel.add(typeCombo);
            JTextField name_id = new JTextField(15);
            panel.add(new JLabel("name/id"));
            panel.add(name_id);
            JTextField codeField = new JTextField(15);
            panel.add(new JLabel("station"));
            panel.add(codeField);
            JTextField sta = new JTextField(15);
            panel.add(new JLabel("start_time"));
            panel.add(sta);
            JTextField end = new JTextField(15);
            panel.add(new JLabel("end_time"));
            panel.add(end);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input 1-n information you ask for", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String type = (String) typeCombo.getSelectedItem();
                String na_i = name_id.getText();
                String stati = codeField.getText();
                String stt = sta.getText();
                String en = end.getText();
                try {
                    DefaultTableModel tableModel = new DefaultTableModel();

                    tableModel.addColumn("id");
                    tableModel.addColumn("user_id");
                    tableModel.addColumn("start_station");
                    tableModel.addColumn("end_station");
                    tableModel.addColumn("price");
                    tableModel.addColumn("start_time");
                    tableModel.addColumn("end_time");

                    askRide a = new askRide();
                    int r = 0;
                    if(type.equals("Passenger")){
                        r = a.askRide1(na_i, stati, stt, en, tableModel);
                    }else {
                        r = a.askRide2(na_i, stati, stt, en, tableModel);
                    }
                    JTable table = new JTable();
                    if (r == 1) {
                        JOptionPane.showMessageDialog(this, "The stations doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }else if(r == 11){
                        JOptionPane.showMessageDialog(this, "It can't be null both.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }else if (r == 2) {
                        JOptionPane.showMessageDialog(this, "nothing record", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }else if(r == 3){
                        table = new JTable(tableModel);
                    }
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("records Ask");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(1000, 300);
                    frame.setLocationRelativeTo(null);
                    frame.add(scrollPane);
                    frame.toFront();
                    frame.setVisible(true);
                    // 在新窗口中显示表格数据

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid money amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(obj == sta){
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField name = new JTextField(15);
            panel.add(new JLabel("station name"));
            panel.add(name);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input the station you ask for", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String na = name.getText();
                changeSta c = new changeSta();
                int r = c.restart(na);
                if(r == 1){
                    JOptionPane.showMessageDialog(this, "The stations doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }else if(r == 0){
                    JOptionPane.showMessageDialog(this, "启动！ ", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }else if(obj == sto){
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField name = new JTextField(15);
            panel.add(new JLabel("station name"));
            panel.add(name);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input the station you ask for", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String na = name.getText();
                changeSta c = new changeSta();
                int r = c.stop(na);
                if(r == 1){
                    JOptionPane.showMessageDialog(this, "The stations doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }else if(r == 0){
                    JOptionPane.showMessageDialog(this, "停车！ ", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }else if(obj == cs){
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JTextField name = new JTextField(15);
            panel.add(new JLabel("station name"));
            panel.add(name);
            int result = JOptionPane.showConfirmDialog(this, panel, "Please input the station you ask for", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String na = name.getText();
                changeSta c = new changeSta();
                String r = c.checkState(na);
                if(r.equals("ex")){
                    JOptionPane.showMessageDialog(this, "The stations doesn't exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(this, na + " 现在的状态是："+ r, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}