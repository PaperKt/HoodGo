package com.hoodgo.service;

import com.hoodgo.dto.OrdersPaymentDTO;
import com.hoodgo.dto.OrdersSubmitDTO;
import com.hoodgo.vo.OrderPaymentVO;
import com.hoodgo.vo.OrderSubmitVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    void paySuccess(String outTradeNo);
}
