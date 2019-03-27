<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title></title>
    <link rel="stylesheet" href="../frame/layui/css/layui.css">
    <link rel="stylesheet" href="../frame/static/css/style.css">
    <link rel="icon" href="../frame/static/image/code.png">
</head>
<body class="body">

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>常规折叠</legend>
</fieldset>
<div class="layui-collapse" lay-filter="test">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">为什么JS社区大量采用未发布或者未广泛支持的语言特性？</h2>
        <div class="layui-colla-content">
            <p>
                有不少其他答案说是因为JS太差。我下面的答案已经说了，这不是根本性的原因。但除此之外，我还要纠正一些对JS具体问题的误解。JS当初是被作为脚本语言设计的，所以某些问题并不是JS设计得差或者是JS设计者的失误。比如var的作用域问题，并不是“错误”，而是当时绝大部分脚本语言都是这样的，如perl/php/sh等。模块的问题也是，脚本语言几乎都没有模块/命名空间功能。弱类型、for-in之类的问题也是，只不过现在用那些老的脚本语言的人比较少，所以很多人都误以为是JS才有的坑。另外有人说JS是半残语言，满足不了开发需求，1999年就该死。半残这个嘛，就夸张了。JS虽然有很多问题，但是设计总体还是优秀的。——来自知乎@贺师俊</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">为什么前端工程师多不愿意用 Bootstrap 框架？</h2>
        <div class="layui-colla-content">
            <p>
                因为不适合。如果希望开发长期的项目或者制作产品类网站，那么就需要实现特定的设计，为了在维护项目中可以方便地按设计师要求快速修改样式，肯定会逐步编写出各种业务组件、工具类，相当于为项目自行开发一套框架。——来自知乎@Kayo</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">layui 更适合哪些开发者？</h2>
        <div class="layui-colla-content">
            <p>在前端技术快速变革的今天，layui 仍然坚持语义化的组织模式，甚至于模块理念都是采用类AMD组织形式，并非是有意与时代背道而驰。layui
                认为以jQuery为核心的开发方式还没有到完全消亡的时候，而早期市面上基于jQuery的UI都普通做得差强人意，所以需要有一个新的UI去重新为这一领域注入活力，并采用一些更科学的架构方式。
                <br><br>
                因此准确地说，layui 更多是面向那些追求开发简单的前端工程师们，以及所有层次的服务端程序员。</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">贤心是男是女？</h2>
        <div class="layui-colla-content">
            <p>man！ 所以这个问题不要再出现了。。。</p>
        </div>
    </div>
</div>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>手风琴折叠</legend>
</fieldset>
<div class="layui-collapse" lay-accordion="">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">layui 更适合哪些开发者？</h2>
        <div class="layui-colla-content layui-show">
            <p>在前端技术快速变革的今天，layui 仍然坚持语义化的组织模式，甚至于模块理念都是采用类AMD组织形式，并非是有意与时代背道而驰。layui
                认为以jQuery为核心的开发方式还没有到完全消亡的时候，而早期市面上基于jQuery的UI都普通做得差强人意，所以需要有一个新的UI去重新为这一领域注入活力，并采用一些更科学的架构方式。
                <br>
                因此准确地说，layui 更多是面向那些追求开发简单的前端工程师们，以及所有层次的服务端程序员。</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">为什么JS社区大量采用未发布或者未广泛支持的语言特性？</h2>
        <div class="layui-colla-content">
            <p>
                有不少其他答案说是因为JS太差。我下面的答案已经说了，这不是根本性的原因。但除此之外，我还要纠正一些对JS具体问题的误解。JS当初是被作为脚本语言设计的，所以某些问题并不是JS设计得差或者是JS设计者的失误。比如var的作用域问题，并不是“错误”，而是当时绝大部分脚本语言都是这样的，如perl/php/sh等。模块的问题也是，脚本语言几乎都没有模块/命名空间功能。弱类型、for-in之类的问题也是，只不过现在用那些老的脚本语言的人比较少，所以很多人都误以为是JS才有的坑。另外有人说JS是半残语言，满足不了开发需求，1999年就该死。半残这个嘛，就夸张了。JS虽然有很多问题，但是设计总体还是优秀的。——来自知乎@贺师俊</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">为什么前端工程师多不愿意用 Bootstrap 框架？</h2>
        <div class="layui-colla-content">
            <p>
                因为不适合。如果希望开发长期的项目或者制作产品类网站，那么就需要实现特定的设计，为了在维护项目中可以方便地按设计师要求快速修改样式，肯定会逐步编写出各种业务组件、工具类，相当于为项目自行开发一套框架。——来自知乎@Kayo</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">贤心是男是女？</h2>
        <div class="layui-colla-content">
            <p>man！ 所以这个问题不要再出现了。。。</p>
        </div>
    </div>
</div>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
    <legend>面板嵌套</legend>
</fieldset>
<div class="layui-collapse" lay-accordion="">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">文豪</h2>
        <div class="layui-colla-content layui-show">

            <div class="layui-collapse" lay-accordion="">
                <div class="layui-colla-item">
                    <h2 class="layui-colla-title">唐代</h2>
                    <div class="layui-colla-content layui-show">

                        <div class="layui-collapse" lay-accordion="">
                            <div class="layui-colla-item">
                                <h2 class="layui-colla-title">杜甫</h2>
                                <div class="layui-colla-content layui-show">
                                    伟大的诗人
                                </div>
                            </div>
                            <div class="layui-colla-item">
                                <h2 class="layui-colla-title">李白</h2>
                                <div class="layui-colla-content">
                                    <p>据说是韩国人</p>
                                </div>
                            </div>
                            <div class="layui-colla-item">
                                <h2 class="layui-colla-title">王勃</h2>
                                <div class="layui-colla-content">
                                    <p>千古绝唱《滕王阁序》</p>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-colla-item">
                    <h2 class="layui-colla-title">宋代</h2>
                    <div class="layui-colla-content">
                        <p>比如苏轼、李清照</p>
                    </div>
                </div>
                <div class="layui-colla-item">
                    <h2 class="layui-colla-title">当代</h2>
                    <div class="layui-colla-content">
                        <p>比如贤心</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">科学家</h2>
        <div class="layui-colla-content">
            <p>伟大的科学家</p>
        </div>
    </div>
    <div class="layui-colla-item">
        <h2 class="layui-colla-title">艺术家</h2>
        <div class="layui-colla-content">
            <p>浑身散发着艺术细胞</p>
        </div>
    </div>
</div>

<br>
<p>支持无限嵌套，应用场景非常多！</p>

<script type="text/javascript" src="../frame/layui/layui.js"></script>
<script type="text/javascript">
    layui.use(['element', 'layer'], function () {
        var element = layui.element
                , layer = layui.layer;

        //监听折叠
        element.on('collapse(test)', function (data) {
            layer.msg('展开状态：' + data.show);
        });

        // you code ...


    });
</script>
</body>
</html>