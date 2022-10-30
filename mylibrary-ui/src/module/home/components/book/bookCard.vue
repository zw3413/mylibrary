<template>
			<el-card :body-style="{ padding: '0px' }" @click="showBook()">
				<div v-if="this.book.hasCover">
				<img :src="this.book.covera" class="image">
				</div>
				<div v-else  class="image" @click="emitShowBook()">
					<p style="padding:30px 10px">
					{{this.book.bookname}}
					</p>
				</div>
				<div style="padding: 14px;">
					<div class="bottom clearfix">
						<!-- <time class="time">{{this.book.publishtime}}</time> -->
						<el-button type="text" class="button">{{this.book.author}}</el-button>
					</div>
				</div>
			</el-card>
</template>

<script>
	export default {
		name: 'bookCard',
		data: function() {
			return {
				coverUrl:this.book.covera ,
				hasCover:this.book.covera!=null
			}
		},
		methods: {
			emitShowBook:function(){
				this.$emit('click');
			}
		},
		props:{
			book:{
				type:Object
			}
		}

	}
</script>

<style>
	.time {
		font-size: 13px;
		color: #999;
	}

	.bottom {
		margin-top: 5px;
		line-height: 12px;
	}

	.button {
		padding: 0;
		float: right;
	}

	.image {
		width: 100%;
		display: block;
		/* padding:14px; */
		height:260px;
		background: rgb(233, 233, 235);;
	}

	.clearfix:before,
	.clearfix:after {
		display: table;
		content: "";
	}

	.clearfix:after {
		clear: both
	}
	.text-cover{
		width:280px;
		height:280px;
	}
	.el-card{
		width:230px;
		margin:5px 30px;
		cursor:pointer;
	}
</style>
