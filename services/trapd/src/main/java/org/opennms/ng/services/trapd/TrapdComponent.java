package org.opennms.ng.services.trapd;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.opennms.netmgt.snmp.SnmpV3User;

import java.util.List;
import java.util.Map;

public class TrapdComponent extends DefaultComponent {

    private TrapdIpMgr trapdIpMgr;

    private String snmpTrapAddress;

    private Integer snmpTrapPort;

    private List<SnmpV3User> snmpV3Users;

    public TrapdComponent(TrapdIpMgr trapdIpMgr, String snmpTrapAddress, Integer snmpTrapPort, List<SnmpV3User> snmpV3Users) {
        this.trapdIpMgr = trapdIpMgr;
        this.snmpTrapAddress = snmpTrapAddress;
        this.snmpTrapPort = snmpTrapPort;
        this.snmpV3Users = snmpV3Users;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        TrapdConfiguration config = new TrapdConfiguration();
        config.setTrapdIpMgr(trapdIpMgr);
        config.setSnmpTrapAddress(snmpTrapAddress);
        config.setSnmpTrapPort(snmpTrapPort);
        config.setSnmpV3Users(snmpV3Users);

        return new TrapdEndpoint(config);
    }

    public void setTrapdIpMgr(TrapdIpMgr trapdIpMgr) {
        this.trapdIpMgr = trapdIpMgr;
    }

    public void setSnmpTrapAddress(String snmpTrapAddress) {
        this.snmpTrapAddress = snmpTrapAddress;
    }

    public void setSnmpTrapPort(Integer snmpTrapPort) {
        this.snmpTrapPort = snmpTrapPort;
    }

    public void setSnmpV3Users(List<SnmpV3User> snmpV3Users) {
        this.snmpV3Users = snmpV3Users;
    }
}
