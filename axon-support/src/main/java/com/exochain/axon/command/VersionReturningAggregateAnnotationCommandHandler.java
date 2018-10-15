package com.exochain.axon.command;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.AnnotationCommandTargetResolver;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.VersionedAggregateIdentifier;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Subclass of AggregateAnnotationCommandHandler that ensures that constructor command handler return not just
 * the aggregate identifier, but a versioned aggregate identifier containing both identifier and version.
 */
public class VersionReturningAggregateAnnotationCommandHandler<T> extends AggregateAnnotationCommandHandler<T> {
    private static final XLogger L = XLoggerFactory.getXLogger(VersionReturningAggregateAnnotationCommandHandler.class);


    public VersionReturningAggregateAnnotationCommandHandler(Class<T> aggregateType, Repository<T> repository) {
        super(aggregateType, repository, new AnnotationCommandTargetResolver());
        L.entry(aggregateType, repository);
    }

    @Override
    protected Object resolveReturnValue(CommandMessage<?> command, Aggregate<T> createdAggregate) {
        L.entry(command, createdAggregate);
        return L.exit(new VersionedAggregateIdentifier(createdAggregate.identifier().toString(), createdAggregate.version()));
    }

}
