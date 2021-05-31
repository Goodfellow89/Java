import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите адрес сайта в формате *example.com* иначе обязательно вылетит исключение:");
        String url = "https://" + sc.nextLine() + "/";
        String path = "src/main/resources/file.txt";

        List<String> list = new ArrayList<>(new ForkJoinPool().invoke(new LinkParser(url)));
        Collections.sort(list);

        try {
            PrintWriter writer = new PrintWriter(path);
            list.forEach(l -> {
                writer.write(getTabs(l) + l + "\n");
            });
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getTabs(String link) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < link.split("/").length - 3; i++) {
            tabs.append('\t');
        }
        return tabs.toString();
    }
}