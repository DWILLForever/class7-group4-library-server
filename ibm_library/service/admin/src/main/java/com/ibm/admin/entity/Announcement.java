package com.ibm.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Announcement {
  private Integer id;

  private String content;

  private String admin_name;

  private String content_date;
}
