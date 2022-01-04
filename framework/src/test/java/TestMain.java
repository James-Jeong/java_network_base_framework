import instance.BaseEnvironment;
import instance.DebugLevel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioDatagramChannel;
import network.socket.GroupSocket;
import network.socket.NetAddress;
import network.socket.SocketManager;
import network.socket.SocketProtocol;
import org.junit.Assert;
import org.junit.Test;
import service.ResourceManager;
import service.scheduler.schedule.ScheduleManager;

public class TestMain {

    @Test
    public void test() {
        BaseEnvironment baseEnvironment = new BaseEnvironment(
                new ScheduleManager(),
                new ResourceManager(5000, 7000),
                DebugLevel.DEBUG
        );

        // 1 key : 1 channel : 1 listen-point
        SocketManager socketManager = new SocketManager(
                baseEnvironment,
                false,
                10,
                500000,
                500000
        );

        //
        NetAddress netAddress1 = new NetAddress("127.0.0.1", 5000,true, SocketProtocol.UDP);
        NetAddress netAddress2 = new NetAddress("127.0.0.1", 6000,true, SocketProtocol.UDP);
        //

        //
        ChannelInitializer<NioDatagramChannel> clientChannelInitializer = new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel nioDatagramChannel) {
                final ChannelPipeline channelPipeline = nioDatagramChannel.pipeline();
                channelPipeline.addLast(new ClientHandler());
            }
        };

        ChannelInitializer<NioDatagramChannel> serverChannelInitializer = new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel nioDatagramChannel) {
                final ChannelPipeline channelPipeline = nioDatagramChannel.pipeline();
                channelPipeline.addLast(new ServerHandler());
            }
        };
        //

        //
        Assert.assertTrue(socketManager.addSocket(netAddress1, serverChannelInitializer));
        GroupSocket groupSocket1 = socketManager.getSocket(netAddress1);
        Assert.assertNotNull(groupSocket1);
        Assert.assertTrue(groupSocket1.getListenSocket().openListenChannel());
        baseEnvironment.printMsg("GROUP-SOCKET1: {%s}", groupSocket1.toString());
        //

        //
        Assert.assertTrue(socketManager.addSocket(netAddress2, serverChannelInitializer));
        GroupSocket groupSocket2 = socketManager.getSocket(netAddress2);
        Assert.assertNotNull(groupSocket2);
        Assert.assertTrue(groupSocket2.getListenSocket().openListenChannel());
        baseEnvironment.printMsg("GROUP-SOCKET2: {%s}", groupSocket2.toString());
        //

        //
        Assert.assertTrue(groupSocket1.addDestination(netAddress2, null, 1234, clientChannelInitializer));
        //

        //
        Assert.assertTrue(socketManager.removeSocket(netAddress1));
        Assert.assertTrue(socketManager.removeSocket(netAddress2));
        //
    }

}
