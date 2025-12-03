package com.hoodgo.service;

import com.hoodgo.dto.*;
import com.hoodgo.result.PageResult;
import com.hoodgo.vo.OrderPaymentVO;
import com.hoodgo.vo.OrderStatisticsVO;
import com.hoodgo.vo.OrderSubmitVO;
import com.hoodgo.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);

    void reminder(Long id);

    PageResult pageQuery4User(int page, int pageSize, Integer status);


    OrderVO details(Long id);

    void userCancelById(Long id);

    void repetition(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);
}
