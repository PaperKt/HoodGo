package com.hoodgo.service;

import com.hoodgo.dto.DishDTO;
import com.hoodgo.dto.DishPageQueryDTO;
import com.hoodgo.entity.Dish;
import com.hoodgo.result.PageResult;
import com.hoodgo.vo.DishVO;

import java.util.List;


public interface DishService {

    void addDish(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dishDTO);

    List<DishVO> listWithFlavor(Dish dish);

    List<Dish> list(Long categoryId);

}
