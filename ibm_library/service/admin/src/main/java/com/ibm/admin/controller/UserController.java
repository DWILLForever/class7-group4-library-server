package com.ibm.admin.controller;


import com.google.gson.JsonObject;
import com.ibm.admin.entity.*;
import com.ibm.admin.service.AdminService;
import com.ibm.admin.service.BookService;
import com.ibm.admin.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired
  private AdminService adminService;
  @Autowired
  private BookService bookService;


////  获取session内容
//  @ResponseBody
//  @RequestMapping(value = "/getUserSession", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
//  public String getUserSession(HttpServletRequest request, HttpSession httpSession) {
//
//  }


  @ApiOperation(value = "登录")
//    @RequestMapping(value = "/loginIn", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
  public List login(@RequestParam("user_name") String user_name, @RequestParam("user_password") String user_password, @RequestParam("check") Boolean check, HttpServletRequest request, HttpSession httpSession) {
    List<String> list = new ArrayList();

    User user = new User();
    Admin admin = new Admin();
    UserInfo.name = user_name;
    System.out.println(adminService.findLogin(user_name));
    if (!check) {
      try {
        if (userService.login(user_name, user_password) != null) {   //用户登录成功

          user.setUser_name(user_name);
          user.setUser_password(user_password);
//          list.add(user);
//
//          //保存用户信息到session
//          httpSession.setAttribute("user_name", user_name);
//          httpSession.setAttribute("user_password", user_password);
          list.add("登录成功");
        } else {
//          if (adminService.find(user_name).getUser_status() == 0) {
//            list.add("用户注销");
//          } else {
            if (adminService.findLogin(user_name)!=null) {
              if (adminService.findLogin(user_name).getUser_status() == 0) {
                list.add("用户已注销");
              }else {
                list.add("密码错误");
              }
            } else {
              list.add("用户名错误");
            }
//          }
        }
      } catch (NullPointerException e) {
        if (adminService.findLogin(user_name)==null) {
          list.add("用户不存在");
        }else {
          list.add("用户已注销");
        }
        return list;
//        throw new NullPointerException("无此用户");

      }
    } else {
      if (adminService.loginAdmin(user_name, user_password) != null) {     //管理员登录成功
        admin.setUser_name(user_name);
        admin.setUser_password(user_password);
        list.add("登录成功");
      } else {
        list.add("用户名或密码错误");
      }
    }
    return list;
  }


  @PostMapping("/updateUser")
  public void updateUser(@RequestParam("user_name") String user_name,
                         @RequestParam(required = false, value = "user_sex") String user_sex, @RequestParam(required = false, value = "user_age") Integer user_age,
                         @RequestParam(required = false, value = "user_email") String user_email, @RequestParam(required = false, value = "user_birthday") String user_birthday,
                         @RequestParam(required = false, value = "user_phone") String user_phone, @RequestParam(required = false, value = "user_address") String user_address
  ) {
    User user = new User();
    System.out.println(user_birthday);
    user.setUser_name(user_name);
    user.setUser_sex(user_sex);
    user.setUser_address(user_address);
    user.setUser_age(user_age);
    user.setUser_birthday(user_birthday);
    user.setUser_email(user_email);
    user.setUser_phone(user_phone);
    userService.updateUser(user);

  }

  @ApiOperation(value = "用户借阅历史")
  @PostMapping("/borrowListPerson")   //11.23改，Getmapping>PostMapping
  public List<BorrowList> borrowListPerson(@RequestParam("user_name") String user_name) {
    System.out.println(user_name);
    List<BorrowList> borrowListPerson = userService.borrowListPerson(user_name);
    System.out.println(borrowListPerson);
    return borrowListPerson;
  }


//  @GetMapping("/get")
//  public String get() {
//    return "跨域成功";
//  }
//
//  @GetMapping("/index")
//  public List index() {
//    List<Object> list1 = new ArrayList();
//    list1.add(UserInfo.name);
////    list1.add(UserInfo.remainingDays);
//    return list1;
//  }

  @ApiOperation(value = "修改用户密码")
  @PostMapping("/updatePassword")
  public List updatePassword(@RequestParam("user_password") String user_password, @RequestParam("user_name") String user_name) {
    User user = new User();
    user.setUser_name(user_name);
    List<String> list = new ArrayList<>();
    user.setUser_password(user_password);
    if (userService.updatePassword(user)) {
      list.add(user_password);
    } else {
      list.add(null);
    }
    return list;
  }

  @ApiOperation(value = "验证用户名是否存在")
  @PostMapping("/existUserName")
  public List<String> existUserName(@RequestParam("user_name") String user_name) {
    List<User> list = userService.existUserName(user_name);
    List<String> responseStr = new ArrayList<>();
    if (list.size() == 0) {
      responseStr.add("可使用当前用户名");
    } else {
      responseStr.add("用户名已存在");
    }
    return responseStr;
  }
//
//  @ApiOperation(value = "申请借书")
//  @PostMapping("/applyBorrow")
//  public List<RequestBorrow> applyBorrow(@RequestParam("user_name") String user_name, @RequestParam("book_name") String book_name) {
//    User user = adminService.find(user_name);
//    Book book = bookService.findOneBookName(book_name);
//    RequestBorrow requestBorrow = new RequestBorrow();
//    BorrowList borrow = null;
//    List<RequestBorrow> requestBorrowList = new ArrayList<>();
//    try {
//      borrow = bookService.borrowListByName(user_name, book_name);
//    } catch (NullPointerException e) {
//      throw new NullPointerException("找不到记录");
//    }
//    if (user.getUser_borrowTimes() < 3 && (borrow == null || borrow.getBorrow_date() == null || borrow.getReturn_date() != null && book.getBook_up_amount() == 1) && book.getBook_status() > 0) {  //每个人只允许借相同的一本书
//      requestBorrow.setUser_name(user_name);
//      Date today = new Date();//获取当前时间
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
//      String now = sdf.format(today);
//      requestBorrow.setRequest_date(now);
//      requestBorrowList = userService.applyBorrow(requestBorrow);
//
//    }
//    return requestBorrowList;
//  }
}
