<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title></title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<div class="layui-row layui-col-space10">
    <div class="layui-col-xs12 layui-col-sm2 layui-col-md2">
        <!-- tree -->
        <ul id="tree" class="tree-table-tree-box"></ul>
    </div>
    <div class="layui-col-xs12 layui-col-sm10 layui-col-md10">
        <!-- 工具集 -->
        <div class="my-btn-box">
            <span class="fl">
                <a class="layui-btn layui-btn-danger" id="btn-delete-all">批量删除</a>
                <a class="layui-btn btn-default btn-add" id="btn-add-article">发布文章</a>
            </span>
            <span class="fr">
                <span class="layui-form-label">搜索条件：</span>
                <div class="layui-input-inline">
                    <input type="text" autocomplete="off" placeholder="请输入搜索条件" class="layui-input">
                </div>
                <button class="layui-btn mgl-20">查询</button>
            </span>
        </div>
        <!-- 表格 -->
        <div id="dateTable" lay-filter="demo"></div>
    </div>
</div>
<form class="layui-form" action="" id="editForm" style="display: none;padding: 20px;">
<div class="layui-form-item" >
    <label class="layui-form-label">用户名</label>
    <div class="layui-input-block">
      <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入标题" class="layui-input">
    </div>
  </div>
</form> 
 <!-- 第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<script id="editDemo" type="text/html">
 <div>
 <form class="layui-form" action="" style="padding: 20px;">
 <div class="layui-form-item" >
    <label class="layui-form-label">用户名</label>
    <div class="layui-input-block">
      <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入标题" class="layui-input" value="{{ d.user_name }}">
    </div>
  </div>
    <div class="layui-form-item">
    <label class="layui-form-label">验证必填项</label>
    <div class="layui-input-block">
      <input type="text" name="username" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
    </div>
  </div>
   <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">验证手机</label>
      <div class="layui-input-inline">
        <input type="tel" name="phone" lay-verify="required|phone" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-inline">
      <label class="layui-form-label">验证邮箱</label>
      <div class="layui-input-inline">
        <input type="text" name="email" lay-verify="email" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">多规则验证</label>
      <div class="layui-input-inline">
        <input type="text" name="number" lay-verify="required|number" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-inline">
      <label class="layui-form-label">验证日期</label>
      <div class="layui-input-inline">
        <input type="text" name="date" id="date" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
      </div>
    </div>
    <div class="layui-inline">
      <label class="layui-form-label">验证链接</label>
      <div class="layui-input-inline">
        <input type="tel" name="url" lay-verify="url" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">验证身份证</label>
    <div class="layui-input-block">
      <input type="text" name="identity" lay-verify="identity" placeholder="" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">自定义验证</label>
    <div class="layui-input-inline">
      <input type="password" name="password" lay-verify="pass" placeholder="请输入密码" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">请填写6到12位密码</div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">范围</label>
      <div class="layui-input-inline" style="width: 100px;">
        <input type="text" name="price_min" placeholder="￥" autocomplete="off" class="layui-input">
      </div>
      <div class="layui-form-mid">-</div>
      <div class="layui-input-inline" style="width: 100px;">
        <input type="text" name="price_max" placeholder="￥" autocomplete="off" class="layui-input">
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">单行选择框</label>
    <div class="layui-input-block">
      <select name="interest" lay-filter="aihao">
        <option value=""></option>
        <option value="0">写作</option>
        <option value="1" selected="">阅读</option>
        <option value="2">游戏</option>
        <option value="3">音乐</option>
        <option value="4">旅行</option>
      </select>
    </div>
  </div>
  
  
  <div class="layui-form-item">
    <div class="layui-inline">
      <label class="layui-form-label">分组选择框</label>
      <div class="layui-input-inline">
        <select name="quiz">
          <option value="">请选择问题</option>
          <optgroup label="城市记忆">
            <option value="你工作的第一个城市">你工作的第一个城市</option>
          </optgroup>
          <optgroup label="学生时代">
            <option value="你的工号">你的工号</option>
            <option value="你最喜欢的老师">你最喜欢的老师</option>
          </optgroup>
        </select>
      </div>
    </div>
    <div class="layui-inline">
      <label class="layui-form-label">搜索选择框</label>
      <div class="layui-input-inline">
        <select name="modules" lay-verify="required" lay-search="">
          <option value="">直接选择或搜索选择</option>
          <option value="1">layer</option>
          <option value="2">form</option>
          <option value="3">layim</option>
          <option value="4">element</option>
          <option value="5">laytpl</option>
          <option value="6">upload</option>
          <option value="7">laydate</option>
          <option value="8">laypage</option>
          <option value="9">flow</option>
          <option value="10">util</option>
          <option value="11">code</option>
          <option value="12">tree</option>
          <option value="13">layedit</option>
          <option value="14">nav</option>
          <option value="15">tab</option>
          <option value="16">table</option>
          <option value="17">select</option>
          <option value="18">checkbox</option>
          <option value="19">switch</option>
          <option value="20">radio</option>
        </select>
      </div>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">联动选择框</label>
    <div class="layui-input-inline">
      <select name="quiz1">
        <option value="">请选择省</option>
        <option value="浙江" selected="">浙江省</option>
        <option value="你的工号">江西省</option>
        <option value="你最喜欢的老师">福建省</option>
      </select>
    </div>
    <div class="layui-input-inline">
      <select name="quiz2">
        <option value="">请选择市</option>
        <option value="杭州">杭州</option>
        <option value="宁波" disabled="">宁波</option>
        <option value="温州">温州</option>
        <option value="温州">台州</option>
        <option value="温州">绍兴</option>
      </select>
    </div>
    <div class="layui-input-inline">
      <select name="quiz3">
        <option value="">请选择县/区</option>
        <option value="西湖区">西湖区</option>
        <option value="余杭区">余杭区</option>
        <option value="拱墅区">临安市</option>
      </select>
    </div>
    <div class="layui-form-mid layui-word-aux">此处只是演示联动排版，并未做联动交互</div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">复选框</label>
    <div class="layui-input-block">
      <input type="checkbox" name="like[write]" title="写作">
      <input type="checkbox" name="like[read]" title="阅读" checked="">
      <input type="checkbox" name="like[game]" title="游戏">
    </div>
  </div>
  
  <div class="layui-form-item" pane="">
    <label class="layui-form-label">原始复选框</label>
    <div class="layui-input-block">
      <input type="checkbox" name="like1[write]" lay-skin="primary" title="写作" checked="">
      <input type="checkbox" name="like1[read]" lay-skin="primary" title="阅读">
      <input type="checkbox" name="like1[game]" lay-skin="primary" title="游戏" disabled="">
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">开关-默认关</label>
    <div class="layui-input-block">
      <input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">开关-默认开</label>
    <div class="layui-input-block">
      <input type="checkbox" checked="" name="open" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">单选框</label>
    <div class="layui-input-block">
      <input type="radio" name="sex" value="男" title="男" checked="">
      <input type="radio" name="sex" value="女" title="女">
      <input type="radio" name="sex" value="禁" title="禁用" disabled="">
    </div>
  </div>
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">普通文本域</label>
    <div class="layui-input-block">
      <textarea placeholder="请输入内容" class="layui-textarea"></textarea>
    </div>
  </div>
  <!--<div class="layui-form-item layui-form-text">
    <label class="layui-form-label">编辑器</label>
    <div class="layui-input-block">
      <textarea class="layui-textarea layui-hide" name="content" lay-verify="content" id="LAY_demo_editor"></textarea>
    </div>
  </div>-->
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit="" lay-filter="submitBtn">立即提交</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form> </div>
</script>

<script type="text/javascript" src="../frame/layui/layui.js"></script>
<script type="text/javascript" src="../js/index.js"></script>
<script type="text/javascript">

    // layui方法
    layui.use(['form','tree', 'table', 'laytpl', 'layer', 'layedit', 'laydate'], function () {

        // 操作对象
        var table = layui.table
                , layer = layui.layer
				,form = layui.form
				 ,layedit = layui.layedit
				,laydate = layui.laydate
                , $ = layui.jquery;
		var laytpl = layui.laytpl;//模板引擎

        // 表格渲染
        var tableIns = table.render({
            elem: '#dateTable'                  //指定原始表格元素选择器（推荐id选择器）
            , height: 320    //容器高度
            , cols: [
				[ //标题栏
				{type:'numbers', title: '序号', width: 80, rowspan: 2,fixed: 'left'} //rowspan即纵向跨越的单元格数, {type:'numbers'} //显示序列号
				,{align: 'center', title: '联系人', width: 80, colspan: 2}
				,{align: 'center', title: '发布时间内容', colspan: 7} //colspan即横跨的单元格数，这种情况下不用设置field和width
				, {fixed: 'right', title: '操作', width: 250, align: 'center', toolbar: '#barOption',unresize: true,rowspan: 2} //这里的toolbar值是模板元素的选择器
			  ], 
			
				[                  //标题栏
               
				{type: 'checkbox'}
                , {field: 'id', title: 'ID', width: 80, unresize: true, sort: true}
                , {field: 'user_name', title: '用户名', width: 120}
                , {field: 'publish_time', title: '发布时间', width: 180}
                , {field: 'publish_content', title: '发布内容',event: 'setSign', width: 180}
                , {field: 'create_time', title: '创建时间', width: 180}
                , {field: 'if_pass', title: '是否通过', width: 70}
				 ,{field:'sex', title:'性别', width:85, templet: '#switchTpl', unresize: true}//模板就是可以让你自由自显示自己想要的内容
				 ,{field:'lock', title:'是否锁定', width:110, templet: '#checkboxTpl', unresize: true}
                
            ]]
            , id: 'dataCheck'
            , url: 'http://120.25.221.94/test/query'
            , method: 'get'
			,where:{//传递给后台的参数
				page:0,
				pageSize:10,
				templateDateMin:"",
				templateDateMax:""
			}
			,request: {
				 pageName: 'page' //页码的参数名称，默认：page
				,limitName: 'pageSize' //每页数据量的参数名，默认：limit
			}
			,response: {
			  statusName: 'code' //数据状态的字段名称，默认：code
			  ,statusCode: 200 //成功的状态码，默认：0
			  ,msgName: 'msg' //状态信息的字段名称，默认：msg
			  ,countName: 'total' //数据总数的字段名称，默认：count
			  ,dataName: 'data' //数据列表的字段名称，默认：data
			}   
            , page: true
            , limits: [10, 20, 30, 40, 50]
            , limit: 10 //默认采用30
            , loading: true
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);

                //得到当前页码
                console.log(curr);

                //得到数据总量
                console.log(count);
            }
        });

		//监听工具条
	  table.on('tool(demo)', function(obj){
		var data = obj.data;
		if(obj.event === 'detail'){
		  layer.msg('ID：'+ data.id + ' 的查看操作');
		} else if(obj.event === 'del'){
		  layer.confirm('真的删除行么', function(index){
			obj.del();
			layer.close(index);
		  });
		} else if(obj.event === 'edit'){
		  //layer.alert('编辑行：<br>'+ JSON.stringify(data))
		  var item = JSON.stringify(data);
		 var  editDemo = document.getElementById('editDemo');
		  var getTpl = editDemo.innerHTML;
		  console.log(getTpl);
			laytpl(getTpl).render(data, function(html){
			  console.log(html);
			   //自定页
				layer.open({
				  type: 1,
				  skin: 'layui-layer-demo', //样式类名
				  closeBtn: 1, //不显示关闭按钮0
				  area: ['600px', '600px'],//大小
				  anim: 2,
				  shadeClose: true, //开启遮罩关闭
				  content: html,
				   cancel: function(){
				   layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', {time: 5000, icon:6});
					
				  }
				});
			});
			 
		}else if(obj.event === 'setSign'){
			  layer.prompt({
				formType: 2
				,title: '修改 ID 为 ['+ data.id +'] 的用户签名'
				,value: data.publish_content
			  }, function(value, index){
				layer.close(index);
				
				//这里一般是发送修改的Ajax请求
				
				//同步更新表格和缓存对应的值
				obj.update({
				  publish_content: value
				});
			  });
			}
	  });
	  
        // 获取选中行
        table.on('checkbox(demo)', function (obj) {
            console.log(obj.checked); //当前是否选中状态
            console.log(obj.data); //选中行的相关数据
            console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
        });

        // 树        更多操作请查看 http://www.layui.com/demo/tree.html
        layui.tree({
            elem: '#tree' //传入元素选择器
            , click: function (item) { //点击节点回调
                layer.msg('当前节名称：' + item.name);
                // 加载中...
                var loadIndex = layer.load(2, {shade: false});
                // 关闭加载
                layer.close(loadIndex);
                // 刷新表格
                tableIns.reload();
            }
            , nodes: [{ //节点
                name: '父节点1'
                , children: [{
                    name: '子节点11'
                    , children: [{
                        name: '子节点111'
                    }]
                }, {
                    name: '子节点12'
                }]
            }, {
                name: '父节点2'
                , children: [{
                    name: '子节点21'
                    , children: [{
                        name: '子节点211纷纷就爱我就覅偶而安静佛尔'
                    }]
                }]
            }]
        });

        
		//监听提交
		  form.on('submit(submitBtn)', function(data){
			layer.alert(JSON.stringify(data.field), {
			  title: '最终的提交信息'
			})
			return false;
		  });
		//日期
		  laydate.render({
			elem: '#date'
		  });
		  laydate.render({
			elem: '#date1'
		  });
		  
		  //创建一个编辑器
		  var editIndex = layedit.build('LAY_demo_editor');
		 
		  //自定义验证规则
		  form.verify({
			title: function(value){
			  if(value.length < 5){
				return '标题至少得5个字符啊';
			  }
			}
			,pass: [/(.+){6,12}$/, '密码必须6到12位']
			,content: function(value){
			  layedit.sync(editIndex);
			}
		  });
		  
		  //监听指定开关
		  form.on('switch(switchTest)', function(data){
			layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
			  offset: '6px'
			});
			layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
		  });
		  
		  
    });
</script>
<script type="text/html" id="switchTpl">
  <!-- 这里的 checked 的状态只是演示 -->
  <input type="checkbox" name="sex" value="{{d.id}}" lay-skin="switch" lay-text="女|男" lay-filter="sexDemo" {{ d.id%2 == 0 ? 'checked' : '' }}>
</script>
<script type="text/html" id="checkboxTpl">
  <!-- 这里的 checked 的状态只是演示 -->
  <input type="checkbox" name="lock" value="{{d.id}}" title="锁定" lay-filter="lockDemo" {{ d.id == 3 ? 'checked' : '' }}>
</script>
<!-- 表格操作按钮集 -->
<script type="text/html" id="barOption">
	<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
</body>


</html>