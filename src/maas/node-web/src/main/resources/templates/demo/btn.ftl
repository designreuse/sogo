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

<fieldset class="layui-elem-field site-demo-button">
    <legend>六种按钮主题</legend>
    <div>
        <button class="layui-btn layui-btn-primary">原始按钮</button>
        <button class="layui-btn">默认按钮</button>
        <button class="layui-btn layui-btn-normal">百搭按钮</button>
        <button class="layui-btn layui-btn-warm">暖色按钮</button>
        <button class="layui-btn layui-btn-danger">警告按钮</button>
        <button class="layui-btn layui-btn-disabled">禁用按钮</button>
    </div>
</fieldset>
<fieldset class="layui-elem-field site-demo-button">
    <legend>四种按钮尺寸</legend>
    <div>
        <button class="layui-btn layui-btn-primary layui-btn-big">大型按钮</button>
        <button class="layui-btn layui-btn-primary">默认按钮</button>
        <button class="layui-btn layui-btn-primary layui-btn-small">小型按钮</button>
        <button class="layui-btn layui-btn-primary layui-btn-mini">迷你按钮</button>

        <br>

        <button class="layui-btn layui-btn-big">大型按钮</button>
        <button class="layui-btn">默认按钮</button>
        <button class="layui-btn layui-btn-small">小型按钮</button>
        <button class="layui-btn layui-btn-mini">迷你按钮</button>

        <br>

        <button class="layui-btn layui-btn-big layui-btn-normal">大型按钮</button>
        <button class="layui-btn layui-btn-normal">默认按钮</button>
        <button class="layui-btn layui-btn-small layui-btn-normal">小型按钮</button>
        <button class="layui-btn layui-btn-mini layui-btn-normal">迷你按钮</button>
    </div>
</fieldset>
<fieldset class="layui-elem-field site-demo-button">
    <legend>灵活的图标按钮（更多图标请阅览：文档-图标）</legend>
    <div>
        <button class="layui-btn"><i class="layui-icon">&#xe611;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#xe614;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#x1002;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#xe60f;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#xe615;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#xe641;</i></button>

        <br>

        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#xe620;</i></button>
        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#xe628;</i></button>
        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#x1006;</i></button>
        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#x1007;</i></button>
        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#xe629;</i></button>
        <button class="layui-btn layui-btn-danger"><i class="layui-icon">&#xe600;</i></button>

        <br>

        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe617;</i></button>
        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe606;</i></button>
        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe609;</i></button>
        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe60a;</i></button>
        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe62c;</i></button>
        <button class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#x1005;</i></button>

        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe61b;</i></button>
        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe610;</i></button>
        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe62d;</i></button>
        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe63d;</i></button>
        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe602;</i></button>
        <button class="layui-btn layui-btn-small"><i class="layui-icon">&#xe603;</i></button>

        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe62e;</i></button>
        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe62f;</i></button>
        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe61f;</i></button>
        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe601;</i></button>
        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe630;</i></button>
        <button class="layui-btn layui-btn-normal layui-btn-small"><i class="layui-icon">&#xe631;</i></button>
        <button class="layui-btn"><i class="layui-icon">&#xe635;</i> 图文</button>
    </div>
</fieldset>
<fieldset class="layui-elem-field site-demo-button">
    <legend>还可以是圆角按钮</legend>
    <div>
        <button class="layui-btn layui-btn-primary layui-btn-radius">原始按钮</button>
        <button class="layui-btn layui-btn-radius">默认按钮</button>
        <button class="layui-btn layui-btn-normal layui-btn-radius">百搭按钮</button>
        <button class="layui-btn layui-btn-warm layui-btn-radius">暖色按钮</button>
        <button class="layui-btn layui-btn-danger layui-btn-radius">警告按钮</button>
        <button class="layui-btn layui-btn-disabled layui-btn-radius">禁用按钮</button>
    </div>
</fieldset>
<fieldset class="layui-elem-field site-demo-button">
    <legend>风格混搭的按钮</legend>
    <div>
        <button class="layui-btn layui-btn-big layui-btn-primary layui-btn-radius">大型加圆角</button>
        <a href="#" class="layui-btn" target="_blank">跳转的按钮</a>
        <button class="layui-btn layui-btn-small layui-btn-normal"><i class="layui-icon">&#xe640;</i> 删除</button>
        <button class="layui-btn layui-btn-mini layui-btn-disabled"><i class="layui-icon">&#xe609;</i> 分享</button>
    </div>
</fieldset>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript">
    // you code ...


</script>
</body>
</html>