import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class LinkParser extends RecursiveTask<LinkedHashSet<String>> {

    private String url;
    LinkedHashSet<String> set = new LinkedHashSet<>();

    public LinkParser(String url) {
        this.url = url;
    }

    @Override
    protected LinkedHashSet<String> compute() {

        List<LinkParser> tasks = new ArrayList<>();

        try {
            Thread.sleep(100);
            set.add(url);
            Elements elements = Jsoup.connect(url).get().getElementsByTag("a");
            for (Element element : elements) {
                String link = element.attr("abs:href");
                if (link.startsWith(url) && link.endsWith("/") && !set.contains(link)) {
                    LinkParser task = new LinkParser(link);
                    task.fork();
                    tasks.add(task);
                }
            }
            tasks.forEach(t -> set.addAll(t.join()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return set;
    }
}
