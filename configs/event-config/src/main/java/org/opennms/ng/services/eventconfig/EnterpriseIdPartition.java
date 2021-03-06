package org.opennms.ng.services.eventconfig;

import org.opennms.netmgt.xml.eventconf.Event;
import org.opennms.netmgt.xml.eventconf.EventMatchers;
import org.opennms.netmgt.xml.eventconf.Field;
import org.opennms.netmgt.xml.eventconf.Partition;

import java.util.List;

public class EnterpriseIdPartition implements Partition {

    private Field m_field = EventMatchers.field("id");

    @Override
    public List<String> group(Event eventConf) {
        return eventConf.getMaskElementValues("id");
    }

    @Override
    public String group(org.opennms.netmgt.xml.event.Event matchingEvent) {
        return m_field.get(matchingEvent);
    }

}
