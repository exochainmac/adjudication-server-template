package com.exochain.data.adjudication.claimtype;

import com.exochain.data.adjudication.claimtype.api.ClaimTypeCreatedEvt;
import com.exochain.data.adjudication.claimtype.api.ClaimTypeUpdatedEvt;
import com.exochain.data.adjudication.claimtype.api.CreateClaimTypeCmd;
import com.exochain.data.adjudication.claimtype.api.UpdateClaimTypeCmd;
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
public class ClaimTypeAggregate {
    private static final XLogger L = XLoggerFactory.getXLogger(ClaimTypeAggregate.class);

    @AggregateIdentifier
    private ClaimTypeId id;
    private String title;
    private String description;
    private Long dataVersion;

    public ClaimTypeAggregate() {
    }

    @CommandHandler
    public ClaimTypeAggregate(CreateClaimTypeCmd cmd, MetaData metaData) {
        L.entry(cmd, metaData);
        // TODO: Prevent duplicate title creation
        apply(new ClaimTypeCreatedEvt(cmd));
        L.exit();
    }

    @CommandHandler
    public Long handleUpdate(UpdateClaimTypeCmd cmd, MetaData metaData) {
        L.entry(cmd, metaData);
        apply(new ClaimTypeUpdatedEvt(cmd));
        return L.exit(dataVersion);
    }

    @EventSourcingHandler
    public void onCreate(ClaimTypeCreatedEvt evt, MetaData metaData, @SequenceNumber long aggVersion) {
        L.entry(evt, metaData);
        id = evt.getTypeId();
        title = evt.getTitle();
        description = evt.getDescription();
        dataVersion = aggVersion;
        L.debug("New ClaimDefinitionAggregate.  Event: {}, Metadata: {}, dataVersion {}", evt, metaData, aggVersion);
        L.exit();
    }

    @EventSourcingHandler
    public void onUpdate(ClaimTypeUpdatedEvt evt, MetaData metaData, @SequenceNumber long aggVersion) {
        L.entry(evt, metaData);
        title = evt.getTitle();
        description = evt.getDescription();
        dataVersion = aggVersion;
        L.debug("Updated claim type.  Event: {}, Metadata: {}, dataVersion {}", evt, metaData, aggVersion);
        L.exit();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("description", description)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimTypeAggregate claimTypeAggregate = (ClaimTypeAggregate) o;
        return Objects.equal(id, claimTypeAggregate.id) &&
                Objects.equal(title, claimTypeAggregate.title) &&
                Objects.equal(description, claimTypeAggregate.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, title, description);
    }
}
