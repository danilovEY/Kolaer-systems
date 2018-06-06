package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.error.UnexpectedParamsDescription;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordHistoryDto;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.server.webportal.exception.ForbiddenException;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.PasswordHistoryConverter;
import ru.kolaer.server.webportal.mvc.model.converter.PasswordRepositoryConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordHistoryDao;
import ru.kolaer.server.webportal.mvc.model.dao.PasswordRepositoryDao;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordHistoryEntity;
import ru.kolaer.server.webportal.mvc.model.entities.kolpass.PasswordRepositoryEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.AccountService;
import ru.kolaer.server.webportal.mvc.model.servirces.AuthenticationService;
import ru.kolaer.server.webportal.mvc.model.servirces.PasswordRepositoryService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    public Page<PasswordRepositoryDto> getAllAuthAccount(Integer number, Integer pageSize) {
        return this.getAllByAccountId(this.authenticationService.getAccountSimpleByAuthentication().getId(), number, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PasswordRepositoryDto> getAllByAccountId(Long accountId, Integer number, Integer pageSize) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(accountId)) {
            throw new ForbiddenException();
        }

        if(pageSize == null || pageSize == 0) {
            List<PasswordRepositoryDto> result = this.getAllByAccountIds(Collections.singletonList(accountId));
            return new Page<>(result, 0, 0, result.size());
        } else {
            Long count = defaultEntityDao.findCountAllAccountId(accountId, number, pageSize);
            List<PasswordRepositoryDto> result = defaultEntityDao.findAllByAccountId(accountId, number, pageSize)
                    .stream()
                    .map(defaultConverter::convertToDto)
                    .collect(Collectors.toList());
            return new Page<>(result, number, count, pageSize);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PasswordRepositoryDto> getAllByAccountIds(List<Long> idsChief) {
        return defaultEntityDao.findAllByAccountId(idsChief)
                .stream()
                .map(defaultConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PasswordRepositoryDto add(PasswordRepositoryDto dto) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        dto.setId(null);
        dto.setAccountId(accountSimpleByAuthentication.getId());

        return super.add(dto);
    }

    @Override
    @Transactional
    public PasswordHistoryDto addPassword(Long repId, PasswordHistoryDto passwordHistoryDto) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
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
    public Page<PasswordHistoryDto> getHistoryByIdRepository(Long repId, Integer number, Integer pageSize) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        if(pageSize == null || pageSize == 0) {
            List<PasswordHistoryDto> result = passwordHistoryDao.findAllHistoryByIdRepository(repId)
                    .stream()
                    .map(passwordHistoryConverter::convertToDto)
                    .collect(Collectors.toList());
            return new Page<>(result, 0, 0, result.size());
        } else {
            Long count = passwordHistoryDao.findCountHistoryByIdRepository(repId, number, pageSize);
            List<PasswordHistoryDto> result = passwordHistoryDao.findHistoryByIdRepository(repId, number, pageSize)
                    .stream()
                    .map(passwordHistoryConverter::convertToDto)
                    .collect(Collectors.toList());

            return new Page<>(result, number, count, pageSize);
        }
    }

    @Override
    @Transactional
    public void deleteByIdRep(Long repId) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        passwordHistoryDao.deleteAllByIdRep(repId);
        defaultEntityDao.delete(passwordRepositoryEntity);
    }

    @Override
    @Transactional
    public void clearRepository(Long repId) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
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

        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
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
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
            throw new ForbiddenException();
        }

        defaultEntityDao.deleteShareRepositoryToAccount(repId, accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccountFromShare(Long repId) {
        List<Long> allAccountFromShareRepository = defaultEntityDao.findAllAccountFromShareRepository(repId);

        if(allAccountFromShareRepository.isEmpty()) {
            return Collections.emptyList();
        }

        return this.accountService.getById(allAccountFromShareRepository);
    }

    @Override
    @Transactional
    public void deletePassword(Long repId, Long passId) {
        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(!accountSimpleByAuthentication.isAccessOit() && !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
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

        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(repId);

        if(passwordRepositoryEntity == null) {
            throw new NotFoundDataException("Такого репозитория нет");
        }

        if(!accountSimpleByAuthentication.isAccessOit() &&
                !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId())) {
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

        AccountSimpleDto accountSimpleByAuthentication = authenticationService.getAccountSimpleByAuthentication();

        PasswordRepositoryEntity passwordRepositoryEntity = defaultEntityDao.findById(dto.getId());

        if(!accountSimpleByAuthentication.isAccessOit() &&
                !accountSimpleByAuthentication.getId().equals(passwordRepositoryEntity.getAccountId()) ||
                (!accountSimpleByAuthentication.isAccessOit() && !passwordRepositoryEntity.getAccountId().equals(dto.getAccountId()))) {
            throw new ForbiddenException();
        }

        return super.update(dto);
    }
}
