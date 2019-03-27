package com.yihexinda.userweb.netty;

/**
 *
 * 服务端核心类
 * @author wenbn
 * @version 1.0
 * @date 2018/12/11 0011
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yihexinda.core.param.QRCodeParam;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.userweb.api.QRCodeResource;
import com.yihexinda.userweb.utils.SpringUtil;
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

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    private WebSocketServerHandshaker handshaker;

    private QRCodeResource qrCode = SpringUtil.getBean(QRCodeResource.class);

    private static String url = "ws://localhost:7397/qr";

    /**
     * 客户端与服务端连接开启
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        Global.group.add(ctx.channel());
        logger.info("客户端与服务端连接开启");
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
        logger.info("客户端与服务端连接关闭");
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
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 返回应答消息
        String message = ((TextWebSocketFrame) frame).text();
        int heartBeat = message.indexOf("HeartBeat");
        if(heartBeat != -1){
            logger.info("终端机号:"+message.substring(9)+"发送的心跳检查");
            ResultVo success = ResultVo.success();
            success.setResult(2222);
            success.setMessage("心跳还在");
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(success)));
            return;
        }
        logger.info("来自客户端的消息：" + message);

        Map<Object, Object> reqMap = JsonUtil.parseJSON2Map(message);
        String oid = StringUtil.trim(reqMap.get("oid"));//获取ID信息，群发就不用了
        String token = StringUtil.trim(reqMap.get("token"));//获取ID信息，群发就不用了
        String expTime = StringUtil.trim(reqMap.get("expTime"));//获取ID信息，群发就不用了
        String deviceCode = StringUtil.trim(reqMap.get("deviceCode"));//获取ID信息，群发就不用了

        QRCodeParam param = new QRCodeParam();
        param.setExpTime(expTime);
        param.setOid(oid);
        param.setToken(token);
        param.setDeviceCode(deviceCode);

        String content = null;
		try {
			ResultVo resultVo = qrCode.callBack(param);
			content = JsonUtil.toJson(resultVo);
		} catch (ParseException e) {
			content = JsonUtil.toJson(ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE));
			e.printStackTrace();
		}

        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", ctx.channel(), message));
        }
//        try {
//            content = new String(content.getBytes("ISO-8859-1"),"gb2312");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        TextWebSocketFrame tws = new TextWebSocketFrame(StringUtil.utf8Togb2312(content));
        TextWebSocketFrame tws = new TextWebSocketFrame(content);
        // 群发
//        Global.group.writeAndFlush(tws);
        // 返回【谁发的发给谁】
        //调用业务层返回处理结果
        if(!StringUtil.isEmpty(content)){
            ctx.channel().writeAndFlush(tws);
            logger.info("返回客户端的消息:"+content);
//            Global.group.writeAndFlush(tws);
        }


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
