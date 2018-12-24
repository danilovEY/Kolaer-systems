package ru.kolaer.server.core.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.kolaer.server.core.entity.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends PagingAndSortingRepository<T, Long>, JpaSpecificationExecutor<T> {
}
