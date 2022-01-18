package com.yxj.yosmfa.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxj.yosmfa.entity.Novel;
import com.yxj.yosmfa.mapper.NovelMapper;
import com.yxj.yosmfa.service.INovelService;
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
 * @since 2021-11-05
 */
@Service
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements INovelService {

    @Autowired
    private NovelMapper novelMapper;

    public List<Novel> getPage(int current,int size,Wrapper wrapper){
        IPage<Novel> userPage = new Page<>(current, size);//参数一是当前页，参数二是每页个数
        IPage<Novel> novelIPage = novelMapper.selectPage(userPage,wrapper);
        return novelIPage.getRecords();
    }
}
