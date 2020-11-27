package com.ibm.admin.service;

import com.ibm.admin.entity.Book;
import com.ibm.admin.entity.BorrowList;
import com.ibm.admin.entity.UserInfo;
import com.ibm.admin.mapper.BookMapper;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;
    //修改书本
    public void bookManage(Book book){
      bookMapper.updateBook(book);

    }
    //增加图书
    public boolean insertBook(Book book){
       return bookMapper.insertBook(book);
    }
//    //查书
//    public List<Book> findAll(){
//        return bookMapper.findAllBook();
//    }
    //查书
    public Book findBook(Integer id){
        return bookMapper.findBook(id);
    }
    //查书
    public Book findOneBookName(String book_name){
      return bookMapper.findOneBookName(book_name);
  }
    //图书上架
    public void changeStatusUp(String book_name){
        bookMapper.changeStatusUp(book_name);
    }
    //图书下架
    public void changeStatusDown(Book book){
        bookMapper.changeStatusDown(book);
    }
    public List<String> borrowCount(){
        return bookMapper.borrowCount();
    }
    public List<String> returnCount(){
        return bookMapper.returnCount();
    }
    //模糊查询图书借阅历史
    public List<Book> bookLike(String book_name){
        return bookMapper.findLikeName(book_name);
    }

    //模糊查询图书借阅状态
    public List<Book> bookLikeStatus(String book_name){
      return bookMapper.findLikeNameStatus(book_name);
    }

    //查询所有图书
    public List<Book> bookList(){
      return bookMapper.allBooks();
    }
    //分类别查询图书
    public List<Book> typeBooks(String book_country,String book_type,String book_theme){
        return bookMapper.typeBooks(book_country,book_type,book_theme);
    }
    //还书
    public void returnBook(String user_name,String return_date,String book_name){
      bookMapper.returnBook(user_name,return_date,book_name);
    }
    //还书记录
    public BorrowList borrowList(String user_name){
      return bookMapper.borrowPerson(user_name);
    }
    //还书记录
    public BorrowList borrowListByName(String user_name,String book_name){
      return bookMapper.borrowPersonByBookName(user_name,book_name);
    }
    //借书
    public void borrowBook(BorrowList borrowList){
      bookMapper.borrowBook(borrowList);
    }
    //查看图书详情
    public Book findOneBook(String book_name){
      return bookMapper.findOneBook(book_name);
    }
    //未还记录
    public List<BorrowList> unReturnList(){
      List<BorrowList> unReturnList = bookMapper.unReturnList();
      return unReturnList;
    }
    //未还记录
    public BorrowList unReturnListByName(String user_name,String book_name){
      BorrowList unReturnListByName = bookMapper.unReturnListByName(user_name,book_name);
      return unReturnListByName;
    }

    //用户未还记录
    public List<BorrowList> unReturnListByUserName(String user_name){
      List<BorrowList> unReturnListByUserName = bookMapper.unReturnListByUserName(user_name);
      return unReturnListByUserName;
    }
    //用户未还记录
    public List<BorrowList> unReturnListByUserId(String user_name){
      List<BorrowList> unReturnListByUserName = bookMapper.unReturnListByUserName(user_name);
      return unReturnListByUserName;
    }

    public List<BorrowList> unReturnListByBookName(String book_name){
      List<BorrowList> unReturnListByBookName = bookMapper.unReturnListByBookName(book_name);
      return unReturnListByBookName;
  }


    //借阅历史
    public List<BorrowList> borrowListPerson(String user_name){
      List<BorrowList> borrowListPerson = bookMapper.borrowListPerson(user_name);
      return borrowListPerson;
    }
    //借阅历史
    public List<BorrowList> borrowListPersonById(Integer id){
      List<BorrowList> borrowListPerson = bookMapper.borrowListPersonById(id);
      return borrowListPerson;
    }

    //借阅历史
    public List<BorrowList> borrowListByBookName(String book_name){
      List<BorrowList> borrowListPerson = bookMapper.borrowListByBookName(book_name);
      return borrowListPerson;
    }

    //模糊查询图书
    public List<Book> findLike(String randomStr){
      List<Book> bookList = bookMapper.findLike(randomStr);
      return bookList;
    }
    //模糊查询借阅图书
    public List<Book> findLikeBorrow(String randomStrBorrow){
      List<Book> bookList = bookMapper.findLikeBorrow(randomStrBorrow);
      return bookList;
    }
    //借阅历史
    public List<BorrowList> borrowList(){
      List<BorrowList> borrowLists = bookMapper.borrowList();
      return borrowLists;
    }

    //图书详情
    public List<Book> findBookByBookName(String book_name){
      List<Book> bookByBookName = bookMapper.findBookByBookName(book_name);
      return bookByBookName;
    }

//    //图书上架
//    public void changeBorrow(String book_name){
//      bookMapper.changeBorrow(book_name);
//    }
//    //图书下架
//    public void changeNoBorrow(String book_name){
//      bookMapper.changeNoBorrow(book_name);
//    }
    //根据书名查询剩余可借图书
    public Integer bookNumber(String book_name){
      Integer number = bookMapper.bookNumber(book_name);
      return number;
    }
    //修改借阅剩余时间
    public void changeDeadLine(BorrowList borrowList){
      bookMapper.changeDeadLine(borrowList);
    }
    public List<BorrowList> countDays(String book_name,String user_name){
      List<BorrowList> countDaysList = bookMapper.countDays(book_name,user_name);
      return countDaysList;
    }
    public List<BorrowList> borrowHistory(String user_name,String book_name){
       return bookMapper.borrowHistory(user_name,book_name);
    }
    //修改图书数量
    public Integer changeBookNumber(Book book){
      Integer number = bookMapper.changeBookNumber(book);
      return number;
    }

}
