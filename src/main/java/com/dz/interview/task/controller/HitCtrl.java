package com.dz.interview.task.controller;

import com.dz.interview.task.dto.Hits;
import com.dz.interview.task.dto.HitsCustom;
import com.dz.interview.task.dto.HitsRange;
import com.dz.interview.task.persist.model.Hit;
import com.dz.interview.task.service.HitsService;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HitCtrl {

    private final Logger logger;
    private final HitsService hitsService;

    @Autowired
    public HitCtrl(Logger logger, HitsService hitsService) {
        this.logger = logger;
        this.hitsService = hitsService;
    }

    /**
     * Register a visitor
     *
     * @param hit uri hit info representation
     * @return current hits report
     */
    @RequestMapping(method = RequestMethod.POST, path = "/hit")
    public Hits hit(@RequestBody Hit hit) {
        return getResponseObject(() -> hitsService.put(hit).get());
    }

    /**
     * Get hits by date range
     *
     * @param hitsRange date range
     * @return hits report by given range
     */
    @RequestMapping(method = RequestMethod.POST, path = "/hits")
    public HitsCustom hits(@RequestBody HitsRange hitsRange) {
        return getResponseObject(() -> hitsService.getCustom(hitsRange).get());
    }

    /**
     * "Handling" exception in one place
     *
     * @param callable payload
     * @param <V>      result type
     * @return result
     */
    private <V> V getResponseObject(Callable<V> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            logger.error("Error occurred:{}", e.getMessage());
            logger.debug("Error: {}", e);
            throw new RuntimeException(e);
        }
    }
}
