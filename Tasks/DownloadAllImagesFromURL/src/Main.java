import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://lenta.ru").get();
            copyAllImagesFromPage(doc, "img.g-picture");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyAllImagesFromPage(Document doc, String tag) {
        Elements imagesByTag = doc.select(tag);
        imagesByTag.forEach(element -> {
            try {
                URL url = new URL(element.attr("abs:src"));
                String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
                File imageFile = new File("images/" + fileName);
                if (!imageFile.exists()) {
                    Files.copy(url.openStream(), imageFile.toPath());
                }
                System.out.println(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
