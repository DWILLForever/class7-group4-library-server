package com.ibm.admin.mapper;

import com.ibm.admin.entity.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    @Select("select * from announcement")
    public List<Announcement> allAnnouncement();
    @Insert("insert into announcement(content,admin_name,content_date) values(#{content},#{admin_name},#{content_date}) ")
    public void insertAnnouncement(Announcement announcement);
    @Update("update announcement set content=#{content},content_date=#{content_date}")
    public void updateAnnouncement(Announcement announcement);
    @Delete("delete  from announcement where id=#{id}")
    public void deleteAnnouncement(Integer id);
}
