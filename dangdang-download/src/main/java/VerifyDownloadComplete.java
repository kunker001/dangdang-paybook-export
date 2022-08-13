import java.io.File;
import java.io.IOException;
import java.util.*;

public class VerifyDownloadComplete {
    static List<String> ignoreList = new ArrayList<String>() {{
        add("我们为什么控制不了自己-诱惑的科学");
        add("如何独处(2021版)");
        add("SPSS统计分析大全（光盘内容另行下载，地址见书封底）");
        // 已经下载完，但是没有当当结束图
        add("Python测试驱动开发：使用Django、Selenium和JavaScript进行Web编程(第2版)");
    }};

    // 我们为什么控制不了自己-诱惑的科学,如何独处(2021版)
    public static void main(String[] args) throws IOException {
        List<File> list = DownloadImg.getDataList("E:\\workspace\\mywork\\dangdang-d\\data");
        int count = list.size();
        for (File file : list) {
            if (file.listFiles().length == 0) {
                file.delete();
                continue;
            }
            File jsDir = new File(file.getAbsolutePath() + File.separator + "data");
            List<File> files = new ArrayList<File>();
            for (File listFile : jsDir.listFiles()) {
                if (listFile.isFile()) {
                    files.add(listFile);
                }
            }
            sortByModified(files);
//            System.out.println(files.get(files.size() - 1));

            if (DownloadImg.readFile(files.get(files.size() - 1).getAbsolutePath()).indexOf("dd-f_epub") == -1) {

                if (ignoreList.indexOf(file.getName()) == -1) {
                    System.out.println(file.getName());
                    count --;
//                    delAllFile(file.getAbsolutePath());
//                    file.delete();
                }
            }
        }

        System.out.println("共下载了" + count + "本书");
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //先删除这个文件夹下面的所有文件
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> sortByModified(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减,如果 if 中修改为 返回1 同时此处修改为返回 -1  排序就会是递增,
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return files;
    }



}
