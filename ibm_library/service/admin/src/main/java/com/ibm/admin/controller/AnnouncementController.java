package com.ibm.admin.controller;

import com.ibm.admin.entity.Announcement;
import com.ibm.admin.service.AnnouncementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
  @Autowired
  private AnnouncementService announcementService;
  @ApiOperation("添加公告")
  @PostMapping("/insertAnnouncement")
  public void insertAnnouncement(@RequestParam("content") String content,@RequestParam("admin_name") String admin_name){
    Announcement announcement=new Announcement();
    announcement.setAdmin_name(admin_name);//设置发布人
    announcement.setContent(content);//设置公告内容
    Date today=new Date();//获取当前时间
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
    String now=sdf.format(today);
    announcement.setContent_date(now);//设置发布公告时间
    announcementService.insertAnnouncement(announcement);
  }
  @ApiOperation(value = "查询公告")
  @GetMapping("/allAnnouncement")
  public List<Announcement> allAnnouncement(){
    List<Announcement> announcementList = announcementService.allAnnouncement();//查询所有公告
    Announcement announcement=announcementList.get(announcementList.size()-1);//每次都把最新的记录显示出来
    List<Announcement> announcements=new ArrayList<>();
    announcements.add(announcement);
    return announcements;//传给前端
  }
//  @ApiOperation(value = "修改公告")
//  @PutMapping("/updateAnnouncement")
//  public void updateAnnouncement(@RequestParam("content") String content,@RequestParam("id") Integer id){
//    Announcement announcement=new Announcement();
//    Date today=new Date();//获取当前时间
//    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
//    String now=sdf.format(today);
//    announcement.setContent_date(now);
//    announcement.setContent(content);
//    announcementService.updateAnnouncement(announcement);
//  }
//  @ApiOperation(value = "删除公告")
//  @DeleteMapping("/deleteAnnouncement")
//  public void deleteAnnouncement(@RequestParam("id") Integer id){
//    announcementService.deleteAnnouncement(id);
//  }
}
