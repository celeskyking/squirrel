package com.github.celeskyking.squirrel.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.brainlag.nsq.ServerAddress;
import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Created by tianqing.wang
 * DATE : 16/3/9
 * TIME : 下午4:31
 * PROJECT : squirrel
 * PACKAGE : com.github.celeskyking.squirrel.helper
 *
 * @author <a href="mailto:celeskyking@163.com">tianqing.wang</a>
 */
public class NsqLookupHelper {

    private static Logger logger = LoggerFactory.getLogger(NsqLookupHelper.class);


    public static Set<ServerAddress> nodes(Set<HostAndPort> lookups){
        Set<ServerAddress> addresses = Sets.newHashSet();
        for (HostAndPort hostAndPort : lookups) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(new URL("http://"+hostAndPort.getHostText()+":"+hostAndPort.getPort() + "/nodes" ));
                logger.debug("Server connection information: " + jsonNode.toString());
                JsonNode producers = jsonNode.get("data").get("producers");
                for (JsonNode node : producers) {
                    String host = node.get("broadcast_address").asText();
                    ServerAddress address = new ServerAddress(host, node.get("tcp_port").asInt());
                    addresses.add(address);
                }
            }catch (IOException e){
                logger.error("",e);
            }
        }
        if (addresses.isEmpty()) {
            logger.warn("Unable to connect to any NSQ Lookup servers, servers tried: " + lookups.toString());
        }
        return addresses;
    }
}
