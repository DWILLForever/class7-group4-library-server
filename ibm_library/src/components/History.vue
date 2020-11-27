<template>
    <div class="bg1">
      <el-container>
        <el-header>
          <div>
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item><a href="/">我是借阅者</a></el-breadcrumb-item>
              <el-breadcrumb-item>信息修改</el-breadcrumb-item>
              <el-breadcrumb-item :to="{ path: '/history' }">借阅历史</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </el-header>
        <el-main>
          <div class="bg2">
            <div class="inputword">
              <el-input v-model="input" placeholder="请输入内容"/>
            </div>
            <div class="sbtn">
            <el-button type="primary">Seach</el-button>
            </div>


            <div class="checkBoard">
              <el-table
                :data="tableData"
                style="width: 100%">
                <el-table-column
                  type="index"
                  label="序号"
                  align='center'
                  sortable
                  width="70">
                </el-table-column>
<!-- 日期  -->
                <el-table-column

                  label="借阅时间"
                  width="180">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{scope.row.user_name}}</span>
                  </template>
                </el-table-column>
<!--书名  -->
                <el-table-column
                  label="书名"
                  width="100">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{scope.row.id}}</span>
                  </template>
                </el-table-column>
<!--归还时间 -->
                <el-table-column
                  label="归还时间"
                  width="180">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{scope.row.user_email}}</span>
                  </template>
                </el-table-column>
<!--借阅有效期-->
                <el-table-column
                  label="借阅有效期"
                  width="100">
                  <template slot-scope="scope">
                    <span style="margin-left: 10px">{{scope.row.return_date}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="详情">
                  <button>详情</button>
                </el-table-column>
              </el-table>
            </div>
            <div class="page">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="paginations.page_index"
                :page-sizes="paginations.page_sizes"
                :page-size="paginations.page_size"
                :layout="paginations.layout"
                :total="paginations.total">
              </el-pagination>
            </div>
          </div>

        </el-main>
      </el-container>
    </div>
</template>

<script>
    export default {
        name: "History",
        data(){


          return{
            input: '',
            tableData:[
              {
                id:'',
                date:'',
                Bookname:''
              }
            ], //数据
            paginations:{
              page_index:1, //当前页
              total:0, //总数
              page_size:3, //一页显示多少
              page_sizes:[5,10,15,20], //每页显示多少条
              layout:'total, sizes, prev, pager, next, jumper'
            },
            allTableData:[]
          }

        },
      methods: {
        getInfoList() {
          this.axios.get('/findAll').then(res => {
            if(res.data!='') {
              // const arr=[]
              // for(let k in res.data){
              //   arr.push(res.data)
              // }
              // console.log('数据'+arr[0])
              // this.tableData = data;
              const arr=res.data
              this.allTableData = arr;
              console.log(this.allTableData)
              this.setPaginations()
            }
          })
        },
        setPaginations(){
          this.paginations.total = this.allTableData.length; //数据的数量
          this.paginations.page_index = 1; //默认显示第一页
          this.paginations.page_size = 5; //每页显示多少数据

          //显示数据
          this.tableData = this.allTableData.filter((item,index) => {
            return index < this.paginations.page_size;
          })
        },
        handleSizeChange(page_size) {
          this.paginations.page_index = 1; //第一页
          this.paginations.page_size = page_size; //每页先显示多少数据
          this.tableData = this.allTableData.filter((item,index) => {
            return index < page_size
          })
        },
        handleCurrentChange(page){
          // 跳转页数
          //获取当前页
          let index = this.paginations.page_size * (page -1);
          //获取总数
          let allData = this.paginations.page_size * page;

          let tablist=[];
          for(let i = index;i<allData;i++) {
            if (this.allTableData[i]) {
              tablist.push(this.allTableData[i])
            }
            this.tableData = tablist
          }
        }
      },
      created(){
        this.getInfoList()
      }
    }
</script>

<style scoped>

  .el-header {
    background-image: linear-gradient(to right, #a8caba 0%, #7ac5d8 100%);
    color: white;
    text-align: center;
    line-height: 60px;
    padding: 10px;
  }
  /*主体部分*/
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    line-height: 20px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }

  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
  /*主体部分尺寸*/
  .bg2{
    height: 620px;
  }
  /*中间操作框*/
  .checkBoard{
    background-color: white;
    height: 400px;
    width: 720px;
    margin-top:50px;
    margin-left: 400px;
  }
/*  输入框*/
  .inputword{
    margin: 0;
    margin-left: 400px;
    width: 30%;
    float: left;
  }
  /*搜索按钮*/
.sbtn{
  margin-right: 300px;
}
  .page{
    margin-top: 10px;
  }
</style>
