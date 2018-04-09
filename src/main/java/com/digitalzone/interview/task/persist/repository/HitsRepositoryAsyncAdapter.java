package com.digitalzone.interview.task.persist.repository;

import com.digitalzone.interview.task.persist.model.Hit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Adapter for async repository calls. Because repository does not have explicit implementation.
 */
@Component
public class HitsRepositoryAsyncAdapter {

    private final Logger logger;
    private final HitsRepository hitsRepository;

    @Autowired
    public HitsRepositoryAsyncAdapter(Logger logger, HitsRepository hitsRepository) {
        this.logger = logger;
        this.hitsRepository = hitsRepository;
    }

    /**
     * Persist hit asynchronously
     *
     * @param hit hit
     * @return persisted hit
     */
    @Async
    public Future<Hit> saveHit(Hit hit) {
        logger.debug("Saving hit asynchronously. UserId: {} URI:{} Date:{}", hit.getUserId(), hit.getUri(), hit.getDatetime());
        Hit persisted = hitsRepository.save(hit);
        logger.debug("Hit saved asynchronously. UserId: {} URI:{} Date:{}", hit.getUserId(), hit.getUri(), hit.getDatetime());
        return CompletableFuture.completedFuture(persisted);
    }
}
