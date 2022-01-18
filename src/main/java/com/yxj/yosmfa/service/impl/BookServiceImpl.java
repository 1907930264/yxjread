package com.yxj.yosmfa.service.impl;

import com.yxj.yosmfa.entity.Book;
import com.yxj.yosmfa.mapper.BookMapper;
import com.yxj.yosmfa.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-04
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Autowired
    private BookMapper bookMapper;


    @Override
    public List<Book> byTop10(String type) {
        return bookMapper.byTop10(type);
    }

    @Override
    public List<Book> byNameAndAuthor(String nameAndAuthor) {
        return bookMapper.byNameAndAuthor(nameAndAuthor);
    }
}
