<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>登录</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="login-body body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<div class="login-box">
    <form class="layui-form layui-form-pane" method="get" action="">
        <div class="layui-form-item">
            <h3>xx后台登录系统</h3>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">账号：</label>

            <div class="layui-input-inline">
                <input type="text" name="account" class="layui-input" lay-verify="account" placeholder="账号"
                       autocomplete="on" maxlength="20"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码：</label>

            <div class="layui-input-inline">
                <input type="password" name="password" class="layui-input" lay-verify="password" placeholder="密码"
                       maxlength="20"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">验证码：</label>

            <div class="layui-input-inline">
                <input type="number" name="code" class="layui-input" lay-verify="code" placeholder="验证码" maxlength="4"/><img
                    src="../frame/static/image/v.png" alt="">
            </div>
        </div>
        <div class="layui-form-item">
            <button type="reset" class="layui-btn layui-btn-danger btn-reset">重置</button>
            <button type="button" class="layui-btn btn-submit" lay-submit="" lay-filter="sub">立即登录</button>
        </div>
    </form>
</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {

        // 操作对象
        var form = layui.form
                , layer = layui.layer
                , $ = layui.jquery;

        // 验证
        form.verify({
            account: function (value) {
                if (value == "") {
                    return "请输入用户名";
                }
            },
            password: function (value) {
                if (value == "") {
                    return "请输入密码";
                }
            },
            code: function (value) {
                if (value == "") {
                    return "请输入验证码";
                }
            }
        });

        // 提交监听
        form.on('submit(sub)', function (data) {
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            return false;
        });

        // you code ...
    })

</script>
</body>
</html>