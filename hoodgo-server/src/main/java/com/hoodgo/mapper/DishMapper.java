package com.hoodgo.mapper;

import com.github.pagehelper.Page;
import com.hoodgo.annotation.AutoFill;
import com.hoodgo.dto.DishDTO;
import com.hoodgo.dto.DishPageQueryDTO;
import com.hoodgo.entity.Dish;
import com.hoodgo.enumeration.OperationType;
import com.hoodgo.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);


    void deleteById(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
