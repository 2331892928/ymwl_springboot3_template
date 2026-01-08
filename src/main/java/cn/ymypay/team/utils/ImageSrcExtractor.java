package cn.ymypay.team.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/11/28 上午10:55
 * @description: 图片工具
 */
public class ImageSrcExtractor {
    /**
     * 从HTML字符串中提取所有img标签的src属性
     * @param html 原始HTML字符串
     * @return 所有src属性值的列表
     */
    public static List<String> extractImgSrcByRegex(String html) {
        List<String> srcList = new ArrayList<>();
        if (html == null || html.isEmpty()) {
            return srcList;
        }

        // 正则表达式匹配img标签的src属性（兼容单/双引号、无引号的情况）
        String regex = "<img\\s+[^>]*src\\s*=\\s*[\"']?([^\"'>]+)[\"']?[^>]*>";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // 忽略大小写
        Matcher matcher = pattern.matcher(html);

        // 遍历所有匹配结果
        while (matcher.find()) {
            String src = matcher.group(1).trim();
            if (!src.isEmpty()) {
                srcList.add(src);
            }
        }
        return srcList;
    }
}
