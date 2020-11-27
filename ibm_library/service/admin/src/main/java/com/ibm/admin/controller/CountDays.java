package com.ibm.admin.controller;

import com.ibm.admin.entity.Book;
import com.ibm.admin.entity.BorrowList;
import com.ibm.admin.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/countDays")
public class CountDays {
  @Autowired
  private BookService bookService;

  @ApiOperation(value = "计算剩余天数")
  @PostMapping("/countDays")
  public List<BorrowList> countDays(@RequestParam("book_name") String book_name,@RequestParam("user_name") String user_name){
    List<BorrowList> countDaysList = bookService.countDays(book_name,user_name);
    Date today = new Date();//获取当前时间
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
    String now = sdf.format(today);
    for (BorrowList borrow:countDaysList) {
      String borrow_date = borrow.getBorrow_date();
      //判断当前年份
      String currentYear = now.substring(0, now.indexOf("-"));
      int currentYearNum = Integer.parseInt(currentYear);//转化为数字
      //判断借书年份
      String borrowYear = borrow_date.substring(0, now.indexOf("-"));
      int borrowYearNum = Integer.parseInt(borrowYear);
      //判断当前月份
      String currentMonth = now.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));
      //转化为数字
      int currentMonthNum = Integer.parseInt(currentMonth);
      //判断借书月份
      String borrowMonth = borrow_date.substring(borrow_date.indexOf("-") + 1, borrow_date.lastIndexOf("-"));
      //转化为数字
      int borrowMonthNum = Integer.parseInt(borrowMonth);
      //判断当前日期
      String currentDay = now.substring(now.lastIndexOf("-") + 1, now.lastIndexOf("-") + 3);
      //转化为数字
      int currentDayNum = Integer.parseInt(currentDay);
      //判断借书日期
      String borrowDay = borrow_date.substring(borrow_date.lastIndexOf("-") + 1, borrow_date.lastIndexOf("-") + 3);
      //转化为数字
      int borrowDayNum = Integer.parseInt(borrowDay);
      int remainingDays = 0;
      //因为设置期限为30天，判断2月份28/29天的情况
      if ((currentMonthNum - borrowMonthNum >= 2)) {
        if(((borrowYearNum % 4 == 0) && (borrowYearNum % 100 != 0)) || (borrowYearNum % 400 == 0)){//如果是闰年，2月有29天
          if((borrowMonthNum == 1) && (borrowDayNum == 31)){//如果是1月31号借书
            if(currentDayNum==1&&currentMonthNum==2){//如果当前时间是2月1日，剩余时间为0
              remainingDays=0;
            }else {
              remainingDays=-1;//否则，超时
            }
          }else {
            remainingDays=-1;//超时
          }
        }else {
            if((borrowMonthNum == 1) && (borrowDayNum == 31)){
              if(currentDayNum==1&&currentMonthNum==2){
                remainingDays=1;
              }else {
                if(currentDayNum==2&&currentMonthNum==2){
                  remainingDays=0;
                }else {
                  remainingDays=-1;
              }
            }
          }else {
              if((borrowMonthNum == 1) && (borrowDayNum == 30)){
                if(currentDayNum==1&&currentMonthNum==2){
                  remainingDays=0;
                }else {
                  remainingDays=-1;
                }
              }
            }
        }
      } else {
        if (currentYearNum == borrowYearNum) {//如果借书时间和当前时间是同一年
          //判断大月31天
          if ((borrowMonthNum == 1) || (borrowMonthNum == 3) || (borrowMonthNum == 5) || (borrowMonthNum == 7) || (borrowMonthNum == 8) || (borrowMonthNum == 10) || (borrowMonthNum == 12)) {
            if ((currentDayNum - borrowDayNum) >= 0) {
              remainingDays = 30 - (currentDayNum - borrowDayNum );
            } else {
              remainingDays = 30 - (31 + currentDayNum - borrowDayNum);
            }

          } else {//判断小月30天
            //判断平年闰年，2月是28天还是29天
            if (borrowMonthNum == 2) {
              if (((borrowYearNum % 4 == 0) && (borrowYearNum % 100 != 0)) || (borrowYearNum % 400 == 0)) {
                if ((currentDayNum - borrowDayNum) >= 0) {//如果是同一个月查询，则只需要两数相减
                  remainingDays = 30 - (currentDayNum - borrowDayNum );//+1是因为从借书那一天就算起
                } else {//如果不是同一个月查询，需要将上一个月的天数加上，再两数相减
                  remainingDays = 30 - (29 + currentDayNum - borrowDayNum );
                }
              } else {
                if ((currentDayNum - borrowDayNum) >= 0) {
                  remainingDays = 30 - (currentDayNum - borrowDayNum );
                } else {
                  remainingDays = 30 - (28 + currentDayNum - borrowDayNum );
                }
              }
            } else {
              if ((currentDayNum - borrowDayNum) >= 0) {
                remainingDays = 30 - (currentDayNum - borrowDayNum );
              } else {
                remainingDays = 30 - (30 + currentDayNum - borrowDayNum);
              }
            }
          }
        } else {//如果借书时间和当前时间不是同一年，只需要考虑12月和1月份的情况
          if ((currentDayNum - borrowDayNum) >= 0) {
            remainingDays = 30 - (currentDayNum - borrowDayNum );
          } else {
            remainingDays = 30 - (30 + currentDayNum - borrowDayNum );
          }
        }
        borrow.setDead_line(remainingDays);;
      }
      System.out.println("剩余天数:"+remainingDays);
      bookService.changeDeadLine(borrow);
    }
    List<BorrowList> bookByBookName = bookService.borrowHistory(user_name,book_name);
    return bookByBookName;

  }
}
