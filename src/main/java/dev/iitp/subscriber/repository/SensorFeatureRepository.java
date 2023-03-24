package dev.iitp.subscriber.repository;

import dev.iitp.subscriber.model.feature.SensorRecordFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class SensorFeatureRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Long save(SensorRecordFeature sensorRecordFeature) {
        em.persist(sensorRecordFeature);
        return sensorRecordFeature.getId();
    }
}
