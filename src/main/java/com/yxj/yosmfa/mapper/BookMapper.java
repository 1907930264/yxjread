package com.yxj.yosmfa.mapper;

import com.yxj.yosmfa.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-11-04
 */
public interface BookMapper extends BaseMapper<Book> {

    public List<Book> byTop10(String type);
    public List<Book> byNameAndAuthor(String nameAndAuthor);
}
