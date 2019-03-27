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

</script>
<div class="layui-tab">
    <ul class="layui-tab-title">
        <li class="layui-this">抢单规则</li>
        <li>派单规则</li>
        <li>绩效规则</li>

    </ul>
    <div class="layui-tab-content" style="height: 100px; border: #ece8e8 0.5px solid;">
        <#--待抢单-->
        <div class="layui-tab-item layui-show">

            <#--<div class="layui-collapse" lay-filter="test"  lay-accordion>-->
            <#--<div class="layui-colla-item">-->
            <#--<h2 class="layui-colla-title" style="height: 17px;"></h2>-->
            <#--<div class="layui-colla-content">-->

            <form class="layui-form" action="">
                <div class="layui-form-item"  >
                    <div class="layui-inline">
                        <label class="layui-form-label">创建时间：</label>
                        <div class="layui-input-inline orderTime">
                            <input type="text" class="layui-input" id="test10" placeholder="" name="date">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">规则名称：</label>
                        <div class="layui-input-inline" style="    margin-right: 23px;">
                            <input type="tel" name="orderNo"  autocomplete="off" class="layui-input" >
                        </div>
                    </div>

                </div>


                <div class="layui-form-item">
                    <div class="layui-inline">
                        <button class="layui-btn layui-btn-primary layui-btn-radius" lay-submit lay-filter="formDemo" style="height: 27px;line-height: 27px;">查询</button>
                    </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-radius" type="reset" style="height: 27px;line-height: 27px;">重置</button>
                        </div>
                    <div class="layui-inline">
                        <button id="addBtn" class="layui-btn layui-btn-radius layui-btn-normal" type="button" style="height: 27px;line-height: 27px;">新增</button>
                    </div>
                </div>
            </form>
            <#--</div></div></div>-->
            <!-- 表格 -->
            <div id="dateTable" lay-filter="demo"></div>





        </div>
        <div class="layui-tab-item">内容2</div>
        <div class="layui-tab-item">内容3</div>

    </div>
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


            }
        });



        var options = {
            elem: '#dateTable'                  //指定原始表格元素选择器（推荐id选择器）
          //  , height: 500    //容器高度
            , cols: [[                  //标题栏
                {title: '序号',templet: '#indexTpl'}
                , {field: 'ruleName', title: '规则名称', width: 98}
                , {field: 'ruleType', title: '规则类型', width: 98}
                , {field: 'ruleProperty', title: '规则属性', width: 98}
                , {field: 'ruleValue', title: '规则值', width: 70}
                , {field: 'sort', title: '排序', width: 70}
                , {field: 'ruleStatus', title: '状态', width: 70}
                , {field: 'createDate', title: '创建时间', width: 120,templet: '#timeTpl'}
                , {field: 'remake', title: '备注', width: 180}
                , {fixed: 'right', title: '操作', width: 180, align: 'center', toolbar: '#barOption'} //这里的toolbar值是模板元素的选择器
            ]]
            , id: 'dataCheck'
         //   , url:LAYUI_FILE_PATH+'/assets/json/rule_table.json'
            ,url:"/api/rule/list"
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
                 "ruleType": "派单",
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


                var data = {ruleName:"", ruleValue:"" ,ruleProperty:"" ,sort:""};
                var getTpl = editDemo.innerHTML;
                laytpl(getTpl).render(data, function(html) {
                    //自定页
                    layer.open({
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
                        ,btn2: function(){
                            console.log("编辑");
                            var params = {ruleName:$("#editForm input[name='ruleName']").val(), ruleValue:$("#editForm input[name='ruleValue']").val(),
                                ruleProperty:$("#editForm input[name='ruleProperty']").val() ,sort:$("#editForm input[name='sort']").val(),
                                ruleStatus:$("#editForm input[name='ruleType']").val(),ruleStatus:$("#editForm input[name='ruleType']").val()
                            };

                            $.ajax({
                                url:"/api/rule/updateRule",
                                data:params,
                                dataType:'json',	//服务器返回json格式数据
                                type:'POST',		//HTTP请求类型
                                success:function(data){
                                    if(data.code == 200){

                                    }
                                },error:function(e){
                                    alert("网络错误")
                                }
                            });


                        }
                    });
                    form.render();

                });


          //  };


            }
            else if(obj.event === 'journal'){
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
                    table.render(journal_options);
                });

            }
        });


        //-------------------------------------新增规则---------------------------------------------------
        $("#addBtn").click(function(){
            var editDemo = document.getElementById('editTpl');


            var data = {ruleName:"", ruleValue:"" ,ruleProperty:"" ,sort:""};
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
                       var params = {ruleName:$("#editForm input[name='ruleName']").val(), ruleValue:$("#editForm input[name='ruleValue']").val(),
                           ruleProperty:$("#editForm input[name='ruleProperty']").val() ,sort:$("#editForm input[name='sort']").val()
                         // , ruleStatus:$("#editForm input[name='ruleType']").val(),ruleStatus:$("#editForm input[name='ruleType']").val()
                           ,ruleType:$("#editForm select[name='ruleType']").find("option:selected").text()
                           ,ruleStatus:$("#editForm input[name='ruleStatus'][checked]").val()
                    };
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
                            url:"/api/rule/addRule",
                            data: JSON.stringify(params),
                            contentType: 'application/json',
                            dataType:'json',	//服务器返回json格式数据2
                            type:'POST',		//HTTP请求类型
                            success:function(data){
                                if(data.success){
                                    layer.msg("插入规则成功", {icon: 1});
                                    table.reload(options);
                                    layer.close(layer_index);
                                }else{

                                    layer.msg(data.msg, {icon: 2});
                                }
                            },error:function(e){
                                alert("网络错误")
                            }
                        });


                    }
                });
                form.render();

            });


        });

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
    <a class="layui-btn layui-btn-xs" lay-event="endorsement">删除</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="journal">有效</a>
</script>
<#--表格里面的时间格式化-->
<script  type="text/html" id="timeTpl">

    {{#
    var fn = function(){
        if(d.timeTpl)
            return (new Date(d.timeTpl)).Format('yyyy-MM-dd HH:mm:ss');
        else
            return ""
    };
    }}
    {{ fn() }}
</script>


<script id="editTpl" type="text/html">
    <form class="layui-form layui-form-pane" id="editForm" action="">
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">规则名称</label>
            <div class="layui-input-block">
                <input type="text" name="ruleName" autocomplete="off" placeholder="请输入规则名称" value="{{d.ruleName}}" class="layui-input" style="height: 38px;">
            </div>
        </div>
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">规则类型</label>
            <div class="layui-input-block">
                <select name="ruleType" lay-filter="ruleType" style="height: 38px;">
                    <option value=""></option>
                    <option value="0"></option>
                    <option value="1" selected="">抢单</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">规则值</label>
            <div class="layui-input-block">
                <input type="text" name="ruleValue" autocomplete="off" placeholder="请输入规则值" value="{{d.ruleValue}}"  class="layui-input" style="height: 38px;">
            </div>
        </div>
        <div class="layui-form-item" style="margin-bottom: 5px;">
            <label class="layui-form-label">规则属性</label>
            <div class="layui-input-block">
                <input type="text" name="ruleProperty" autocomplete="off" placeholder="规则名称首字母大写" value="{{d.ruleProperty}}" class="layui-input" style="height: 38px;">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-block">
                <input type="text" name="sort" autocomplete="off" placeholder="规则名称首字母大写" value="{{d.sort}}" class="layui-input" style="height: 38px;">
            </div>
        </div>
        <div class="layui-form-item" pane="">
            <label class="layui-form-label">是否有效</label>
            <div class="layui-input-block">
                <input type="radio" name="ruleStatus" value="1" title="有效" checked="">
                <input type="radio" name="ruleStatus" value="0" title="失效">
            </div>
        </div>
        </form>
    </script>
</body>
</html>