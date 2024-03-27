import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchTextField;
    private JButton loadButton, searchButton, quitButton;
    private Path filePath;

    public DataStreamsGUI() {
        setTitle("Java Data Streams Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchTextField = new JTextField(20);
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search String:"));
        topPanel.add(searchTextField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);
        textPanel.add(originalScrollPane);
        textPanel.add(filteredScrollPane);
        add(textPanel, BorderLayout.CENTER);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(DataStreamsGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().toPath();
                    try {
                        String content = new String(Files.readAllBytes(filePath));
                        originalTextArea.setText(content);
                        filteredTextArea.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataStreamsGUI.this, "Error loading file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filePath != null) {
                    String searchString = searchTextField.getText();
                    try (Stream<String> lines = Files.lines(filePath)) {
                        String filteredContent = lines.filter(line -> line.contains(searchString))
                                .reduce("", (acc, line) -> acc + line + "\n");
                        filteredTextArea.setText(filteredContent);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataStreamsGUI.this, "Error searching file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DataStreamsGUI().setVisible(true);
            }
        });
    }
}
