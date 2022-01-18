package com.yxj.yosmfa.controller;
import com.yxj.yosmfa.common.RestResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yxj.yosmfa.service.IBookService;
import com.yxj.yosmfa.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ${author}
 * @since 2021-11-04
 */
@RestController
public class BookController {
private final Logger logger=LoggerFactory.getLogger(BookController.class);

    @Autowired
    public IBookService iBookService;

/**
* id查询
*/
@RequestMapping(method = RequestMethod.GET, value = {"/book/{id}" })
public RestResponse<Book> byId(@PathVariable("id") Long id){
    Book result =  iBookService.getById(id);
        return RestResponse.success(result);
}

/**
* ids查询
*/
@RequestMapping(method = RequestMethod.GET, value = {"/book/ids" })
public RestResponse<List<Book>> byIds(@RequestParam("ids") String ids){
        String[] split = ids.split(",");
        List<Long> longs = new ArrayList<>(split.length);
        for (String s : split) {
        longs.add(Long.parseLong(s));
        }
        List<Book> result =  iBookService.listByIds(longs);
        return RestResponse.success(result);
}

    /**
     * 书名、作者搜索
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/book/search" })
    public RestResponse<List<Book>> search(@RequestParam String nameAndAuthor){
        List<Book> result = iBookService.byNameAndAuthor(nameAndAuthor);
        return RestResponse.success(result);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/book/all" })
    public RestResponse<List<Book>> getAll(){
        List<Book> result =  iBookService.list();
        return RestResponse.success(result);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/book/top" })
    public RestResponse<List<Book>> getTop(@RequestParam String type){
        List<Book> books = iBookService.byTop10(type);
        return RestResponse.success(books);
    }

/**
* 插入
*/
@RequestMapping(method = RequestMethod.POST, value = {"/book"})
public RestResponse<Book> save(@RequestBody Book entity){
    LocalDateTime now = LocalDateTime.now();
    entity.setCreateTime(now);
        entity.setUpdateTime(now);
        boolean result = iBookService.save(entity);
        return result ? RestResponse.success(entity) : RestResponse.fail("保存失败");
}

/**
* 更新
*/
@RequestMapping(method = RequestMethod.PUT, value = {"/book" })
public RestResponse<Book> update(@RequestBody Book entity){
        boolean result = iBookService.updateById(entity);
        return result ? RestResponse.success(iBookService.getById(entity.getId())) :
        RestResponse.fail("保存失败");
}

    /**
     * id删除
     */
    @RequestMapping(method = RequestMethod.DELETE, value = {"/book/{id}" })
    public RestResponse<Book> delById(@PathVariable("id") Long id){
        boolean result = iBookService.removeById(id);
        return RestResponse.success("删除成功");
    }


}