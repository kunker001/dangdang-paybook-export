import com.alibaba.fastjson.JSONObject;
import com.dangdang.entity.BookList;
import com.dangdang.entity.DMedia;
import com.dangdang.util.DateUtils;
import com.dangdang.util.HttpUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadBook {

    /**
     * token
     */
    public static String token = "pc_fbc435477cd09a6ad70d9049a3790e331b7cfafdcd08c2460fa5fc782f8a40dd";

    public static List<String> ignoreBookList = new ArrayList<String>(){{
        add("佛教艺术经典(全三册)");
        add("边缘型人格障碍：深入解读边缘型人格的权威著作");
        add("总裁强占：毒妻难驯服");
        add("父亲的格局，母亲的情绪，决定孩子的未来");
        add("西游八十一案(第1季)");
        add("何求美人折");
        add("婚成蜜就：总裁的契约新娘");
        add("销售洗脑：把逛街者变成购买者的8条黄金法则");
        add("月亮与六便士（荣获4项大奖中译本）");
        add("惹爱成婚：娇妻追入怀");
    }};

    // 续传列表
    public static List<String> continueBookList = new ArrayList<String>(){{
//        add("C# 8.0本质论");
//        add("C Primer Plus(第6版)中文版(修订版)");
//        add("CentOS 8 Linux系统管理与一线运维实战");
//        add("Elasticsearch全面解析与实践");
//        add("Go语言学习指南-惯例模式与编程实践");
//        add("JavaScript悟道");
//        add("Java修炼指南-核心框架精讲");
//        add("从零开始-Qt可视化程序设计基础教程");
//        add("低代码开发平台的设计与实现——基于元数据模型");
//        add("分布式中间件技术实战：Java版");
//        add("动手打造深度学习框架");
//        add("物联网全栈开发原理与实战");
//        add("用Python动手学统计学");
//        add("药店店员基础训练手册");
    }};

    /**
     * 书籍列表
     */
    public static String bookListApi = "http://e.dangdang.com/media/api.go?action=getUserBookList&deviceSerialNo=html5&macAddr=html5&channelType=html5&permanentId=20220504203952887270049631688712053&returnType=json&channelId=70000&clientVersionNo=6.8.0&platformSource=DDDS-P&fromPlatform=106&deviceType=pconline&token={0}&lastMediaAuthorityId=&pageSize=150&type=full";

    public static void main(String[] args) {
        String body = HttpUtils.sendGet(MessageFormat.format(bookListApi, token), null);
        Map<String, Object> map = JSONObject.parseObject(body);
        BookList data = JSONObject.parseObject(((JSONObject)map.get("data")).toJSONString(), BookList.class);

        List<String[]> cmds = new ArrayList<String[]>();
        List<String> dataList = getDataList();
        for (DMedia dMedia : data.getMediaList()) {
            String title = dMedia.getTitle().replace(":", "-").replace("*", "");
            if ((dataList.indexOf(title) == -1 || continueBookList.contains(title)) && ignoreBookList.indexOf(title) == -1)  {
                System.out.println(title);
                if (continueBookList.contains(title)) {
                    cmds.add(new String[] {"cmd", "/c", MessageFormat.format("cd \"data/{0}\" & dangdang {1} {2} slow", title, dMedia.getMediaId() + "", token)});
                } else {
                    cmds.add(new String[] {"cmd", "/c", MessageFormat.format("mkdir \"data/{0}\" & cd \"data/{0}\" & dangdang {1} {2} slow", title, dMedia.getMediaId() + "", token)});
                }
            }
//            cmds.add(new String[] {"powershell", "/c", MessageFormat.format("mkdir \"data/{0}\" ; cd \"data/{0}\"; dangdang {1} {2} slow", dMedia.getTitle().replace(":", "-"), dMedia.getMediaId(), token)});
//            cmds.add(new String[] {"cmd", "/c", MessageFormat.format("mkdir \"data/{0}\" & cd \"data/{0}\"", dMedia.getTitle().replace(":", "-").replace("*", ""), dMedia.getMediaId() + "", token)});
        }

//        cmds.add(new String[] {"cmd", "/c", MessageFormat.format("mkdir \"data/{0}\" ; cd \"data/{0}\"", "测试")});

        try {
            for (int i = 0; i < cmds.size(); i++) {
                execute(cmds.get(i));
//                System.out.println(cmds.get(i)[2]);
                System.out.println("-----------------------------------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void execute(String[] cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();
        SequenceInputStream sequenceIs = new SequenceInputStream(inStream, errStream);
        BufferedInputStream bufStream = new BufferedInputStream(sequenceIs);
        Reader reader = new InputStreamReader(bufStream, getDefaultEncoding());
        BufferedReader bufReader = new BufferedReader(reader);
        String line;
        while ((line = bufReader.readLine()) != null) {
            System.out.println(line);
        }
        inStream.close();
        errStream.close();
        process.destroy();
    }

    public static String getDefaultEncoding() {
        if (getOS().trim().toLowerCase().startsWith("win")) {
            return "UTF-8";
//            return "GBK";
        } else {
            return "UTF-8";
        }
    }

    public static List<String> getDataList() {
        File dir = new File("./data");
        List<String> list = new ArrayList<String>();
        for (File file : dir.listFiles()) {
            list.add(file.getName());
        }

        return list;
    }

    public static String getOS() {
        String os = System.getProperty("os.name");
        System.out.println(os);
        return os;
    }
}
