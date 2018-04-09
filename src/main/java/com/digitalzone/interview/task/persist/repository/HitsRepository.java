package com.digitalzone.interview.task.persist.repository;

import com.digitalzone.interview.task.persist.model.Hit;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HitsRepository extends JpaRepository<Hit, String> {

    /**
     * Get total hits count by given date range from persistence
     *
     * @param begin date range begin
     * @param end   date range end
     * @return total hits count by given date range
     */
    @Query("SELECT COUNT(h) FROM Hit h WHERE h.datetime BETWEEN ?1 AND ?2")
    long getTotalCount(Date begin, Date end);

    /**
     * Get unique hits count by given date range
     *
     * @param begin date range begin
     * @param end   date range end
     * @return unique hits count by given date range
     */
    @Query("SELECT COUNT(DISTINCT h.userId) FROM Hit h WHERE h.datetime BETWEEN ?1 AND ?2")
    long getUniqueCount(Date begin, Date end);

    /**
     * Get active users count by given date range and hits threshold
     *
     * @param begin     date range begin
     * @param end       date range end
     * @param threshold number of desired hits by active user
     * @return active users count by given date range
     */
    @Query("SELECT COUNT(DISTINCT h.userId) FROM Hit h WHERE h.datetime BETWEEN ?1 AND ?2 AND (SELECT COUNT (ih) FROM Hit ih WHERE ih.datetime BETWEEN ?1 AND ?2 AND ih.userId = h.userId) > ?3")
    long getRetainedUsers(Date begin, Date end, long threshold);

    /**
     * Get unique users by given date range
     *
     * @param begin date range begin
     * @param end   date range end
     * @return collection of unique user ids
     */
    @Query("SELECT DISTINCT h.userId FROM Hit h WHERE h.datetime BETWEEN ?1 AND ?2")
    Collection<String> getUniqueUsers(Date begin, Date end);
}
