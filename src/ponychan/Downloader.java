package ponychan;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class Downloader {
    private static Downloader INSTANCE = new Downloader();

    private ChanThread chanThread;

    private HashMap<String, String> filesURLList;

    private static final String imageServerStringURL = "http://i.4cdn.org/";

    public static Downloader getInstance() {
        return INSTANCE;
    }

    private Downloader() {}

    public void getImagesList() {
        if (chanThread != null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) chanThread.getThreadApiURL().openConnection();
                connection.setRequestMethod("GET");
                int status = connection.getResponseCode();
                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    in.close();
                    connection.disconnect();

                    String result = content.toString();

                    JSONObject jsonResult = new JSONObject(result);

                    int posts = jsonResult.getJSONArray("posts").getJSONObject(0).getInt("replies") + 1;

                    filesURLList = new HashMap<>();

                    String imageServerStringURLBoard = imageServerStringURL.concat(chanThread.getBoard()).concat("/");

                    for (int i = 0; i < posts; i++) {
                        try {
                            long timestamp = jsonResult.getJSONArray("posts").getJSONObject(i).getLong("tim");
                            String format = jsonResult.getJSONArray("posts").getJSONObject(i).getString("ext");
                            String fileName = jsonResult.getJSONArray("posts").getJSONObject(i).getString("filename");
                            String distantFileName = Long.toString(timestamp).concat(format);
                            filesURLList.put(imageServerStringURLBoard.concat(distantFileName), fileName.concat(format));
                        }
                        catch (JSONException ex) {}
                    }
                }
                else {

                }
            }
            catch (IOException ex) {
                System.err.println("An IO exception as occurred");
            }
        }
        else {
            throw new NullPointerException("Error : No thread set in the downloader");
        }
    }

    public void download() {
        System.out.println("Downloading " + filesURLList.size() + " files :");

        File directory = new File(chanThread.getThreadNumber());
        if (!directory.exists()) {
            boolean result = false;
            try {
                directory.mkdir();
            }
            catch (SecurityException se) {
                System.err.println("Error : Can't write in this directory");
            }
        }

        ArrayList<DownloaderThread> dThreadsList = new ArrayList<>();

        for (Map.Entry<String, String> e : filesURLList.entrySet()) {
            DownloaderThread dthread = new DownloaderThread(e.getValue(), e.getKey(), directory.getName(), e.getValue());
            dthread.start();
            dThreadsList.add(dthread);
        }
        for (DownloaderThread dthread : dThreadsList) {
            try {
                dthread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getThreadURL() {
        return chanThread.getThreadURLString();
    }

    public void setChanThread(ChanThread thread) {
        chanThread = thread;
    }

    public void setChanThread(String url) throws ChanThreadMatchException {
        chanThread = new ChanThread(url);
    }

    public HashMap<String, String> getFilesURLList() {
        return filesURLList;
    }
}
