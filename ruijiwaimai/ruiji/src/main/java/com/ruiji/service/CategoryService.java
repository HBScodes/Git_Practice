package com.ruiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruiji.entity.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    void remove(Long id);
}
