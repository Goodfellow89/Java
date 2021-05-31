import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ShopManager {

    public static MongoCollection<Document> shopCollection;
    public static MongoCollection<Document> goodsCollection;
    public static ObjectMapper mapper;

    public static void main(String[] args) {

        MongoClient client = new MongoClient("127.0.0.1", 27017);
        MongoDatabase database = client.getDatabase("test");
        shopCollection = database.getCollection("shops");
        goodsCollection = database.getCollection("goods");
        mapper = new ObjectMapper();

        shopCollection.drop();
        goodsCollection.drop();

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите команду:");

        while (true) {
            String[] command = sc.nextLine().split(" ");
            switch (command[0]) {
                case "СТАТИСТИКА_ТОВАРОВ":
                    statistics();
                    break;
                case "ДОБАВИТЬ_МАГАЗИН":
                    addShop(command[1]);
                    break;
                case "ДОБАВИТЬ_ТОВАР":
                    addGoods(command[1], Double.parseDouble(command[2]));
                    break;
                case "ВЫСТАВИТЬ_ТОВАР":
                    exposeGoods(command[2], command[1]);
                    break;
                default:
                    System.out.println("Вы ввели несуществующую команду. Попробуйте ввести команду еще раз.");
                    break;
            }
        }
    }

    public static void statistics() {
        Bson lookup = Aggregates.lookup("goods", "goods", "name", "production");
        Bson unwind = Aggregates.unwind("$production");
        Bson group = Aggregates.group("$name", Accumulators.avg("averageCost", "$production.price"), Accumulators.max("maxPrice", "$production.price"), Accumulators.min("minPrice", "$production.price"));

        AggregateIterable<Document> shopList = shopCollection.aggregate(Arrays.asList(lookup, unwind, group));

        for (Document doc : shopList) {
            Bson shopFilter = Aggregates.match(Filters.eq("name", doc.get("_id")));
            Bson replaceRoot = Aggregates.replaceRoot("$production");

            ArrayList<Bson> extraFilters = new ArrayList<>(Arrays.asList(lookup, shopFilter, unwind, replaceRoot));

            ArrayList<Bson> fullNumOfGoods = new ArrayList<>(extraFilters);
            fullNumOfGoods.add(Aggregates.count("production"));

            ArrayList<Bson> priceLT = new ArrayList<>(extraFilters);
            priceLT.addAll(Arrays.asList(Aggregates.match(Filters.lt("price", 100)), Aggregates.count()));

            System.out.println("Магазин " + doc.get("_id") +
                    "\n\tВсего товаров - " + numOfGoods(fullNumOfGoods, "production") +
                    "\n\tСредняя стоимость всех товаров - " + doc.get("averageCost") +
                    "\n\tСамый дорогой товар - " + extraPrice(doc, "maxPrice", extraFilters) +
                    "\n\tСамый дешевый товар - " + extraPrice(doc, "minPrice", extraFilters) +
                    "\n\tКоличество товаров дешевле 100 руб. - " + numOfGoods(priceLT, "count"));
        }
    }

    public static int numOfGoods(ArrayList<Bson> filters, String query) {
        AggregateIterable<Document> numOfGoods = shopCollection.aggregate(filters);
        return Integer.parseInt(numOfGoods.first().get(query).toString());
    }

    public static String extraPrice(Document doc, String query, ArrayList<Bson> filters) {
        filters.add(Aggregates.match(Filters.eq("price", doc.get(query))));
        AggregateIterable<Document> priceGoods = shopCollection.aggregate(filters);

        StringBuilder priceString = new StringBuilder();
        for (Document document : priceGoods) {
            priceString.append(" и ").append(document.get("name"));
        }

        filters.remove(filters.size() - 1);
        return priceString.delete(0, 3).toString();
    }

    public static void addShop(String shopName) {
        Shop shop = new Shop();
        shop.setName(shopName);
        shop.setGoods(new ArrayList<Goods>());
        try {
            shopCollection.insertOne(Document.parse(mapper.writeValueAsString(shop)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("Магазин добавлен");
    }

    public static void addGoods(String goods, double price) {
        Goods product = new Goods();
        product.setName(goods);
        product.setPrice(price);
        try {
            goodsCollection.insertOne(Document.parse(mapper.writeValueAsString(product)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("Товар добавлен");
    }

    public static void exposeGoods(String shop, String goods) {
        shopCollection.updateOne(Filters.eq("name", shop), Updates.push("goods", goods), new UpdateOptions().upsert(true));
        System.out.println("Товар на витрине");
    }
}
