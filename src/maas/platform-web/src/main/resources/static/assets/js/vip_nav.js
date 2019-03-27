/**
 * Created by Administrator on 2017/5/20.
 * @name:   vip-admin 后台模板 菜单navJS
 * @author: 随丶
 */
layui.define(['layer', 'element'], function (exports) {
    // 操作对象
    var layer = layui.layer
        , element = layui.element
        , $ = layui.jquery;

    // 封装方法
    var mod = {
        // 添加 HTMl
        addHtml: function (addr, obj, treeStatus, data) {

            var menu_arr = [];
            $.ajax({
                url:_PATH+"/api/auth/menus",

                contentType: 'application/json',
                dataType:'json',	//服务器返回json格式数据2
                type:'GET',		//HTTP请求类型
                success:function(_data){
                    if(_data.success){
                      console.log("===============================",_data);

                      for( var i = 0,m = _data.obj.length; i < m ;i++ ){
                           var item = _data.obj[i];
                            var menu_obj = {};
                          menu_obj.text = item.name;
                          if( item.icon)
                            menu_obj.icon = item.icon;
                          else
                              menu_obj.icon = "&#xe61c;";
                          menu_obj.href = _PATH+item.path;
                          if( item.children && item.children.length > 0 ){
                              menu_obj.subset = [];
                              for( var j=0,n= item.children.length;j<n;j++){
                                    var mi = item.children[j];
                                    var mo = {};
                                  mo.text = mi.name;
                                  if( item.icon)
                                      mo.icon = mi.icon;
                                  else
                                      mo.icon = "&#xe61c;";
                                  mo.href =_PATH+  mi.path;
                                  menu_obj.subset.push(mo);
                              }

                          }
                          menu_arr.push(menu_obj);
                      }
                        console.log("=========%%%%%%%%==================",menu_arr);
                      var res={"data":menu_arr};
                       makemenus(addr, data,res)
                    }else{


                    }
                },error:function(e){
                    alert("网络错误")
                }
            });


            // 请求数据
            // $.get(addr, data, function (res) {
           function makemenus(addr,data,res){
                console.log("-----获取后台-----",res.data);
                var view = "";
                if (res.data) {
                    $(res.data).each(function (k, v) {
                        v.subset && treeStatus ? view += '<li class="layui-nav-item layui-nav-itemed">' : view += '<li class="layui-nav-item">';
                        if (v.subset) {
                            view += '<a href="javascript:;"><i class="layui-icon">' + v.icon + '</i>' + v.text + '</a><dl class="layui-nav-child">';
                            $(v.subset).each(function (ko, vo) {
                                view += '<dd>';
                                if(vo.target){
                                    view += '<a href="' + vo.href + '" target="_blank">';
                                }else{
                                    view += '<a href="javascript:;" href-url="' + vo.href + '">';
                                }
                                view += '<i class="layui-icon">' + vo.icon + '</i>' + vo.text + '</a></dd>';
                            });
                            view += '<dl>';
                        } else {
                            if (v.target) {
                                view += '<a href="' + v.href + '" target="_blank">';
                            } else {
                                view += '<a href="javascript:;" href-url="' + v.href + '">';
                            }
                            view += '<i class="layui-icon">' + v.icon + '</i>' + v.text + '</a>';
                        }
                        view += '</li>';
                    });
                } else {
                    layer.msg('接受的菜单数据不符合规范,无法解析');
                }
                // 添加到 HTML
                $(document).find(".layui-nav[lay-filter=" + obj + "]").html(view);
                // 更新渲染
                element.init();
            }//,'json');
        }
        // 左侧主体菜单 [请求地址,过滤ID,是否展开,携带参数]
        , main: function (addr, obj, treeStatus, data) {
            // 添加HTML
            this.addHtml(addr, obj, treeStatus, data);
        }
        // 顶部左侧菜单 [请求地址,过滤ID,是否展开,携带参数]
        , top_left: function (addr, obj, treeStatus, data) {
            // 添加HTML
            //this.addHtml(addr, obj, treeStatus, data);
        }
        /*// 顶部右侧菜单
         ,top_right: function(){

         }*/
    };

    // 输出
    exports('vip_nav', mod);
});


