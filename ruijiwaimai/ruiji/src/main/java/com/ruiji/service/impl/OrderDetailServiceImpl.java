package com.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruiji.entity.OrderDetail;
import com.ruiji.mapper.OrderDetailMapper;
import com.ruiji.service.OrderDetailService;
import com.ruiji.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
