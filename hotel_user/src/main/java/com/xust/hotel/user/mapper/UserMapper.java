package com.xust.hotel.user.mapper;


import com.xust.hotel.user.pojo.UserDO;
import com.xust.hotel.user.pojo.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author bhj
 */
@Mapper
@Repository
public interface UserMapper {

    /**
     * delete
     * @param id
     * @return
     */
    int delete(Integer id);

    /**
     * insert
     * @param user
     * @return
     */
    int insert(UserDO user);

    /**
     * dynamic insert
     * @param user
     * @return
     */
    int insertDynamic(UserDO user);

    /**
     * dynamic update
     * @param user
     * @return
     */
    int updateDynamic(UserDO user);

    /**
     * update
     * @param user
     * @return
     */
    int update(UserDO user);

    /**
     * select by id
     * @param id
     * @return
     */
    UserDO selectById(Integer id);

    /**
     * select by user
     * @param user
     * @return
     */
    UserDO selectByUser(String user);

    /**
     * page query
     * @param userDTO
     * @return
     */
    List<UserDO> findPageWithResult(UserDTO userDTO);

    /**
     * page count
     * @param userDTO
     * @return
     */
    Integer findPageWithCount(UserDTO userDTO);
}