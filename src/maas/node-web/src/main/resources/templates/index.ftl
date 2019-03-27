<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>一体化调度网点运营平台</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body>
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
    var _PATH = "${Session.basePath}";
    var username = "${Session.user.username}";
</script>

<!-- layout admin -->
<div class="layui-layout layui-layout-admin"> <!-- 添加skin-1类可手动修改主题为纯白，添加skin-2类可手动修改主题为蓝白 -->
    <!-- header -->
    <div class="layui-header my-header">

        <a href="">
            <img src="${Session.basePath}/assets/image/logo_new.png" style="width: 100px;margin-top: 15px;" >
            <!--<img class="my-header-logo" src="" alt="logo">-->
            <div class="my-header-logo" style="color: #e2cd2d;">一体化调度平台</div>
        </a>
        <#--<div class="my-header-btn">-->
            <#--<button class="layui-btn layui-btn-small btn-nav"><i class="layui-icon">&#xe65f;</i></button>-->
        <#--</div>-->

        <!-- 顶部左侧添加选项卡监听 -->
        <ul class="layui-nav" lay-filter="side-top-left">
            <!--<li class="layui-nav-item"><a href="javascript:;" href-url="demo/btn.ftl"><i class="layui-icon">&#xe621;</i>按钮</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;"><i class="layui-icon">&#xe621;</i>基础</a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" href-url="demo/btn.ftl"><i class="layui-icon">&#xe621;</i>按钮</a></dd>
                    <dd><a href="javascript:;" href-url="demo/form.ftl"><i class="layui-icon">&#xe621;</i>表单</a></dd>
                </dl>
            </li>-->
        </ul>

        <!-- 顶部右侧添加选项卡监听 -->
        <ul class="layui-nav my-header-user-nav" lay-filter="side-top-right">
            <!--<li class="layui-nav-item"><a href="javascript:;" class="pay" href-url="">支持作者</a></li>-->
            <#--<li class="layui-nav-item">-->
                <#--<a class="name" href="javascript:;"><i class="layui-icon">&#xe629;</i>主题</a>-->
                <#--<dl class="layui-nav-child">-->
                    <#--<dd data-skin="0"><a href="javascript:;">默认</a></dd>-->
                    <#--<dd data-skin="1"><a href="javascript:;">纯白</a></dd>-->
                    <#--<dd data-skin="2"><a href="javascript:;">蓝白</a></dd>-->
                <#--</dl>-->
            <#--</li>-->
            <li class="layui-nav-item">
                <a class="name" href="javascript:;"  style="color: #e2cd2d;"><span id="username">&nbsp;&nbsp; ${Session.user.username}</span></a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" id="onlineBtn"><i class="layui-icon">&#xe621;</i>上线</a></dd>
                    <#--<dd><a href="javascript:;" href-url="demo/map.html"><i class="layui-icon">&#xe621;</i>图表</a></dd>-->
                    <dd><a href="javascript:;" id="outlineBtn"><i class="layui-icon">&#x1006;</i>下线</a></dd>
                    <dd><a href="javascript:;"  href-url="${Session.basePath}/ssoLogout"><i class="layui-icon">&#x1006;</i>登出</a></dd>
                </dl>
            </li>
        </ul>

    </div>
    <!-- side -->
    <div class="layui-side my-side">
        <div class="layui-side-scroll">
            <!-- 左侧主菜单添加选项卡监听 -->
            <ul class="layui-nav layui-nav-tree" lay-filter="side-main">
                <!--<li class="layui-nav-item"> 去除 layui-nav-itemed 即可关闭展开
                    <a href="javascript:;"><i class="layui-icon">&#xe620;</i>基础</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" href-url="demo/btn.ftl"><i class="layui-icon">&#xe621;</i>按钮</a></dd>
                        <dd><a href="javascript:;" href-url="demo/form.ftl"><i class="layui-icon">&#xe621;</i>表单</a></dd>
                        <dd><a href="javascript:;" href-url="demo/table.ftl"><i class="layui-icon">&#xe621;</i>表格</a></dd>
                        <dd><a href="javascript:;" href-url="demo/tab-card.ftl"><i class="layui-icon">&#xe621;</i>选项卡</a></dd>
                        <dd><a href="javascript:;" href-url="demo/progress-bar.ftl"><i class="layui-icon">&#xe621;</i>进度条</a></dd>
                        <dd><a href="javascript:;" href-url="demo/folding-panel.ftl"><i class="layui-icon">&#xe621;</i>折叠面板</a></dd>
                        <dd><a href="javascript:;" href-url="demo/auxiliar.ftl"><i class="layui-icon">&#xe621;</i>辅助元素</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item layui-nav-itemed"> 去除 layui-nav-itemed 即可关闭展开
                    <a href="javascript:;"><i class="layui-icon">&#xe628;</i>扩展</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" href-url="demo/login.ftl"><i class="layui-icon">&#xe621;</i>登录页</a></dd>
                        <dd><a href="javascript:;" href-url="demo/register.ftl"><i class="layui-icon">&#xe621;</i>注册页</a></dd>
                        <dd><a href="javascript:;" href-url="demo/login2.ftl"><i class="layui-icon">&#xe621;</i>登录页2</a></dd>
                        <dd><a href="javascript:;" href-url="demo/map.ftl"><i class="layui-icon">&#xe621;</i>图表</a></dd>
                        <dd><a href="javascript:;" href-url="demo/add-edit.ftl"><i class="layui-icon">&#xe621;</i>添加-修改</a></dd>
                        <dd><a href="javascript:;" href-url="demo/data-table.ftl"><i class="layui-icon">&#xe621;</i>data-table 表格页</a></dd>
                        <dd><a href="javascript:;" href-url="demo/tree-table.ftl"><i class="layui-icon">&#xe621;</i>Tree table树表格页</a></dd>
                        <dd><a href="javascript:;" href-url="demo/404.ftl"><i class="layui-icon">&#xe621;</i>404页</a></dd>
                        <dd><a href="javascript:;" href-url="demo/tips.ftl"><i class="layui-icon">&#xe621;</i>提示页</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item"><a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=ad6ba602ae228be2222ddb804086e0cfa42da3d74e34b383b665c2bec1adfc6e"><i class="layui-icon">&#xe61e;</i>加入群下载源码</a></li>-->
            </ul>

        </div>
    </div>
    <!-- body -->
    <div class="layui-body my-body">
        <div class="layui-tab layui-tab-card my-tab" lay-filter="card" lay-allowClose="true">
            <ul class="layui-tab-title" style="display: none">
                <li class="layui-this" lay-id="1"><span><i class="layui-icon">&#xe638;</i>欢迎页</span></li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <iframe id="iframe" src="demo/welcome.html" frameborder="0"></iframe>
                </div>
            </div>
        </div>
    </div>
    <!-- footer -->
    <div class="layui-footer my-footer">
        <p><a href="http://vip-admin.com" target="_blank">一体化调度平台v1.8.0</a>&nbsp;&nbsp;&&nbsp;&nbsp;<a href="http://vip-admin.com/index/gather/index.html" target="_blank">一体化调度v1.2.0</a></p>
        <p>2017 © copyright 广ICP备17005881号</p>
    </div>
</div>

<!-- pay -->
<div class="my-pay-box none">
    <div><img src="${Session.basePath}/assets/image/zfb.png" alt="支付宝"><p>支付宝</p></div>
    <div><img src="${Session.basePath}/assets/image/wx.png" alt="微信"><p>微信</p></div>
</div>

<!-- 右键菜单 -->
<div class="my-dblclick-box none">
    <table class="layui-tab dblclick-tab">
        <tr class="card-refresh">
            <td><i class="layui-icon">&#x1002;</i>刷新当前标签</td>
        </tr>
        <tr class="card-close">
            <td><i class="layui-icon">&#x1006;</i>关闭当前标签</td>
        </tr>
        <tr class="card-close-all">
            <td><i class="layui-icon">&#x1006;</i>关闭所有标签</td>
        </tr>
    </table>
</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/js/vip_comm.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/js/config.js"></script>
<script type="text/javascript">
    //表示从首页进入,1表示已经进入订单页面
    var _flag = 0;
layui.use(['layer','vip_nav'], function () {

    // 操作对象
    var layer       = layui.layer
        ,vipNav     = layui.vip_nav
        ,$          = layui.jquery;

    // 顶部左侧菜单生成 [请求地址,过滤ID,是否展开,携带参数]
    vipNav.top_left('${Session.basePath}/assets/json/nav_top_left.json','side-top-left',false);
    // 主体菜单生成 [请求地址,过滤ID,是否展开,携带参数]
    vipNav.main('${Session.basePath}/assets/json/nav_main.json','side-main',true);

    // 上线
    $("#onlineBtn").click(function(e){
        var params ={};
        params.onlineStatus="在线";
        $.ajax({
            url:_PATH+"/api/user/updateUserOnlineStatus",
            data: JSON.stringify(params),
            contentType: 'application/json',
            dataType:'json',	//服务器返回json格式数据2
            type:'POST',		//HTTP请求类型
            success:function(_data){
                if(_data.success){


                   // layer.msg('', {icon: 1});

                    $("#username").html(username+"- 在线");

                }else{

                    layer.msg(_data.msg, {icon: 2});
                }
            },error:function(e){
                alert("网络错误")
            }
        });
    });

    //下线
    $("#outlineBtn").click(function(e){
        var params ={};
        params.onlineStatus="离线";
        $.ajax({
            url:_PATH+"/api/user/updateUserOnlineStatus",
            data: JSON.stringify(params),
            contentType: 'application/json',
            dataType:'json',	//服务器返回json格式数据2
            type:'POST',		//HTTP请求类型
            success:function(_data){
                if(_data.success){


                   // layer.msg('', {icon: 1});
                    $("#username").html(username+"- 离线");


                }else{

                    layer.msg(_data.msg, {icon: 2});
                }
            },error:function(e){
                alert("网络错误")
            }
        });
    });
    checkUserOnlineStatus();
    var t_user;
    //获取上线状态
    function checkUserOnlineStatus(){
        if(t_user){
            clearTimeout(t_user);
        }

        t_user = setTimeout(function(){
            var params ={};
            $.ajax({
                url:_PATH+"/api/user/checkUserOnlineStatus/",
                // data: JSON.stringify(params),
                contentType: 'application/json',
                dataType:'json',	//服务器返回json格式数据2
                type:'GET',		//HTTP请求类型
                success:function(_data){
                    if(_data.success){


                        //  layer.msg('', {icon: 1});
                        if(_data.obj){
                            $("#username").html(username+"- "+_data.obj);
                        }



                    }else{

                        layer.msg(_data.msg, {icon: 2});
                    }
                    checkUserOnlineStatus()
                },error:function(e){
                    // alert("网络错误")
                    checkUserOnlineStatus()
                }
            });

        },5000);

    }

    beginMessage();
    var beginMessage_time,layermsg_index;
    function beginMessage(){
        // layermsg_index =  layer.load(0, {shade: [0.5, '#393D49']});

        /* $.ajax({
             url: _PATH + "/api/order/deleteMessageById/" + orderId,

             // contentType: 'application/json',
             dataType: 'json',	//服务器返回json格式数据2
             type: 'POST',		//HTTP请求类型
             success: function (_data) {
                 if (_data.success) {

                     //layer.msg('已经抢到订单，到调度平台处理', {icon: 1});
                     //layer.close(layermsg_index);

                     layer.confirm('已经抢单订单', {
                         btn: ['确定','放弃'] //按钮
                         ,title:"确定处理"
                     }, function(){
                         //-------------------------已经抢订单确认接口------------------------
                         // confirmGrabbedOrder(orderId);
                        // layer.msg('到调度平台处理', {icon: 1});
                        // table.reload(options);
                     }, function(){
                         layer.msg('放弃', {
                             time: 20000, //20s后自动关闭
                             btn: ['明白了', '知道了']
                         });
                     });


                 } else {

                     //layer.msg(data.msg, {icon: 2});
                     beginMessage(orderId);


                 }
                 layer.close(layermsg_index);
             }, error: function (e) {
                 alert("网络错误");
                 layer.close(layermsg_index);
             }
         });*/
        if(beginMessage_time){
            clearTimeout(beginMessage_time);
        }


        beginMessage_time = setTimeout(function(){

            // if(_flag==1)return;


            $.ajax({
                url: _PATH + "/api/message/findMessageList" ,

                // contentType: 'application/json',
                dataType: 'json',	//服务器返回json格式数据2
                type: 'GET',		//HTTP请求类型
                success: function (_data) {
                    if (_data.success) {
                        layer.close(layermsg_index);
                        //layer.msg('已经抢到订单，到调度平台处理', {icon: 1});
                        //layer.close(layermsg_index);
                        // if(_flag == 1){
                        //     return;
                        // }
                        if(_data.obj&&_data.obj.length>0){

                            for( var i=0,m= _data.obj.length; i < m ; i++ ){
                                var item = _data.obj[i];
                                // console.log(item);
                                if(item.isRead==0&& item.ableId&& item.type == "抢单成功" ){
                                    // layer.msg('已经抢到订单，到调度平台处理', {icon: 1});
                                    // console.log("已经抢到订单，到调度平台处理===%%%===",item);
                                    var pt= '0px'//(window.innerHeight-200)+'px';
                                    var px ='0px'// window.innerWidth-300+'px';
                                    // console.log(px,pt);
                                    // layer.msg('有抢单成功，到已抢单查看', {offset:[px,pt]});
                                    // if(_flag == 1){
                                    //     return;
                                    // }
                                    layer.closeAll();

                                   layer.confirm('立即处理该订单', {
                                        btn: ['是'] //按钮
                                        ,title:"确认已抢单"
                                       ,cancel: function(index, layero){
                                           //取消操作，点击右上角的X
                                           $.ajax({
                                               url:_PATH+"/api/message/readMessage/"+item.id,
                                               // data: JSON.stringify(params),
                                               contentType: 'application/json',
                                               dataType:'json',	//服务器返回json格式数据2
                                               type:'GET',		//HTTP请求类型
                                               success:function(__data){
                                                   if(__data.success){





                                                   }else{

                                                       // layer.msg(__data.msg, {icon: 2});
                                                   }
                                                   beginMessage();
                                               },error:function(e){
                                                   // alert("网络错误")
                                                   beginMessage();
                                               }
                                           });

                                       }
                                    }, function(){
                                        //-------------------------已经抢订单确认接口------------------------

                                        // layer.msg('到调度平台处理', {icon: 1});
                                        //table.reload(options);

                                        confirmGrabbedOrder(item.ableId,item);

                                    }, function(){
                                        layer.msg('放弃', {
                                            time: 20000, //20s后自动关闭
                                            btn: ['明白了', '知道了']
                                        });
                                        beginMessage();
                                    });
                                    // table.reload("dataCheck",options);

                                    return;
                                }else  if( item.isRead==0&&item.ableId && item.type == "派单成功" ){
                                    // layer.msg('已经抢到订单，到调度平台处理', {icon: 1});
                                    // console.log("已经p派到订单，到调度平台处理===%%%===",item);
                                    var pt= '0px';//(window.innerHeight-200)+'px';
                                    var px ='0px';// window.innerWidth-300+'px';
                                    // if(_flag == 1){
                                    //     return;
                                    // }
                                    layer.closeAll();

                                    layer.msg('有派单成功，到已派单查看', {offset:[px,pt]});
                                    layer.confirm('立即处理该订单', {
                                        btn: ['是'] //按钮
                                        ,title:"确认已派单"
                                        ,cancel: function(index, layero){
                                            //取消操作，点击右上角的X
                                            $.ajax({
                                                url:_PATH+"/api/message/readMessage/"+item.id,
                                                // data: JSON.stringify(params),
                                                contentType: 'application/json',
                                                dataType:'json',	//服务器返回json格式数据2
                                                type:'GET',		//HTTP请求类型
                                                success:function(__data){
                                                    if(__data.success){





                                                    }else{

                                                        // layer.msg(__data.msg, {icon: 2});
                                                    }
                                                    beginMessage();
                                                },error:function(e){
                                                    // alert("网络错误")
                                                    beginMessage();
                                                }
                                            });
                                        }
                                    }, function(){
                                        //-------------------------已经抢订单确认接口------------------------

                                        // layer.msg('到调度平台处理', {icon: 1});
                                        //table.reload(options);

                                        comfirmDispatchOrder(item.ableId,item);

                                    }, function(){
                                        layer.msg('放弃', {
                                            time: 20000, //20s后自动关闭
                                            btn: ['明白了', '知道了']
                                        });
                                        beginMessage();
                                    });
                                    // table.reload("dataCheck",options);

                                    return;
                                }
                            }

                        }
                        beginMessage();

                    } else {

                        //layer.msg(data.msg, {icon: 2});
                        // layer.close(layermsg_index);
                        beginMessage();


                    }

                }, error: function (e) {
                    alert("网络错误");
                    beginMessage();
                    // layer.close(layermsg_index);
                }
            });
        },2000)




    }


    //-------------------------已抢订单确认接口-----------------------------------
    function confirmGrabbedOrder(orderId,_item) {

        layermsg_index =  layer.load(0, {shade: [0.5, '#393D49']});
        $.ajax({
            url: _PATH + "/api/order/confirmGrabbedOrder/" + orderId,

            contentType: 'application/json',
            dataType: 'json',	//服务器返回json格式数据2
            type: 'GET',		//HTTP请求类型
            success: function (data) {
                layer.close(layermsg_index);
                layer.closeAll();
                if (data.success) {
                    /* layer.confirm('是否现在处理该订单', {
                         btn: ['是','否'] //按钮
                         ,title:"抢单成功"
                     }, function(){
                         //-------------------------已经抢订单确认接口------------------------

                         layer.msg('到调度平台处理', {icon: 1});
                         table.reload(options);
                     }, function(){
                         layer.msg('放弃', {
                             time: 20000, //20s后自动关闭
                             btn: ['明白了', '知道了']
                         });
                     });*/
                    // layer.msg('已经抢到订单，到调度平台处理', {icon: 1});
                    var date = new Date().Format("yyyy-MM-dd");
                    var _d = {
                        orderNo:"",
                        externalOrderNo:"",
                        orderNo:""
                    };
                    try {
                        if(_item.extra)
                        _d = JSON.parse(_item.extra);
                    }catch (e) {

                    }


                    var return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagerzcd/list?optflag=ZCD_DCP&sddbh="+_d.orderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
                    if(  _d.orderType =="改期"  ){
                        return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagergqd/list?optflag=GQD_DGQ&gqdh="+_d.externalOrderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
                    }else if(  _d.orderType =="退票"  ){
                        return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagertfd/list?optflag=TFD_DBL&sddbh="+_d.orderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
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
                } else {

                    layer.msg(data.msg, {icon: 2});
                }

                beginMessage();
            }, error: function (e) {
                alert("网络错误");
                beginMessage();
                layer.close(layermsg_index);
            }
        });
    }
    //-------------------------已派单确认接口-----------------------------------

    function comfirmDispatchOrder(orderId,_item) {

        layermsg_index =  layer.load(0, {shade: [0.5, '#393D49']});
        $.ajax({
            url: _PATH + "/api/order/comfirmDispatchOrder/" + orderId,

            contentType: 'application/json',
            dataType: 'json',	//服务器返回json格式数据2
            type: 'GET',		//HTTP请求类型
            success: function (data) {
                layer.close(layermsg_index);
                layer.closeAll();
                if (data.success) {
                    /* layer.confirm('是否现在处理该订单', {
                         btn: ['是','否'] //按钮
                         ,title:"抢单成功"
                     }, function(){
                         //-------------------------已经抢订单确认接口------------------------

                         layer.msg('到调度平台处理', {icon: 1});
                         table.reload(options);
                     }, function(){
                         layer.msg('放弃', {
                             time: 20000, //20s后自动关闭
                             btn: ['明白了', '知道了']
                         });
                     });*/
                    var date = new Date().Format("yyyy-MM-dd");
                    var _d = {
                        orderNo:"",
                        externalOrderNo:"",
                        orderNo:""
                    };
                    try {
                        if(_item.extra)
                         _d = JSON.parse(_item.extra);
                    }catch (e) {
                        
                    }


                    var return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagerzcd/list?optflag=ZCD_DCP&sddbh="+_d.orderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
                    if(  _d.orderType =="改期"  ){
                        return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagergqd/list?optflag=GQD_DGQ&gqdh="+_d.externalOrderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
                    }else if(  _d.orderType =="退票"  ){
                        return_url = "/asms/ticket/order/ticketmanager/asmsticketmanagertfd/list?optflag=TFD_DBL&sddbh="+_d.orderNo+"&ksrq=2017-01-01&jsrq="+date+"&force=true";
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
                } else {

                    layer.msg(data.msg, {icon: 2});
                }

                beginMessage();
            }, error: function (e) {
                alert("网络错误");
                layer.close(layermsg_index);
                beginMessage();
            }
        });
    }

});

function callParentHandler(a){
    _flag = a;
}
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
</body>
</html>