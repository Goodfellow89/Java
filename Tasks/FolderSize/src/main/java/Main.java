import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path, dimension;
        boolean dimCh;
        double size;
        int type;

        DecimalFormat format = new DecimalFormat("#.##");
        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.println("Введите путь до папки:");
            path = scanner.nextLine();
            if (path.matches("[\\w\\d\\/\\-.:,]+")) {
                size = FileUtils.calculateFolderSize(path);
                type = 0;
                dimCh = true;
                while (dimCh && type != 4) {
                    if (size > 820) {
                        type++;
                        size /= 1024;
                    } else {
                        dimCh = false;
                    }
                }
                switch (type) {
                    case 0:
                        dimension = "байт";
                        break;
                    case 1:
                        dimension = "Кб";
                        break;
                    case 2:
                        dimension = "Мб";
                        break;
                    case 3:
                        dimension = "Гб";
                        break;
                    default:
                        dimension = "Тб";
                }
                System.out.println("Размер папки " + path + " cоставляет " + format.format(size) + " " + dimension);
            } else {
                System.out.println("Неверный формат пути");
            }
        }
    }
}
