import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    public static void copyFolder(String sourceDirectory, String destinationDirectory) {
        String destination;
        File[] files = new File(sourceDirectory).listFiles();

        try {
            for (File file : files) {
                destination = destinationDirectory + file.getPath().substring(sourceDirectory.length());
                File dstFile = new File(destination);
                if (file.isDirectory()) {
                    if (!Files.exists(Paths.get(destination))) {
                        Files.createDirectory(Paths.get(destination));
                    }
                    copyFolder(file.getPath(), destination);
                } else {
                    if (!Files.exists(Paths.get(destination))) {
                        Files.copy(file.toPath(), dstFile.toPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
