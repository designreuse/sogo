<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>子窗口操作</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<!-- 操作区域-新增 -->
<form class="layui-form">
    <button type="button" class="layui-btn layui-btn-small add-tab" lay-id="1">
        <a href="javascript:;" href-url="demo/_blank.html"><span><i class="layui-icon">&#xe621;</i>按照按钮上的数据生成选项卡</span></a>
    </button>
    <button type="button" class="layui-btn layui-btn-small add-tab2">填写信息生成选项卡</button>
</form>
<br/>
<!-- 操作区域-删除 -->
<form class="layui-form">
    <button type="button" class="layui-btn layui-btn-small del-tab">关闭当前选项卡</button>
</form>
<br/>
<!-- 提示信息 -->
<blockquote class="layui-elem-quote">详细操作方法请看：【demo/children.html】 js方法中，很简单，调用一个方法就行，一看就明白。</blockquote>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/js/index.js"></script>
<script type="text/javascript">

    // layui方法
    layui.use(['layer', 'vip_tab'], function () {

        // 操作对象
        var layer = layui.layer
                , vipTab = layui.vip_tab
                , $ = layui.jquery;

        // 按照按钮上的数据生成选项卡
        $(document).on('click', '.add-tab', function () {
            vipTab.add($(this));
        });

        // 按照填写的数据生成选项卡
        $(document).on('click', '.add-tab2', function () {
            // 口令弹框
            layer.prompt({
                formType: 0,
                value: '标题',
                title: '请输入标题',
                maxlength: 10
            }, function (val, index) {
                // 添加选项卡[自身对象,标题,url地址]
                vipTab.add($(this), val, 'demo/children.html'); // 有传入三个参数，第一个参数完全没有作用了，就是站位
                // 关闭弹窗
                layer.close(index);
            });

            // vipTab.add($(this),'标题','地址');
        });

        // 删除选项卡
        $(document).on('click', '.del-tab', function () {
            // 删除
            vipTab.del(vipTab.getThisTabId());
        });

        // you code ...


    });
</script>
</body>
</html>