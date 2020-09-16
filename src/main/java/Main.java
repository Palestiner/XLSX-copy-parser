import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import static java.nio.file.Files.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        String propsFileName = "config.yaml";
        InputStream in = newInputStream(Paths.get(propsFileName));
        props.load(in);
        while (true) {
            walk(Paths.get(props.getProperty("from")))
                    .forEach(file -> {
                        if (file.endsWith(".log")) {
                            try {
                                lines(file).forEach(line -> {
                                    String[] split = line.split("\t");
                                    System.out.println(line);
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            try {
                Thread.sleep(Integer.parseInt(props.getProperty("pause")) * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
