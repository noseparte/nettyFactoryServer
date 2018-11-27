package com.xmbl.ops.config;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());
//	private WebSocketServerHandshaker handshaker;

	/**
	 * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
		Global.group.add(ctx.channel());
		logger.info("客户端与服务端连接开启：" + ctx.channel().remoteAddress().toString());
	}

	/**
	 * channel 通道 Inactive 不活跃的
	 * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		Global.group.remove(ctx.channel());
		logger.info("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
	}

	/**
	 * 接收客户端发送的消息 channel 通道 Read 读
	 * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
	 */
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof WebSocketFrame) {
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	/**
	 * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			logger.info("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}
		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		logger.info("服务端收到：" + request);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(String.format("%s received %s", ctx.channel(), request));
		}
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
		// 群发
		Global.group.writeAndFlush(tws);
		// 返回【谁发的发给谁】
		ctx.channel().writeAndFlush(tws); 
	}
}
