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
    <p style="color: #26bec5;">报表统计 > 总订单量统计</p>
    <hr class="layui-bg-blue">


    <form class="layui-form" action="">
        <div class="layui-form-item"  >
            <#--<div class="layui-inline" style="margin-right: -14px;">-->
                <#--<label class="layui-form-label">进单时间：</label>-->
                <#--<div class="layui-input-inline orderTime">-->
                    <#--<input type="text" class="layui-input" id="test10" placeholder="时间区间" name="date">-->
                <#--</div>-->
            <#--</div>-->
            <div class="layui-inline">
                <label class="layui-form-label">订单号：</label>
                <div class="layui-input-inline orderTime">
                    <input type="text" class="layui-input"  placeholder="" name="orderNo">
                </div>
            </div>
            <div class="layui-inline" >
                <label class="layui-form-label" >订单类型：</label>
                <div class="layui-input-block select">
                    <select name="orderType" lay-filter="aihao" id="orderType">
                        <option value="" selected="">全部</option>
                        <option value="出票" >出票</option>
                        <option value="退票">退票</option>
                        <option value="改期">改期</option>
                        <option value="航延">航延</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label" >订单来源：</label>
                <div class="layui-input-block select">
                    <select name="orderSource" lay-filter="aihao">
                        <option value="" selected="">选择</option>
                        <option value="营业部订单" >营业部订单</option>
                        <option value="网上订单" >网上订单</option>
                        <option value="外部传入">外部传入</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline"  style="margin-right:7px">
                <label class="layui-form-label">订单类别：</label>
                <div class="layui-input-block select">
                    <select name="orderSort" lay-filter="aihao">
                        <option value="">选择</option>
                        <option value="机票" selected="">机票</option>
                        <option value="酒店" >酒店</option>

                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">处理状态：</label>
                <div class="layui-input-block select">
                    <select name="lastOperateStatus" lay-filter="aihao">
                        <option value="全部" selected="">全部</option>
                        <option value="待处理" >待处理</option>
                        <option value="处理中" >处理中</option>
                        <option value="已完成">已完成</option>
                        <option value="已暂存">已暂存</option>
                        <option value="已超时">已超时</option>
                        <option value="已退回">已退回</option>
                        <option value="已抽回">已抽回</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline"  style="margin-right:21px">
                <label class="layui-form-label">来源方式：</label>
                <div class="layui-input-block select">
                    <select name="fromAction" lay-filter="aihao">
                        <option value="" selected="">选择</option>
                        <option value="抢单">抢单</option>
                        <option value="派单" >派单</option>

                    </select>
                </div>
            </div>
            <div class="layui-inline" style="width: 30px;">
                <button class="layui-btn layui-btn-xs layui-btn-disabled" style="display: none" type="button" id="showBtn"> 更多<i class="layui-icon">&#xe65b;</i></button>
            </div>
        </div>

    <#--<div class="layui-form-item">-->
    <#--<div class="layui-collapse" lay-filter="test">-->
    <#--<div class="layui-colla-item">-->
    <#--<h2 class="layui-colla-title">更多...</h2>-->
    <#--<div class="layui-colla-content">-->
    <#--<p>有不少其他答案说是因为JS太差。我下面的答案已经说了，这不是根本性的原因。但除此之外，我还要纠正一些对JS具体问题的误解。JS当初是被作为脚本语言设计的，所以某些问题并不是JS设计得差或者是JS设计者的失误。比如var的作用域问题，并不是“错误”，而是当时绝大部分脚本语言都是这样的，如perl/php/sh等。模块的问题也是，脚本语言几乎都没有模块/命名空间功能。弱类型、for-in之类的问题也是，只不过现在用那些老的脚本语言的人比较少，所以很多人都误以为是JS才有的坑。另外有人说JS是半残语言，满足不了开发需求，1999年就该死。半残这个嘛，就夸张了。JS虽然有很多问题，但是设计总体还是优秀的。——来自知乎@贺师俊</p>-->
    <#--</div>-->
    <#--</div>-->
    <#--</div>-->
    <#--</div>-->
        <div class="layui-form-item">
            <div class="layui-inline">
                <button class="layui-btn layui-btn-primary layui-btn-radius" lay-submit lay-filter="formDemo" style="height: 27px;line-height: 27px;">查询</button>
            </div>
            <div class="layui-inline">
                <button class="layui-btn layui-btn-radius" type="reset" style="height: 27px;line-height: 27px;">重置</button>
            </div>
            <div class="layui-inline">
                <button class="layui-btn layui-btn-radius" type="button" style="height: 27px;line-height: 27px;" id="exportBtn">导出</button>
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
                    _startTime=_startTime.Format("yyyy-MM-dd");
                    _endTime = (new Date());
                    _endTime .setFullYear(endDate.year,endDate.month-1,endDate.date);
                    _endTime.setHours(endDate.hours,endDate.minutes,endDate.seconds);
                    _endTime=_endTime.Format("yyyy-MM-dd");

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
                , {field: 'orderProperty', title: '订单属性', width: 86}

                , {field: 'externalOrderNo', title: '外部订单号', width: 120}
                , {field: 'orderSource', title: '订单来源', width: 120}
                , {field: 'orderNo', title: '订单编号', width: 180}
                , {field: 'orderType', title: '订单类型', width: 180}
                , {field: 'orderSort', title: '订单类别', width: 180}
                , {field: 'orderInTime', title: '进单时间', width: 120}
                , {field: 'fromAction', title: '来源方式', width: 180 }
                , {field: 'createdAt', title: '抢单/派单时间', width: 120}
                , {field: 'disposeCountdown', title: '处理倒计时', width: 120}
                , {field: 'updatedAt', title: '锁单时间', width: 120}
                , {field: 'processDuration', title: '处理时长', width: 120}
                , {field: 'lastOperateStatus', title: '处理状态', width: 120}

                , {field: 'confirmTime', title: '出票时间', width: 120}
                , {field: 'username', title: '出票员', width: 120}
            ]]
            , id: 'dataCheck'
            // , url:LAYUI_FILE_PATH+'/assets/json/order_table.json'// '/api/order/list'
            ,url:_PATH+"/api/orderRecord/selectOrdReAllPage"
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
            }
            options.where = data.field;
            if(options.where.orderSource==""){
                options.where.orderSource=null;
            }
            if(options.where.orderType==""){
                options.where.orderType=null;
            }
            if(options.where.orderSort==""){
                options.where.orderSort=null;
            }
            if( options.where.lastOperateStatus== "全部"){
                options.where.lastOperateStatus=null;
            }
            if( options.where.date == ""){
                options.where.startOrderIn = null;
                options.where.endOrderIn = null;
            }else if( _startTime&&_startTime!="" ){
                options.where.startOrderIn=_startTime//.Format('yyyy-MM-dd HH:mm:ss');
                options.where.endOrderIn=_endTime//.Format('yyyy-MM-dd HH:mm:ss');

            }
            if( options.where.fromAction== ""){
                options.where.fromAction=null;
            }


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
            if(obj.event === 'detail'){
                //详情跳转到差旅平台

                layer.msg('ID：'+ data.id + ' 的查看操作');

                // window.open("http://14.21.67.172:8088/asms/ticket/order/zcd/asmslticketorderzcd/detail?ddbh="+data.orderNo+"&mkbh=404577&cgqx_gn=15&cgqx_gj=17&xsqx=16&open_czfrom=");
                // window.open("http://14.21.67.172:8088/asms/ticket/order/ticketmanager/asmsticketmanagerzcd/list");
                var date = new Date().Format("yyyy-MM-dd");
                var return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagerzcd/list?optflag=ZCD_DCP&sddbh="+data.orderNo+"&ksrq=2018-01-01&jsrq="+date;
                if(  data.orderType =="改期"  ){
                    return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagergqd/list?optflag=GQD_DGQ&gqdh="+data.externalOrderNo+"&ksrq=2018-01-01&jsrq="+date;
                }else if(  data.orderType =="退票"  ){
                    return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagertfd/list?optflag=TFD_DBL&sddbh="+data.orderNo+"&ksrq=2018-01-01&jsrq="+date;
                }
                layer.open({
                    type: 2,
                    title: '差旅查询页',
                    shadeClose: true,
                    shade: 0.8,
                    maxmin: true,
                    area: ['100%', '100%'],
                    content: CONFIG.TRADE+return_url //iframe的url
                });
            } else if(obj.event === 'endorsement'){
                current_id = data.id;
                var editDemo = document.getElementById('endorsementTpl');



                var getTpl = editDemo.innerHTML;
                laytpl(getTpl).render(data, function(html) {
                    //自定页
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-demo', //样式类名
                        closeBtn: 1, //不显示关闭按钮0
                        area: ['600px', '500px'], //大小
                        title:"签注信息",
                        anim: 2,
                        shadeClose: true, //开启遮罩关闭
                        content: html,
                        cancel: function() {

                        },btn: ['保存', '关闭']
                        ,yes:function(){
                            console.log("===保存====");
                            var params = {
                                remarkInfo:$("#remarkInfo").val()
                                ,userId:data.userId
                                ,ableId:current_id

                            };
                            //先暂时用form表单传
                            /* var formData = new FormData();
                             formData.append("remarkInfo", $("#remarkInfo").val());
                             formData.append("userId", data.userId);
                             formData.append("ableId", current_id);*/


                            $.ajax({
                                url:_PATH+"/api/order/addRemark",
                                data:JSON.stringify(params),
                                //  contentType: false,//这里
                                // processData: false,//这两个一定设置为false
                                contentType: 'application/json',
                                dataType:'json',	//服务器返回json格式数据2
                                type:'POST',		//HTTP请求类型
                                success:function(_data){
                                    if(_data.success){
                                        layer.msg("添加成功", {icon: 1});

                                        endorsement_options.url = _PATH +'/api/order/remark/'+current_id
                                        table.reload("endorsement_dataCheck",endorsement_options);
                                        // layer.close(layer_index);
                                    }else{

                                        layer.msg(_data.msg, {icon: 2});
                                    }
                                },error:function(e){
                                    alert("网络错误")
                                }
                            });


                        }
                        ,btn2: function(){
                            layer.closeAll();
                        }
                    });
                    form.render();
                    endorsement_options.url = _PATH +'/api/order/remark/'+current_id
                    table.render(endorsement_options);
                });
            } else if(obj.event === 'journal'){
                current_id = data.id;
                var editDemo = document.getElementById('journal');



                var getTpl = editDemo.innerHTML;
                laytpl(getTpl).render(data, function(html) {
                    //自定页
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-demo', //样式类名
                        closeBtn: 1, //不显示关闭按钮0
                        area: ['600px', '500px'], //大小
                        title:"日志",
                        anim: 2,
                        shadeClose: true, //开启遮罩关闭
                        content: html,
                        cancel: function() {

                        },btn: [ '关闭']

                        ,btn2: function(){
                            layer.closeAll();
                        }
                    });
                    form.render();
                    $.ajax({
                        url:_PATH+"/api/order/findOrderDetails/"+data.externalOrderNo,


                        //contentType: 'application/json',
                        dataType:'json',	//服务器返回json格式数据2
                        type:'GET',		//HTTP请求类型
                        success:function(_data){
                            if(_data.success){
                                console.log(_data.obj);
                                var o = _data.obj;
                                if(o){
                                    $("#order_detail").html(" PNR： " + o.pnrNo + " / 无   ； "+o.pnrHclxStr+"； "+o.ddlxStr+"； 本地；  " + o.ifxf + "；   "+o.pnrHcglgjStr+"<br>\n" +
                                            "订单编号：" + o.ddbh + " ；  " + o.dsDeptId + "<br>\n" +
                                            "订单状态：" + o.ddzt + "；  订单当前所属营业部：" + o.dsDeptId + "； 调度时间：" + o.dsDateTime);
                                }
                            }else{
                                layer.msg(_data.msg, {icon: 2});
                            }
                        },error:function(e){
                            alert("网络错误")
                        }
                    });



                    journal_options.url = _PATH +'/api/order/operateLog/'+current_id
                    table.render(journal_options);
                });


            }else if(obj.event === 'return'){//抽回
                var params =data;
                var  layer_index = layer.load(0, {shade: [0.5, '#393D49'],title:"抽回中...."});
                $.ajax({
                    url:_PATH+"/api/order/withdrawOrder/"+data.id,
                    //data: JSON.stringify(params),
                    //contentType: 'application/json',
                    dataType:'json',	//服务器返回json格式数据2
                    type:'GET',		//HTTP请求类型
                    success:function(_data){
                        if(_data.success){

                            layer.msg('订单抽回回成功', {icon: 1});
                            table.reload('dataCheck',options);

                        }else{
                            layer.msg(_data.msg, {icon: 2});
                        }

                        layer.close(layer_index);
                    },error:function(e){
                        alert("网络错误")
                    }
                });

            }
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
        countOrderTypeNum();
        function countOrderTypeNum(){
            var params = {orderStatus:"已派单"};
            $.ajax({
                url:_PATH+"/api/order/countOrderTypeNum",
                data:JSON.stringify(params),
                // contentType: false,//form表单提交这里
                // processData: false,//form表单提交这两个一定设置为false
                contentType: 'application/json',
                dataType:'json',	//服务器返回json格式数据2
                type:'POST',		//HTTP请求类型
                success:function(_data){
                    if(_data.success){
                        $("#totalOrderNum").html(_data.obj.totalOrderNum);
                        $("#outOrderNum").html(_data.obj.outOrderNum);
                        $("#exitOrderNum").html(_data.obj.exitOrderNum);
                        $("#editOrderNum").html(_data.obj.editOrderNum);
                        $("#delayNum").html(_data.obj.delayNum);
                    }else{


                    }
                },error:function(e){

                }
            });
        }

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
    <#--{{# if(d.orderProperty == 0){ }}-->
    <#--<span class="layui-badge">新</span>-->
    <#--{{#  } }}-->
    <#--{{# if(d.orderProperty == 1){ }}-->
    <#--<span class="layui-badge">第一次退单</span>-->
    <#--{{#  } }}-->
    <#--{{# if(d.orderProperty == 2){ }}-->
    <#--<span class="layui-badge layui-bg-blue">第二次退单</span>-->
    <#--{{#  } }}-->
    <#--{{# if(d.orderProperty == 3){ }}-->
    <#--<span class="layui-badge layui-bg-blue">普</span>-->
    <#--{{#  } }}-->
    {{# if(d.orderProperty == "普"){ }}
    <span class="layui-badge layui-bg-blue" style="width: 44px;">{{d.orderProperty}}</span>
    {{#  } else { }}
    <span class="layui-badge" style="width: 44px;">{{d.orderProperty}}</span>
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
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">详情</a>
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="return">抽回</a>
    <a class="layui-btn layui-btn-xs" lay-event="endorsement">签注</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="journal">日志</a>
</script>
<!-- 第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<#--日志模板-->
<script id="journal" type="text/html">
    <div style="padding: 20px; background-color: #F2F2F2;">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <div class="layui-card-header">基本信息</div>
                    <div class="layui-card-body" id="order_detail">
                        PNR： JN8YRP / 无   ； 往返程； 普通订单； 本地；  现返；   国际<br>
                        订单编号：181024000031 ；  深航工贸 / 国际线上组 / G05180 黄虎  18-10-24 11:04 网店导单<br>
                        订单状态：已调度；  订单当前所属营业部：国际线上组； 调度时间：18-10-24 11:04
                    </div>
                </div>
            </div>
            <div class="layui-col-md12">
                <div class="layui-card">
                <#--<div class="layui-card-header">卡片面板</div>-->
                    <div class="layui-card-body">
                        <!-- 操作日志表格 -->
                        <div id="journalTable" lay-filter="journalFilter"></div>
                    </div>

                </div>
            </div>
        <#--<div class="layui-col-md12">-->
        <#--<div class="layui-card">-->
        <#--<div class="layui-card-header">标题</div>-->
        <#--<div class="layui-card-body">-->
        <#--内容-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        </div>
    </div>
</script>
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