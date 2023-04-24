package com.cmgzs.domain.archive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 文档树存储(mongodb存储结构) --> 化为扁平结构，存储
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实例（项目）唯一标识
     * 索引
     */
    private String archiveId;

    /*主键*/
    private String id;

    /**
     * 子级Id
     */
    private String subId;

    /**
     * 父级Id
     */
    private String parentId;

    /**
     * 子级列表
     */
    private List<Content> children;

    /**
     * 同级 下一个id
     */
    private String nextId;

    /**
     * 类型
     */
    private String type;

    /**
     * 文本内容
     */
    private String text;

    // 表格
    private String line;
    private List<List<String>> data;
    private List<String> width;

    private boolean inline;

    private String latex;

    private String title;

    private String layer;

    private String pageNumber;

    private String url;

    private String cid;

    private String paper;

    private String svg;
    private String source;
    private String language;

    private Map<String, Object> setting;

    private Map<String, Object> property;

}
