package com.hnz.mymall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hnz.common.utils.PageUtils;
import com.hnz.mymall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author hnz
 * @email dicaprio17h@gmail.com
 * @date 2020-04-12 17:58:32
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

