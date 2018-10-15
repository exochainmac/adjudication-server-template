package com.exochain.data.adjudication.claimtype.query;

import com.exochain.data.adjudication.claimtype.api.ClaimTypeCreatedEvt;
import com.exochain.data.adjudication.claimtype.api.ClaimTypeUpdatedEvt;
import com.google.common.collect.ImmutableList;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.eventsourcing.SequenceNumber;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

// import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

public class ClaimTypeSummaryProjection {
    private static final XLogger L = XLoggerFactory.getXLogger(ClaimTypeSummaryProjection.class);

    private final EntityManager entityManager;
    private final EventBus queryUdateEventBus;


    public ClaimTypeSummaryProjection(EntityManager entityManager, @Qualifier("queryUpdates") EventBus queryUdateEventBus) {
        this.entityManager = entityManager;
        this.queryUdateEventBus = queryUdateEventBus;
    }

    @EventHandler
    public void onClaimTypeCreated(ClaimTypeCreatedEvt evt, @Timestamp Instant instant, @SequenceNumber long aggVersion) {
        L.entry(evt, instant, aggVersion);
        ClaimTypeSummary claimTypeSummary = new ClaimTypeSummary(evt);

        claimTypeSummary.setDataVersion(aggVersion);
        entityManager.persist(claimTypeSummary);

        // Publish disabled for now because their is nothing listening
        // queryUdateEventBus.publish(asEventMessage(new ClaimTypeSummaryUpdatedEvt(evt.getTypeId())));
        L.exit();
    }

    @EventHandler
    public void onClaimTypeUpdated(ClaimTypeUpdatedEvt evt, @Timestamp Instant instant, @SequenceNumber long aggVersion) {
        L.entry(evt, instant, aggVersion);
        ClaimTypeSummary summary = entityManager.find(ClaimTypeSummary.class, evt.getTypeId().getValue());
        summary.setDataVersion(aggVersion);
        summary.setTitle(evt.getTitle());
        summary.setDescription(evt.getDescription());
        // Publish disabled for now because their is nothing listening
        // queryUdateEventBus.publish(asEventMessage(new ClaimTypeSummaryUpdatedEvt(evt.getTypeId())));
        L.exit();
    }

    @QueryHandler
    public FindClaimTypeSummaryResponse findClaimTypeSummaryResponse(FindClaimTypeSummaryQuery query) {
        L.entry(query);
        ClaimTypeSummary summary = entityManager.find(ClaimTypeSummary.class, query.getTypeId().getValue());
        List<ClaimTypeSummary> data;
        if(null == summary) {
            data = Collections.emptyList();
        } else {
            data = ImmutableList.of(summary);
        }
        FindClaimTypeSummaryResponse response = new FindClaimTypeSummaryResponse(data);
        return L.exit(response);
    }

}
