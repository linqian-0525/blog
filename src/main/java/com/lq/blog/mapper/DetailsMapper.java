package com.lq.blog.mapper;

import com.lq.blog.model.Details;
import com.lq.blog.model.DetailsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DetailsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    long countByExample(DetailsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int deleteByExample(DetailsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int insert(Details record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int insertSelective(Details record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    List<Details> selectByExampleWithRowbounds(DetailsExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    List<Details> selectByExample(DetailsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    Details selectByPrimaryKey(Long userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int updateByExampleSelective(@Param("record") Details record, @Param("example") DetailsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int updateByExample(@Param("record") Details record, @Param("example") DetailsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int updateByPrimaryKeySelective(Details record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table details
     *
     * @mbg.generated Mon Nov 25 10:04:40 CST 2019
     */
    int updateByPrimaryKey(Details record);
}