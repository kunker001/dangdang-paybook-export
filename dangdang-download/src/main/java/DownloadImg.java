import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadImg {
    public static void main(String[] args) throws IOException {
        List<File> bookList = getDataList("E:\\workspace\\mywork\\dangdang-d\\data");
        for (File file : bookList) {
            System.out.println(file.getName() + "图片开始下载");
            String path = file.getAbsolutePath();
            String dataPath = path + File.separator + "data";
            List<File> jslist = getDataList(dataPath);
            for (File jsFile : jslist) {
                if (jsFile.isFile()) {
                    getImgsAndSave(readFile(jsFile.getAbsolutePath()), dataPath);
                }
            }
            System.out.println(file.getName() + "图片下载完毕");
        }
    }

    /**
     * 图片url正则表达
     */
    public static Pattern pattern = Pattern.compile("\"http://[\\S\\.]+[:\\d]?[/\\S]+\\??[\\S=\\S&?]+[^\u4e00-\u9fa5]\"");

    /**
     * 获取图片列表
     * @param str
     * @return
     */
    public static void getImgsAndSave(String str, String basePath) {
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String url = matcher.group().replaceAll("\"", "").replaceAll("\\\\", "");
            if (url.equals("http://www.w3.org/2000/svg")) {
                continue;
            }
            String[] arr = url.split("/");
            int index = indexOf(arr, "ddimg.cn") + 1;
            if (index != -1) {
                String path = arrJoin(index, arr.length - 1, arr, File.separator);
                if (path.indexOf("www.ptpress.com") != -1) {
                    System.out.println(path);
                }
                downloadFile(url, basePath + path);
            }
        }
    }

    /**
     * 读取文件内容
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);

        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file),"UTF-8"); // 建立一个输入流对象reader

        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = "";

        line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine(); // 一次读入一行数据
        }
        reader.close();
        br.close();
        return sb.toString();
    }

    public static List<File> getDataList(String path) {
        File dir = new File(path);
        List<File> list = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            list.add(file);
        }

        return list;
    }


    public static int indexOf(String[] arr, String str) {
        for (int i = 0; i < arr.length; i ++) {
            if (arr[i].indexOf(str) != -1) {
                return i;
            }
        }
        return -1;
    }

    public static String arrJoin(int startIndex, int endIndex, String[] arr, String joinStr) {
        if (startIndex >= arr.length - 1  || startIndex == -1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            sb.append(joinStr);
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 下载图片
     * @param fileUrl
     * @param saveUrl
     * @return
     */
    public static String downloadFile(String fileUrl,String saveUrl) {
        HttpURLConnection httpUrl = null;
        byte[] buf = new byte[1024];
        int size = 0;
        try {
            //下载的地址
            URL url = new URL(fileUrl);
            //支持http特定功能
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            //缓存输入流,提供了一个缓存数组,每次调用read的时候会先尝试从缓存区读取数据
            BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
            File file = new File(saveUrl);
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdirs();//如果不存在就创建一个文件夹
            }
            //讲http上面的地址拆分成数组,
            String arrUrl[] = fileUrl.split("/");
            //输出流,输出到新的地址上面
            FileOutputStream fos = new FileOutputStream(saveUrl+"/"+arrUrl[arrUrl.length-1]);
            while ((size = bis.read(buf)) != -1){
                fos.write(buf, 0, size);
            }
            //记得及时释放资源
            fos.close();
            bis.close();
        } catch (IOException e) {
            System.out.println(fileUrl + "下载失败");
            e.printStackTrace();
        }
        httpUrl.disconnect();
        return null;
    }

}
