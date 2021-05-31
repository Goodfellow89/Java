import org.redisson.Redisson;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisStorage {

    private RScoredSortedSet<String> set;

    public void connectClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);
        set = client.getScoredSortedSet("users");
        set.clear();
    }

    public void add(int userId, String name) {
        set.add(userId, name);
    }

    public int getScore(String v) {
        return set.getScore(v).intValue();
    }

    public void delete(String v) {
        set.remove(v);
    }

    public String getFirst() {
        return set.first();
    }

    public int size() {
        return set.readAll().size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }
}