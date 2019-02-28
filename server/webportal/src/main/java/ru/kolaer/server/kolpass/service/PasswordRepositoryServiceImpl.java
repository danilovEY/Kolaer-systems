package ru.kolaer.server.kolpass.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.auth.AccountDto;
import ru.kolaer.common.dto.auth.AccountSimpleDto;
import ru.kolaer.common.dto.error.UnexpectedParamsDescription;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.account.service.AccountService;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.model.dto.FilterType;
import ru.kolaer.server.core.model.dto.ResultUpdate;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.kolpass.dao.PasswordHistoryDao;
import ru.kolaer.server.kolpass.dao.PasswordRepositoryDao;
import ru.kolaer.server.kolpass.model.entity.PasswordHistoryEntity;
import ru.kolaer.server.kolpass.model.entity.PasswordRepositoryEntity;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordFilter;
import ru.kolaer.server.kolpass.model.request.RepositoryPasswordSort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 20.01.2017.
 */
@Service
public class PasswordRepositoryServiceImpl
        extends AbstractDefaultService<PasswordRepositoryDto, PasswordRepositoryEntity, PasswordRepositoryDao, PasswordRepositoryConverter>
        implements PasswordRepositoryService {

    private final PasswordHistoryDao passwordHistoryDao;
    private final PasswordHistoryConverter passwordHistoryConverter;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    public PasswordRepositoryServiceImpl(PasswordRepositoryDao passwordRepositoryDao,
                                         PasswordRepositoryConverter converter,
                                         PasswordHistoryDao passwordHistoryDao,
                                         PasswordHistoryConverter passwordHistoryConverter,
                                         AccountService accountService,
                                         AuthenticationService authenticationService) {
        super(passwordRepositoryDao, converter);
        this.passwordHistoryDao = passwordHistoryDao;
        this.passwordHistoryConverter = passwordHistoryConverter;
        this.accountService = accountService;
        this.authenticationService = authenticationService;
    }



    @Override
    @Transactional(readOnly = true)
    public PageDto<PasswordRepositoryDto> getAll(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                              Integer number, Integer pageSize) {
        RepositoryPasswordFilter filter = Optional.ofNullable(filterParam)
                .orElse(new RepositoryPasswordFilter());

        filter.setFilterAccountId(authenticationService.getAccountAuthorized().getId());

        return super.getAll(sortParam, filterParam, number, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<PasswordRepositoryDto> getAllShared(RepositoryPasswordSort sortParam, RepositoryPasswordFilter filterParam,
                                                    Integer number, Integer pageSize) {

        AccountAuthorizedDto currentAccount = authenticationService.getAccountAuthorized();

        List<Long> repIds = defaultEntityDao.findAllRepositoryFromShare(currentAccount.getId());
        if(repIds.isEmpty()) {
            return new PageDto<>();
        }

        RepositoryPasswordFilter filter = Optional.ofNullable(filterParam)
                .orElse(new RepositoryPasswordFilter());

        filter.setFilterIds(repIds);
        filter.setTypeFilterIds(FilterType.IN);
        filter.setFilterAccountId(null);

        return super.getAll(sortParam, filter, number, pageSize);
    }

    @Override
    @Transactional
    public PasswordRepositoryDto add(PasswordRepositoryDto dto) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultConverter.convertToModel(dto);
        passwordRepositoryEntity.setAccountId(accountSimpleByAuthentication.getId());

        return defaultConverter.convertToDto(defaultEntityDao.persist(passwordRepositoryEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordRepositoryDto getById(Long repId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);
        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId()) &&
                !allAccountFromShareRepository.contains(accountSimpleByAuthentication.getId())) {
            throw new ForbiddenException();
        }

        return super.getById(repId);
    }

    @Override
    @Transactional
    public PasswordHistoryDto addPassword(Long repId, PasswordHistoryDto passwordHistoryDto) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        PasswordHistoryEntity passwordHistoryEntity = passwordHistoryConverter.convertToModel(passwordHistoryDto);

        passwordHistoryEntity.setId(null);
        passwordHistoryEntity.setPasswordWriteDate(LocalDateTime.now());
        passwordHistoryEntity.setRepositoryPassId(repId);

        return passwordHistoryConverter.convertToDto(passwordHistoryDao.persist(passwordHistoryEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<PasswordHistoryDto> getHistoryByIdRepository(Long repId, Integer number, Integer pageSize) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);
        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId()) &&
                !allAccountFromShareRepository.contains(accountSimpleByAuthentication.getId())) {
            throw new ForbiddenException();
        }

        if(pageSize == null || pageSize < 1) {
            List<PasswordHistoryDto> result = passwordHistoryDao.findAllHistoryByIdRepository(repId)
                    .stream()
                    .map(passwordHistoryConverter::convertToDto)
                    .collect(Collectors.toList());
            return new PageDto<>(result, 0, 0, result.size());
        } else {
            Long count = passwordHistoryDao.findCountHistoryByIdRepository(repId, number, pageSize);
            List<PasswordHistoryDto> result = passwordHistoryDao.findHistoryByIdRepository(repId, number, pageSize)
                    .stream()
                    .map(passwordHistoryConverter::convertToDto)
                    .collect(Collectors.toList());

            return new PageDto<>(result, number, count, pageSize);
        }
    }

    @Override
    @Transactional
    public void deleteByIdRep(Long repId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        passwordHistoryDao.deleteAllByIdRep(repId);
        defaultEntityDao.delete(passwordRepositoryEntity);
    }

    @Override
    @Transactional
    public void clearRepository(Long repId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        passwordHistoryDao.deleteAllByIdRep(repId);
    }

    @Override
    @Transactional
    public void shareRepository(Long repId, Long accountId) {
        if(accountId == null || accountId < 1) {
            throw new UnexpectedRequestParams("Не указаны пользователи");
        }

        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(!allAccountFromShareRepository.contains(accountId)) {
            defaultEntityDao.shareRepositoryToAccount(repId, accountId);
        } else {
            throw new UnexpectedRequestParams("Данный пароль пользователю уже расшарин");
        }
    }

    @Override
    @Transactional
    public void deleteAccountFromShare(Long repId, Long accountId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        defaultEntityDao.deleteShareRepositoryToAccount(repId, accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccountFromShare(Long repId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(allAccountFromShareRepository.isEmpty()) {
            return Collections.emptyList();
        }

        return this.accountService.getById(allAccountFromShareRepository);
    }

    @Override
    @Transactional
    public void deletePassword(Long repId, Long passId) {
        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        PasswordHistoryEntity passwordHistoryEntity = passwordHistoryDao.findByRepAndId(repId, passId);

        if(passwordHistoryEntity == null) {
            throw new NotFoundDataException("Нет записи в репозитории");
        }

        passwordHistoryDao.delete(passwordHistoryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordHistoryDto getLastHistoryInRepository(Long repId) {
        if(repId == null) {
            throw new UnexpectedRequestParams(new UnexpectedParamsDescription("id", "ID не должен быть пустым"));
        }

        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(passwordRepositoryEntity == null) {
            throw new NotFoundDataException("Такого репозитория нет");
        }

        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId()) &&
                !allAccountFromShareRepository.contains(accountSimpleByAuthentication.getId())) {
            throw new ForbiddenException();
        }

        PasswordHistoryEntity lastHistory = this.passwordHistoryDao.findLastHistoryInRepository(repId);
        if(lastHistory == null) {
            throw new NotFoundDataException("Репозиторий пуст");
        }

        return passwordHistoryConverter.convertToDto(lastHistory);
    }

    @Override
    @Transactional
    public PasswordRepositoryDto update(PasswordRepositoryDto dto) {
        if(dto.getId() == null) {
            throw new UnexpectedRequestParams(new UnexpectedParamsDescription("id", "ID не должен быть пустым"));
        }

        AccountAuthorizedDto accountSimpleByAuthentication = authenticationService.getAccountAuthorized();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(dto.getId());

        if(!Long.valueOf(accountSimpleByAuthentication.getId()).equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        passwordRepositoryEntity.setName(dto.getName());
        passwordRepositoryEntity.setUrlImage(dto.getUrlImage());

        return defaultConverter.convertToDto(defaultEntityDao.update(passwordRepositoryEntity));
    }

    @Override
    @Transactional
    @Async
    public void updateEmployee(ResultUpdate resultUpdate) {
        List<AccountSimpleDto> addAccounts = resultUpdate.getAddAccounts();

        if(addAccounts.isEmpty()) {
            return;
        }

        Map<Long, AccountSimpleDto> accountMap = addAccounts.stream()
                .collect(Collectors.toMap(AccountSimpleDto::getId, Function.identity()));

        List<PasswordRepositoryEntity> repositories = addAccounts.stream()
                .map(this::accountToRepository)
                .collect(Collectors.toList());

        repositories = defaultEntityDao.save(repositories);

        LocalDateTime now = LocalDateTime.now();

        List<PasswordHistoryEntity> histories = repositories.stream()
                .map(repository -> {
                    AccountSimpleDto accountSimpleDto = accountMap.get(repository.getAccountId());

                    PasswordHistoryEntity passwordHistoryEntity = new PasswordHistoryEntity();
                    passwordHistoryEntity.setRepositoryPassId(repository.getId());
                    passwordHistoryEntity.setLogin(accountSimpleDto.getUsername());
                    passwordHistoryEntity.setPassword(accountSimpleDto.getUsername());
                    passwordHistoryEntity.setPasswordWriteDate(now);

                    return passwordHistoryEntity;
                }).collect(Collectors.toList());

        passwordHistoryDao.save(histories);
    }

    private PasswordRepositoryEntity accountToRepository(AccountSimpleDto accountSimpleDto) {
        PasswordRepositoryEntity passwordRepositoryEntity = new PasswordRepositoryEntity();
        passwordRepositoryEntity.setName("Система КолАЭР");
        passwordRepositoryEntity.setAccountId(accountSimpleDto.getId());

        return passwordRepositoryEntity;
    }
}
