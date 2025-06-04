package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
public class TreeSelect implements Serializable {

    /**
     * 节点ID
     */
    private Object id;

    /**
     * 上级节点ID
     */
    private Object parentId;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 节点禁用
     */
    private boolean disabled = false;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

}
