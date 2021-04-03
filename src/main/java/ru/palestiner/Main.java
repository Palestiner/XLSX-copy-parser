package ru.palestiner;

import jxl.write.WriteException;
import ru.palestiner.xlsx.XLSXFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.nio.file.Files.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        String propsFileName = "config.yaml";
        InputStream in = newInputStream(Paths.get(propsFileName));
        props.load(in);
        in.close();
        String from = props.getProperty("from");
        String to = props.getProperty("to");
        System.gc();
        for (; ; ) {
            walk(Paths.get(from)).forEach(file -> {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(".log")) {
                    try {
                        XLSXFile xlsx = new XLSXFile(to + "\\" + file.getFileName().toString().replace("log", "xls"));
                        List<String> lines = readAllLines(file, StandardCharsets.ISO_8859_1);
                        lines
                                .forEach(line -> {
                                    try {
                                        xlsx.writeRow(line.trim().split("\t"));
                                    } catch (WriteException e) {
                                        e.printStackTrace();
                                    }
                                });
                        xlsx.writeBook();
                    } catch (IOException | WriteException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("Complete!");
            Thread.sleep(Integer.parseInt(props.getProperty("pause")) * 60 * 1000);
        }
    }
}
