package ponychan.ui;

import ponychan.downloader.DownloaderThread;

import javax.swing.*;
import java.awt.*;

public class DownloaderThreadUI extends JPanel {
    private JProgressBar barDownloadState;
    private JLabel fileTitle;

    private DownloaderThread thread;

    public DownloaderThreadUI(String fileName, DownloaderThread dthread) {
        super(new BorderLayout());

        thread = dthread;
        fileTitle.setText(fileName);

    }
}
