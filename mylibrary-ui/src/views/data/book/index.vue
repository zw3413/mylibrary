<template>
  <div class="app-container">
<!--    <el-form :inline="true" :model="formInline" class="demo-form-inline">-->
      <el-form :inline="true"  class="demo-form-inline">

      <el-form-item label="名称">
        <el-input v-model="bookName" placeholder="请输入内容"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData()">搜索</el-button>
      </el-form-item>

    </el-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
      @row-dblclick="rowDblClick"
    >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item  label="文件大小" >
                {{ scope.row.filesize }}
            </el-form-item>
            <el-form-item label="文件路径" >
                {{ scope.row.filepath }}
            </el-form-item>
            <el-form-item label="文件时间" >
                {{ scope.row.filetime }}
            </el-form-item>
            <el-form-item label="根路径" >
                {{scope.row.baseFilePath}}
            </el-form-item>
            <el-form-item label="id" >
              {{scope.row.id}}
            </el-form-item>
            <el-form-item label="fileid" >
              {{scope.row.fileid}}
            </el-form-item>
            <el-form-item label="postid" >
              {{scope.row.postid}}
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column label="书名">
        <template slot-scope="scope">
          {{ scope.row.bookname }}
        </template>
      </el-table-column>


      <el-table-column label="作者" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>

      <el-table-column label="格式" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.format }}
        </template>
      </el-table-column>

      <el-table-column label="分类" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.classification }}
        </template>
      </el-table-column>

      <el-table-column label="标签" width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.tag }}
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="更新时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.updatetime }}
        </template>
      </el-table-column>


    </el-table>

    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[15, 30, 45, 60]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <detail v-if="showDetail" :row="clickedRow">
    </detail>

  </div>
</template>

<script>
  import { getBookList } from '@/api/data'
  import detail from './detail.vue'

  export default {
    name: 'book',
    components:{detail},
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'gray',
          deleted: 'danger'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        list: null,
        listLoading: true,
        currentPage: 1,
        pageSize: 15,
        total: null,
        bookName:'',
        showDetail: false,
        clickedRow:null,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      rowDblClick(row, column, event){
        console.log(row)
        this.showDetail=true;
        this.clickedRow=row;
      },
      fetchData() {
        this.listLoading = true
        getBookList({ pageSize: this.pageSize, currentPage: this.currentPage,bookName:this.bookName }).then(response => {
          this.list = response.data.items
          this.total = response.data.count
          this.listLoading = false
        })
      },
      handleSizeChange(val) {
        this.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.currentPage = val
        this.fetchData()
      }
    }
  }
</script>

<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
