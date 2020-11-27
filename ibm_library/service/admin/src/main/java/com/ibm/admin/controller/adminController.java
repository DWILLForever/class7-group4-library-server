package com.ibm.admin.controller;


import com.ibm.admin.entity.*;
import com.ibm.admin.service.AdminService;


import com.ibm.admin.service.BookService;
import com.ibm.admin.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@RestController
public class adminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @ApiOperation(value = "管理员修改用户信息")
    @PutMapping("/updateUser")
    public void updateUser(@RequestParam("user_name") String user_name,
                           @RequestParam(required = false,value = "user_sex") String user_sex, @RequestParam(required = false,value ="user_age") Integer user_age,
                           @RequestParam(required = false,value ="user_email") String user_email,@RequestParam(required = false,value ="user_birthday") String user_birthday,
                           @RequestParam(required = false,value ="user_phone") String user_phone, @RequestParam(required = false,value ="user_address") String user_address

    ){
        User user=new User();
        user.setUser_sex(user_sex);//设置用户的性别
        user.setUser_name(user_name);//设置用户名
        user.setUser_address(user_address);//设置地址
        user.setUser_age(user_age);//设置年龄
        user.setUser_birthday(user_birthday);//设置生日
        user.setUser_email(user_email);//设置邮箱
        user.setUser_phone(user_phone);//设置电话
        adminService.updateUser(user);//修改用户
    }

  /**
   * 没完成
   */
  @ApiOperation(value = "添加用户")
  @RequestMapping(value = "/insertUser",method = RequestMethod.POST)
  public List insert(@RequestParam("user_name") String user_name,@RequestParam("user_password") String user_password,
                     @RequestParam("user_email") String user_email
                     ){

    if(adminService.find(user_name)==null) {//因为设置了用户名唯一，因此当用户名不存在时，才允许新增用户
        User user=new User();
        user.setUser_name(user_name);//设置用户名
        user.setUser_borrowTimes(0);//设置借书当前借书量为0
        user.setUser_email(user_email);//设置email
        user.setUser_password(user_password);//设置用户密码
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        user.setUser_borrowId(uuid);//设置id
        user.setUser_status(1);//设置为有效用户
        adminService.insertUser(user);//新增用户
        //方便测试用
        List<String> StringList = new ArrayList<>();
        StringList.add("注册成功");
        System.out.println(StringList);
        return StringList;
      }
      else {
        //方便测试用
        List<String> StringList = new ArrayList<>();
        StringList.add(null);
        System.out.println(StringList);
        return StringList;
      }

    }

    @ApiOperation(value = "通过用户名查询单个用户")
    @PostMapping("/findUserByName")
    public List findU(@RequestParam("user_name") String user_name ) {
        List<User>list=new ArrayList<>();//用于接收数据，返回给前端
        User user = adminService.find(user_name); //通过用户名查询用户
        list.add(user);//将数据添加进列表
        return list;//返回数据给前端
    }

    @ApiOperation(value = "查看所有用户列表")
    @GetMapping("/findAll")
    public List<User> findAllUser(){
        List<User> list = adminService.findAll();//查询所有用户
        System.out.println(list);
        return list;//存进列表，返回给前端
    }
    @ApiOperation(value = "通过user_name逻辑删除用户")
    @PostMapping("/delete")
    public List<User> deleteUser(@RequestParam("user_name") String user_name){
      List<User> allUser=adminService.findAll();//先通过查询所有用户，初始化列表内容
      List<BorrowList> list=bookService.unReturnListByUserName(user_name);
      System.out.println(list.size());
      if(list.size()==0){//查询需要删除的用户是否存在未还图书
        System.out.println("1");
        adminService.removeUser(user_name);//判断为空时，执行逻辑删除
        allUser = adminService.findAll();//更新列表
        return allUser;//返回列表
      }else {
        allUser=null;
        return allUser;
      }

    }


//  @ApiOperation(value = "用户登录")
//  @PostMapping("/login")
//  public List login(@RequestParam("user_name") String user_name, @RequestParam("user_password") String user_password){
//    List<Object> list=new ArrayList();
//    Admin admin=new Admin();
//    User user=new User();
//    if(userService.login(user_name,user_password)!=null){
//      user.setUser_name(user_name);
//      user.setUser_password(user_password);
//      list.add(user);
//      return list;
//    }
//
//    else{
//      list.add(null);
//      return list;
//    }
//  }

  @ApiOperation(value = "根据用户名模糊查询用户")
  @PostMapping("/findLikeUser")
  public List<User> findLikeUser(@RequestParam(required = false,value = "user_name") String user_name){
    List<User> userList = adminService.findLikeUser(user_name);
    return userList;
  }

//  @ApiOperation(value = "确认借书")
//  @PostMapping("/verifyBorrow")
//  public List<String> verifyBorrow(@RequestParam("id") Integer id ,@RequestParam("user_name") String user_name,@RequestParam("book_name") String book_name){
//    List<String> list=new ArrayList<>();
//    RequestBorrow requestBorrow=adminService.applyBorrowById(id);
//    Date today = new Date();//获取当前时间
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
//    String now = sdf.format(today);
//    requestBorrow.setVerify_date(now);
//    requestBorrow.setId(id);
//    if(adminService.verifyBorrow(id)!=null){
//      BorrowList borrowList=new BorrowList();
//      borrowList.setUser_name(user_name);
//      borrowList.setBook_name(book_name);
//      bookService.borrowBook(borrowList);
//    }else {
//      list.add("确认失败");
//    }
//    return list;
//  }
  @ApiOperation(value = "图书上架")
  @PostMapping("/changeStatusUp")
  public List<String> changeStatusUp(@RequestParam("book_name") String book_name,@RequestParam("number") Integer number){
    Book oneBook = bookService.findOneBook(book_name);//通过书名去查找库中记录
    List<String> list=new ArrayList<>();
    if(number>0){//新增数值大于0
      oneBook.setBook_status(number+oneBook.getBook_status());//设置上架书本数量
      bookService.changeStatusUp(book_name);//更新数据库
      list.add("上架成功");
    }else {
      list.add("上架失败");
    }
    return list;
  }
  @ApiOperation(value = "图书下架")
  @PostMapping("/changeStatusDown")
  public List<String> changeStatusDown(@RequestParam("book_name") String book_name ){
    Book oneBook = bookService.findOneBook(book_name);
    List<String> list=new ArrayList<>();
    System.out.println(bookService.unReturnListByBookName(book_name));
    if (bookService.unReturnListByBookName(book_name).isEmpty()){//当这本书没有被借走时，才可以下架
      bookService.changeStatusDown(oneBook);
      list.add("下架成功");
    }else {
      list.add("下架失败");
    }
    return list;
  }


}
