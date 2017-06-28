package org.k2.processmining.support.mining.model;

/**
 * Created by nyq on 2017/6/25.
 */
public enum  DiagramType {

    PetriNet("PetriNet"), ResourceRelation("ResourceRelation"),
    Sankey("Sankey"), TransitionSystem("TransitionSystem");

    private String value;
    DiagramType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
