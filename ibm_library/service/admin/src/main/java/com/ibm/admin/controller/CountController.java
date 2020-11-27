package com.ibm.admin.controller;

import com.ibm.admin.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/count")
public class CountController {
    @Autowired
    private BookService bookService;
    @ApiOperation(value = "借还书统计")
    @GetMapping("/number")
    public List<Integer> count(){
        Date today=new Date();//获取当前时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
        String now=sdf.format(today);
        //2020-11-21 17:23:20
        List<Integer> list=new ArrayList<>();//用于存放人数
        String todayStr=now.substring(now.lastIndexOf("-")+1,now.lastIndexOf("-")+3);//截取日期
        String monthStr =now.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));//截取月份
        String yearStr=now.substring(0,now.indexOf("-"));//截取年份
        List<String> borrowList = bookService.borrowCount();//借书列表
        List<String> returnList=bookService.returnCount();//还书列表
        //判断今天有多少人借书
        int todayCount=0;//初始化借书人数为0
        int todayCount1=0;//初始化还书人数为0
        for (String b:borrowList
        ) {
            System.out.println(b);
            if(b!=null){
                String day=b.substring(b.lastIndexOf("-")+1,b.lastIndexOf("-")+3);//获取借书的日期
                String month=b.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));//获取借书的月份
                String year=b.substring(0,b.indexOf("-"));//获取借阅历史的年份
                if(day.equals(todayStr)&&month.equals(monthStr)&&year.equals(yearStr)){//当前时间和借书历史时间，如果是同年同月同日，就是今天的借书数据
                    todayCount++;
                }
            }
        }
        for (String b:returnList) {
            if(b!=null){
              String day=b.substring(b.lastIndexOf("-")+1,b.lastIndexOf("-")+3);//获取还书的日期
              String month=b.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));//获取还书的月份
              String year=b.substring(0,b.indexOf("-"));//获取还书的年份
              if(day.equals(todayStr)&&month.equals(monthStr)&&year.equals(yearStr)){//当前时间和还书历史时间，如果是同年同月同日，就是今天的借书数据
                  todayCount1++;
              }
            }
        }
        list.add(todayCount);//今天借书人数
        list.add(todayCount1);//今天还书人数
        //这个月借书人数
      //2020-11-21
        int monthCount=0;
        int monthCount1=0;

        for (String b:borrowList
        ) {
            if(b!=null){
              String month=b.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));
              String year=b.substring(0,b.indexOf("-"));
              if(month.equals(monthStr)&&year.equals(yearStr)){
                  monthCount++;
              }
            }
        }
        for (String b:returnList
        ) {
            if(b!=null){
                String month=b.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));
                String year=b.substring(0,b.indexOf("-"));
                if(month.equals(monthStr)&&year.equals(yearStr)){
                    monthCount1++;
                }
            }
        }
        list.add(monthCount);
        list.add(monthCount1);

        //今年借书人数

        int yearCount=0;
        int yearCount1=0;
        for (String b:borrowList
        ) {
           if(b!=null){
              String year=b.substring(0,b.indexOf("-"));
               if(year.equals(yearStr)){
                   yearCount++;
               }
           }
        }
        for (String b:returnList
        ) {
            if(b!=null){
                String year=b.substring(0,b.indexOf("-"));
                if(year.equals(yearStr)){
                    yearCount1++;
                }
            }
        }

        list.add(yearCount);
        list.add(yearCount1);
        return list;
    }
}
