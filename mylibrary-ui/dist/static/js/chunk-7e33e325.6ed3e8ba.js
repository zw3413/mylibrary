(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7e33e325"],{"60fe":function(e,t,n){"use strict";n.d(t,"a",(function(){return a}));var a="http://139.196.142.70:8090/mylibrary"},"7cff":function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0}},[n("el-form-item",{attrs:{label:"名称"}},[n("el-input",{attrs:{placeholder:"请输入内容"},model:{value:e.name,callback:function(t){e.name=t},expression:"name"}})],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search"},on:{click:function(t){return e.fetchData()}}},[e._v("搜索")])],1)],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"ID",width:"95"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.$index)+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"书名"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.bookname)+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"作者",width:"110",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.author))])]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"格式",width:"110",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.format)+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{"class-name":"status-col",label:"添加时间",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.createtime)+"\n      ")]}}])}),e._v(" "),n("el-table-column",{attrs:{"class-name":"status-col",label:"更新时间",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.updatetime)+"\n      ")]}}])})],1),e._v(" "),n("div",{staticClass:"block"},[n("el-pagination",{attrs:{"current-page":e.currentPage,"page-sizes":[15,30,45,60],"page-size":e.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)],1)},r=[],i=n("7e1e"),l={name:"book",filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]}},data:function(){return{list:null,listLoading:!0,currentPage:1,pageSize:15,total:null,name:""}},created:function(){this.fetchData()},methods:{fetchData:function(){var e=this;this.listLoading=!0,Object(i["e"])({pageSize:this.pageSize,currentPage:this.currentPage,name:this.name}).then((function(t){e.list=t.data.items,e.total=t.data.count,e.listLoading=!1}))},handleSizeChange:function(e){this.pageSize=e,this.fetchData()},handleCurrentChange:function(e){this.currentPage=e,this.fetchData()}}},u=l,o=n("2877"),c=Object(o["a"])(u,a,r,!1,null,"11444976",null);t["default"]=c.exports},"7e1e":function(e,t,n){"use strict";n.d(t,"b",(function(){return i})),n.d(t,"e",(function(){return l})),n.d(t,"d",(function(){return u})),n.d(t,"f",(function(){return o})),n.d(t,"g",(function(){return c})),n.d(t,"c",(function(){return s})),n.d(t,"a",(function(){return f}));var a=n("b775"),r=n("60fe");function i(e){return Object(a["a"])({url:r["a"]+"/v2/book/"+e.pageSize+"/"+e.currentPage,method:"get",params:e})}function l(e){return Object(a["a"])({url:r["a"]+"/file/"+e.pageSize+"/"+e.currentPage,method:"get",params:e})}function u(){return Object(a["a"])({url:r["a"]+"/file/baseurl",method:"get"})}function o(){return Object(a["a"])({url:r["a"]+"/file/pdfviewer",method:"get"})}function c(){return Object(a["a"])({url:r["a"]+"/v2/book/publishhouse"})}function s(){return Object(a["a"])({url:r["a"]+"/v2/book/classification"})}function f(){return Object(a["a"])({url:r["a"]+"/v2/book/author"})}}}]);