package edu.stanford.protege.webprotege.linearizationservice.handlers;


import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.linearizationservice.events.ClassDeletedEvent;
import edu.stanford.protege.webprotege.linearizationservice.repositories.history.LinearizationHistoryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ClassDeletedEventHandler implements EventHandler<ClassDeletedEvent> {

    private final LinearizationHistoryRepository linearizationHistoryRepository;

    public ClassDeletedEventHandler(LinearizationHistoryRepository linearizationHistoryRepository) {
        this.linearizationHistoryRepository = linearizationHistoryRepository;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ClassDeletedEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return this.getClass().getName();
    }

    @Override
    public Class<ClassDeletedEvent> getEventClass() {
        return ClassDeletedEvent.class;
    }

    @Override
    public void handleEvent(ClassDeletedEvent event) {
        if (event.deletedIris() == null || event.deletedIris().isEmpty()) {
            return;
        }
        event.deletedIris().forEach(iri ->
                linearizationHistoryRepository.deleteEntityHistory(iri.toString(), event.projectId())
        );
    }
}
