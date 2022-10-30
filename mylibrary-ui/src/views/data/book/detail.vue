<template>
  <el-dialog title="详情" :visible.sync="visible" width="100%" style="top:-100px">
    <el-row>
      <el-col :span="5">
        <div class="grid-content bg-purple">
          <el-form :model="form">


            <el-form-item label="名称" :label-width="formLabelWidth">
              <el-input v-model="data.bookname" autocomplete="off"></el-input>
            </el-form-item>


            <el-form-item label="出版社" :label-width="formLabelWidth">
              <el-select
                v-model="data.publishhouse"
                filterable
                allow-create

                placeholder="请选择">

                <el-option
                  v-for="item in publishhouse"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>

              </el-select>
            </el-form-item>

            <el-form-item label="出版时间" :label-width="formLabelWidth">


              <el-date-picker
                v-model="data.publishtime"
                value-format="yyyy-MM"
                type="month"
                placeholder="选择">
              </el-date-picker>

            </el-form-item>

            <el-form-item label="作者" :label-width="formLabelWidth">

              <el-select
                v-model="data.author"
                filterable
                allow-create

                placeholder="请选择">

                <el-option
                  v-for="item in author"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>

              </el-select>

            </el-form-item>

            <el-form-item label="分类" :label-width="formLabelWidth">

              <el-select
                v-model="data.classification"
                filterable
                allow-create

                placeholder="请选择">

                <el-option
                  v-for="item in classification"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>

              </el-select>

            </el-form-item>

            <el-form-item label="标签" :label-width="formLabelWidth">

              <el-tag
                :key="tag"
                v-for="tag in tagArr"
                closable
                :disable-transitions="false"
                @close="handleTagClose(tag)">
                {{tag}}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisible"
                v-model="inputValue"
                ref="saveTagInput"
                size="small"
                @keyup.enter.native="handleTagInputConfirm"
                @blur="handleTagInputConfirm"
              >
              </el-input>
              <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ New Tag</el-button>


            </el-form-item>

            <el-form-item label="简介" :label-width="formLabelWidth">
              <el-input v-model="data.brief" autocomplete="off"></el-input>
            </el-form-item>

            <el-form-item label="评分" :label-width="formLabelWidth">
              <el-input v-model="data.ratings" autocomplete="off"></el-input>
            </el-form-item>

          </el-form>

        </div>
      </el-col>


      <el-col :span="13">
        <el-row style="padding: 0 20px">

          <div class="grid-content bg-purple-light">

            <iframe width="100%" height="600px" :src="pdfUrl"/>

          </div>
        </el-row>
      </el-col>
      <el-col :span="6">

        <el-row>

        </el-row>

        <el-row>

          <el-carousel :interval="4000" type="card" height="280px" v-if="refreshCarouselFlag">
            <el-carousel-item v-for="item in imageUrls" :key="item"  >

              <el-image
                :src="item"

              >
              </el-image>

            </el-carousel-item>
          </el-carousel>

        </el-row>


        <el-row>

          <el-col :span="12">
            <el-image
              style="height: 300px"
              :src="coveraUrl"
              fit="scale-down"></el-image>
          </el-col>
          <el-col :span="12">
            <div style="margin-top: 20px">

              生成<input type="number" min=0  max=3 value=0 step=1 v-model="cacheImageNumber"></input>张缓存图片<el-button @click="cacheImageHandler">do</el-button>
              <br/>
              设置第<input type="number" min=1  max=3 value=0 step=1 v-model="coverNo"></input>张为封面<el-button @click="setCover()">do</el-button>
              <br/>

              <el-button @click="deletePost()">删除post</el-button><el-button @click="setCoverToDefault()">设置默认图片为封面</el-button>


            </div>

          </el-col>


        </el-row>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible=false">取 消</el-button>
      <el-button type="primary" @click="submit()">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>

  import { getPdfViewerUrl, getPublishHouse, getClassification, getAuthor, getFileBaseurl } from '@/api/data.js'
  import {doCacheImage,doSaveBook,doRepublish,doDeletePost} from '@/api/operation'

  export default {
    name: 'detail',
    methods: {
      closeModal(){
        this.$parent.fetchData();
        this.$parent.showDetail=false;
      },
      deletePost(){

        doDeletePost(this.data.postid).then(resp=>{
          if(resp.state){
            this.$notify({
              title: '成功',
              message: '执行成功',
              type: 'success'
            });
          }else{
            this.$notify.error({
              title: '失败',
              message: '执行失败'+resp.message
            });
          }
        })

      },
      submit(){

        var publishtime=new Date();
        doSaveBook(this.data).then((resp)=>{
          if(resp.state){
            doRepublish(this.data.id).then(resp=>{
              if(resp.state){
                this.$notify({
                  title: '成功',
                  message: '执行成功',
                  type: 'success'
                });
                this.closeModal()
              }else{
                this.$notify.error({
                  title: '失败',
                  message: '执行失败'+resp.message
                });
              }
            })
          }else{
            this.$notify.error({
              title: '失败',
              message: '执行失败'+resp.message
            });
          }
        })



        //this.visible=false;
      },
      setCover(img){
        var covera;
        if(img){
          covera=img;
        }else{
          covera=this.data.fileid+"/"+this.coverNo+".jpg";
        }
        this.data.covera=covera;
        var param={}
        param.id=this.data.id;
        param.covera=covera;
        doSaveBook(param).then(resp=>{
          if(resp.state){
            this.$notify({
              title: '成功',
              message: '执行成功',
              type: 'success'
            });
          }else{
            this.$notify.error({
              title: '失败',
              message: '执行失败'+resp.message
            });
          }
        })
      },
      setCoverToDefault(){
        var covera='default/1.jpg';
        this.setCover(covera);

      },
      refreshCarousel(){
        this.refreshCarouselFlag=false;
        this.$nextTick(()=>{
          this.refreshCarouselFlag=true;
        })
      },
      cacheImageHandler(){
        var filepath=this.data.filepath;
        doCacheImage(filepath,this.cacheImageNumber).then(resp=>{
          if(resp.state){
            this.refreshCarousel();
            this.$notify({
              title: '成功',
              message: '执行成功',
              type: 'success'
            });
          }else{
            this.$notify.error({
              title: '失败',
              message: '执行失败'+resp.message
            });
          }
        });
      },
      generateImageUrls() {
        var pdfFolder = this.data.filepath.split('\.')[0]
        var imageFolder = this.fileBaseUrl + pdfFolder

        // for(var i=0; i< this.cacheImageNumber ;i++){
        //   var imageNo=i+1;
        //   this.imageUrls.push(imageFolder+'/'+imageNo+'.jpg');
        //
        // }
        this.imageUrls.push(imageFolder + '/' + '1.jpg')
        this.imageUrls.push(imageFolder + '/' + '2.jpg')
        this.imageUrls.push(imageFolder + '/' + '3.jpg')

        this.coveraUrl = this.fileBaseUrl+this.data.covera;

      },
      handleCurrentChange(val) {
        this.currentRow = val
      },
      handleTagClose(tag) {
        this.tagArr.splice(this.tagArr.indexOf(tag), 1)
        this.data.tag = this.tagArr.join('/')
      },
      showTagInput() {
        this.inputVisible = true
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus()
        })
      },
      handleTagInputConfirm() {
        let inputValue = this.inputValue
        if (inputValue) {
          if (this.tagArr.indexOf(inputValue) < 0) {
            this.tagArr.push(inputValue)
          }
        }
        this.inputVisible = false
        this.inputValue = ''
        this.data.tag = this.tagArr.join('/')
      }
    },
    data() {
      return {
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        coveraUrl:'',
        coverNo:1,
        refreshCarouselFlag:true,
        cacheImageNumber:3,
        input1Max:6,
        input1Min:0,
        input1Step:1,
        imageUrls: [],
        currentRow: null,
        inputVisible: false,
        inputValue: '',
        formLabelWidth: '120px',
        visible: true,
        data: this.row,
        pdfViewer: '',
        fileBaseUrl: '',
        classification: [],
        publishhouse: [],
        author: []

      }
    },
    computed: {

      pdfUrl: function() {
        return this.pdfViewer + '?file=' +this.fileBaseUrl+ this.data.filepath
      },

      tagArr: function() {
        if (this.data.tag) {
          return this.data.tag.split('/')
        } else {
          return []
        }
      }
    },
    watch: {
      visible: function(v) {
        this.$parent.showDetail = v
        this.visible = true
      },
      coverNo:function(v){
        var pdfFolder = this.data.filepath.split('\.')[0]
        var imageFolder = this.fileBaseUrl + pdfFolder
        this.coveraUrl = imageFolder+'/'+v+'.jpg'
      }
      //   ,
      // cacheImageNumber:function(v){ debugger
      //   this.generateImageUrls();
      // }
    },
    props: {
      row: {
        type: Object
      }
    },
    mounted: function() {

      //this.data=JSON.parse(JSON.stringify(this.templateData)); //this.templateData是父组件传递的对象

      getFileBaseurl().then(resp => {
        if (resp.state) {
          this.fileBaseUrl = resp.data
          this.generateImageUrls()
        }
      })
      getPdfViewerUrl().then(resp => {
        if (resp.state) {
          this.pdfViewer = resp.data
        }
      })
      getClassification().then(resp => {
        if (resp.state) {
          this.classification = resp.data
        }
      })
      getPublishHouse().then(resp => {
        if (resp.state) {
          this.publishhouse = resp.data
        }
      })
      this.author = getAuthor().then(resp => {
        if (resp.state) {
          this.author = resp.data
        }
      })

    }
  }
</script>
<style scoped>
  .el-input {
    width: 198px;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }

  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

  .grid-content {
    padding: 0 5px;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
