package com.github.celeskyking.squirrel.helper;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by tianqing.wang
 * DATE : 16/1/27
 * TIME : 下午3:41
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.helper
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class NetHelper {

    private static Logger logger  = LoggerFactory.getLogger(NetHelper.class);

    private static List<String> blackList = Lists.newArrayList();
    static{
        blackList.add("docker");
        blackList.add("lo");
    }

    public static String getLocalIp()  {
        Collection<InetAddress> addresses =getAllHostAddress();
        for (InetAddress address : addresses) {
            try {
                if (!address.isLoopbackAddress() && address instanceof Inet4Address
                        && address.isSiteLocalAddress()
                        && address.isReachable(1000)){
                    return address.getHostAddress();
                }
            } catch (IOException e) {
                logger.error("",e);
            }

        }
        //throw new RuntimeException("获取本地IP失败");
        return null;
    }

    public static String getHost(){
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static Collection<InetAddress> getAllHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Collection<InetAddress> addresses = Lists.newArrayList();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(networkInterface.isUp()&&!networkInterface.isLoopback()){
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        addresses.add(inetAddress);
                    }
                }
            }
            return addresses;
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws IOException {
        NetHelper.getLocalIp();
    }

}
