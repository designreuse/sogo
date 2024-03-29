<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>进度条</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>默认风格的进度条</legend>
</fieldset>

<div class="layui-progress">
    <div class="layui-progress-bar" lay-percent="40%"></div>
</div>
<div style="margin-top: 15px; width:300px">
    <div class="layui-progress">
        <div class="layui-progress-bar" lay-percent="70%"></div>
    </div>
</div>
温馨提示：进度条的宽度是100%适配于它的父级元素，如上面的进度条是在一个300px的父容器中。

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>更多颜色选取</legend>
</fieldset>

<div class="layui-progress">
    <div class="layui-progress-bar layui-bg-red" lay-percent="20%"></div>
</div>

<br>

<div class="layui-progress">
    <div class="layui-progress-bar layui-bg-orange" lay-percent="30%"></div>
</div>

<br>

<div class="layui-progress">
    <div class="layui-progress-bar layui-bg-green" lay-percent="40%"></div>
</div>

<br>

<div class="layui-progress">
    <div class="layui-progress-bar layui-bg-blue" lay-percent="50%"></div>
</div>

<br>

<div class="layui-progress">
    <div class="layui-progress-bar layui-bg-cyan" lay-percent="60%"></div>
</div>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>大尺寸进度条</legend>
</fieldset>
<div class="layui-progress layui-progress-big">
    <div class="layui-progress-bar" lay-percent="20%"></div>
</div>

<br>
<div class="layui-progress layui-progress-big">
    <div class="layui-progress-bar layui-bg-green" lay-percent="35%"></div>
</div>
<br>
<div class="layui-progress layui-progress-big">
    <div class="layui-progress-bar layui-bg-cyan" lay-percent="75%"></div>
</div>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>显示百分比</legend>
</fieldset>

<div class="layui-progress" lay-showpercent="true">
    <div class="layui-progress-bar" lay-percent="20%"></div>
</div>

<br>

<div class="layui-progress layui-progress-big" lay-showpercent="true">
    <div class="layui-progress-bar" lay-percent="70%"></div>
</div>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>动态改变进度</legend>
</fieldset>

<div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="demo">
    <div class="layui-progress-bar layui-bg-red" lay-percent="0%"></div>
</div>

<div class="site-demo-button" style="margin-top: 20px; margin-bottom: 0;">
    <button class="layui-btn site-demo-active" data-type="setPercent">设置50%</button>
    <button class="layui-btn site-demo-active" data-type="loading">模拟loading</button>
</div>

<!-- 通用-970*90 -->
<div>
    <ins class="adsbygoogle" style="display:inline-block;width:970px;height:90px"
         data-ad-client="ca-pub-6111334333458862" data-ad-slot="6835627838"></ins>
</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript">
    layui.use('element', function () {
        var element = layui.element
                , $ = layui.jquery;

        //触发事件
        var active = {
            setPercent: function () {
                //设置50%进度
                element.progress('demo', '50%')
            }
            , loading: function (othis) {
                var DISABLED = 'layui-btn-disabled';
                if (othis.hasClass(DISABLED)) return;

                //模拟loading
                var n = 0, timer = setInterval(function () {
                    n = n + Math.random() * 10 | 0;
                    if (n > 100) {
                        n = 100;
                        clearInterval(timer);
                        othis.removeClass(DISABLED);
                    }
                    layui.element().progress('demo', n + '%');
                }, 300 + Math.random() * 1000);

                othis.addClass(DISABLED);
            }
        };

        $('.site-demo-active').on('click', function () {
            var othis = $(this), type = $(this).data('type');
            active[type] ? active[type].call(this, othis) : '';
        });

        // you code ...


    });
</script>
</body>
</html>