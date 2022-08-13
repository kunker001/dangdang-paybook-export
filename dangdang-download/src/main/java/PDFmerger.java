import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 保存的pdf合并
 */
public class PDFmerger {
    public static void main(String[] args) {
        List<File> list =  DownloadImg.getDataList("E:\\tmp\\book");
        List<String> completeList =  DownloadImg.getDataList("E:\\tmp\\bookMerger").stream().map(File::getName).collect(Collectors.toList());
        for (File file : list) {
            if (completeList.contains(file.getName())) {
                continue;
            }
            System.out.println(file.getName() + " 整合开始");
            doMerger(file, "E:\\tmp\\bookMerger");
            System.out.println(file.getName() + " 整合结束");
        }
    }

    /**
     * 单本书合并
     * @param parent
     */
    public static void doMerger(File parent, String savePath) {
        List<File> list = Arrays.asList(parent.listFiles());
        sortByName(list);
        List<String> megerList = new ArrayList<String>();
        for (File file : list) {
            megerList.add(file.getAbsolutePath());
        }
        doMergePdf(megerList, parent.getName(), savePath + File.separator + parent.getName());
    }

    public static List<File> sortByName(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return getChapterIndex(f1.getName()) - getChapterIndex(f2.getName());
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return files;
    }

    public static int getChapterIndex(String name) {
        return Integer.parseInt(name.substring(getStartIndexByName(name), getEndIndexByName(name)));
    }

    public static int getStartIndexByName(String name) {
        return name.indexOf("chapter-") + "chapter-".length();
    }

    public static int getEndIndexByName(String name) {
        return name.indexOf(".htm.pdf");
    }

    public static void doMergePdf(List<String> fileList, String pdfName, String savePath) {
        //pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        //合并pdf生成的文件名
        String destinationFileName = pdfName + ".pdf";
        // 合并后pdf存放路径
        File file3 = new File(savePath);
        try{
            if(!file3.exists()){
                file3.mkdirs();
            }
        }catch(Exception e1){
            e1.printStackTrace();
        }

        for (String s : fileList) {
            mergePdf.addSource(s);
        }

        //设置合并生成pdf文件名称
        mergePdf.setDestinationFileName(savePath + File.separator + destinationFileName);
        //合并pdf
        try {
            try {
                mergePdf.mergeDocuments();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (COSVisitorException e) {
            e.printStackTrace();
        }
        System.out.println("pdf文件合并成功");
    }
}
