package com.yxj.yosmfa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2021-11-04
 */
@TableName("yxj_book")
@Data
public class Book {


      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String bookName;

    private String bookDesc;

    private String type;

    private Integer status;

    private String author;

    private String tag;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Integer delFlag;

    private String picture;

    private String nameAndAuthor;

}
