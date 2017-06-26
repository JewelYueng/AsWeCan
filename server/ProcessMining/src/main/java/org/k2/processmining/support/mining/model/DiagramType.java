package org.k2.processmining.support.mining.model;

/**
 * Created by nyq on 2017/6/25.
 */
public enum  DiagramType {

    PetriNet("PetriNet"), ResourceRelation("ResourceRelatrion"),
    Sankey("Sankey"), TransitionSystem("TransitionSystem");

    private String value;
    DiagramType(String value) {
        this.value = value;
    }

    public String getValue(String v) {
        DiagramType.valueOf(v);
        switch (v) {

        }
        return value;
    }
}
