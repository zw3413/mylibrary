<template>
  <div class="book_detail">
    <h4 @click="toggleEdit()">基本信息:</h4>
    <el-row>
      <div class="title">名称</div>
      <el-input v-model="book.bookname" :readonly="readonly" />
    </el-row>

    <el-row>
      <div class="title">作者</div>
      <el-input v-model="book.author" :readonly="readonly" />
    </el-row>
    <el-row>
      <div class="title">语言</div>
      <el-input v-if="readonly" v-model="book.language" :readonly="readonly" />
      <el-select v-else v-model="book.language" allow-create filterable placeholder="语言">
        <el-option v-for="p in condition.language" :key="p.value" :label="p.label" :value="p.value" />
      </el-select>
    </el-row>

    <el-row>
      <div class="title">出版社</div>
      <el-input v-if="readonly" v-model="book.publishhouse" readonly />
      <el-select v-else v-model="book.publishhouse" allow-create filterable placeholder="出版社">
        <el-option v-for="p in condition.publishhouse" :key="p.value" :label="p.label" :value="p.value" />
      </el-select>
    </el-row>
    <el-row>
      <div class="title">出版时间</div>
      <el-date-picker v-model="book.publishtime" :readonly="readonly" value-format="yyyy-MM" type="month" placeholder="选择月" />
    </el-row>
    <el-row>
      <div class="title">分类</div>
      <el-input v-if="readonly" v-model="book.classification" :readonly="readonly" />
      <el-cascader
        v-else
        :options="condition.classification"
        clearable
        :value="book.classification"
        :placeholder="book.classification"
        @change="(evt)=>{book.classification=_(evt).join('/') } "
      />
    </el-row>
    <el-row>
      <div class="title">标签</div>
      <span v-if="readonly">
        <el-tag v-for="t in tag" :key="t" type="warning">{{ t }}</el-tag>
      </span>
      <el-select v-else v-model="tag" filterable allow-create multiple default-first-option placeholder="tag">
        <el-option v-for="p in condition.tag" :key="p.value" :label="p.label" :value="p.value" />
      </el-select>
    </el-row>

    <el-row>
      <div class="title">摘要</div>
      <el-input v-model="book.brief" style="display:block; width:400px;" type="textarea" :readonly="readonly" />
    </el-row>

    <h4>在线购买:</h4>
    <h4>网络资源:</h4>
    <el-link type="success" target="_blank" :href="this.book.absolutePath">{{ this.book.cnname }}</el-link>
  </div>

</template>

<script>
export default {
  name: 'BookDetail',
  props: {
    currentBook: {
      type: Object
    },
    condition: {
      type: Object
    }
  },
  data: function() {
    return {
      readonly: true,
      book: {},
      tag: []
    }
  },
  watch: {
    // readonly: function(nl, ol) {
    // 	if (nl) {debugger
    // 		console.log("[bookDetail] has changed the vue,and emit the change event")
    // 		this.$root.$children[0].saveCurrentBook(this.book);
    // 	}
    // },
    current_book: function() {
      this.book = Object.assign({}, this.current_book)
    },
    tag: function() {
      this.book.tag = this.tag.join('/')
    }

  },
  mounted: function() {
    this.initBook()
  },
  methods: {
    initBook: function() {
      var _this = this
      this.book = Object.assign({}, this.current_book)
      this.tag = []
      if (this.book.tag && this.book.tag.length > 0) {
        if (this.book.tag.indexOf('/') < 0) {
          this.tag.push(this.book.tag)
        } else {
          var a = this.book.tag.split('/')
          a.forEach(function(v, i) {
            _this.tag.push(v)
          })
        }
      }
    },
    toggleEdit: function() {
      this.activeEdit(this.readonly)
    },
    activeEdit: function(flag) {
      this.readonly = !flag
      if (this.readonly) {
        console.log('[bookDetail] has changed the vue,and emit the change event')
        debugger
        this.$root.$children[0].$children[0].saveCurrentBook(this.book)
      }
    }
  }
}
</script>

<style>
	.book_detail {
		line-height: 16px;
		text-align: left;
    padding-top: 20px;
	}

	.book_detail .el-row{
		margin-top: 10px;
		margin-bottom: 10px;
	}

	.book_detail h4 {
		width: 70%
	}

	.book_detail label {
		margin: 0 10px;
		color: #42B983
	}

	.book_detail .el-input {
		width: 300px;
	}

	.book_detail .title {
		display: inline-block;
		width: 70px;
		text-align-last: justify;
		margin-right: 20px;
	}

	.book_detail .el-textarea {
		width: 370px
	}

	.book_detail .edit {
		float: right;
	}
</style>
