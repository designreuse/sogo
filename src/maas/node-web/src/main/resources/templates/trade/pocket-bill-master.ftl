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
        <li class="layui-this">待抢单</li>
        <li>待派单</li>
        <li>已抢单</li>
        <li>已派单</li>
        <li>完成单</li>
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
                        <label class="layui-form-label">进单时间：</label>
                        <div class="layui-input-inline orderTime">
                            <input type="text" class="layui-input" id="test10" placeholder="时间区间" name="date">
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">订单号：</label>
                        <div class="layui-input-inline" style="    margin-right: 23px;">
                            <input type="tel" name="orderNo"  autocomplete="off" class="layui-input" >
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">订单类型：</label>
                        <div class="layui-input-block select">
                            <select name="orderType" lay-filter="aihao">
                                <option value=""></option>
                                <option value="0" selected="">出票</option>
                                <option value="1"></option>
                                <option value="2"></option>
                                <option value="3"></option>
                                <option value="4"></option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline" style="width: 30px;">
                        <button class="layui-btn layui-btn-xs layui-btn-disabled" style="display: none" type="button" id="showBtn"> 更多<i class="layui-icon">&#xe65b;</i></button>
                    </div>
                </div>
                <div class="layui-form-item" id="secondRow">
                    <div class="layui-inline"  style="margin-right:21px">
                        <label class="layui-form-label">订单类别：</label>
                        <div class="layui-input-block select">
                            <select name="orderSort" lay-filter="aihao">
                                <option value=""></option>
                                <option value="0" selected="">机票</option>
                                <option value="1" ></option>
                                <option value="2"></option>
                                <option value="3"></option>
                                <option value="4"></option>
                            </select>
                        </div>
                    </div>
                        <div class="layui-inline">
                            <label class="layui-form-label" >订单来源：</label>
                            <div class="layui-input-block select">
                                <select name="orderSource" lay-filter="aihao">
                                    <option value=""></option>
                                    <option value="0" selected="">差旅系统</option>
                                    <option value="1" ></option>
                                    <option value="2"></option>
                                    <option value="3"></option>
                                    <option value="4"></option>
                                </select>
                            </div>
                    </div>
                        <div class="layui-inline">
                            <label class="layui-form-label" style="width: 90px;">外部订单号：</label>
                            <div class="layui-input-inline">
                                <input type="tel" name="externalOrderNo"  autocomplete="off" class="layui-input">
                            </div>
                        </div>
                    <div class="layui-inline" style="width: 30px;">
                        <button class="layui-btn layui-btn-xs layui-btn-disabled" type="button" id="hideBtn"><i class="layui-icon">&#xe65a;</i> 隐藏</button>
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
                        <label class="layui-form-label" style="width: 700px;">总订单：<span style="color: red;">0</span>单，出票：<span style="color: red;">0</span>单，退票：<span style="color: red;">0</span>单，改期：<span style="color: red;">0</span>单，航延：<span style="color: red;">0</span>单</label>
                    </div>
                </div>
            </form>
            <#--</div></div></div>-->
            <!-- 表格 -->
            <div id="dateTable" lay-filter="demo"></div>





        </div>
        <div class="layui-tab-item">内容2</div>
        <div class="layui-tab-item">内容3</div>
        <div class="layui-tab-item">内容4</div>
        <div class="layui-tab-item">内容5</div>
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
            ,range: true
            ,format:"yyyy-MM-ddTHH:mm:ss"
            ,done: function(value, date, endDate){
                console.log(value); //得到日期生成的值，如：2017-08-18
                console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
                console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。


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
                {title: '序号',templet: '#indexTpl'}
                , {field: 'orderProperty', title: '订单属性', width: 86,templet: '#orderTpl'}

                , {field: 'grabSingleTime', title: '抢单倒计时', width: 120}
                , {field: 'externalOrderNo', title: '外部订单号', width: 120}
                , {field: 'orderSource', title: '订单来源', width: 180}
                , {field: 'orderNo', title: '订单号', width: 180}
                , {field: 'orderType', title: '订单类型', width: 180}
                , {field: 'orderSort ', title: '订单类别', width: 120}
                , {field: 'orderEntranceTime', title: '进单时间', width: 180,templet:"#orderEntranceTimeTpl"}
                , {fixed: 'right', title: '操作', width: 210, align: 'center', toolbar: '#barOption'} //这里的toolbar值是模板元素的选择器
            ]]
            , id: 'dataCheck'
            , url:LAYUI_FILE_PATH+'/assets/json/order_table.json'// '/api/order/list'
            , method: 'get'//'post'
            //,contentType: 'application/json'
            , page:true// {first:0,curr:0}


            , limits: [10, 20, 40, 100, 300]
            , limit: 10 //默认采用30
            , loading: true
            ,request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
            }
            ,where:{
                // "endDate": "",
                // "externalOrderNo": $("input[name='externalOrderNo ']").val(),
                // "id": "",
                // "limit": 20,
                //  "orderEntranceTime": "2018-10-22T07:34:46.725Z",
                // "orderNo": $("input[name='orderNo']").val(),
                // "orderSort": $("select[name='orderSort']").val(),
                // "orderSource":  $("select[name='orderSource']").val(),
                // "orderStatus": "0",
                // "orderType": "1",//$("select[name='orderType']").val(),
               // "page": 0,
               // "pageSize": 10,
               //  "sortBy": "",
               //  "sortDir": "asc",
               //  "start": 0,
               //  "startDate": ""
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
                , {field: 'remarkDate', title: '签注时间', width: 170}
                , {field: 'userName', title: '签注人', width: 80}
                , {field: 'remarkInfo', title: '签注内容', width: 120}


            ]]
            , id: 'endorsement_dataCheck'
            //, url:LAYUI_FILE_PATH+'/assets/json/endorsement_table.json'// '/api/order/list'
            ,url:_PATH +'/api/order/remark/'+current_id
            , method: 'get'//'post
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

                layer.msg('ID：'+ data.id + ' 的查看操作');

                window.open("http://14.21.67.172:8088/asms/ticket/order/zcd/asmslticketorderzcd/detail?ddbh=181024000032&mkbh=404577&cgqx_gn=15&cgqx_gj=17&xsqx=16&open_czfrom=");

            } else if(obj.event === 'endorsement'){
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

                        ,btn2: function(){
                            layer.closeAll();
                        }
                    });
                    form.render();
                    table.render(endorsement_options);
                });
            } else if(obj.event === 'journal'){
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



    });
</script>
<script type="text/html" id="indexTpl">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script  type="text/html" id="orderTpl">
    {{# if(d.orderProperty =='新'){ }}
        <span class="layui-badge">{{d.orderProperty}}</span>
    {{#  } else { }}
        <span class="layui-badge layui-bg-blue">{{d.orderProperty}}</span>
    {{#  } }}

</script>
<!-- 表格操作按钮集 -->
<script type="text/html" id="barOption">
    <#--<a class="layui-btn layui-btn-mini" lay-event="detail">详情</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-normal" lay-event="edit">签注</a>-->
    <#--<a class="layui-btn layui-btn-mini layui-btn-danger" lay-event="del">日志</a>-->
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">详情</a>
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">派单</a>
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
                    <div class="layui-card-body">
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
                        <textarea name="desc" placeholder="输入签注信息" class="layui-textarea"></textarea>
                    </div>
                </div>

            </div>
        </div>

    </div>
    </script>
</body>
</html>