package ponychan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                try {
                    Downloader downloader = Downloader.getInstance();
                    downloader.setChanThread(args[i]);
                    downloader.getImagesList();
                    HashMap<String, String> a = downloader.getFilesURLList();

                    for (Map.Entry<String, String> e : a.entrySet()) {
                        System.out.println(e.getValue() + " : " + e.getKey());
                    }

                    downloader.download();
                } catch (ChanThreadMatchException e) {
                    System.out.println(args[i] + "is not a 4chan thread");
                }
            }
        }
        else {
            System.err.println("Specify the 4chan threads in command line arguments");
        }
    }
}
