<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>欢迎页</title>
    <link rel="stylesheet" href="${Session.basePath}/assets/libs/layui/css/layui.css">
    <link rel="stylesheet" href="${Session.basePath}/assets/css/style.css">
    <link rel="icon" href="${Session.basePath}/assets/image/code.png">
</head>
<script type="text/javascript">
    var  LAYUI_FILE_PATH  = "${Session.basePath}";
</script>
<body class="body">

<div class="layui-row layui-col-space10 my-index-main">
    <div class="layui-col-xs12 layui-col-sm6 layui-col-md4">
        <div class="layui-collapse">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">调度单统计</h2>
                <div class="layui-colla-content layui-show">

                    <div id="main-circle" style="height: 450px;"></div>


                </div>
            </div>
        </div>
    </div>


<div class="layui-row layui-col-space10 my-index-main">
    <div class="layui-col-xs12 layui-col-sm6 layui-col-md4">
        <div class="layui-collapse">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">图表</h2>
                <div class="layui-colla-content layui-show">

                    <div id="main-line" style="height: 450px;"></div>

                </div>
            </div>
        </div>
    </div>

    <div class="layui-col-xs12 layui-col-sm6 layui-col-md6">
        <div class="layui-collapse">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">表格</h2>
                <div class="layui-colla-content layui-show">

                    <table class="layui-table">
                        <colgroup>
                            <col width="150">
                            <col width="200">
                            <col>
                        </colgroup>
                        <thead>
                        <tr>
                            <th>昵称</th>
                            <th>加入时间</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td></td>
                            <td>2016-11-29</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>许闲心</td>
                            <td>2016-11-28</td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>

                </div>
            </div>
        </div>
    </div>

    <div class="layui-col-xs12 layui-col-sm6 layui-col-md4">
        <div class="layui-collapse">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">版本</h2>
                <div class="layui-colla-content layui-show">

                    <ul class="layui-timeline max-auto">
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.8.0</h3>
                                <p>
                                    更新日期:2017-08-26
                                </p>
                                <ul>
                                    <li>更新layui-v1.0.9为layui-v2.0.2版本</li>
                                    <li>右键增加关闭全部标签按钮</li>
                                    <li>更新欢迎页面</li>
                                    <li>更新data-table页面和tree-table页面为layui自带table组件</li>
                                    <li>
                                        <h4>新增js功能</h4>
                                        <ul>
                                            <li>
                                                <p>vip_table.js</p>
                                                <ul>
                                                    <li>getFullHeight方法  getFullHeight();    // 返回子页面整体高度,用于table组件设置全屏高度</li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </li>
                                    <li>修改已知BUG</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.7.0</h3>
                                <p>更新时间:2017-05-21</p>
                                <ul>
                                    <li>优化主题样式细节</li>
                                    <li>标签新增双击关闭当前标签功能</li>
                                    <li>标签新增右键功能</li>
                                    <li>
                                        <h5>新增js功能。   详细可查看【frame/static/js】文件夹内的js</h5>
                                        <ul>
                                            <li>
                                                <h4>vip_nav.js 【主页菜单js】</h4>
                                                <ul>
                                                    <li>main方法       main(请求地址,过滤ID,是否展开,携带参数);</li>
                                                    <li>top_left方法   top_left(请求地址,过滤ID,是否展开,携带参数);</li>
                                                </ul>
                                            </li>
                                            <li>
                                                <h4>vip_tab.js 【子页面操作父页面选项卡js】</h4>
                                                <ul>
                                                    <li>add方法            add(操作对象，标签标题，url地址);</li>
                                                    <li>getThisTabId方法   getThisTabId();     // 返回当前展示页面父级窗口标签的lay-id</li>
                                                    <li>del方法            del(标签lay-id);</li>
                                                </ul>
                                            </li>
                                            <li>
                                                <h4>vip_table.js 【表格js,该js属于整合】</h4>
                                                <ul>
                                                    <li>deleteAll方法      deleteAll(ids,请求的url,操作成功跳转url,操作失败跳转url);</li>
                                                    <li>unixToDate方法     unixToDate(时间戳,是否只显示年月日时分,8);        // 返回正常时间</li>
                                                    <li>getIds方法         getIds(表格对象,每条数据的属性id);      // 返回需要的 ids</li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </li>
                                    <li>修改已知BUG。</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.6.0</h3>
                                <p>更新时间:2017-04-25</p>
                                <ul>
                                    <li>优化CSS、JS</li>
                                    <li>新增新的登录、注册页面</li>
                                    <li>新增主题功能，提供默认、纯白、蓝白三种主题配置</li>
                                    <li>导航栏添加图标</li>
                                    <li>修改已知BUG</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.5.1</h3>
                                <p>更新时间:2017-03-21</p>
                                <ul>
                                    <li>修改浏览器窗口resize时不断闪烁BUG。  感谢：Clannad-</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.5.0</h3>
                                <p>更新时间:2017-03-20</p>
                                <ul>
                                    <li>更新layui框架为最新版1.0.9_rts版本</li>
                                    <li>优化css,样式更加接近vip-admin管理系统v1.0.5</li>
                                    <li>新增效果：导航栏点击栏目右侧添加相应tab选项卡,点击已经生成过的选项卡直接跳转到该选选项卡</li>
                                    <li>新增导航栏收缩按钮</li>
                                    <li>修改已知BUG</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">1.1.0</h3>
                                <p>更新时间:2017-02-27</p>
                                <ul>
                                    <li>登录页面添加头部标题</li>
                                    <li>新增tree table 页面</li>
                                    <li>新增404页面</li>
                                    <li>新增tips提示页面</li>
                                    <li>
                                        <h4>js功能: 具体操作请查看 js/table-tool.js</h4>
                                        <ul>
                                            <li>getIds(table对象,获取input为id的属性);</li>
                                            <li>deleteAll(ids,请求url,操作成功跳转url,操作失败跳转url);</li>
                                            <li>UnixToDate(时间戳,显示年月日时分,加8小时显示正常时间区);</li>
                                        </ul>
                                    </li>
                                    <li>该版本已兼容手机浏览</li>
                                    <li>修改已知BUG</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.0.1</h3>
                                <p>更新时间:2017-02-16</p>
                                <ul>
                                    <li>优化datatables表格，添加排序图标，点击升序降序更加美观；表格全选优化，全选后所有选中项添加背景颜色，使之选中更加明显</li>
                                    <li>添加echearts图表插件，展示了基本的柱状图和饼图示例</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <h3 class="layui-timeline-title">v1.0.0</h3>
                                <p>更新时间:2017-01-06</p>
                                <ul>
                                    <li>该模板最大化保留了原生layui的基础样式，整合行成的一套后台模板</li>
                                    <li>该模板集合了layui插件、datatables插件</li>
                                    <li>该模板使用layui基础样式中的按钮、表单、表格、和选项卡</li>
                                    <li>datatables表格和layui表格深度整合，使用更加方便、美观、人性化</li>
                                    <li>扩展了欢迎页面、登录页面</li>
                                </ul>
                            </div>
                        </li>
                        <li class="layui-timeline-item">
                            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>
                            <div class="layui-timeline-content layui-text">
                                <div class="layui-timeline-title">开始于2017年01月06日</div>
                            </div>
                        </li>
                    </ul>

                </div>
            </div>
        </div>
    </div>

    <div class="layui-col-xs12 layui-col-sm6 layui-col-md4">
        <div class="layui-collapse">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">表单</h2>
                <div class="layui-colla-content layui-show">

                    <form class="layui-form max-auto" action="">
                        <div class="layui-form-item">
                            <label class="layui-form-label">输入框</label>
                            <div class="layui-input-block">
                                <input type="text" name="title" required lay-verify="required" placeholder="请输入标题"
                                       autocomplete="off" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">密码框</label>
                            <div class="layui-input-inline">
                                <input type="password" name="password" required lay-verify="required"
                                       placeholder="请输入密码" autocomplete="off" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">辅助文字</div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">选择框</label>
                            <div class="layui-input-block">
                                <select name="city" lay-verify="required">
                                    <option value=""></option>
                                    <option value="0">北京</option>
                                    <option value="1">上海</option>
                                    <option value="2">广州</option>
                                    <option value="3">深圳</option>
                                    <option value="4">杭州</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">复选框</label>
                            <div class="layui-input-block">
                                <input type="checkbox" name="like[write]" title="写作">
                                <input type="checkbox" name="like[read]" title="阅读" checked>
                                <input type="checkbox" name="like[dai]" title="发呆">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">开关</label>
                            <div class="layui-input-block">
                                <input type="checkbox" name="switch" lay-skin="switch">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">单选框</label>
                            <div class="layui-input-block">
                                <input type="radio" name="sex" value="男" title="男">
                                <input type="radio" name="sex" value="女" title="女" checked>
                            </div>
                        </div>
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label">文本域</label>
                            <div class="layui-input-block">
                                <textarea name="desc" placeholder="请输入内容" class="layui-textarea"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
                                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${Session.basePath}/assets/libs/layui/layui.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/js/index.js"></script>
<script type="text/javascript" src="${Session.basePath}/assets/libs/echarts/echarts.min.js"></script>
<script type="text/javascript">
    layui.use(['element', 'form', 'table', 'layer', 'vip_tab'], function () {
        var form = layui.form
                , table = layui.table
                , layer = layui.layer
                , vipTab = layui.vip_tab
                , element = layui.element
                , $ = layui.jquery;

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main-circle'));

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option = {
            title : {
                text: '调度订单',
                subtext: '各状态订单统计',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                x : 'left',
                y : 'bottom',
                data:['抢单量','派单量','退回量','抽回量','超时量','暂存量']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {
                        show: true,
                        type: ['pie', 'funnel']
                    },
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [

                {
                    name:'面积模式',
                    type:'pie',
                    radius : [30, 110],
                    center : ['45%', '50%'],
                    roseType : 'area',
                    data:[
                        {value:10, name:'抢单量'},
                        {value:5, name:'派单量'},
                        {value:15, name:'退回量'},
                        {value:25, name:'抽回量'},
                        {value:20, name:'超时量'},
                        {value:35, name:'暂存量'}//,
                        // {value:30, name:'rose7'},
                        // {value:40, name:'rose8'}
                    ]
                }
            ]
        });


        // 打开选项卡
        $('.my-nav-btn').on('click', function(){
            if($(this).attr('data-href')){
                //vipTab.add('','标题','路径');
                vipTab.add($(this),'<i class="layui-icon">'+$(this).find("button").html()+'</i>'+$(this).find('p:last-child').html(),$(this).attr('data-href'));
            }
        });

        // you code ...


    });
</script>
</body>
</html>