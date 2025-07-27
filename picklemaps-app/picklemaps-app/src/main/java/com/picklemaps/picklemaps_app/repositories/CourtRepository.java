package com.picklemaps.picklemaps_app.repositories;

import com.picklemaps.picklemaps_app.domain.entities.Court;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends ElasticsearchRepository<Court, String> {

    Page<Court> findByAverageRatingGreaterThanEqual(Float minRating, Pageable pageable);

    @Query(
            "{"
                    + "  \"bool\": {"
                    + "    \"must\": ["
                    + "      {\"range\": {\"averageRating\": {\"gte\": ?1}}}"
                    + "    ],"
                    + "    \"should\": ["
                    + "      {\"match\": {\"name\": \"?0\"}},"
                    + "      {\"match\": {\"courtType\": \"?0\"}},"
                    + "      {\"fuzzy\": {\"name\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}},"
                    + "      {\"fuzzy\": {\"courtType\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}"
                    + "    ],"
                    + "    \"minimum_should_match\": 1"
                    + "  }"
                    + "}")
    Page<Court> findByQueryAndMinRating(String query, Float minRating, Pageable pageable);

    @Query(
            "{"
                    + "  \"bool\": {"
                    + "    \"must\": ["
                    + "      {\"geo_distance\": {"
                    + "        \"distance\": \"?2km\","
                    + "        \"geoLocation\": {"
                    + "          \"lat\": ?0,"
                    + "          \"lon\": ?1"
                    + "        }"
                    + "      }}"
                    + "    ]"
                    + "  }"
                    + "}")
    Page<Court> findByLocationNear(
            Float latitude, Float longitude, Float radiusKm, Pageable pageable);
}
