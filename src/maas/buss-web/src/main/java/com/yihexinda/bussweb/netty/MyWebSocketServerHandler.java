package com.yihexinda.bussweb.netty;

/**
 * 服务端核心类
 * @author wenbn
 * @version 1.0
 * @date 2018/12/11 0011
 */
import com.yihexinda.core.constants.MessageConstant;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import net.sf.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    private WebSocketServerHandshaker handshaker;

    private static String url = "ws://localhost:7398/buss";


    public static Map<String, ChannelHandlerContext> clients = new ConcurrentHashMap<String, ChannelHandlerContext>();
    public static Map<String, ChannelHandlerContext> drivers = new ConcurrentHashMap<String, ChannelHandlerContext>();

    /**
     * 司机导航监听消息
     * @param content
     */
    @KafkaListener(topics = {MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE})
    private void handleMessage(String content) {
        logger.info("重新规划导航："+content);
        Map<Object, Object> messageMap = JsonUtil.parseJSON2Map(content);
        Iterator<Map.Entry<Object, Object>> entries = messageMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Object, Object> entry = entries.next();
            ChannelHandlerContext channelHandlerContext = clients.get(entry.getKey());
            if(channelHandlerContext!=null){
                Object value = entry.getValue();
                TextWebSocketFrame tws = new TextWebSocketFrame(JsonUtil.object2Json(value));
                logger.info("发送给客户端的真实数据："+JsonUtil.object2Json(value));
                channelHandlerContext.channel().writeAndFlush(tws);
            }else{
                logger.info("客户端已断开连接："+entry.getKey());
            }
        }
    }

    /**
     * 监听消息
     * @param content
     */
    @KafkaListener(topics = {MessageConstant.BUSS_STOP_TIMES_TOPIC_MESSAGE})
    private void driverhandleMessage(String content) {
        Map<Object, Object> messageMap = JsonUtil.parseJSON2Map(content);
        Iterator<Map.Entry<Object, Object>> entries = messageMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Object, Object> entry = entries.next();
            ChannelHandlerContext channelHandlerContext = drivers.get(entry.getKey());
            if(channelHandlerContext!=null){
                Object value = entry.getValue();
                TextWebSocketFrame tws = new TextWebSocketFrame(JsonUtil.object2Json(value));
                logger.info("发送给客户端的真实数据："+JsonUtil.object2Json(value));
                channelHandlerContext.channel().writeAndFlush(tws);
            }else{
                logger.info("客户端已断开连接："+entry.getKey());
            }
        }
    }

    /**
     * 客户端与服务端连接开启
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        Global.group.add(ctx.channel());
        System.out.println("客户端与服务端连接开启7398 1");
    }

    /**
     * 客户端与服务端连接关
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        Global.group.remove(ctx.channel());
        System.out.println("客户端与服务端连接关闭7398 1");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 返回应答消息
        String message = ((TextWebSocketFrame) frame).text();
        System.out.println("来自客户端的消息：" + message);

        Map<Object, Object> reqMap = JsonUtil.parseJSON2Map(message);
        String carId = StringUtil.trim(reqMap.get("carId"));//获取ID信息，群发就不用了
        if(StringUtil.isEmpty(carId)){
            carId = StringUtil.trim(reqMap.get("driver"));
            drivers.put(carId,ctx);
        }else{
            clients.put(carId,ctx);
        }
//        String content = "连接成功";
//        if (logger.isLoggable(Level.FINE)) {
//            logger.fine(String.format("%s received %s", ctx.channel(), message));
//        }
//        TextWebSocketFrame tws = new TextWebSocketFrame(content);
//        // 群发
////        Global.group.writeAndFlush(tws);
//        // 返回【谁发的发给谁】
//        //调用业务层返回处理结果
//        if(!StringUtil.isEmpty(content)){
//            ctx.channel().writeAndFlush(tws);
////            Global.group.writeAndFlush(tws);
//        }


    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
//        if (!req.decoderResult().isSuccess()) {
//            sendHttpResponse(ctx, req,
//                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
//            return;
//        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(url, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
