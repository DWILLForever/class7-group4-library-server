package com.ibm.admin.service;

import com.ibm.admin.entity.BorrowList;
import com.ibm.admin.entity.RequestBorrow;
import com.ibm.admin.entity.User;
import com.ibm.admin.entity.UserInfo;
import com.ibm.admin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  @Autowired
  private UserMapper userMapper;
  public User login(String user_name,String user_password){
    return userMapper.login(user_name, user_password);
  }
  public void updateBorrow(String user_name ,Integer user_borrowTimes){
    userMapper.updateBorrow(user_name,user_borrowTimes);
  }
  //修改用户
  public void updateUser(User user){
    userMapper.updateUser(user);
  }

  //借阅历史
  public List<BorrowList> borrowListPerson(String user_name){
//    user_name= UserInfo.name;   //11.23g改，加了此行代码
    List<BorrowList> borrowListPerson = userMapper.borrowListPerson(user_name);
    return borrowListPerson;
  }

  //修改用户密码
  public boolean updatePassword(User user){
    return userMapper.updatePassword(user);
  }

  //验证用户名是否存在
  public List<User> existUserName(String user_name){
    List<User> list = userMapper.existUserName(user_name);
    return list;
  }

  //申请借书
  public List<RequestBorrow> applyBorrow(RequestBorrow requestBorrow){
    List<RequestBorrow> requestBorrowList = userMapper.applyBorrow(requestBorrow);
    return requestBorrowList;
  }

}
