package com.cmgzs.utils;

import com.cmgzs.domain.archive.Content;
import com.cmgzs.utils.text.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档数结构的相互转换
 * 扁平树 <==> 嵌套树
 *
 * @author huangzhenyu
 * @date 2022/9/23
 */
public class TreeUtil {

    /**
     * 集合转哈希表
     *
     * @param list 扁平结构
     * @return 哈希表
     */
    public static Map<String, Content> list2Map(List<Content> list) {

        Map<String, Content> mapTable = new HashMap<>();

        for (Content content : list) {
            if (content.getChildren() == null) {
                content.setChildren(new ArrayList<>());
            }
            mapTable.put(content.getId(), content);
        }
        return mapTable;
    }

    /**
     * 扁平树 ==> 嵌套树
     *
     * @param list     扁平结构
     * @param mapTable 哈希表
     * @param startId  文档起始点Id
     * @return 嵌套树形结构
     */
    public static List<Content> generateTree(List<Content> list, Map<String, Content> mapTable, String startId) {

        ArrayList<Content> res = new ArrayList<>();

        Content startContent = mapTable.get(startId);
        while (startContent != null) {
            res.add(startContent);
            if (StringUtils.isNotEmpty(startContent.getNextId())) {
                startContent = mapTable.get(startContent.getNextId());
            } else {
                break;
            }
        }

        // TODO: 2023/2/24 子层级
        for (Content content : res) {
            if (StringUtils.isEmpty(content.getSubId())) {
                break;
            }
            List<Content> children = generateTree(list, mapTable, content.getSubId());
            content.setChildren(children);
        }
        return res;
    }


    /**
     * 嵌套树 ==> 扁平树
     *
     * @param content Tree结构
     * @param result  结果
     */
    public static void tree2List(Content content, List<Content> result) {
        if (content == null)
            return;
        result.add(content);
        List<Content> children = content.getChildren();
        content.setChildren(null);
        if (children != null) {
            children.forEach(e -> {
                if (StringUtils.isNotEmpty(e.getId())) {
                    tree2List(e, result);
                }
            });
        }
    }

    /**
     * 嵌套树 ==> 扁平树
     *
     * @param content Tree结构
     * @param result  结果
     */
    public static void tree2ListByType(Content content, List<Content> result) {
        if (content == null)
            return;
        result.add(content);

        List<Content> children = content.getChildren();

        if (children != null) {
            children.forEach(e -> {
                if (StringUtils.isNotEmpty(e.getType())) {
                    tree2ListByType(e, result);
                }
            });
            if (StringUtils.isNotEmpty(children.get(0).getType())) {
                content.setChildren(null);
            }
        }
    }


}
