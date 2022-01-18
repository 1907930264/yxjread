package com.yxj.yosmfa.controller;
import ch.qos.logback.classic.gaffer.GafferUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxj.yosmfa.common.Chapter;
import com.yxj.yosmfa.common.GrabRule;
import com.yxj.yosmfa.common.GrabUtil;
import com.yxj.yosmfa.common.RestResponse;
import com.yxj.yosmfa.entity.Book;
import com.yxj.yosmfa.service.IBookService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yxj.yosmfa.service.INovelService;
import com.yxj.yosmfa.entity.Novel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 *
 * @author ${author}
 * @since 2021-11-05
 */
@RestController
public class NovelController {
private final Logger logger=LoggerFactory.getLogger(NovelController.class);

    @Autowired
    public INovelService iNovelService;
    @Autowired
    public IBookService bookService;

/**
* id查询
*/
@RequestMapping(method = RequestMethod.GET, value = {"/novel/{id}" })
public RestResponse<Novel> byId(@PathVariable("id") Long id){
    Novel result =  iNovelService.getById(id);
        return RestResponse.success(result);
}

    /**
     * bookId查询
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/novel/chapter" })
    public RestResponse<Novel> byBookId(@RequestParam("bookId") Long bookId,@RequestParam("chapterId") Long chapterId){

        Novel novel = new Novel();
        novel.setBookId(bookId);
        novel.setChapterid(chapterId);
        QueryWrapper<Novel> wrapper = new QueryWrapper<>(novel);
        Novel result =  iNovelService.getOne(wrapper);
        return RestResponse.success(result);
    }

/**
* ids查询
*/
@RequestMapping(method = RequestMethod.GET, value = {"/novel/ids" })
public RestResponse<List<Novel>> byIds(@RequestParam("ids") String ids){
        String[] split = ids.split(",");
        List<Long> longs = new ArrayList<>(split.length);
        for (String s : split) {
        longs.add(Long.parseLong(s));
        }
        List<Novel> result =  iNovelService.listByIds(longs);
        return RestResponse.success(result);
}

    /**
     * 查询小说目录列表
     * @param bookId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/novel/chapterList" })
    public RestResponse<List<Novel>> getChapterList(
            @RequestParam("bookId") Long bookId,@RequestParam Integer current,@RequestParam Integer size){
        Novel novel = new Novel();
        novel.setBookId(bookId);
        QueryWrapper<Novel> wrapper = new QueryWrapper<>(novel);
        wrapper.select("chapter","chapterid");
        List<Novel> result = iNovelService.getPage(current,size,wrapper);
        return RestResponse.success(result);
    }

/**
* 插入
*/
@RequestMapping(method = RequestMethod.POST, value = {"/novel"})
public RestResponse<Novel> save(@RequestBody Novel entity){
        boolean result = iNovelService.save(entity);
        return result ? RestResponse.success(entity) : RestResponse.fail("保存失败");
}

/**
* 更新
*/
@RequestMapping(method = RequestMethod.PUT, value = {"/novel" })
public RestResponse<Novel> update(@RequestBody Novel entity){
        boolean result = iNovelService.updateById(entity);
        return result ? RestResponse.success(iNovelService.getById(entity.getId())) :
        RestResponse.fail("保存失败");
}


    @RequestMapping(method = RequestMethod.POST, value = {"/grab"})
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<Novel> grab(@RequestBody GrabRule grabRule){
    GrabUtil grabUtil = new GrabUtil();
        Book book = grabUtil.createBook(grabRule);
        boolean save = bookService.save(book);
        if (save){
            List<Novel> novels = grabUtil.getNovels(grabRule);
            novels.sort(Comparator.comparing(Novel::getChapterid));
            novels.stream().forEach(novel -> {
                novel.setBookId(book.getId());
                //novel.setBookName(book.getBookName());
            });
            //取余
            int i = novels.size() % 500;
            int i1 = (novels.size() - i) / 500;
            for (int j = 1; j <= i1; j++) {
                List<Novel> subList = novels.subList(j*500, (j+1) * 500);
                if (j == 1){
                    subList = novels.subList(0, j * 500);
                }else {
                    subList = novels.subList(0, j * 500);
                }
            }
            boolean result = iNovelService.saveBatch(novels);
            if (result){
                System.out.printf("爬取完成");
                return RestResponse.success("成功");
            }
        }

        return RestResponse.fail("报存book失败");

    }

    @RequestMapping(method = RequestMethod.POST, value = {"/createBook"})
    public RestResponse<Novel> createBook(@RequestBody GrabRule grabRule){
        GrabUtil grabUtil = new GrabUtil();
        Book book = grabUtil.createBook(grabRule);
        boolean save = bookService.save(book);
        if (save){
            return RestResponse.success("成功");
        }
        return RestResponse.fail("失败");

    }

}