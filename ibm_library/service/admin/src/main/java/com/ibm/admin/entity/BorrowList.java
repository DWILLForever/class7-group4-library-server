package com.ibm.admin.entity;

import lombok.Data;

@Data
public class BorrowList {
  private Integer id;
  private String user_name;
  private String book_name;
  private String book_theme;
  private String borrow_date;
  private String return_date;
  private Integer dead_line;

}
