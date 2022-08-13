package com.dangdang;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.entity.BookList;
import com.dangdang.util.HttpUtils;

import java.util.Map;

public class HttpTest {

    public static void main(String[] args) {
        String body = HttpUtils.sendGet("http://e.dangdang.com/media/api.go?action=getUserBookList&deviceSerialNo=html5&macAddr=html5&channelType=html5&permanentId=20220504203952887270049631688712053&returnType=json&channelId=70000&clientVersionNo=6.8.0&platformSource=DDDS-P&fromPlatform=106&deviceType=pconline&token=pc_cb4193a341924eda76ea7b48b58b3cdf10cf48e94c5c9bcee6c876c02d6e2758&lastMediaAuthorityId=&pageSize=150&type=full", null);
        Map<String, Object> map = JSONObject.parseObject(body);
        BookList data = JSONObject.parseObject(((JSONObject)map.get("data")).toJSONString(), BookList.class);
        System.out.println(data);
    }
}
