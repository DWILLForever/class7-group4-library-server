package com.ibm.admin.service;

import com.ibm.admin.entity.Announcement;
import com.ibm.admin.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
  @Autowired
  private AnnouncementMapper announcementMapper;
  //添加公告
  public void insertAnnouncement(Announcement announcement){
      announcementMapper.insertAnnouncement(announcement);
  }
  //查询公告
  public List<Announcement> allAnnouncement(){
    List<Announcement> announcementList = announcementMapper.allAnnouncement();
    return announcementList;
  }
  //修改公告
  public void updateAnnouncement(Announcement announcement){
    announcementMapper.updateAnnouncement(announcement);
  }
  //删除公告
  public void deleteAnnouncement(Integer id){
    announcementMapper.deleteAnnouncement(id);
  }
}
