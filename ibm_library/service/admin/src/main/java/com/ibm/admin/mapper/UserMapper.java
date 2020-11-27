package com.ibm.admin.mapper;

import com.ibm.admin.entity.BorrowList;
import com.ibm.admin.entity.RequestBorrow;
import com.ibm.admin.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
  @Select("select * from user_info where user_name=#{user_name} and user_password=#{user_password} and user_status=1")
  public User login(String user_name,String user_password);
  @Update("update user_info set user_borrowTimes=#{user_borrowTimes} where user_name=#{user_name}")
  public void updateBorrow(String user_name,Integer user_borrowTimes);
  //修改用户
  @Update(value = {" <script>" +
    " update user_info set " +
    " <if test=\"user_sex != null\" >   user_sex=#{user_sex}</if> " +
    " <if test=\"user_age != null\" > ,</if> " +
    " <if test=\"user_age != null\" >   user_age=#{user_age}</if> " +
    " <if test=\"user_email != null\" >  , </if> " +
    " <if test=\"user_email != null\" >  user_email=#{user_email}</if> " +
    " <if test=\"user_birthday != null\" >,</if> " +
    " <if test=\"user_birthday != null\" >   user_birthday=#{user_birthday}</if> " +
    " <if test=\"user_phone != null\" >  ,</if> " +
    " <if test=\"user_phone != null\" >   user_phone=#{user_phone}</if> " +
    " <if test=\"user_address != null\" >,</if> " +
    " <if test=\"user_address != null\" >   user_address=#{user_address}</if> " +
    " <where> user_name=#{user_name} " +
    " </where>" +
    " </script>"})
  @Results({
    @Result(property = "user_name", column = "user_name"),
    @Result(property = "user_sex", column = "user_sex"),
    @Result(property = "user_age", column = "user_age"),
    @Result(property = "user_email", column = "user_email"),
    @Result(property = "user_birthday", column = "user_birthday"),
    @Result(property = "user_phone", column = "user_phone"),
    @Result(property = "user_address", column = "user_address")
  })
  public void updateUser(User user);

  //借阅历史，还没写完
  @Select("select * from borrow_history where user_name=#{user_name}")
  public List<BorrowList> borrowListPerson(String user_name);

  //修改用户密码
  @Update("update user_info set user_password=#{user_password} where user_name=#{user_name}")
  public boolean updatePassword(User user);

  //验证用户名是否存在
  @Select("select * from user_info where user_name=#{user_name}")
  public List<User> existUserName(String user_name);

  //申请借书
  @Update("insert into request_borrow(user_name,book_name,request_date) values(#{user_name},#{book_name},#{request_date}")
  public List<RequestBorrow> applyBorrow(RequestBorrow requestBorrow);

}
