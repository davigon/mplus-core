package com.github.davigon.providers.data;

import com.github.davigon.domain.Programme;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class Xml {

    @XmlElement(name = "export", required = true)
    private Export export;

    public List<Programme> toProgrammeList() {
        List<Pase> pases = this.getExport().getPase();

        List<Programme> programmes = new ArrayList<>();
        HashMap<String, Integer> lastProgrammeIndex = new HashMap<>();

        for (Pase p : pases) {
            Programme pPrev;
            Integer lastIndex = lastProgrammeIndex.get(p.getCadena());
            if (lastIndex != null)
                pPrev = programmes.get(lastIndex);
            else
                pPrev = null;
            programmes.add(p.toProgramme(pPrev));
            lastProgrammeIndex.put(p.getCadena(), programmes.size() - 1);
        }
        return programmes;
    }
}
