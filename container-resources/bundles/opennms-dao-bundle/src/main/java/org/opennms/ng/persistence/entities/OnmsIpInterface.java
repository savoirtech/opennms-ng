package org.opennms.ng.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ipInterface")
@Entity
@Table(name = "ipInterface")
public class OnmsIpInterface extends org.opennms.netmgt.model.OnmsIpInterface {

    @Override
    @Id
    @Column(nullable = false)
    @XmlTransient
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence", strategy = GenerationType.SEQUENCE)
    public Integer getId() {
        return super.getId();
    }



}
