package com.yxj.yosmfa.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.yxj.yosmfa.entity.Novel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-05
 */
public interface INovelService extends IService<Novel> {

    public List<Novel> getPage(int current, int size, Wrapper wrapper);

}
