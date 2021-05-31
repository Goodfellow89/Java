import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sourse, destination;

        for (; ; ) {
            System.out.println("Введите адрес папки для копирования:");
            sourse = scanner.nextLine();
            System.out.println("Введите адрес папки для записи:");
            destination = scanner.nextLine();
            FileUtils.copyFolder(sourse, destination);
            System.out.println("Copying success!");
        }
    }
}
