package org.example.edumanagementservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class ScoreStatRepository {

    @PersistenceContext
    private EntityManager em;

    public Map<String, Object> getStatByCourseId(Integer courseId) {
        StoredProcedureQuery query = em.createStoredProcedureQuery("stat_course_scores");
        query.registerStoredProcedureParameter("courseId", Integer.class, ParameterMode.IN);
        query.setParameter("courseId", courseId);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) return Collections.emptyMap();

        Object[] row = results.get(0);
        return Map.of(
                "courseName", row[0],
                "studentCount", row[1],
                "avgScore", row[2],
                "maxScore", row[3],
                "minScore", row[4]
        );
    }
}
