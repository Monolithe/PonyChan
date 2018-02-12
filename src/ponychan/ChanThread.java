package ponychan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.*;

public class ChanThread {
    private static Pattern chanURL = Pattern.compile("http://boards.4chan.org/([a-z]+)/thread/([0-9]+)/(.*)");

    private URL threadURL;
    private URL threadApiURL;

    private String board;
    private String threadNumber;


    public ChanThread(String url) throws ChanThreadMatchException {
        Matcher matcher = chanURL.matcher(url);
        if (matcher.matches()) {
            try {
                threadURL = new URL(url);

                String threadApiURLString = "http://a.4cdn.org/";

                String path = threadURL.getPath();
                String[] parts = path.split("/");

                board = parts[1];
                threadNumber = parts[3];

                threadApiURLString = threadApiURLString.concat(board);
                threadApiURLString = threadApiURLString.concat("/thread/");
                threadApiURLString = threadApiURLString.concat(threadNumber);
                threadApiURLString = threadApiURLString.concat(".json");

                threadApiURL = new URL(threadApiURLString);
            }
            catch (MalformedURLException ex) {
                throw new ChanThreadMatchException("Error : This is not even a URL");
            }
        }
        else {
            throw new ChanThreadMatchException("Error : This is not a 4chan thread");
        }
    }

    public URL getThreadApiURL() {
        return threadApiURL;
    }

    public String getThreadApiURLString() {
        return threadApiURL.toString();
    }

    public URL getThreadURL() {
        return threadURL;
    }

    public String getThreadURLString() {
        return threadURL.toString();
    }

    public String getBoard() {
        return board;
    }

    public String getThreadNumber() {
        return threadNumber;
    }
}
