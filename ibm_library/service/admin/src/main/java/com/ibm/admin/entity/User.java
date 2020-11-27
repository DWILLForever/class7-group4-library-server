package com.ibm.admin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String user_name;
    private String user_sex;
    private Integer user_age;
    private String user_email;
    private String user_birthday;
    private String user_borrowId;
    private String user_phone;
    private String user_address;
    private String user_password;
    private Integer user_borrowTimes;
    private Integer user_status;


}
