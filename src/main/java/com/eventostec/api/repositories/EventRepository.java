package com.eventostec.api.repositories;

import java.sql.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eventostec.api.domain.event.Event;


public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.date >= :currentDate")
    public Page<Event> findUpComingEvents(@Param("currentDate") java.util.Date currentDate, Pageable pageable);

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
    "FROM Event e JOIN Address a ON e.id = a.event.id " +
    "WHERE (:city = '' OR a.city LIKE %:city%) " +
    "AND (:uf = '' OR a.uf LIKE %:uf%) " +
    "AND (e.date >= :startDate AND e.date <= :endDate)")
Page<Event> findFilteredEvents( @Param("currentDate") Date currentDate,
                                @Param("city") String title,
                                @Param("city") String city,
                                @Param("uf") String uf,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate,
                                Pageable pageable);

}