package ponychan.ui;

import ponychan.chan.ChanThreadMatchException;
import ponychan.downloader.Downloader;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DownloaderUI extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel topButtonsPanel;
    private JScrollPane centerScrollPane;
    private JPanel centerPanel;

    private JTextField txtThreadURL;
    private JLabel lblThreadURL;

    private JButton filesListButton;
    private JButton downloadButton;

    private Downloader downloader;

    public DownloaderUI() {
        downloader = Downloader.getInstance();
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new BorderLayout());
        topButtonsPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new GridLayout(0, 1));

        txtThreadURL = new JTextField();

        topPanel.add(txtThreadURL, BorderLayout.NORTH);

        filesListButton = new JButton("Get files list");
        topButtonsPanel.add(filesListButton, BorderLayout.WEST);

        downloadButton = new JButton("Download");
        topButtonsPanel.add(downloadButton, BorderLayout.EAST);

        topPanel.add(topButtonsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        centerScrollPane = new JScrollPane();

        mainPanel.add(centerScrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setSize(new Dimension(800, 600));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        filesListButton.addActionListener(e -> {
            if (txtThreadURL.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please specify an URL", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    centerPanel.removeAll();
                    downloader.setChanThread(txtThreadURL.getText());
                    downloader.getImagesList();
                    HashMap<String, String> urls = downloader.getFilesURLList();
                    for (Map.Entry<String, String> ent : urls.entrySet()) {
                        centerPanel.add(new JLabel(ent.getKey()));
                    }
                    centerScrollPane.setViewportView(centerPanel);
                    repaint();
                } catch (ChanThreadMatchException ex) {
                    JOptionPane.showMessageDialog(this, "This is not a 4chan thread", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        downloadButton.addActionListener(e -> {
            try {
                downloader.download();
            }
            catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this, "Can't write into this directory", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
