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
    <#--<ul class="layui-tab-title">-->
        <#--<li class="layui-this">抢单规则</li>-->
        <#--<li>派单规则</li>-->
        <#--<li>绩效规则</li>-->

    <#--</ul>-->
    <#--<div class="layui-tab-content" style="height: 100px; border: #ece8e8 0.5px solid;">-->
        <#--待抢单-->
        <#--<div class="layui-tab-item layui-show">-->

            <#--<div class="layui-collapse" lay-filter="test"  lay-accordion>-->
            <#--<div class="layui-colla-item">-->
            <#--<h2 class="layui-colla-title" style="height: 17px;"></h2>-->
            <#--<div class="layui-colla-content">-->
        <div class="layui-btn-group">
            <div class="layui-inline" id="switchBtn">
                <button class="layui-btn layui-btn-primary layui-btn-sm" type="button" >抢单规则</button>
                <button class="layui-btn layui-btn-primary layui-btn-sm" type="button" >派单规则</button>
                <button class="layui-btn layui-btn-primary layui-btn-sm" type="button" >绩效规则</button>
            </div>

        </div>
        <hr class="layui-bg-blue">
            <form class="layui-form" action="">
                <div class="layui-form-item"  >
                    <#--<div class="layui-inline">-->
                        <#--<label class="layui-form-label">创建时间：</label>-->
                        <#--<div class="layui-input-inline orderTime">-->
                            <#--<input type="text" class="layui-input" id="test10" placeholder="" >-->
                        <#--</div>-->
                    <#--</div>-->
                        <blockquote class="layui-elem-quote">
                    <div class="layui-inline">
                        <#--<label class="layui-form-label">规则名称：</label>-->
                        <#--<div class="layui-input-inline" style="    margin-right: 23px;">-->
                            <#--<input type="tel" name="orderNo"  autocomplete="off" class="layui-input" >-->
                        <#--</div>-->
                            <label class="layui-form-label">规则类型</label>
                            <div class="layui-input-block">
                                <select name="type" lay-filter="type" style="height: 38px;">
                                    <option value="全部">全部</option>
                                    <option value="抢单池停留时间">抢单池停留时间</option>
                                    <option value="抢单成功确认超时时间">抢单成功确认超时时间</option>
                                    <option value="抢单处理超时时间" >抢单处理超时时间</option>
                                    <option value="订单可抢次数" >订单可抢次数</option>
                                    <option value="抢单可抽回次数" >抢单可抽回次数</option>
                                    <option value="派单池停留时间" >派单池停留时间</option>
                                    <option value="派单成功确认超时时间" >派单成功确认超时时间</option>
                                    <option value="派单处理超时时间" >派单处理超时时间</option>
                                    <option value="派单可派次数" >派单可派次数</option>
                                    <option value="派单可抽回次数" >派单可抽回次数</option>
                                    <option value="派单可退回次数" >派单可退回次数</option>
                                    <option value="派单地区规则" >派单地区规则</option>
                                    <option value="可同时处理订单数" >可同时处理订单数</option>
                                    <option value="是否可暂存" >是否可暂存</option>
                                </select>
                            </div>
                    </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">订单类型：</label>
                                <div class="layui-input-block select">
                                    <select name="orderType" lay-filter="aihao">
                                        <option value="" selected="">全部</option>
                                        <option value="出票" >出票</option>
                                        <option value="退票">退票</option>
                                        <option value="改期">改期</option>
                                        <option value="航延">航延</option>
                                    </select>
                                </div>
                            </div>
                        <div class="layui-inline">
                            <legend></legend>
                            <div class="layui-field-box" id="tips" style="color: red">

                            </div>
                        </div>
                        </blockquote>
                </div>


                <div class="layui-form-item">
                    <div class="layui-inline">
                        <button class="layui-btn layui-btn-primary layui-btn-radius" lay-submit lay-filter="formDemo" style="height: 27px;line-height: 27px;">查询</button>
                    </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-radius" type="reset" style="height: 27px;line-height: 27px;">重置</button>
                        </div>
                    <div class="layui-inline">
                        <button class="layui-btn layui-btn-radius" id="checkBtn" type="button" style="height: 27px;line-height: 27px;">检查规则配置</button>
                    </div>
                    <div class="layui-inline">
                        <button id="addBtn" class="layui-btn layui-btn-radius layui-btn-normal" type="button" style="height: 27px;line-height: 27px;">新增</button>
                    </div>
                </div>
            </form>
            <#--</div></div></div>-->
            <!-- 表格 -->
            <div id="dateTable" lay-filter="demo"></div>





        <#--</div>-->
        <#--<div class="layui-tab-item">内容2</div>-->
        <#--<div class="layui-tab-item">内容3</div>-->

    <#--</div>-->
</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui2/layui.js"></script>
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

        // you code ...
        //日期时间范围
        laydate.render({
            elem: '#test10'
            ,type: 'datetime'

            ,format:"yyyy-MM-ddTHH:mm:ss"
            ,done: function(value, date, endDate){
                console.log(value); //得到日期生成的值，如：2017-08-18
                console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
                //console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
                if(value!=""){

                    _startTime = (new Date());
                    _startTime .setFullYear(date.year,date.month-1,date.date);
                    _startTime.setHours(date.hours,date.minutes,date.seconds);
                    _startTime=_startTime.Format("yyyy-MM-dd HH:mm:ss");


                }else{
                    _startTime = null;

                }

            }
        });



        var options = {
            elem: '#dateTable'                  //指定原始表格元素选择器（推荐id选择器）
          //  , height: 500    //容器高度
            , cols: [[                  //标题栏
                {title: '序号',templet: '#indexTpl', width: 70}
                , {field: 'orderType', title: '订单类型', width: 90}
                , {field: 'type', title: '规则类型', width: 170}
                // , {field: 'ruleProperty', title: '规则属性', width: 98}
                , {field: 'value', title: '规则值', width: 90,templet: '#valueTpl'}
                , {field: 'unit', title: '规则单位', width: 90}
                , {field: 'priority', title: '优先权', width: 80}
                , {field: 'status', title: '状态', width: 70}
                , {field: 'createdAt', title: '创建时间', width: 158,templet: '#createdAtTpl'}
                , {field: 'updatedAt', title: '更新时间', width: 158,templet: '#updatedAtTpl'}
                , {field: 'description', title: '备注', width: 180}
                , {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#barOption'} //这里的toolbar值是模板元素的选择器
            ]]
            , id: 'dataCheck'
         //   , url:LAYUI_FILE_PATH+'/assets/json/rule_table.json'
            ,url:_PATH+"/api/rules/findRulePage"
           // , method: 'get'//'post'
            , method: 'post'
            ,contentType: 'application/json'
            , page:true


            , limits: [10, 20, 40, 100, 300]
            , limit: 10 //默认采用10
            , loading: true
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,where:{
                // "enable_": 0,
                // "endDate": "string",
                // "id": "string",
                // "ruleName": "string",
                // "ruleStatus": 0,
                // "ruleType": "抢单",
                // "sortBy": "string",
                // "sortDir": "asc",
                // "startDate": "string"
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
            options.where = data.field;

            if( options.where.type =="全部" ){
                options.where.type = null;
            }
            if( options.where.orderType =="全部" ||options.where.orderType =="" ){
                options.where.orderType = null;
            }

            if( _startTime&&_startTime!="" ){
                options.where.startDate=_startTime//.Format('yyyy-MM-dd HH:mm:ss');

            }
            table.reload('dataCheck',options);
            return false;
        });



        //----------------------------------------操作日志得表格--------------------------------------------------------
        var journal_options = {
            elem: '#journalTable'                  //指定原始表格元素选择器（推荐id选择器）
            //  , height: 500    //容器高度
            , cols: [[                  //标题栏

                 {field: 'a', title: '操作时间', width: 180}
                , {field: 'b', title: '操作类型', width: 120}
                , {field: 'c', title: '操作人', width: 120}
                , {field: 'd', title: '操作公司', width: 180}
                , {field: 'e', title: '操作部门', width: 180}
                , {field: 'f ', title: 'PNR状态', width: 120}

            ]]
            , id: 'journal_dataCheck'
           // , url:LAYUI_FILE_PATH+'/assets/json/journal_table.json'// '/api/order/list'
            ,url:"/api/rule/list"
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

        //----------------------------------------签注表格--------------------------------------------------------
        var endorsement_options = {
            elem: '#endorsementTable'                  //指定原始表格元素选择器（推荐id选择器）
            //  , height: 500    //容器高度
            , cols: [[                  //标题栏
                {title: '序号',templet: '#indexTpl'}
               , {field: 'a', title: '签注时间', width: 170}
                , {field: 'b', title: '签注人', width: 80}
                , {field: 'c', title: '签注内容', width: 120}


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





        //监听工具条
        table.on('tool(demo)', function(obj){
            var data = obj.data;
            if(obj.event === 'detail'){
                //详情跳转到差旅平台

              //  layer.msg('ID：'+ data.id + ' 的查看操作');
                var editDemo = document.getElementById('editTpl');


               // var data = {ruleName:"", ruleValue:"" ,ruleProperty:"" ,sort:""};
                var getTpl = editDemo.innerHTML;
                laytpl(getTpl).render(data, function(html) {
                    //自定页
                    var layer_index = layer.open({
                        type: 1,
                        skin: 'layui-layer-demo', //样式类名
                        closeBtn: 1, //不显示关闭按钮0
                        area: ['500px', '360px'], //大小
                        title:"编辑规则",
                        anim: 2,
                        shadeClose: true, //开启遮罩关闭
                        content: html,
                        cancel: function() {
                            console.log("取消");
                        },btn: ['保存', '取消']

                        //保存
                        ,btn1:function(){


                        },done:function(){

                        }
                        ,yes: function(){
                            console.log("编辑");
                            var params = {
                                id:data.id,
                                ruleName:$("#editForm input[name='ruleName']").val(), description:$("#editForm input[name='description']").val(),
                                priority :$("#editForm input[name='priority']").val() ,unit:$("#editForm input[name='unit']").val()
                                // , ruleStatus:$("#editForm input[name='ruleType']").val(),ruleStatus:$("#editForm input[name='ruleType']").val()
                                ,type:$("#editForm select[name='type']").find("option:selected").text()
                                ,value:$("#editForm input[name='value']").val()//
                                ,status :$("#editForm input[name='status'][checked]").val()
                                // ,timeOutMinutes:$("#editForm input[name='timeOutMinutes']").val()
                                ,orderType:$("#editForm select[name='orderType']").find("option:selected").text()
                                // ,sendBackNum:$("#editForm input[name='sendBackNum']").val()
                            };

                            if(params.type=="派单地区规则"){
                                params.value =parseInt($("#editForm select[name='areaSelect']").find("option:selected").val());
                                params.priority = $("#editForm input[name='priority']").val();
                            }

                            $.ajax({
                                url:_PATH+"/api/rules/updateRule",
                                data: JSON.stringify(params),
                                contentType: 'application/json',
                                dataType:'json',	//服务器返回json格式数据2
                                type:'POST',		//HTTP请求类型
                                success:function(_data){
                                    if(_data.success ){
                                        layer.msg("更新规则成功", {icon: 1});
                                        table.reload("dataCheck",options);
                                        layer.close(layer_index);
                                    }else{
                                        layer.msg(_data.msg, {icon: 2});
                                    }
                                    checkRulesProblems();
                                },error:function(e){
                                    alert("网络错误")
                                }
                            });


                        }
                    });
                    if( data.type=="派单地区规则" ){

                        $("#ruleUnit").html("");
                        $("#areaSelect").show();
                        $("#ruleName").hide();
                        $("#priority").show();
                    }
                    form.render();

                });


          //  };


            }else if( obj.event === 'endorsement' ){//删除
                //询问框

                layer.confirm('请再次确认？', {
                    btn: ['确定删除','取消'] //按钮
                }, function(){
                  //  layer.msg('的确很重要', {icon: 1});
                    $.ajax({
                        url:_PATH+"/api/rules/deleteRule/"+data.id,

                        // contentType: 'application/json',
                        // dataType:'json',	//服务器返回json格式数据2
                        type:'GET',		//HTTP请求类型
                        success:function(_data){
                            if(_data.success ){
                                layer.msg("删除规则成功", {icon: 2});
                                table.reload("dataCheck",options);
                            }else
                            {
                                layer.msg(_data.msg, {icon: 4});
                            }
                            checkRulesProblems();
                        },error:function(e){
                            alert("网络错误")
                        }
                    });
                }, function(){
                    // layer.msg('放弃操作', {
                    //     time: 20000, //20s后自动关闭
                    //     btn: ['明白了', '知道了']
                    // });
                });
            }else if(obj.event === 'journal'){


                var params = data;

                if(data.ruleStatus=="停用"){
                    params.ruleStatus = "启用"
                }else if(data.ruleStatus=="启用"){
                    params.ruleStatus="停用"
                }
                params.ruleId = params.id;
                $.ajax({
                    url:_PATH+"/api/rules/changeRuleStatus",
                    // data: JSON.stringify(params),
                    data:{ruleId:params.id},
                    // contentType: 'application/json',
                    dataType:'json',	//服务器返回json格式数据2
                    type:'POST',		//HTTP请求类型
                    success:function(_data){
                        if(_data.success ){
                            // layer.msg("更新规则成功", {icon: 1});
                            table.reload("dataCheck",options);
                        }else
                            {
                                layer.msg(_data.msg, {icon: 2});
                            }
                    },error:function(e){
                        alert("网络错误")
                    }
                });

            }
        });


        //-------------------------------------新增规则---------------------------------------------------
        $("#addBtn").click(function(){
            var editDemo = document.getElementById('editTpl');


            var data = {orderType:"", status:"启用" ,type :"" ,priority :"",orderType:"",unit:"分钟",description:"",value:""};
            var getTpl = editDemo.innerHTML;
            laytpl(getTpl).render(data, function(html) {
                //自定页
                var  layer_index =  layer.open({
                    type: 1,
                    skin: 'layui-layer-demo', //样式类名
                    closeBtn: 1, //不显示关闭按钮0
                    area: ['500px', '360px'], //大小
                    title:"新增规则",
                    anim: 2,
                    shadeClose: true, //开启遮罩关闭
                    content: html,
                    cancel: function() {
                        console.log("取消");
                    },btn: ['保存', '取消']

                    //保存
                    ,btn1:function(){


                    }
                    ,yes: function(){
                       console.log("保存");
                       var params = {ruleName:$("#editForm input[name='ruleName']").val(), description:$("#editForm input[name='description']").val(),
                           priority :$("#editForm input[name='priority']").val() ,unit:$("#editForm input[name='unit']").val()
                         // , ruleStatus:$("#editForm input[name='ruleType']").val(),ruleStatus:$("#editForm input[name='ruleType']").val()
                           ,type:$("#editForm select[name='type']").find("option:selected").text()
                           ,value:$("#editForm input[name='value']").val()//
                           ,status :$("#editForm input[name='status'][checked]").val()
                           // ,timeOutMinutes:$("#editForm input[name='timeOutMinutes']").val()
                           ,orderType:$("#editForm select[name='orderType']").find("option:selected").text()
                           // ,sendBackNum:$("#editForm input[name='sendBackNum']").val()
                    };

                       if(params.type=="派单地区规则"){
                           params.value =parseInt($("#editForm select[name='areaSelect']").find("option:selected").val());
                            params.priority = $("#editForm input[name='priority']").val();
                       }

                       /*{
                        "createBy": "string",
                                "createDate": "2018-10-25T07:09:19.436Z",
                                "enable_": 0,
                                "id": "string",
                                "lastUpdateBy": "string",
                                "lastUpdateDate": "2018-10-25T07:09:19.436Z",
                                "orderType": "出票",
                                "remake": "string",
                                "ruleName": "string",
                                "ruleProperty": "string",
                                "ruleStatus": "停用",
                                "ruleType": "抢单",
                                "ruleValue": 0,
                                "sendBackNum": 0,
                                "sort": 0,
                                "timeOutMinutes": 0
                    }*/

                        $.ajax({
                            url:_PATH+"/api/rules/addRule",
                            data: JSON.stringify(params),
                            contentType: 'application/json',
                            dataType:'json',	//服务器返回json格式数据2
                            type:'POST',		//HTTP请求类型
                            success:function(data){
                                if(data.success){
                                    layer.msg("插入规则成功", {icon: 1});
                                    table.reload("dataCheck",options);
                                    layer.close(layer_index);
                                }else{

                                    layer.msg(data.msg, {icon: 2});
                                }
                                checkRulesProblems();
                            },error:function(e){
                                alert("网络错误")
                            }
                        });


                    }
                });
                form.render();

            });


        });



        $("#switchBtn button").eq(0).css("color","red");
        $("#switchBtn").click(function(e){

            var flag = $(e.target).html();
            var url = "";
            if(flag=="待抢单"){
                //调用父页面index.js添加菜单方法

            }else if(flag=="待派单") {


            }


        });



        form.on("select(ruleType)",function(e){
            console.log(e);
            if(e.value.indexOf("时间")!=-1||e.value == "是否可暂存"){
                $("#ruleUnit").html("分钟");
                $("#areaSelect").hide();
                $("#priority").hide();
                $("#ruleName").show();
            }else if( e.value == "派单地区规则" ){
                $("#ruleUnit").html("");
                $("#areaSelect").show();
                $("#priority").show();
                $("#ruleName").hide();
            }else{
                $("#ruleUnit").html("次数");
                $("#areaSelect").hide();
                $("#priority").hide();
                $("#ruleName").show();
            }
            form.render();

        });

        //--------------核验规则是否符合------------------------------------
        $("#checkBtn").click(function(){

            checkRulesProblems();
        });
        checkRulesProblems();
            function checkRulesProblems(){

                $.ajax({
                    url:_PATH+"/api/rules/checkRulesProblems",

                    // contentType: 'application/json',
                    // dataType:'json',	//服务器返回json格式数据2
                    type:'GET',		//HTTP请求类型
                    success:function(_data){
                        if(_data.success ){

                            var tip = "";
                            for( var i=0;i<_data.obj.length;i++ ){
                                tip+= _data.obj[i]+"<br>";

                            }
                          //  layer.msg(tip, {icon: 1});
                            $("#tips").html(tip);
                        }else
                        {
                           // layer.msg(_data.msg, {icon: 2});
                        }
                    },error:function(e){
                        alert("网络错误")
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
    {{# if(d.orderProperty =='新'){ }}

    {{#  } else { }}

    {{#  } }}

</script>
<!-- 表格操作按钮集 -->
<script type="text/html" id="barOption">
    <#--<a class="layui-btn layui-btn-mini" lay-event="detail">详情</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-normal" lay-event="edit">签注</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-danger" lay-event="del">日志</a>-->
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">编辑</a>
    {{# if(d.status =='停用'){ }}
    <a class="layui-btn layui-btn-xs" lay-event="endorsement">删除</a>
    {{#  } }}
    {{# if(d.status =='停用'){ }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="journal">有效</a>
    {{#  } else { }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="journal" disabled>失效</a>
    {{#  } }}

</script>
<#--表格里面的时间格式化-->
<script  type="text/html" id="createdAtTpl">
    {{#
    var fn = function(){
        if(d.createdAt)
            return (new Date(d.createdAt)).Format('yyyy-MM-dd HH:mm:ss');
        else
            return ""
    };
    }}
    {{ fn() }}
</script>
<script  type="text/html" id="updatedAtTpl">
{{#
var fn = function(){
if(d.updatedAt)
return (new Date(d.updatedAt)).Format('yyyy-MM-dd HH:mm:ss');
else
return ""
};
}}
{{ fn() }}
</script>
<script  type="text/html" id="valueTpl">
    {{# if(d.type =='派单地区规则'){ }}
        {{# if(d.value =='1'){ }}
                 客户所在地
         {{#  } }}
        {{# if(d.value =='2'){ }}
        始发地城市
        {{#  } }}
        {{# if(d.value =='3'){ }}
        目的地城市
        {{#  } }}
    {{#  } else { }}
    {{d.value}}
    {{#  } }}
    </script>
<script id="editTpl" type="text/html">
    <form class="layui-form layui-form-pane" id="editForm" action="">
        <#--<div class="layui-form-item" style="margin-bottom: 5px;">-->
            <#--<label class="layui-form-label">规则名称</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="ruleName" autocomplete="off" placeholder="请输入规则名称" value="{{d.ruleName}}" class="layui-input" style="height: 38px;">-->
            <#--</div>-->
        <#--</div>-->
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">规则类型</label>
            <div class="layui-input-inline">
                <select name="type" lay-filter="ruleType" >

                    <option value="抢单池停留时间" {{d.type=="抢单池停留时间"?"selected=''":""}}>抢单池停留时间</option>
                    <option value="抢单成功确认超时时间" {{d.type=="抢单成功确认超时时间"?"selected=''":""}}>抢单成功确认超时时间</option>
                    <option value="抢单处理超时时间"  {{d.type=="抢单处理超时时间"?"selected=''":""}}>抢单处理超时时间</option>
                    <option value="订单可抢次数"  {{d.type=="订单可抢次数"?"selected=''":""}}>订单可抢次数</option>
                    <option value="抢单可抽回次数"  {{d.type=="抢单可抽回次数"?"selected=''":""}}>抢单可抽回次数</option>
                    <option value="派单池停留时间"  {{d.type=="派单池停留时间"?"selected=''":""}}>派单池停留时间</option>
                    <option value="派单成功确认超时时间"  {{d.type=="派单成功确认超时时间"?"selected=''":""}}>派单成功确认超时时间</option>
                    <option value="派单处理超时时间" {{d.type=="派单处理超时时间"?"selected=''":""}}>派单处理超时时间</option>
                    <option value="派单可派次数" {{d.type=="派单可派次数"?"selected=''":""}}>派单可派次数</option>
                    <option value="派单可抽回次数" {{d.type=="派单可抽回次数"?"selected=''":""}}>派单可抽回次数</option>
                    <option value="派单可退回次数" {{d.type=="派单可退回次数"?"selected=''":""}}>派单可退回次数</option>
                    <option value="派单地区规则" {{d.type=="派单地区规则"?"selected=''":""}}>派单地区规则</option>
                    <option value="可同时处理订单数" {{d.type=="可同时处理订单数"?"selected=''":""}}>可同时处理订单数</option>
                    <option value="是否可暂存" {{d.type=="是否可暂存"?"selected=''":""}}>是否可暂存</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">订单类型</label>
            <div class="layui-input-inline">
                <select name="orderType" lay-filter="orderType" style="height: 38px;">
                    <option value="出票" {{d.orderType=="出票"?"selected=''":""}}>出票</option>
                    <option value="退票" {{d.orderType=="退票"?"selected=''":""}}>退票</option>
                    <option value="改期" {{d.orderType=="改期"?"selected=''":""}}>改期</option>
                    <option value="航延" {{d.orderType=="航延"?"selected=''":""}}>航延</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <div class="layui-inline">
            <label class="layui-form-label">规则值</label>
            <div class="layui-input-inline" id="ruleName">
                <input  type="text" name="value" autocomplete="off" placeholder="请输入规则值" value="{{d.value}}"  class="layui-input" style="height: 38px;">


            </div>
                <div class="layui-input-inline" id="areaSelect" style="display: none">
                    <select  name="areaSelect" lay-filter="value" style="height: 38px;">
                        <option value="1" {{d.value=="1"?"selected=''":""}}>客户所在地</option>
                        <option value="2" {{d.value=="2"?"selected=''":""}}>始发地城市</option>
                        <option value="3" {{d.value=="3"?"selected=''":""}}>目的地城市</option>
                    </select>
                </div>
                <label id="ruleUnit" class="layui-form-label">{{d.unit||""}}</label>
                <#--<div class="layui-input-inline">-->
                    <#--<input type="text" name="unit" autocomplete="off" placeholder="" value="{{d.unit||""}}" class="layui-input" style="height: 38px;">-->
                <#--</div>-->
            </div>
        </div>
            <#--<div class="layui-form-item">-->
                <#--<label class="layui-form-label">规则值单位</label>-->
                <#--<div class="layui-input-block">-->

                <#--</div>-->
            <#--</div>-->
        <#--<div class="layui-form-item" style="margin-bottom: 5px;">-->
            <#--<label class="layui-form-label">规则属性</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="ruleProperty" autocomplete="off" placeholder="规则名称首字母大写" value="{{d.ruleProperty}}" class="layui-input" style="height: 38px;">-->
            <#--</div>-->
        <#--</div>-->
        <div class="layui-form-item" id="priority" style="display: none">
            <label class="layui-form-label">优先权</label>
            <div class="layui-input-inline">
                <input type="text" name="priority" autocomplete="off" placeholder="只能输入数字" value="{{d.priority}}" class="layui-input" style="height: 38px;" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </div>
        </div>
        <div class="layui-form-item" >
            <label class="layui-form-label">是否有效</label>
            <div class="layui-input-inline">
                {{# if(d.status  =='启用' ){ }}
                <input type="radio" name="status" value="启用" title="有效" checked="">
                <input type="radio" name="status" value="停用" title="失效" disabled>
                {{#  } }}
                {{# if(d.status  !='启用'  && d.type!="派单地区规则"){ }}
                <input type="radio" name="status" value="启用" title="有效" disabled>
                <input type="radio" name="status" value="停用" title="失效" checked="" disabled>
                {{#  } }}

                {{# if(d.status  !='启用'  && d.type=="派单地区规则"){ }}
                    <input type="radio" name="status" value="启用" title="有效" >
                    <input type="radio" name="status" value="停用" title="失效" checked="" disabled>
                {{#  } }}

            </div>
        </div>

        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">订单退回次数</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="sendBackNum" autocomplete="off" placeholder="" value="{{d.sendBackNum}}" class="layui-input" style="height: 38px;">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">抽回次数 </label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="pumpBackTimes" autocomplete="off" placeholder="" value="{{d.pumpBackTimes||""}}" class="layui-input" style="height: 38px;">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="layui-form-item">-->
            <#--<label class="layui-form-label">超时时间</label>-->
            <#--<div class="layui-input-block">-->
                <#--<input type="text" name="timeOutMinutes" autocomplete="off" placeholder="" value="{{d.timeOutMinutes}}" class="layui-input" style="height: 38px;">-->
            <#--</div>-->
        <#--</div>-->
        <div class="layui-form-item">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block">
                <input type="text" name="description" autocomplete="off" placeholder="" value="{{d.description}}" class="layui-input" style="height: 38px;">
            </div>
        </div>
    </form>
    </script>
</body>
</html>