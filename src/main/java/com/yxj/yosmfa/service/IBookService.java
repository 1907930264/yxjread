package com.yxj.yosmfa.service;

import com.yxj.yosmfa.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-04
 */
public interface IBookService extends IService<Book> {

    public List<Book> byTop10(String type);
    public List<Book> byNameAndAuthor(String nameAndAuthor);
}
