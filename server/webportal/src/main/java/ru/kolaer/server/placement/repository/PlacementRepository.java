package ru.kolaer.server.placement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.placement.model.entity.PlacementEntity;

import java.util.List;

@Repository
public interface PlacementRepository extends BaseRepository<PlacementEntity> {

    @Query("FROM PlacementEntity WHERE name LIKE %:searchText%")
    List<PlacementEntity> findAllByName(@Param("searchText") String searchText);

}
