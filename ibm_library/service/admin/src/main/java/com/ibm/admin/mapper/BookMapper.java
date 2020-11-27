package com.ibm.admin.mapper;

import com.ibm.admin.entity.Book;
import com.ibm.admin.entity.BorrowList;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface BookMapper {
    //修改书本
    @ApiOperation(value = "修改书本")
    @Update(value = {" <script>" +
      " update book set " +
      " <if test=\"book_country != null \"> book_country=#{book_country}</if> " +
      " <if test=\" book_type != null \"> ,</if> " +
      " <if test=\" book_type != null \" > book_type=#{book_type}</if> " +
      " <if test=\"book_theme != null\" > ,</if> " +
      " <if test=\"book_theme != null\" > book_theme=#{book_theme}</if> " +
      " <if test=\"book_status != null\" > , </if> " +
      " <if test=\"book_status != null\" > book_status=#{book_status}</if> " +
      " <where> id=#{id} " +
      " </where>" +
      " </script>"})
    @Results({
      @Result(property = "id", column = "id"),
      @Result(property = "book_type", column = "book_type"),
      @Result(property = "book_theme", column = "book_theme"),
      @Result(property = "book_country", column = "book_country"),
      @Result(property = "book_status",column = "book_status")

    })
//    @Update("update book set book_country=#{book_country},book_type=#{book_type}, book_theme=#{book_theme} where id=#{id}")
    public void updateBook(Book book);

    //增加图书
    @ApiOperation(value = "增加图书")
    @Insert("insert into book(book_name,book_country,book_type,book_up_amount,book_theme,book_summary,book_status,book_uptime) values(#{book_name},#{book_country},#{book_type},#{book_up_amount},#{book_theme},#{book_summary},#{book_status},#{book_uptime})")
    public boolean insertBook(Book book);

//  查书
//    @Select("select * from book")
//    public List<Book> findAllBook();
    //查书
    @Select("select * from book where id=#{id} ")
    public Book findBook(Integer id);
    //图书上架
    @Update("update book set book_status=#{book_status} where book_name=#{book_name}")
    public void changeStatusUp(String book_name);
    //图书下架
    @Update("update book set book_status=-1 where book_name=#{book_name}")
    public void changeStatusDown(Book book);
    //还书统计
    @Select("select return_date from borrow_history")
    public List<String> returnCount();
    //借书统计
    @Select("select borrow_date from borrow_history")
    public List<String> borrowCount();
    //模糊查询图书借阅历史
    @Select("select * from borrow" +
      "_history where book_name like CONCAT('%',#{book_name},'%') ")
    public List<Book> findLikeName(String book_name);

    //模糊查询图书借阅状态
    @Select("select book_name,book_status from book where book_name like CONCAT('%',#{book_name},'%')")
    public List<Book> findLikeNameStatus(String book_name);

    //查询所有种类图书
    @Select("select * from book ")
    public List<Book> allBooks();

    @Select(value = {" <script>" +
      " SELECT * FROM book " +
      " <where> 1=1 " +
      " <if test=\"book_country != null\"> AND book_country=#{book_country}</if> " +
      " <if test=\"book_type != null\" >  AND book_type=#{book_type}</if> " +
      " <if test=\"book_theme != null\" >  AND book_theme=#{book_theme}</if> " +
      " </where>" +
      " </script>"})
    @Results({
      @Result(property = "book_type", column = "book_type"),
      @Result(property = "book_theme", column = "book_theme"),
      @Result(property = "book_country", column = "book_country"),

    })
    public List<Book> typeBooks(String book_country,String book_type,String book_theme);
    //还书
    @Update("update borrow_history set return_date=#{return_date} where user_name=#{user_name} and book_name=#{book_name} and return_date is null")
    public void returnBook(String user_name,String return_date,String book_name);
    //借书记录
    @Select("select * from borrow_history where user_name=#{user_name}")
    public BorrowList borrowPerson(String user_name);
    //借书记录
    @Select("select * from borrow_history where user_name=#{user_name} and book_name=#{book_name} and return_date is null")
    public BorrowList borrowPersonByBookName(String user_name,String book_name);
    //借书
    @Insert("insert into borrow_history(user_name,book_name,book_theme,borrow_date,dead_line) values(#{user_name},#{book_name},#{book_theme},#{borrow_date},30)")
    public void borrowBook(BorrowList borrowList);
    //查看图书
    @Select("select * from book where book_name=#{book_name} ")
    public Book findOneBook(String book_name);
    //通过书名查看图书
    @Select("select * from book where book_name=#{book_name} ")
    public Book findOneBookName(String book_name);

    //未还记录
    @Select("select * from borrow_history where return_date=null ")
    public List<BorrowList> unReturnList();
      //未还记录
    @Select("select * from borrow_history where return_date is null and user_name=#{user_name} and book_name=#{book_name} ")
    public BorrowList unReturnListByName(String user_name,String book_name);

    //未还记录
    @Select("select * from borrow_history where return_date is null and user_name=#{user_name}  ")
    public List<BorrowList> unReturnListByUserName(String user_name);

  //未还记录
  @Select("select * from borrow_history where return_date is null and book_name=#{book_name}  ")
  public List<BorrowList> unReturnListByBookName(String book_name);

    //未还记录(id)
    @Select("select * from borrow_history where return_date is null and id=#{id}  ")
    public List<BorrowList> unReturnListByUserId(Integer id);

    //未还记录(书名)
    @Select("select * from borrow_history where book_name=#{book_name} ")
    public List<BorrowList> borrowListByBookName(String book_name);


  //借阅历史，还没写完
    @Select("select * from borrow_history where user_name=#{user_name}")
    public List<BorrowList> borrowListPerson(String user_name);
    //借阅历史
    @Select("select * from borrow_history where id=#{id}")
    public List<BorrowList> borrowListPersonById(Integer id);
    //模糊查询图书
    @Select("select * from book where book_name like CONCAT('%',#{randomStr},'%') or book_country like CONCAT('%',#{randomStr},'%') " +
      "or book_type like CONCAT('%',#{randomStr},'%') or book_theme like CONCAT('%',#{randomStr},'%') ")
    public List<Book> findLike(String randomStr);
    //模糊查询借阅历史图书
    @Select("select * from borrow_history where book_name like CONCAT('%',#{randomStr},'%') or book_theme like CONCAT('%',#{randomStr},'%') ")
    public List<Book> findLikeBorrow(String randomStrBorrow);
    //借阅历史
    @Select("select * from borrow_history")
    public List<BorrowList> borrowList();

    //书本详情
    @Select("select * from book where book_name=#{book_name} ")
    public List<Book> findBookByBookName(String book_name);
//    //图书修改为可借
//    @Update("update book set book_status=1 where book_name=#{book_name}")
//    public void changeBorrow(String book_name);
//    //图书修改为不可借
//    @Update("update book set book_status=0 where book_name=#{book_name}")
//    public void changeNoBorrow(String book_name);

    //根据书名查询剩余可借图书
    @Select("select count(book_name) from book where book_name like CONCAT('%',#{book_name},'%') and book_status>0")
    public Integer bookNumber(String book_name);

    //设置剩余借阅时间
    @Update("update borrow_history set dead_line=#{dead_line} where id=#{id}")
    public void changeDeadLine(BorrowList borrowList);
    //计算剩余归还时间
    @Select("select * from borrow_history where return_date is null and book_name=#{book_name} and user_name=#{user_name}")
    public List<BorrowList> countDays(String book_name,String user_name);
    //借阅历史详情
    @Select("select user_name,dead_line," +
      "book_name,borrow_date from borrow_history where book_name=#{book_name} and user_name=#{user_name}")
    public List<BorrowList> borrowHistory(String user_name,String book_name);
    //修改图书数量
    @Update("update book set book_status=#{book_status} where book_name=#{book_name}")
    public Integer changeBookNumber(Book book);


}
