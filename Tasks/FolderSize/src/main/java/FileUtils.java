import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static long calculateFolderSize(String path) {
        Path pathDir = Paths.get(path);
        long size = 0;
        try {
            List<Path> files = Files.walk(pathDir).collect(Collectors.toList());
            for (Path file : files) {
                if (!file.toFile().isDirectory()) {
                    size += file.toFile().length();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
}
