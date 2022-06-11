package com.github.davigon.providers.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "export")
public class Export {

    @XmlElement(name = "pase", required = true)
    private List<Pase> pase;

    public List<Pase> getPase() {
        if (pase == null) {
            pase = new ArrayList<Pase>();
        }
        return this.pase;
    }
}
