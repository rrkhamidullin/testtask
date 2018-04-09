package com.digitalzone.interview.task.service;

import com.digitalzone.interview.task.dto.Hits;
import com.digitalzone.interview.task.dto.HitsCustom;
import com.digitalzone.interview.task.dto.HitsRange;
import com.digitalzone.interview.task.persist.model.Hit;
import com.digitalzone.interview.task.persist.repository.HitsRepository;
import com.digitalzone.interview.task.persist.repository.HitsRepositoryAsyncAdapter;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HitsService {

    private final Logger logger;

    private final CounterService counterService;

    private final HitsRepository hitsRepository;

    private final HitsRepositoryAsyncAdapter repositoryAsyncAdapter;

    private static final Long ACTIVE_USER_HITS_THRESHOLD = 10L;//TODO move it to properties

    @Autowired
    public HitsService(Logger logger, CounterService counterService, HitsRepository hitsRepository, HitsRepositoryAsyncAdapter repositoryAsyncAdapter) {
        this.logger = logger;
        this.counterService = counterService;
        this.hitsRepository = hitsRepository;
        this.repositoryAsyncAdapter = repositoryAsyncAdapter;
    }

    /**
     * Add a hit
     *
     * @param hit hit
     * @return future current hits state representation
     */
    public Future<Hits> put(Hit hit) {
        hit.setDatetime(new Date());
        logger.debug("Passing hit for persistence. UserId:{} URI:{} Date:{} ", hit.getUserId(), hit.getUri(), hit.getDatetime());
        repositoryAsyncAdapter.saveHit(hit);
        logger.debug("Hit passed for persistence. UserId:{} URI:{} Date:{} ", hit.getUserId(), hit.getUri(), hit.getDatetime());
        return counterService.incrementAndGet(hit);
    }

    /**
     * Get hits report for given date range
     *
     * @param hitsRange date range
     * @return future hits report representation
     */
    @Async
    public Future<HitsCustom> getCustom(HitsRange hitsRange) {
        long total = hitsRepository.getTotalCount(hitsRange.getBegin(), hitsRange.getEnd());
        long unique = hitsRepository.getUniqueCount(hitsRange.getBegin(), hitsRange.getEnd());
        long retained = hitsRepository.getRetainedUsers(hitsRange.getBegin(), hitsRange.getEnd(), ACTIVE_USER_HITS_THRESHOLD);
        return CompletableFuture.completedFuture(new HitsCustom(total, unique, retained, hitsRange));
    }
}
