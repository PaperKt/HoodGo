package com.hoodgo.service;

import com.hoodgo.dto.SetmealDTO;
import com.hoodgo.dto.SetmealPageQueryDTO;
import com.hoodgo.entity.Setmeal;
import com.hoodgo.result.PageResult;
import com.hoodgo.vo.DishItemVO;
import com.hoodgo.vo.SetmealVO;
import java.util.List;

public interface SetmealService {

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    void add(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);
}
