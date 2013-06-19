package org.opennms.ng.services.trapd;

import org.opennms.netmgt.snmp.SnmpV3User;

import java.util.List;

public class TrapdConfiguration {

    private TrapdIpMgr trapdIpMgr;

    private String snmpTrapAddress;

    private Integer snmpTrapPort;

    private List<SnmpV3User> snmpV3Users;

    public TrapdIpMgr getTrapdIpMgr() {
        return trapdIpMgr;
    }

    public void setTrapdIpMgr(TrapdIpMgr trapdIpMgr) {
        this.trapdIpMgr = trapdIpMgr;
    }

    public String getSnmpTrapAddress() {
        return snmpTrapAddress;
    }

    public void setSnmpTrapAddress(String snmpTrapAddress) {
        this.snmpTrapAddress = snmpTrapAddress;
    }

    public Integer getSnmpTrapPort() {
        return snmpTrapPort;
    }

    public void setSnmpTrapPort(Integer snmpTrapPort) {
        this.snmpTrapPort = snmpTrapPort;
    }

    public List<SnmpV3User> getSnmpV3Users() {
        return snmpV3Users;
    }

    public void setSnmpV3Users(List<SnmpV3User> snmpV3Users) {
        this.snmpV3Users = snmpV3Users;
    }
}
