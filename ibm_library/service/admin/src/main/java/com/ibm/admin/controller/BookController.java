package com.ibm.admin.controller;

import com.ibm.admin.entity.Book;
import com.ibm.admin.entity.BorrowList;
import com.ibm.admin.entity.User;
import com.ibm.admin.entity.UserInfo;
import com.ibm.admin.service.AdminService;
import com.ibm.admin.service.BookService;
import com.ibm.admin.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @ApiOperation(value = "修改书本信息")
    @PostMapping("/updateBook")
    public List<Book> updateBook(@RequestParam("id") Integer id,
                           @RequestParam(required = false,value = "book_country") String book_country, @RequestParam(required = false,value = "book_type") String book_type,
                           @RequestParam(required = false,value = "book_theme") String book_theme,@RequestParam(required = false,value = "book_status") Integer book_status

    ){
        Book book=bookService.findBook(id);//通过id来接收书本信息，因为字段允许为空，避免发空值过来，导致数据清空
        book.setId(id);
        if(book_theme==""){//如果发空过来，就将原来的值传到到数据里
          book.setBook_theme(book.getBook_theme());
        }else {
          book.setBook_theme(book_theme);//如果不为空值，就替换成新的值
        }
        System.out.println(book_theme);
        if(book_type==""){
          book.setBook_type(book.getBook_type());
        }else {
          book.setBook_type(book_type);
        }

        System.out.println(book_type);
        if(book_country==""){
          book.setBook_country(book.getBook_country());
        }else {
          book.setBook_country(book_country);
        }
        if(book_status==null){
          book.setBook_status(book.getBook_status());
        }else {
          book.setBook_status(book.getBook_status()+book_status);
        }
        System.out.println(book_country);
        bookService.bookManage(book);
        List<Book> allBooksList = allBooks();
        return allBooksList;

    }
    @ApiOperation(value = "增加图书")
    @PostMapping("/insertBook")
    public List<String> addBook(@RequestParam("book_name") String book_name,@RequestParam("book_type") String book_type,
                        @RequestParam("book_theme") String book_theme,@RequestParam("book_summary") String book_summary,
                        @RequestParam("book_country") String book_country){
        Book book=new Book();
        List<String> list = new ArrayList<>();
        if(bookService.findOneBook(book_name)==null) {
          Date today = new Date();//获取当前时间
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
          String now = sdf.format(today);
//        String uuid=UUID.randomUUID().toString().replace("-","");
//        book.setId(uuid);
          book.setBook_country(book_country);//设置书本国家
          book.setBook_up_amount(1);//保留字段，设为1
          book.setBook_type(book_type);//设置书本类型
          book.setBook_theme(book_theme);//设置书本主题
          book.setBook_summary(book_summary);//设置书本简介
          book.setBook_name(book_name);//设置书本名字
          book.setBook_uptime(now);//设置上架时间
          book.setBook_status(1);//设置数量为1
          bookService.insertBook(book);
            list.add("插入成功");
          } else {
            list.add("书本已存在");
        }
        return list;
    }
//    @GetMapping("/allBook")
//    public List<Book> allBook(){
//      return   bookService.findAll();
//    }

//    @ApiOperation(value = "图书上/下架")
//    @PutMapping("/changeStatus")
//    public void changeStatus(@RequestParam("id") Integer id){
//        Book book = bookService.findBook(id);
//        if (book.getBook_up_amount()==0){
//            bookService.changeStatusUp(id);
//            System.out.println("上架成功");
//        }
//        if (book.getBook_up_amount()==1){
//            bookService.changeStatusDown(id);
//            System.out.println("下架成功");
//        }
//    }

    @ApiOperation(value = "根据书名模糊查询借阅历史")
    @GetMapping("/bookLike")
    public List<Book> bookLike(@RequestParam("book_name") String book_name){
        List<Book> books = bookService.bookLike(book_name);
        return books;
    }
    @ApiOperation(value = "根据书名模糊查询借阅状态")
    @GetMapping("/bookLikeStatus")
    public List<Book> bookLikeStatus(@RequestParam("book_name") String book_name){
      List<Book> books = bookService.bookLikeStatus(book_name);
      return books;
    }
    @ApiOperation(value = "查询所有图书")
    @GetMapping("/allBooks")
    public List<Book> allBooks(){
      List<Book> books = bookService.bookList();
      return books;
    }

    //条件分类查询图书
    @ApiOperation(value = "分类别查询书本")
    @PostMapping("/typeBooks")
    public List<Book> typeBooks(@RequestParam(required = false,value = "book_country") String book_country,@RequestParam(required = false,value = "book_type")String book_type,
                                @RequestParam(required = false,value = "book_theme") String book_theme){
      List<Book> bookList = bookService.typeBooks(book_country, book_type,  book_theme);
      return bookList;
    }


    @ApiOperation(value = "还书")
    @PostMapping("/returnBook")
    public List<String> returnBook(@RequestParam("user_name") String user_name,@RequestParam("book_name") String book_name){
      Date today=new Date();//获取当前时间
      Book book=bookService.findOneBook(book_name);
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
      String now=sdf.format(today);
      User user = adminService.find(user_name);//通过用户名查询用户
      List<String> list=new ArrayList<>();
      int user_borrowTimes= user.getUser_borrowTimes();//获取该用户是否借了书
      if(user_borrowTimes>0){//大于0借了书
        user_borrowTimes= user_borrowTimes-1;//还书-1
        BorrowList borrowList = bookService.borrowListByName(user_name,book_name);//查看借阅表，查找对应数据
        borrowList.setReturn_date(now);//处理空异常
        userService.updateBorrow(user_name,user_borrowTimes);//更新用户的借书粱
        bookService.returnBook(user_name,now,book_name);//还书
        book.setBook_status(book.getBook_status()+1);
        bookService.changeBookNumber(book);
//        bookService.changeBorrow(book_name);
        borrowList.setDead_line(0);//还书后剩余归还时间设置为0
        bookService.changeDeadLine(borrowList);
        System.out.println("还书成功");
        list.add("还书成功");
      }else {
        list.add("还书失败");
      }
      return list;
    }
    @ApiOperation(value = "借书")
    @PostMapping("/borrowBook")
    public List<String> borrowBook(@RequestParam("user_name") String user_name,@RequestParam("book_name") String book_name
                           ){
        synchronized (this) {//加锁，避免读写错误
          User user = adminService.find(user_name);
          Book book = bookService.findOneBookName(book_name);
          BorrowList borrow = null;
          List<String> list = new ArrayList<>();
          try {
            borrow = bookService.borrowListByName(user_name, book_name);
          } catch (NullPointerException e) {
            throw new NullPointerException("找不到记录");
          }
          //当用户未还图书达到3本，已借此本图书未还，书库中剩余可借图书为0，都不能借书
          if (user.getUser_borrowTimes() < 3 && (borrow == null || borrow.getBorrow_date() == null || borrow.getReturn_date() != null) && book.getBook_status() >0) {  //每个人只允许借相同的一本书
            BorrowList borrowList = new BorrowList();
            Date today = new Date();//获取当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
            String now = sdf.format(today);
            borrowList.setBorrow_date(now);//设置借书时间
            borrowList.setBook_name(book_name);//设置借书书名
            borrowList.setBook_theme(book.getBook_theme());//设置书本主题
            System.out.println(book.getBook_theme());
            borrowList.setUser_name(user_name);//设置借书人
            int user_borrowTimes = user.getUser_borrowTimes() + 1;//将借书数量加1
            userService.updateBorrow(user_name, user_borrowTimes);//插入借阅历史
            bookService.borrowBook(borrowList);//借书
            book.setBook_status(book.getBook_status()-1);//剩余数量减1
            bookService.changeBookNumber(book);//修改图书剩余数量
//            bookService.changeNoBorrow(book_name);
            list.add("1");//代表借书成功
            System.out.println("借书成功");
          } else {
            if(book.getBook_status()==-1){
              list.add("2");//代表已下架
            }else {
              if (book.getBook_status()==0){
                list.add("3");//代表书本数量为0
              }else {
                if(user.getUser_borrowTimes() >=3){
                  list.add("4");//代表借书数量已达上限
                }else {
                  if(borrow != null || borrow.getBorrow_date() != null || borrow.getReturn_date() == null){
                    list.add("5");//代表已借了这一本书
                  }
                }
              }


            }

            System.out.println("借书失败");

          }
          return list;
        }
    }
//
//    @ApiOperation(value = "借书")
//    @PostMapping("/borrowBook")
//    public void borrowBook(@RequestParam("user_name") String user_name,@RequestParam("book_name") String book_name){
//      Book book = findOneBook(book_name);
//      User user = adminService.find(user_name);
//      System.out.println(book);
//      if(book.getBook_status()==1){
//        BorrowList borrowList = new BorrowList();
//          Date today = new Date();//获取当前时间
//          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
//          String now = sdf.format(today);
//          borrowList.setBorrow_date(now);
//          borrowList.setBook_name(book_name);
//          borrowList.setBook_theme(book.getBook_theme());
//          System.out.println(book.getBook_theme());
//          borrowList.setUser_name(user_name);
//          int user_borrowTimes=user.getUser_borrowTimes()+1;
//          userService.updateBorrow(user_name,user_borrowTimes);
//          bookService.changeNoBorrow(book_name);
//          bookService.borrowBook(borrowList);
//      }
//    }



    @ApiOperation(value = "查看图书详情")
    @PostMapping("/findOneBook")
    public List<Book> findOneBook(@RequestParam("book_name") String book_name){
//     book_name=UserInfo.name;
      Book oneBook = bookService.findOneBook(book_name);
      List<Book> bookList=new ArrayList<>();
      bookList.add(oneBook);
      return bookList;
    }
//    @PostMapping("/findOneBookScan")
//    public Book findOneBookScan(@RequestParam("book_name") String book_name){
//
//    }

//    //剩余天数判断
//    @ApiOperation(value = "查看剩余天数")
//    @PostMapping("/remainingDays")
//    public List<Integer> remainingDays(@RequestParam("user_name") String user_name,@RequestParam("book_name") String book_name) {
//      BorrowList byName = bookService.unReturnListByName(user_name, book_name);
//      String borrow_date = byName.getBorrow_date();
//      List<Integer> list=new ArrayList<>();
//      Date today = new Date();//获取当前时间
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换时间格式
//      String now = sdf.format(today);
//      //判断当前年份
//      String currentYear = now.substring(0, now.indexOf("-"));
//      int currentYearNum = Integer.parseInt(currentYear);//转化为数字
//      //判断借书年份
//      String borrowYear = borrow_date.substring(0, now.indexOf("-"));
//      int borrowYearNum = Integer.parseInt(borrowYear);
//      //判断当前月份
//      String currentMonth = now.substring(now.indexOf("-") + 1, now.lastIndexOf("-"));
//      //转化为数字
//      int currentMonthNum = Integer.parseInt(currentMonth);
//      //判断借书月份
//      String borrowMonth = borrow_date.substring(borrow_date.indexOf("-") + 1, borrow_date.lastIndexOf("-"));
//      //转化为数字
//      int borrowMonthNum = Integer.parseInt(borrowMonth);
//      //判断当前日期
//      String currentDay = now.substring(now.lastIndexOf("-") + 1, now.lastIndexOf("-") + 3);
//      //转化为数字
//      int currentDayNum = Integer.parseInt(currentDay);
//      //判断借书日期
//      String borrowDay = borrow_date.substring(borrow_date.lastIndexOf("-") + 1, borrow_date.lastIndexOf("-") + 3);
//      //转化为数字
//      int borrowDayNum = Integer.parseInt(borrowDay);
//      int remainingDays = 0;
//      //因为设置期限为30天，超过2个月，又不是1月31号的话，必定会超时
//      if ((currentMonthNum - borrowMonthNum >= 2) && (borrowMonthNum != 1) && (borrowDayNum != 31)) {//如果不是1月31日借的
//        if ((currentMonthNum - borrowMonthNum == 2) && (borrowMonthNum == 1) && (borrowDayNum == 31)) {//如果是1月31日借的
//          if (((borrowYearNum % 4 == 0) && (borrowYearNum % 100 != 0)) || (borrowYearNum % 400 == 0)) {//是不是闰年
//            if (currentDayNum != 1) {//如果是闰年，2月有29天，必须在3月1号前还书
//              list.add(-1);
//            }
//          } else {
//            if (currentDayNum != 2) {//如果不是闰年，2月有28天，必须在3月2号前还书
//              list.add(-1);
//            }
//          }
//        } else {
//          list.add(-1);//代表超时
//        }
//      } else {
//        if (currentYearNum == borrowYearNum) {//如果借书时间和当前时间是同一年
//          //判断大月31天
//          if ((borrowMonthNum == 1) || (borrowMonthNum == 3) || (borrowMonthNum == 5) || (borrowMonthNum == 7) || (borrowMonthNum == 8) || (borrowMonthNum == 10) || (borrowMonthNum == 12)) {
//            if ((currentDayNum - borrowDayNum) >= 0) {
//              remainingDays = 30 - (currentDayNum - borrowDayNum );
//            } else {
//              remainingDays = 30 - (31 + currentDayNum - borrowDayNum);
//            }
//
//          } else {//判断小月30天
//            //判断平年闰年，2月是28天还是29天
//            if (borrowMonthNum == 2) {
//              if (((borrowYearNum % 4 == 0) && (borrowYearNum % 100 != 0)) || (borrowYearNum % 400 == 0)) {
//                if ((currentDayNum - borrowDayNum) >= 0) {//如果是同一个月查询，则只需要两数相减
//                  remainingDays = 30 - (currentDayNum - borrowDayNum );//+1是因为从借书那一天就算起
//                } else {//如果不是同一个月查询，需要将上一个月的天数加上，再两数相减
//                  remainingDays = 30 - (29 + currentDayNum - borrowDayNum );
//                }
//              } else {
//                if ((currentDayNum - borrowDayNum) >= 0) {
//                  remainingDays = 30 - (currentDayNum - borrowDayNum );
//                } else {
//                  remainingDays = 30 - (28 + currentDayNum - borrowDayNum );
//                }
//              }
//            } else {
//              if ((currentDayNum - borrowDayNum) >= 0) {
//                remainingDays = 30 - (currentDayNum - borrowDayNum );
//              } else {
//                remainingDays = 30 - (30 + currentDayNum - borrowDayNum);
//              }
//            }
//          }
//        } else {//如果借书时间和当前时间不是同一年，只需要考虑12月和1月份的情况
//          if ((currentDayNum - borrowDayNum) >= 0) {
//            remainingDays = 30 - (currentDayNum - borrowDayNum );
//          } else {
//            remainingDays = 30 - (30 + currentDayNum - borrowDayNum );
//          }
//        }
//        list.add(remainingDays);
//      }
//      System.out.println("剩余天数:"+remainingDays);
//      return list;
//    }
//    @ApiOperation(value = "管理员借阅历史")
//    @PostMapping("/borrowListPerson")   //11.23改，Getmapping>PostMapping
//    public List<BorrowList> borrowListPerson(@RequestParam("user_name") String user_name){
//      System.out.println(user_name);
//      List<BorrowList> borrowListPerson = bookService.borrowListPerson(user_name);
//      System.out.println(borrowListPerson);
//      return borrowListPerson;
//    }
//    @ApiOperation(value = "个人借阅历史(ID)")
//    @PostMapping("/borrowListPersonById")   //11.23改，Getmapping>PostMapping
//    public List<BorrowList> borrowListPersonById(@RequestParam("id") Integer id){
//      List<BorrowList> borrowListPersonById = bookService.borrowListPersonById(id);
//      return borrowListPersonById;
//    }

    @ApiOperation(value = "通过书名查看借阅历史")
    @PostMapping("/borrowListByBookName")   //11.23改，Getmapping>PostMapping
    public List<BorrowList> borrowListByBookName(@RequestParam("book_name") String book_name){
      List<BorrowList> borrowListByBookNameList = bookService.borrowListByBookName(book_name);
      return borrowListByBookNameList;
    }

    @ApiOperation(value = "模糊查询图书")
    @PostMapping("/findLike")
    public List<Book> findLike(@RequestParam(required = false,value = "randomStr") String randomStr){
      List<Book> bookList = bookService.findLike(randomStr);
      return bookList;
    }
    @ApiOperation(value = "模糊查询借阅图书")
    @PostMapping("/findLikeBorrow")
    public List<Book> findLikeBorrow(@RequestParam(required = false,value = "randomStrBorrow") String randomStrBorrow){
      List<Book> bookList = bookService.findLikeBorrow(randomStrBorrow);
      return bookList;
    }
    @ApiOperation("全部借阅历史")
    @GetMapping("/borrowList")
    public List<BorrowList> borrowList(){
      List<BorrowList> borrowLists = bookService.borrowList();
      return borrowLists;
    }

    @ApiOperation(value = "用户未还图书")
    @PostMapping("/unReturnListByUserName")
    public List<BorrowList> unReturnListByUserName(@RequestParam("user_name") String user_name){
      List<BorrowList> unReturnListByUserName = bookService.unReturnListByUserName(user_name);
      return unReturnListByUserName;
    }

    @ApiOperation(value = "书名查看书本详情")
    @PostMapping("/findBookByBookName")
    public List<Book> findBookByBookName(@RequestParam("book_name") String book_name){
      List<Book> bookByBookName = bookService.findBookByBookName(book_name);
      return bookByBookName;
    }
    @ApiOperation(value = "通过ID查看图书")
    @PostMapping("/findBook")
    public List<Book> findBook(@RequestParam("id") Integer id){
      Book book = bookService.findBook(id);
      List<Book> list=new ArrayList<>();
      list.add(book);
      return list;
    }

//    @ApiOperation(value = "根据书名查询可借图书")
//    @PostMapping("/bookNumber")
//    public Integer bookNumber(@RequestParam("book_name") String book_name){
//      Integer number = bookService.bookNumber(book_name);
//      System.out.println(number);
//      return number;
//    }

}

