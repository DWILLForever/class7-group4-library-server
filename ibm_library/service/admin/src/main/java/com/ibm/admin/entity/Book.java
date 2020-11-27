package com.ibm.admin.entity;

import lombok.Data;

@Data
public class Book {
    private Integer id;
    private String book_name;
    private String book_type;
    private Integer book_up_amount;
    private String book_theme;
    private String book_summary;
    private String book_country;
    private String book_uptime;
    private Integer book_status;

}
