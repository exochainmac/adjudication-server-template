package com.exochain.data.account;

import com.exochain.data.account.api.CreateExoAccountCmd;
import com.exochain.data.account.api.ExoAccountCreatedEvt;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.SequenceNumber;
import org.axonframework.messaging.MetaData;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class ExoAccountAggregate {
    private static final XLogger L = XLoggerFactory.getXLogger(ExoAccountAggregate.class);

    @AggregateIdentifier
    private ExoAccountId id;
    private String displayName;
    private Long dataVersion;

    public ExoAccountAggregate() {
    }

    @CommandHandler
    public ExoAccountAggregate(CreateExoAccountCmd cmd, MetaData metaData) {
        L.entry(cmd, metaData);
        apply(new ExoAccountCreatedEvt(cmd));
        L.exit();
    }

    @EventSourcingHandler
    public void onCreate(ExoAccountCreatedEvt evt, MetaData metaData, @SequenceNumber long aggVersion) {
        L.entry(evt, metaData, aggVersion);
        id = evt.getId();
        displayName = evt.getDisplayName();
        dataVersion = aggVersion;
        L.debug("New ExoAccountAggregate.  Event: {}, dataVersion: {}", evt, dataVersion);
        L.exit();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("displayName", displayName)
                .add("dataVersion", dataVersion)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExoAccountAggregate that = (ExoAccountAggregate) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(displayName, that.displayName) &&
                Objects.equal(dataVersion, that.dataVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, displayName, dataVersion);
    }
}
