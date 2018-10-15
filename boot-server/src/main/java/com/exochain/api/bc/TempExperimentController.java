package com.exochain.api.bc;

import com.exochain.api.bc.domain.Account;
import com.exochain.api.bc.persistence.entities.LoginChannel;
import com.exochain.api.bc.persistence.repositories.LoginChannelRepository;
import com.exochain.api.bc.service.Registrar;
import com.exochain.axon.trace.StandardTimeTraceInfo;
import com.exochain.axon.trace.StandardTimeTraceInfoBuilder;
import com.exochain.data.account.ExoAccountId;
import com.exochain.data.account.api.CreateExoAccountCmd;
import com.exochain.data.adjudication.claimtype.query.ClaimTypeSummary;
import com.exochain.data.adjudication.claimtype.query.FindClaimTypeSummaryQuery;
import com.exochain.data.adjudication.claimtype.query.FindClaimTypeSummaryResponse;
import com.exochain.data.adjudication.claimtype.ClaimTypeId;
import com.exochain.data.adjudication.claimtype.api.CreateClaimTypeCmd;
import com.exochain.data.adjudication.claimtype.api.UpdateClaimTypeCmd;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Future;

@Profile("internal")
@RestController
@RequestMapping("/api/removeme")
public class TempExperimentController {
    private static final XLogger L = XLoggerFactory.getXLogger(TempExperimentController.class);
    private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();

    // Prepopulated by Flyway DB script
    public static final String BC_CHANNEL_ID = "94f6f147-38f3-11e8-b12b-b3a10111c182";

    private final LoginChannelRepository loginChannels;
    private final CommandGateway commandGateway;
    private final Registrar registrar;
    private final QueryGateway queryGateway;
    private final Tracer tracer;

    @Autowired
    public TempExperimentController(CommandGateway commandGateway, LoginChannelRepository loginChannels,
                                    Registrar registrar, QueryGateway queryGateway, Tracer tracer) {
        L.debug("Initializing tmp controller with command gateway: {}", commandGateway);
        this.commandGateway = commandGateway;
        this.loginChannels = loginChannels;
        this.registrar = registrar;
        this.queryGateway = queryGateway;
        this.tracer = tracer;
    }

    @GetMapping("/createClaimType")
    public Future<Void> createClaim(@RequestParam("id") String typeId,
                                    @RequestParam("title") String title,
                                    @RequestParam("description") String claimDescription) {
        CreateClaimTypeCmd command = new CreateClaimTypeCmd(new ClaimTypeId(typeId), title, claimDescription);
        L.debug("Creating new claim with command: {}", command);

        Span currentSpan = tracer.getCurrentSpan();
        String spanId = Span.idToHex(currentSpan.getSpanId());
        String traceId = currentSpan.traceIdString();

        StandardTimeTraceInfo traceInfo = StandardTimeTraceInfoBuilder.aStandardTimeTraceInfo()
                .withCreationSpanId(spanId)
                .withCreationTraceId(traceId)
                .withModificationSpanId(spanId)
                .withModificationTraceId(traceId)
                .build();

        L.debug("Trace info created: [{}]", traceInfo);
        return commandGateway.send(command);
    }

    @GetMapping("/updateClaimType")
    public Long updateClaim(@RequestParam("id") String typeId,
                                                    @RequestParam("title") String title,
                                                    @RequestParam("description") String claimDescription,
                                                    @RequestParam("dataVersion") long dataVersion) {

        UpdateClaimTypeCmd cmd = new UpdateClaimTypeCmd(new ClaimTypeId(typeId), title, claimDescription, dataVersion);
        L.debug("Updating claim type with command: [{}]", cmd);
        return commandGateway.sendAndWait(cmd);
    }

    @GetMapping("/findClaimType")
    public List<ClaimTypeSummary> findClaim(@RequestParam("id") String typeId) {
        FindClaimTypeSummaryQuery query = new FindClaimTypeSummaryQuery(new ClaimTypeId(typeId));
        return queryGateway.query(query, FindClaimTypeSummaryResponse.class).join().getData();
    }

    /**
     * Utility method to add a login channel to database for testing.
     *
     * @param title
     * @param description
     * @return
     */
    @GetMapping("/addLoginChannel")
    public LoginChannel createLoginChannel(@RequestParam("title") String title,
                                    @RequestParam("description") String description) {
        LoginChannel channel = new LoginChannel();

        channel.setId(UUID_GENERATOR.generate().toString());
        channel.setTitle(title);
        channel.setDescription(description);

        L.debug("Creating new login channel: {}", channel);
        loginChannels.save(channel);
        return channel;
    }


    @GetMapping("/account/addAccount")
    public Future<Void> createExoAccount(@RequestParam("id") String id,
                                         @RequestParam("displayName") String displayName) {
        CreateExoAccountCmd cmd = new CreateExoAccountCmd(new ExoAccountId(id), displayName);
        L.debug("Creating ExoAccount with command: {}", cmd);
        return commandGateway.send(cmd);
    }
}
