package com.ibm.admin.mapper;


import com.ibm.admin.entity.Admin;
import com.ibm.admin.entity.Book;
import com.ibm.admin.entity.RequestBorrow;
import com.ibm.admin.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    //查询单个用户（用于查看详情）
    @Select("select * from user_info where user_name=#{user_name} and user_status=1")
    public User findUser(String user_name);

    @Select("select * from user_info where user_name=#{user_name}")
    public User findUserLogin(String user_name);

    //添加用户
    @Insert("insert into user_info(user_name,user_sex,user_age,user_email,user_birthday,user_borrowId,user_phone,user_address,user_password,user_borrowTimes,user_status)" +
      " values(#{user_name},#{user_sex},#{user_age},#{user_email},#{user_birthday},#{user_borrowId},#{user_phone},#{user_address},#{user_password},#{user_borrowTimes},#{user_status})")
    public void insertUser(User user);
    //修改用户
    @Update("update user_info set user_name=#{user_name},user_sex=#{user_sex},user_age=#{user_age}," +
            "user_email=#{user_email},user_phone=#{user_phone},user_address=#{user_address},user_birthday=#{user_birthday},user_status=#{user_status} where user_name=#{user_name} and user_status=1")
    public void updateUser(User user);
    //查询所有用户(部分显示）
    @Select("select *from user_info where user_status=1")
    public List<User> findAllUser();

    //删除用户
    @Delete("update user_info set user_status=0 where user_name=#{user_name}")
    public void deleteUser(String user_name);

    @Select("select * from admin where user_name=#{user_name} and user_password=#{user_password}")
    public Admin loginAdmin(String user_name, String user_password);

    //模糊查询用户
    @Select("select * from user_info where user_name like CONCAT('%',#{user_name},'%')")
    public List<User> findLikeUser(String user_name);

    //确认借书
    @Update("update request_borrow set verify_date=#{verify_date} where id=#{id}")
    public RequestBorrow verifyBorrow(Integer id);
    //查看所有借书申请
    @Select("select * from request_borrow")
    public List<RequestBorrow> applyBorrowList();
    //查看个人借书申请
    @Select("select * from request_borrow where id=#{id}")
    public RequestBorrow applyBorrowById(Integer id);



}
