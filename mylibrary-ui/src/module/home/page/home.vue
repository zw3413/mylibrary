<template>
  <div style="background-color:cornsilk;height:100%">
    <el-container>
      <el-header :height="headerHight" style="background-color:#404040;margin-bottom:2px">
        <el-row v-if="expandSearchFlag">
          <el-col :span="24">
            <div class="grid-content bg-purple-dark" style="float: left;">
              <!-- author -->
              <el-select
                v-model="hint.author"
                style="width: 100px;"
                clearable
                filterable
                placeholder="作者"
              >
                <el-option
                  v-for="p in this.condition.author"
                  :key="p.value"
                  :label="p.label"
                  :value="p.value"
                />
              </el-select>
              <!-- publisher -->
              <el-select v-model="hint.publishhouse" clearable filterable placeholder="出版社">
                <el-option
                  v-for="p in this.condition.publishhouse"
                  :key="p.value"
                  :label="p.label"
                  :value="p.value"
                />
              </el-select>
              <span style="margin-left: 20px;" />
              <!-- classification -->
              <el-cascader
                v-model="hint.classification"
                :options="condition.classification"
                :props="{checkStrictly: true}"
                clearable
                placeholder="分类"
              />
              <span style="margin-left: 20px;" />
              <!-- tag -->
              <el-select v-model="hint.tag" clearable filterable placeholder="tag" style="width: 100px;">
                <el-option
                  v-for="p in this.condition.tag"
                  :key="p.value"
                  :label="p.label"
                  :value="p.value"
                />
              </el-select>

              <span style="margin-left: 20px;" />
              <!-- publicationdateRange -->
              <el-date-picker
                v-model="hint.publishtimeRange"
                type="monthrange"
                range-separator="-"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                value-format="yyyy-MM"
                unlink-panels
              />
              <!-- language -->
              <el-select
                v-model="hint.language"
                filterable
                clearable
                placeholder="语言"
                style="width: 5rem;"
              >
                <el-option
                  v-for="p in condition.language"
                  :key="p.value"
                  :label="p.label"
                  :value="p.value"
                />
              </el-select>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <div class="grid-content bg-purple">
              <el-input v-model="hint.name" placeholder="请输入内容" style="width: 80%;float:left">
                <el-button
                  slot="prepend"
                  :icon="expandIconClass"
                  @click="toggleExpandSearch()"
                />
                <el-button slot="append" icon="el-icon-search" @click="search()" />
              </el-input>
            </div>
          </el-col>
          <!-- <el-col :span="12" v-if="expandSearchFlag">
                        <div class="grid-content bg-purple-light" style="line-height:30px;float: left;">
                            <span style="margin-left: 20px;"></span>
                            <el-switch v-model="hint.hasSource" active-text="网络资源">
                            </el-switch>
                            <span style="margin-left: 20px;"></span>
                            <el-switch v-model="hint.hasBDSource" active-text="百度网盘资源">
                            </el-switch>
                            <span style="margin-left: 20px;"></span>
                            <el-switch v-model="hint.hasOtherSource" active-text="其他资源">
                            </el-switch>
                        </div>
                    </el-col> -->
        </el-row>

      </el-header>
      <el-container>
        <el-aside width="280px" style="background-color:#404040">
          <el-row v-for="(book,index) in books" :key="book.id">
            <el-col>
              <bookCard :book="book" @click="setCurrentBook(book,index)" />
            </el-col>
          </el-row>
          <span
            v-if="books!=null && books.length>0"
            style="color: #42B983;cursor:pointer;"
            @click="loadNextPageBooks()"
          >点击加载更多内容</span>
        </el-aside>
        <el-main style="background-color:#404040;padding-left: 20px;color:ghostwhite">
          <el-row style="height:100%;">
            <el-col :span="10">
              <div class="grid-content" style="height:90%">
                <bookDetail
                  v-if="currentBook!=null"
                  :current_book="currentBook"
                  :condition="this.condition"
                />
              </div>
            </el-col>
            <el-col :span="14" style="height:100%">
              <div class="grid-content" style="height:100%;">
                <!-- <bookPreview v-if="currentBook!=null" :url="this.currentBook.absolutePath">
                                </bookPreview> -->
                <iframe
                  id="bookpreview"
                  style="text-align:center; height:100%; width:100%;border:0;padding:0;margin:0 "
                  name="bookpreview"
                  :src="bookPreviewUrl"
                />
              </div>
            </el-col>
          </el-row>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import bookCard from '../components/book/bookCard.vue'
import bookDetail from '../components/book/bookDetail.vue'
import bookPreview from '../components/book/bookPreview.vue'
import {parseParam} from '../../../utils/url'

export default {
  components: {
    bookCard,
    bookDetail,
    // eslint-disable-next-line vue/no-unused-components
    bookPreview
  },
  // name: 'app',
  data: () => ({
    welcomeMsg: '资料检索服务',

    condition: {
      language: null,
      author: null,
      condition: null,
      publishhouse: null,
      tag: null
    },

    hint: {
      name: '',
      publishhouse: '',
      author: '',
      publishtimeRange: '',
      language: '',
      tag: '',
      classification: ''
      // hasSource: false,
      // hasBDSource: false,
      // hasOtherSource: false
    },
    books: [],
    currentBook: null,
    urlContext: '',
    // restBaseUrl: "http://139.196.142.70:8090/mylibrary",
    restBaseUrl: 'http://139.196.142.70:8090/mylibrary',
    fileBaseUrl: 'http://139.196.142.70/file/mylibrary/book/',
    // pdfViewerUrl: "http://localhost/#/viewer/pdf",
    pdfViewerUrl: 'http://139.196.142.70/pdf/web/viewer.html' ,
    bookPreviewUrl: null,
    expandSearchFlag: false,
    activeIndex: 0,
    page: '1',
    pageSize: '5'

  }),
  computed: {
    noneFileUrl: function() {
      return this.fileBaseUrl + 'noneFile.png'
    },
    headerHight: function() {
      return this.expandSearchFlag ? '120px' : '60px'
    },
    expandIconClass: function() {
      return this.expandSearchFlag ? 'el-icon-caret-top' : 'el-icon-caret-bottom'
    },
    restUrl: function() {
      return {
        book: this.restBaseUrl + '/book'
      }
    },
    host: function() {
      return window.location.host
    },
    pathname: function() {
      return window.location.pathname
    },
    hash: function() {
      return window.location.hash
    },
    href: function() {
      return window.location.href
    }// ,
    // pdfViewerUrl: function() {
    //   return this.href + 'viewer/pdf'
    // }

  },
  watch: {
    bookPreviewUrl: function() {
      window['bookpreview'].location.reload()
    }
  },
  created: function() {
    this.fetchConditions()
    this.initParam()
  },
  mounted() {
    this.search()
  },
  methods: {
    initParam: function() {

    },
    search: function(param) {
      // alert(this.hint.publicationdateRange)
      this.hint.page = this.page
      this.hint.pageSize = this.pageSize
       fetch(this.restUrl.book+"?"+parseParam(this.hint)
         //, {
      //   method: 'get',
      //   body: JSON.stringify(this.hint),
      //   headers: {
      //     // 'user-agent': 'Mozilla/4.0 MDN Example',
      //     'content-type': 'application/json'
      //   }
      // }
      ).then(resp => {
        return resp.json()
      }).then(json => {
        // if(this.books.length>0 ){
        var _this = this

        if (!'loadMore' === param) {
          this.books = []
        }

        json.forEach(function(v) {
          _this.books.push(v)
        })
        // }
        // this.books = json;
      })
    },
    loadNextPageBooks() {
      var cp = eval(this.page)
      this.page = (++cp) + ''
      this.search('loadMore')
    },
    setCurrentBook: function(book, index) {
      this.activeIndex = index
      // 处理classification和tag
      // var classification = book.classification;
      // book.absolutePath = book.filepath?this.fileBaseUrl + book.filepath.trim():'';
      this.bookPreviewUrl = this.pdfViewerUrl + '?file=' + this.fileBaseUrl + book.filepath.trim()
      this.currentBook = this.books[index]
    },
    toggleExpandSearch: function() {
      this.expandSearchFlag = !this.expandSearchFlag
    },
    saveCurrentBook: function(book) {
      if (book) {
        debugger
        // this.setCurrentBook(book);
        Object.assign(this.books[this.activeIndex], book)
        // this.books[this.activeIndex]=book;
      }
      // 上传currentBook
      var url = this.restBaseUrl + '/book'
      fetch(url, {
        method: 'put',
        body: JSON.stringify(this.currentBook),
        headers: {
          // 'user-agent': 'Mozilla/4.0 MDN Example',
          'content-type': 'application/json'
        }
      }).then(resp => {
        return resp.json()
      }).then(json => {
        console.log(json)
      })
      console.log('saveCurrentBook')
      console.log(this.currentBook)
    },
    fetchConditions: function() {
      var url = this.restBaseUrl + '/table?name='
      var _this = this

      // 出版社列表
      fetch(url + 'mlb_book_publishhouse')
        .then(resp => {
          return resp.json()
        })
        .then(json => {
          var publishhouse = []
          json.forEach((v) => {
            publishhouse.push({
              label: v ? v.name : '',
              value: v ? v.name : ''
            })
            // publishhouse.push(v.name)
          })
          _this.condition.publishhouse = publishhouse
        })
      // 分类
      fetch(url + 'mlb_book_classification')
        .then(resp => {
          return resp.json()
        })
        .then(json => {
          var _ = this._
          var map = {}
          // 第一次解构
          _.forEach(json, function(v, i, c) {
            if (v && v.name) {
              if (v.name.indexOf('/') > 0) {
                var [c1, c2] = v.name.split('/')
                if (map[c1]) {
                  map[c1].add(c2)
                } else {
                  map[c1] = new Set([c2])
                }
              }
            }
          })
          var arr = []
          // 第二次重构
          _.forEach(map, function(v, k, c) {
            var o = {
              value: k,
              label: k,
              children: []
            }
            var c2 = Array.from(v)
            _.forEach(c2, function(v, i) {
              var o1 = {
                value: v,
                label: v
              }
              o.children.push(o1)
            })
            arr.push(o)
          })
          this.condition.classification = arr
        })
      // 语言
      fetch(url + 'mlb_book_language')
        .then((resp) => (resp.json()))
        .then(json => {
          var language = []
          json.forEach((v) => {
            if (v) {
              language.push({
                label: v.name,
                value: v.name
              })
            }
            // language.push(v.name)
          })
          _this.condition.language = language
        })
      // tag
      fetch(url + 'mlb_book_tag')
        .then(resp => (resp.json()))
        .then(json => {
          var tag = []
          json.forEach((v) => {
            tag.push({
              label: v.name,
              value: v.name
            })
            // tag.push(v.name)
          })
          _this.condition.tag = tag
        })
      // author
      fetch(url + 'mlb_book_author')
        .then(resp => (resp.json()))
        .then(json => {
          var author = []
          json.forEach((v) => {
            // publisher.push({label:v.name,value:v.name})
            author.push({
              label: v.name,
              value: v.name
            })
          })
          _this.condition.author = author
        })
    }
  }
}
</script>
<style>
    html,
    body {
        height: 100%;
        margin: 0;
    }

    #app {
        height: 100%;
    }

    .el-header,
    .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        height: 100%;
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        padding: 10px 0
        /* line-height: 200px; */
    }

    .el-main {
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        line-height: 160px;
        height: 100%;
        padding: 0;
    }

    .el-container {
        padding-bottom: 0px;
        height: 100%;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }

    .el-header .el-input {
        margin-left: 10px;
    }

    .el-header .el-checkbox {
        margin-left: 10px;
    }

    .el-header {
        padding-top: 10px !important
    }

    .el-cascader .el-cascader__tags {
        margin-left: 20px
    }
</style>
