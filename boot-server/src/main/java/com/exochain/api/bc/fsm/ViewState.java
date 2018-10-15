package com.exochain.api.bc.fsm;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jeasy.states.api.State;

public class ViewState extends State {
    private final String viewName;

    private ViewState(String name) {
        this("N/A", "N/A");

    }

    public ViewState(String name, String viewName) {
        super(name);
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ViewState viewState = (ViewState) o;
        return Objects.equal(viewName, viewState.viewName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), viewName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("viewName", viewName)
                .toString();
    }
}
