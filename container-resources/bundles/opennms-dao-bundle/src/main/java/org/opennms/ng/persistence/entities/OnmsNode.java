package org.opennms.ng.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "node")
@Entity()
@Table(name = "node")
@SecondaryTable(name = "pathOutage")
public class OnmsNode extends org.opennms.netmgt.model.OnmsNode {

    @Override
    @Id
    @Column(name = "nodeId", nullable = false)
    @SequenceGenerator(name = "nodeSequence", sequenceName = "nodeNxtId")
    @GeneratedValue(generator = "nodeSequence", strategy = GenerationType.SEQUENCE)
    @XmlTransient
    public Integer getId() {
        return super.getId();    //TODO
    }


}
