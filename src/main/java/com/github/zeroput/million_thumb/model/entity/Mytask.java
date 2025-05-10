package com.github.zeroput.million_thumb.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private Integer taskID;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private Integer isCompleted;

    /**
     * 
     */
    private Date dueDate;

    /**
     * 
     */
    private Object priority;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}