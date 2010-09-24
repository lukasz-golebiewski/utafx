/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.pref;

import utafx.ui.pref.jaxb.Criteria;
import utafx.ui.criteria.CriteriaUI;
import utafx.ui.pref.jaxb.ObjectFactory;
import utafx.ui.pref.jaxb.CriteriaType;
import utafx.ui.alternative.AlternativesUI;
import utafx.ui.pref.jaxb.Alternatives;
import utafx.ui.rank.ReferenceRankUI;
import utafx.ui.pref.jaxb.RefRank;
import uta.Alternative;
import uta.Ranking;

/**
 * This class acts like a bridge between the FX classes and JAXB generated classes.
It is used to simplify the process of importing and exporting
preferences.
 */
public class JAXBSupport {

    public function convert(data: uta.Criterion[]): Criteria {
        var returnObject: Criteria;
        if (data != null) {
            var factory = ObjectFactory {};
            returnObject = factory.createCriteria();
            for (d in data) {
                var c = factory.createCriterion();
                c.setId(indexof d);
                c.setName(d.getName());
                c.setSegments(d.getNoOfSegments());
                if (d.isGain()) {
                    c.setType(CriteriaType.GAIN);
                } else {
                    c.setType(CriteriaType.COST);
                }
                returnObject.getCriterion().add(c);
            }
        }
        return returnObject;
    }

    public function convert(data: uta.Alternative[]): Alternatives {
        var returnObject: Alternatives;
        if (data != null) {
            var factory = ObjectFactory {};
            returnObject = factory.createAlternatives();
            for (d in data) {
                var a = factory.createAlternative();
                a.setId(indexof d);
                a.setName(d.getName());
                var values = factory.createAltValues();
                for (v in d.getValues()) {
                    var vv = factory.createValue();
                    vv.setId(indexof v);
                    vv.setValue("{v}");
                    values.getValue().add(vv);
                }
                a.setValues(values);
                returnObject.getAlternative().add(a);
            }
        }
        return returnObject;
    }

    public function convert(refRankData: Ranking, alterns: uta.Alternative[]): RefRank {
        var returnObject: RefRank;
        if (refRankData != null) {
            var factory = ObjectFactory {};
            returnObject = factory.createRefRank();
            for (aName in refRankData.getAlternatives()) {
                var rrItem = factory.createRrItem();
                rrItem.setId(getIndexOf(aName as String, alterns));
                rrItem.setRank(refRankData.getRank(aName));
                returnObject.getItem().add(rrItem);
            }
        }
        return returnObject;
    }

    function getIndexOf(alternativeName: String, all: uta.Alternative[]) {
        for (a in all) {
            if (a.getName() == alternativeName) {
                return indexof a;
            }
        }
        return -1;
    }

}
