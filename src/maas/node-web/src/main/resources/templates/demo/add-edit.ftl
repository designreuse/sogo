<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>添加-修改</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<body class="body">
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<form class="layui-form layui-form-pane" action="">
    <div class="layui-form-item">
        <label class="layui-form-label">长输入框</label>

        <div class="layui-input-block">
            <input type="text" name="title" autocomplete="off" placeholder="请输入标题" lay-verify="required"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">短输入框</label>

        <div class="layui-input-inline">
            <input type="text" name="username" lay-verify="required" placeholder="请输入" autocomplete="off"
                   class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">日期选择</label>

            <div class="layui-input-block">
                <input type="text" name="date" id="date" autocomplete="off" class="layui-input"
                       onclick="layui.laydate({elem: this})">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">行内表单</label>

            <div class="layui-input-inline">
                <input type="number" name="number" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码</label>

        <div class="layui-input-inline">
            <input type="password" name="password" placeholder="请输入密码" autocomplete="off" class="layui-input">
        </div>
        <div class="layui-form-mid layui-word-aux">请务必填写用户名</div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">范围</label>

            <div class="layui-input-inline" style="width: 100px;">
                <input type="text" name="price_min" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid">-</div>
            <div class="layui-input-inline" style="width: 100px;">
                <input type="text" name="price_max" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">单行选择框</label>

        <div class="layui-input-block">
            <select name="interest" lay-filter="aihao">
                <option value=""></option>
                <option value="0">写作</option>
                <option value="1" selected="">阅读</option>
                <option value="2">游戏</option>
                <option value="3">音乐</option>
                <option value="4">旅行</option>
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">行内选择框</label>

        <div class="layui-input-inline">
            <select name="quiz1">
                <option value="">请选择省</option>
                <option value="浙江" selected="">浙江省</option>
                <option value="你的工号">江西省</option>
                <option value="你最喜欢的老师">福建省</option>
            </select>
        </div>
        <div class="layui-input-inline">
            <select name="quiz2">
                <option value="">请选择市</option>
                <option value="杭州">杭州</option>
                <option value="宁波" disabled="">宁波</option>
                <option value="温州">温州</option>
                <option value="温州">台州</option>
                <option value="温州">绍兴</option>
            </select>
        </div>
        <div class="layui-input-inline">
            <select name="quiz3">
                <option value="">请选择县/区</option>
                <option value="西湖区">西湖区</option>
                <option value="余杭区">余杭区</option>
                <option value="拱墅区">临安市</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <div class="layui-input-block">
            <textarea id="LAY_demo_editor" placeholder="请输入内容" class="layui-textarea"></textarea>
        </div>
        <label class="layui-form-label">请输入内容</label>
    </div>
    <div class="layui-form-item">
        <button class="layui-btn" lay-submit="" lay-filter="sub">提交</button>
    </div>
</form>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript">
    layui.use(['form', 'layedit', 'laydate', 'element'], function () {
        var form = layui.form
                , layer = layui.layer
                , layedit = layui.layedit
                , laydate = layui.laydate
                , element = layui.element;

        //创建一个编辑器
        var editIndex = layedit.build('LAY_demo_editor');

        //自定义验证规则
        form.verify({
            title: function (value) {
                if (value.length < 5) {
                    return '标题至少得5个字符啊';
                }
            }
            , pass: [/(.+){6,12}$/, '密码必须6到12位']
            , content: function (value) {
                layedit.sync(editIndex);
            }
        });

        //监听提交
        form.on('submit(sub)', function (data) {
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            return false;
        });

        // you code ...


    });
</script>
</body>
</html>