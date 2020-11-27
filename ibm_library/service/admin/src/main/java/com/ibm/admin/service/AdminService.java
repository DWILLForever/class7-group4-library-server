package com.ibm.admin.service;

import com.ibm.admin.entity.Admin;
import com.ibm.admin.entity.RequestBorrow;
import com.ibm.admin.mapper.AdminMapper;
import com.ibm.admin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;
    //查询单个用户
    public User find(String user_name){
      User user=null;
      try{

        user=adminMapper.findUser(user_name);
      }catch (NullPointerException e){
        throw new NullPointerException("处理");
      }
       return user;
   }

  //查询单个用户
  public User findLogin(String user_name){
    User user=null;
    try{

      user=adminMapper.findUserLogin(user_name);
    }catch (NullPointerException e){
      throw new NullPointerException("处理");
    }
    return user;
  }

   //添加用户
    public void insertUser(User user){
        adminMapper.insertUser(user);
    }
//    //查询用户
//    public List<User> findAll() {
//        List<User> list=adminMapper.findAll();
//        System.out.println(list);
//        return adminMapper.findAll();
//
//
//    }
    //修改用户
    public void updateUser(User user){
       adminMapper.updateUser(user);
    }

    //删除用户
    public void removeUser(String user_name){
        adminMapper.deleteUser(user_name);
    }
   //查询所有用户
    public List<User> findAll(){
       return adminMapper.findAllUser();
    }
    //管理员登录
    public Admin loginAdmin(String user_name, String user_password){
      return adminMapper.loginAdmin(user_name, user_password);
    }
    public List<User> findLikeUser(String user_name){
      List<User> userList = adminMapper.findLikeUser(user_name);
      return userList;
    }
    //确认借书
    public List<RequestBorrow> verifyBorrow(Integer id){
      List<RequestBorrow> requestBorrows=new ArrayList<>();
      requestBorrows.add(adminMapper.verifyBorrow(id));
      return requestBorrows;
    }
    //查看借书申请
    public List<RequestBorrow> applyBorrowList(){
      List<RequestBorrow> applyBorrowList = adminMapper.applyBorrowList();
      return applyBorrowList;
    }
    //查看个人借书申请
    public RequestBorrow applyBorrowById(Integer id){
      RequestBorrow requestBorrows = adminMapper.applyBorrowById(id);
      return requestBorrows;
    }
}
