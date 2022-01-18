package com.yxj.yosmfa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2021-11-05
 */
@TableName("yxj_novel")
public class Novel {


      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long bookId;

    private String bookName;

    private String chapter;

    @TableField("chapterId")
    private Long chapterid;

    private String novelContent;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private Integer delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public Long getChapterid() {
        return chapterid;
    }

    public void setChapterid(Long chapterid) {
        this.chapterid = chapterid;
    }

    public String getNovelContent() {
        return novelContent;
    }

    public void setNovelContent(String novelContent) {
        this.novelContent = novelContent;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "Novel{" +
        "id=" + id +
        ", bookId=" + bookId +
        ", bookName=" + bookName +
        ", chapter=" + chapter +
        ", chapterid=" + chapterid +
        ", novelContent=" + novelContent +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", delFlag=" + delFlag +
        "}";
    }
}
