package com.yihexinda.userweb.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/11 0011
 */
public class Global {

    //存储每一个客户端接入进来时的channel对象
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
