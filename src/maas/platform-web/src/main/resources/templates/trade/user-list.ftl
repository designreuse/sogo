<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title></title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui2/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <style>
        .layui-form-item .layui-inline {
            margin-bottom: 5px;
            margin-right: -4px;
        }
        .orderTime {

            width: 211px;
            font-size: 10px;


        }
        .select,.layui-unselect .layui-form-select{
            width: 190px;

        }
        .layui-colla-icon {
            position: absolute;
            left: 15px;
            top: -12px;

        }
        .layui-form-item {
            margin-bottom: -8px;
            clear: both;
        }
    </style>
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
    var _PATH = "${Session.basePath}";
</script>
<div class="layui-tab">

    <div class="layui-btn-group">
        <div class="layui-inline" id="switchBtn">

        </div>

    </div>
    <hr class="layui-bg-blue">


    <form class="layui-form" action="">
        <div class="layui-form-item"  >
            <div class="layui-inline">
                <label class="layui-form-label">姓名：</label>
                <div class="layui-input-inline orderTime">
                    <input type="text" class="layui-input" name="name"  placeholder="请输入" >
                </div>
            </div>

            <#--<div class="layui-inline">-->
                <#--<label class="layui-form-label">在线状态：</label>-->
                <#--<div class="layui-input-block select">-->
                    <#--<select name="countWay" lay-filter="aihao">-->
                        <#--<option value="" selected=""></option>-->
                        <#--<option value="上线" >上线</option>-->
                        <#--<option value="忙碌">忙碌</option>-->
                        <#--<option value="下线">下线</option>-->
                    <#--</select>-->
                <#--</div>-->
            <#--</div>-->
            <div class="layui-inline"  style="margin-right:21px">
                <label class="layui-form-label">登陆账号：</label>
                <div class="layui-input-inline orderTime">
                    <input type="text" class="layui-input" name="username"  placeholder="请输入" >
                </div>
            </div>
            <div class="layui-inline">
            <label class="layui-form-label">公司编号：</label>
            <div class="layui-input-inline orderTime">
                <input type="text" class="layui-input" name="compId"  placeholder="请输入" >
            </div>
                <div class="layui-inline">
                    <label class="layui-form-label">部门编号：</label>
                    <div class="layui-input-inline orderTime">
                        <input type="text" class="layui-input" name="deptId"  placeholder="请输入" >
                    </div>
                </div>
        </div>

            <div class="layui-inline" style="width: 30px;">
                <button class="layui-btn layui-btn-xs layui-btn-disabled" style="display: none" type="button" id="showBtn"> 更多<i class="layui-icon">&#xe65b;</i></button>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-inline">
                <button class="layui-btn layui-btn-primary layui-btn-radius" lay-submit lay-filter="formDemo" style="height: 27px;line-height: 27px;">查询</button>
            </div>
            <div class="layui-inline">
                <button class="layui-btn layui-btn-radius" type="reset" style="height: 27px;line-height: 27px;">重置</button>
            </div>

        </div>
        <div class="layui-form-item" style="height: 20px;">
        </div>

    </form>
<#--</div></div></div>-->
    <!-- 表格 -->
    <div id="dateTable" lay-filter="demo"></div>






</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui2/layui.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/js/config.js"></script>
<script type="text/javascript">
    layui.use(['form','element','laydate',/*'vip_table',*/'table' ,'laytpl'], function () {
        var $ = layui.jquery
                ,layer = layui.layer 			//弹层
                ,form = layui.form
                ,laydate = layui.laydate
                , table = layui.table
                //  , vipTable = layui.vip_table
                , $ = layui.jquery
                , element = layui.element;
        var laytpl = layui.laytpl; //模板引擎
        var _startTime ;
        var _endTime;
        var current_id;

        // you code ...
        //日期时间范围
        laydate.render({
            elem: '#test10'
            ,type: 'datetime'
            ,range: true
            ,format:"yyyy-MM-ddTHH:mm:ss"
            ,done: function(value, date, endDate){
                console.log(value); //得到日期生成的值，如：2017-08-18
                console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
                console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
                if(value!=""){

                    _startTime = (new Date());
                    _startTime .setFullYear(date.year,date.month-1,date.date);
                    _startTime.setHours(date.hours,date.minutes,date.seconds);
                    _startTime=_startTime.Format("yyyy-MM-dd HH:mm:ss");
                    _endTime = (new Date());
                    _endTime .setFullYear(endDate.year,endDate.month-1,endDate.date);
                    _endTime.setHours(endDate.hours,endDate.minutes,endDate.seconds);
                    _endTime=_endTime.Format("yyyy-MM-dd HH:mm:ss");

                }else{
                    _startTime = null;
                    _endTime = null;
                }

            }
        });

        //隐藏第二行查询
        $("#hideBtn").click(function(){
            $('#secondRow').hide();
            $('#showBtn').show();
        });

        //更多
        $("#showBtn").click(function(){
            $('#secondRow').show();
            $('#showBtn').hide();
        });

        var options = {
            elem: '#dateTable'                  //指定原始表格元素选择器（推荐id选择器）
            //  , height: 500    //容器高度
            , cols: [[                  //标题栏
                {title: '序号',templet: '#indexTpl', width: 76}
                , {field: 'username', title: '登陆账号', width: 120}
                , {field: 'name', title: '用户名', width: 206}
                , {field: 'deptName', title: '部门名称', width: 226}

                , {field: 'onlineStatus', title: '状态', width: 220}
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#barOption'}

            ]]
            , id: 'dataCheck'
            // , url:LAYUI_FILE_PATH+'/assets/json/order_table.json'// '/api/order/list'
            ,url:_PATH+"/api/user/findUserPage"
            , method: 'post'
            // , method: 'get'//'post'
            ,contentType: 'application/json'
            , page:true// {first:0,curr:0}


            , limits: [10, 20, 40, 100, 300]
            , limit: 10 //默认采用30
            , loading: true
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,where:{

                // "countWay": "订单类别",
                // "orderSort":"机票"

            }
            ,parseData: function(res){ //res 即为原始返回的数据

                //后端返回为true非false就不用抛出
                if(res.success&&res.obj){
                    return {
                        "code": 0, //解析接口状态
                        "msg": res.msg, //解析提示文本
                        "count": res.obj.total, //解析数据长度
                        "data": res.obj.rows //解析数据列表
                    };

                }


                return {
                    "code": 0, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": 0, //解析数据长度
                    "data": [] //解析数据列表
                };
            }
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);

                //得到当前页码
                console.log(curr);

                //得到数据总量
                console.log(count);
            }
        }

        // 表格渲染
        var tableIns = table.render(options);


        //监听提交查询订单列表信息
        form.on('submit(formDemo)', function(data){
            // layer.msg(JSON.stringify(data.field));
            console.log(data.field);
            console.log("--------------------------",$("select[name='orderType']").val());
            for(var key in data.field){
                data.field[key] = $.trim(data.field[key]);
                if( data.field[key]=="")
                    data.field[key]=null;
            }
            options.where = data.field;



            table.reload('dataCheck',options);
            return false;
        });



        //----------------------------------------操作日志得表格--------------------------------------------------------
        var journal_options = {
            elem: '#journalTable'                  //指定原始表格元素选择器（推荐id选择器）
            //  , height: 500    //容器高度
            , cols: [[                  //标题栏

                {field: 'operateTime', title: '操作时间', width: 180,templet: '#operateTimeTpl'}
                , {field: 'operateType', title: '操作类型', width: 120}
                , {field: 'userName', title: '操作人', width: 120}
                , {field: 'compName', title: '操作公司', width: 180}
                , {field: 'deptName', title: '操作部门', width: 180}
                , {field: 'f ', title: 'PNR状态', width: 120}

            ]]
            , id: 'journal_dataCheck'
            , url:LAYUI_FILE_PATH+'/assets/json/journal_table.json'// '/api/order/list'
            , method: 'get'//'post'
            //,contentType: 'application/json'
            , page:false// {first:0,curr:0}


            , limits: [10, 20, 40, 100, 300]
            , limit: 10 //默认采用30
            , loading: true
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,where:{

            }
            ,parseData: function(res){ //res 即为原始返回的数据

                //后端返回为true非false就不用抛出
                if(res.success&&res.obj){
                    return {
                        "code": 0, //解析接口状态
                        "msg": res.msg, //解析提示文本
                        // "count": res.obj.total, //解析数据长度
                        "data": res.obj //解析数据列表
                    };

                }


                return {
                    "code": 0, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": 0, //解析数据长度
                    "data": [] //解析数据列表
                };
            }
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);

                //得到当前页码
                console.log(curr);

                //得到数据总量
                console.log(count);
            }
        }

        //----------------------------------------签注表格--------------------------------------------------------
        var endorsement_options = {
            elem: '#endorsementTable'                  //指定原始表格元素选择器（推荐id选择器）
            //  , height: 500    //容器高度
            , cols: [[                  //标题栏
                {title: '序号',templet: '#indexTpl', width: 60}
                , {field: 'remarkDate', title: '签注时间', width: 170,templet: '#remarkDateTpl'}
                , {field: 'userName', title: '签注人', width: 105}
                , {field: 'remarkInfo', title: '签注内容', width: 260}


            ]]
            , id: 'endorsement_dataCheck'
            , url:LAYUI_FILE_PATH+'/assets/json/endorsement_table.json'// '/api/order/list'
            , method: 'get'//'post'
            //,contentType: 'application/json'
            , page:false// {first:0,curr:0}


            , limits: [10, 20, 40, 100, 300]
            , limit: 10 //默认采用30
            , loading: true
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,where:{

            }
            ,parseData: function(res){ //res 即为原始返回的数据

                //后端返回为true非false就不用抛出
                if(res.success&&res.obj){
                    return {
                        "code": 0, //解析接口状态
                        "msg": res.msg, //解析提示文本
                        //  "count": res.obj.total, //解析数据长度
                        "data": res.obj //解析数据列表
                    };

                }


                return {
                    "code": 0, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": 0, //解析数据长度
                    "data": [] //解析数据列表
                };
            }
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);

                //得到当前页码
                console.log(curr);

                //得到数据总量
                console.log(count);
            }
        }





        //监听工具条
        table.on('tool(demo)', function(obj){
            var data = obj.data;
            var params ={};
            params.onlineStatus="离线";
            params.userId = data.userId;
           var  layermsg_index =  layer.load(0, {shade: [0.5, '#393D49']});
            if(obj.event === 'online'){
                //在线
                params.onlineStatus="在线";
            } else if(obj.event === 'offline'){
                //离线
                params.onlineStatus="离线";
            } else if(obj.event === 'busy'){
                //忙碌

                params.onlineStatus="忙碌";
            }


                $.ajax({
                    url:_PATH+"/api/user/updateUserOnlineStatus",
                    data: JSON.stringify(params),
                    contentType: 'application/json',
                    dataType:'json',	//服务器返回json格式数据2
                    type:'POST',		//HTTP请求类型
                    success:function(_data){
                        layer.close(layermsg_index);
                        if(_data.success){


                            // layer.msg('', {icon: 1});
                            // $("#username").html(username+"- 离线");
                            table.reload('dataCheck',options);

                        }else{

                            layer.msg(_data.msg, {icon: 2});
                        }

                    },error:function(e){
                        alert("网络错误")
                    }
                });

        });

        ///-------------------------------------状态切换按钮----------------------------------------------
        $("#statusBtn button").eq(0).css("color","red");
        $("#statusBtn button").click(function(e){
            //   console.log($(e.target).html());
            var flag = $(e.target).html();
            $("#manageStatus").val(flag);
            $(e.target).siblings().css("color","black");
            $(e.target).css("color","red");
            form.render();
            options.where.manageStatus=flag;
            if(options.where.manageStatus=="全部"||options.where.manageStatus==""){
                options.where.manageStatus=null;
            }
            table.reload('dataCheck',options);
            if( flag == "处理中"  ){
                $("#grabSingleMember_lab").html("抢单员：");

            }else if( flag == "已完成" ){

                $("#grabSingleMember_lab").html("出票员：");
            }else if( flag == "已暂存" ){
                $("#grabSingleMember_lab").html("抢单员：");

            }else if( flag == "已超时" ){
                $("#grabSingleMember_lab").html("抢单员：");

            }else if( flag == "已退回" ){
                $("#grabSingleMember_lab").html("退单员：");

            }else if( flag ==  "已抽回" ){

                $("#grabSingleMember_lab").html("抢单员：");
            }


        });

        $("#switchBtn button").eq(3).css("color","red");
        $("#switchBtn").click(function(e){

            var flag = $(e.target).html();
            var url = "";
            if(flag=="待抢单"){
                //调用父页面index.js添加菜单方法
                var layId =  window.parent.getThisTabID();//getTitleId_children('card',"已派单");
                console.log("layId=",layId);
                window.parent.addTab_parent(_PATH+"/trade/pending-order-master.html","待抢单",layer);
                window.parent.delTab(layId);

                //var xxObj = document.frames;xxObj[0].src="index.htm";.
                /* console.log(window.location.href);
                 var href = window.location.href;
                 var lastIndex = href.indexOf("/");
                 href = href.substring(0,lastIndex);
                 href+="pending-order-master.html";
                 console.log("href=",href);

                 window.location.href=href;*/
            }else if(flag=="待派单"){
                var layId =  window.parent.getThisTabID();//getTitleId_children('card',"已派单");
                console.log("layId=",layId);
                window.parent.addTab_parent(_PATH+"/trade/pending-bill-master.html","待派单",layer);
                window.parent.delTab(layId);

            }else if(flag=="已抢单"){
                var layId =  window.parent.getThisTabID();//getTitleId_children('card',"已派单");
                console.log("layId=",layId);
                window.parent.addTab_parent(_PATH+"/trade/already-robbed-master.html","已抢单",layer);
                window.parent.delTab(layId);

            }else if(flag=="兜底单"){
                var layId =  window.parent.getThisTabID();//getTitleId_children('card',"已派单");
                console.log("layId=",layId);
                window.parent.addTab_parent(_PATH+"/trade/pocket-bill-master.html","兜底单",layer);
                window.parent.delTab(layId);

            }


        });

        //--------------订单分类统计----------------------------


    });

    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时
            "H+" : this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
</script>
<script type="text/html" id="indexTpl">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script  type="text/html" id="orderTpl">
    {{# if(d.orderProperty == 0){ }}
    <span class="layui-badge">新</span>
    {{#  } }}
    {{# if(d.orderProperty == 1){ }}
    <span class="layui-badge">第一次退单</span>
    {{#  } }}
    {{# if(d.orderProperty == 2){ }}
    <span class="layui-badge layui-bg-blue">第二次退单</span>
    {{#  } }}
    {{# if(d.orderProperty == 3){ }}
    <span class="layui-badge layui-bg-blue">普</span>
    {{#  } }}
</script>
<script  type="text/html" id="operateTimeTpl">

    {{#
    var fn = function(){

    return d.operateTime?(new Date(d.operateTime)).Format('yyyy-MM-dd HH:mm:ss'):"";
    };
    }}
    {{ fn() }}
</script>
<script  type="text/html" id="remarkDateTpl">

    {{#
    var fn = function(){

    return d.remarkDate?(new Date(d.remarkDate)).Format('yyyy-MM-dd HH:mm:ss'):"";
    };
    }}
    {{ fn() }}
</script>
<script  type="text/html" id="orderEntranceTpl">

    {{#
    var fn = function(){

    return d.orderEntranceTime?(new Date(d.orderEntranceTime)).Format('yyyy-MM-dd HH:mm:ss'):"";
    };
    }}
    {{ fn() }}
</script>
<!-- 表格操作按钮集 -->
<script type="text/html" id="barOption">
    <#--<a class="layui-btn layui-btn-mini" lay-event="detail">详情</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-normal" lay-event="edit">签注</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-danger" lay-event="del">日志</a>-->
    {{# if(d.onlineStatus != "在线"){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="online">在线</a>
    {{#  } }}
    {{# if(d.onlineStatus != "忙碌"){ }}
    <#--<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="busy">忙碌</a>-->
    {{#  } }}
    {{# if(d.onlineStatus != "离线"){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="offline">离线</a>
    {{#  } }}
</script>
<!-- 第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<#--日志模板-->

<script id="endorsementTpl" type="text/html">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
            <#--<div class="layui-card-header"></div>-->
                <div class="layui-card-body">
                    <!-- 签注表格 -->
                    <div id="endorsementTable" lay-filter="endorsementFilter"></div>
                </div>
            </div>
        </div>
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">签注</div>
                <div class="layui-card-body">
                    <div class="layui-input-block" style="margin-left: 0px;">
                        <textarea id="remarkInfo" name="desc" placeholder="输入签注信息" class="layui-textarea"></textarea>
                    </div>
                </div>

            </div>
        </div>

    </div>
</script>
</body>
</html>