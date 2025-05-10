package com.github.zeroput.million_thumb.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName mytask
 */
@TableName(value ="mytask")
@Data
public class Mytask implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer taskid;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private Integer iscompleted;

    /**
     * 
     */
    private Date duedate;

    /**
     * 
     */
    private Object priority;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}