package com.digitalzone.interview.task.service;

import com.digitalzone.interview.task.dto.Hits;
import com.digitalzone.interview.task.persist.model.Hit;
import com.digitalzone.interview.task.persist.repository.HitsRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CounterService {

    private final Logger logger;

    private AtomicLong hits;
    private Set<String> entratsCache = ConcurrentHashMap.newKeySet();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final HitsRepository hitsRepository;

    @Autowired
    public CounterService(Logger logger, HitsRepository hitsRepository) {
        this.logger = logger;
        this.hitsRepository = hitsRepository;
    }

    /**
     * Bean initialization on startup. In case of server reboot.
     * Obviously won't work with in memory embedded h2 (all data will be zero here)
     */
    @PostConstruct
    public void init() {
        logger.info("PostConstruct");
        Date end = new Date();
        Date begin = new Date(end.getTime() - (end.getTime() % (24 * 60 * 60 * 1000)));

        hits = new AtomicLong(hitsRepository.getTotalCount(begin, end));
        entratsCache.addAll(hitsRepository.getUniqueUsers(begin, end));
    }

    /**
     * Add a hit and get actual state.
     *
     * @param hit hit representation
     * @return actual state representation
     */
    @Async
    Future<Hits> incrementAndGet(Hit hit) {
        return CompletableFuture.completedFuture(doInLock(() -> {
            entratsCache.add(hit.getUserId());
            return new Hits(hits.incrementAndGet(), entratsCache.size());
        }));
    }

    /**
     * This scheduler zeroes counts and users caches once a day at midnight
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cron() {
        doInLock((Callable<Void>) () -> {
            hits.set(0);
            entratsCache.clear();
            return null;
        });
    }

    /**
     * Just for convenience
     *
     * @param callable callable to execute
     * @param <V>      result value type
     * @return result of execution
     */
    private <V> V doInLock(Callable<V> callable) {
        lock.writeLock().lock();
        try {
            return callable.call();
        } catch (Exception e) {
            logger.error("Error occurred:{}", e.getMessage());
            logger.debug("Error: {}", e);
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
