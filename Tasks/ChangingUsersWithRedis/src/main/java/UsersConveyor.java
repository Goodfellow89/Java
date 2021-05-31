public class UsersConveyor {
    public static void main(String[] args) {

        int numberOfUsers = 20;
        int timeDelayInSec = 1;

        RedisStorage storage = new RedisStorage();
        storage.connectClient();

        while (true) {
            int userId;
            String name;

            for (int i = 0; i < numberOfUsers; i++) {
                storage.add(i + 1, "u-" + (i + 1));
            }

            while (!storage.isEmpty()) {
                name = storage.getFirst();
                userId = storage.getScore(name);
                if ((Math.random() < 0.1) && (userId < numberOfUsers)) {
                    name = "u-" + (int) (userId + 1 + (Math.random() * (storage.size() - 1)));
                    System.out.println("> Пользователь " + name + " оплатил платную услугу");
                }
                System.out.println("- На главной странице показываем пользователя " + name);
                try {
                    Thread.sleep(timeDelayInSec * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                storage.delete(name);
            }
        }
    }
}
