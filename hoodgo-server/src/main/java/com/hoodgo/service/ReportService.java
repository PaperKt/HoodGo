package com.hoodgo.service;


import com.hoodgo.vo.OrderReportVO;
import com.hoodgo.vo.SalesTop10ReportVO;
import com.hoodgo.vo.TurnoverReportVO;
import com.hoodgo.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
