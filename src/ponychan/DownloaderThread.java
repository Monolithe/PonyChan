package ponychan;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloaderThread extends Thread {
    private URL fileUrl;
    private String directory;
    private String fileName;

    public DownloaderThread(String name, String url, String dir, String file) {
        super(name);

        try {
            fileUrl = new URL(url);
            directory = dir;
            fileName = file;
        }
        catch (MalformedURLException ex) {
            System.err.println("Error : Invalid file URL");
        }
    }

    @Override
    public void run() {
        try {
            ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
            FileOutputStream fos = new FileOutputStream(directory + "/" + fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println(fileUrl.toString() + " : finished");
        }
        catch (IOException ex) {
            System.err.println("Error : Connection problem");
        }
    }
}
