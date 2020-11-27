package com.ibm.admin.entity;

import lombok.Data;

@Data
public class RequestBorrow {
  private Integer id;
  private String user_name;
  private String book_name;
  private String request_date;
  private String verify_date;
}
