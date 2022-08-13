import java.io.File;

public class DataTest {

    public static void main(String[] args) {
        File dir = new File("./data");
        for (File file : dir.listFiles()) {
            System.out.println(file.getName());
        }
    }
}
