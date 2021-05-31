import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String location = "src/resources/map.json";
        writeFile(location);
        JSONObject map = parseJSON(location);
        getNumOfStatonsByLine(map).forEach(System.out::println);
        System.out.println("\nОбщее количество переходов - " + ((JSONArray) map.get("connections")).size());
    }

    public static void writeFile(String location) {
        try {
            FileWriter dataFile = new FileWriter(location);
            dataFile.write(fillingOfJSONFile().toJSONString());
            dataFile.flush();
            dataFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject fillingOfJSONFile() {
        JSONObject data = new JSONObject();

        try {
            Document doc = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
            data.put("lines", fillLines(doc));
            data.put("connections", fillConnections(doc));
            data.put("stations", fillStations(doc));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static JSONArray fillLines(Document doc) {
        JSONArray lines = new JSONArray();
        Elements elLines = doc.select("div.js-toggle-depend");

        elLines.forEach(e -> {
            JSONObject line = new JSONObject();
            line.put("number", e.select("span.js-metro-line").attr("data-line"));
            line.put("name", e.text());
            lines.add(line);
        });
        return lines;
    }

    public static JSONObject fillStations(Document doc) {
        JSONObject stations = new JSONObject();
        Elements elStations = doc.select("div.js-metro-stations");

        elStations.forEach(e -> {
            JSONArray listOfStations = new JSONArray();
            listOfStations.addAll(e.select("span.name").eachText());
            stations.put(e.attr("data-line"), listOfStations);
        });
        return stations;
    }

    public static JSONArray fillConnections(Document doc) {
        JSONArray connections = new JSONArray();
        Elements elLines = doc.select("div.js-metro-stations");

        elLines.forEach(l -> {
            l.select("p").forEach(s -> {
                JSONArray connection = new JSONArray();

                JSONObject currentStation = new JSONObject();
                String station = s.select("span.name").text();
                currentStation.put("line", l.attr("data-line"));
                currentStation.put("station", station);
                connection.add(currentStation);

                s.select("span.t-icon-metroln").forEach(c -> {
                    JSONObject connectStation = new JSONObject();

                    connectStation.put("line", c.className().replace("t-icon-metroln ln-", ""));
                    connectStation.put("station", c.attr("title").split("«|»")[1]);
                    connection.add(connectStation);
                });

                connection.sort((Comparator<JSONObject>) (c1, c2) -> c2.get("line").toString().compareTo(c1.get("line").toString()));
                if (connection.size() > 1 && !connections.contains(connection)) {
                    connections.add(connection);
                }
            });
        });

        return connections;
    }

    public static JSONObject parseJSON(String location) {
        JSONObject map = new JSONObject();
        StringBuilder builder = new StringBuilder();
        try {
            List<String> data = Files.readAllLines(Paths.get(location));
            data.forEach(s -> builder.append(s));
            String dataFile = builder.toString();
            map = (JSONObject) new JSONParser().parse(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> getNumOfStatonsByLine(JSONObject map) {
        JSONObject stations = (JSONObject) map.get("stations");
        JSONArray lines = (JSONArray) map.get("lines");
        ArrayList<String> numOfStationByLine = new ArrayList<>();
        lines.forEach(l -> numOfStationByLine.add(((JSONObject) l).get("name") + " содержит " +
                ((JSONArray) stations.get(((JSONObject) l).get("number"))).size() + " станций"));
        return numOfStationByLine;
    }
}
