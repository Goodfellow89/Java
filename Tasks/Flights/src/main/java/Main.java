import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uu");
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public static void main(String[] args) {
        String file = "src/main/resources/tickets.json";
        int percent = 90;

        JSONObject tickets = parseJSON(file);
        JSONArray flights = (JSONArray) tickets.get("tickets");

        List<Integer> durations = new ArrayList<>();
        flights.forEach(f ->
                durations.add((int) Duration.between(getDateTime(((JSONObject) f), "departure"),
                        getDateTime(((JSONObject) f), "arrival")).toMinutes()));
        Collections.sort(durations);

        System.out.println(averageFlightTime(durations) + '\n' + percentileFlightTime(durations, percent));
    }

    public static JSONObject parseJSON(String file) {
        JSONObject flights = new JSONObject();
        StringBuilder data = new StringBuilder();
        try {
            Files.readAllLines(Path.of(file)).forEach(data::append);
            flights = (JSONObject) new JSONParser().parse(data.toString().replaceAll("\\uFEFF", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flights;
    }

    public static LocalDateTime getDateTime(JSONObject flight, String cond) {
        LocalDate date = LocalDate.parse(flight.get(cond + "_date").toString(), formatter);
        LocalTime time = LocalTime.parse(flight.get(cond + "_time").toString(), timeFormatter);
        return LocalDateTime.of(date, time);
    }

    public static String averageFlightTime(List<Integer> durations) {
        int commonDuration = 0;
        for (int d : durations) {
            commonDuration += d;
        }
        double averageFT = (double) commonDuration / durations.size();

        return "Average" + goodVision(averageFT);
    }

    public static String percentileFlightTime(List<Integer> durations, int percent) {
        double num = (double) percent * (durations.size()-1) / 100 + 1;
        System.out.println(num);
//        int percentile = (num == (int) num) ? durations.get((int) num) : durations.get((int) Math.ceil(num - 1));

        double percentile = durations.get((int) Math.floor(num - 1)) + (num % Math.floor(num)) * (durations.get((int) Math.floor(num)) - durations.get((int) Math.floor(num - 1)));

        return percent + " percentile" + goodVision(percentile);
    }

    public static String goodVision(double time) {
        int hours = (int) time / 60;
        int minutes = (int) time - hours * 60;
        int seconds = (int) (time - hours * 60 - minutes) * 60;

        return " flight time - " + hours + " hours " + minutes + " minutes " + seconds + " seconds.";
    }
}
