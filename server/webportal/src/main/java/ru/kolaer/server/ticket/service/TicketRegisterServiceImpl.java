package ru.kolaer.server.ticket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.bean.RegisterTicketScheduler;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.ServerException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.model.dto.FilterValue;
import ru.kolaer.server.core.model.dto.SortField;
import ru.kolaer.server.core.model.dto.SortParam;
import ru.kolaer.server.core.model.dto.upload.UploadFileDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;
import ru.kolaer.server.ticket.dao.BankAccountDao;
import ru.kolaer.server.ticket.dao.TicketDao;
import ru.kolaer.server.ticket.dao.TicketRegisterDao;
import ru.kolaer.server.ticket.model.TypeOperation;
import ru.kolaer.server.ticket.model.dto.RequestTicketDto;
import ru.kolaer.server.ticket.model.dto.SendTicketDto;
import ru.kolaer.server.ticket.model.dto.TicketDto;
import ru.kolaer.server.ticket.model.dto.TicketRegisterDto;
import ru.kolaer.server.ticket.model.entity.BankAccountEntity;
import ru.kolaer.server.ticket.model.entity.TicketEntity;
import ru.kolaer.server.ticket.model.entity.TicketRegisterEntity;
import ru.kolaer.server.ticket.model.request.BankAccountFilter;
import ru.kolaer.server.ticket.model.request.GenerateTicketRegister;
import ru.kolaer.server.ticket.model.request.ReportTicketsConfig;
import ru.kolaer.server.ticket.model.request.TicketFilter;
import ru.kolaer.server.upload.service.UploadFileService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 30.11.2016.
 */
@Service
public class TicketRegisterServiceImpl extends AbstractDefaultService<TicketRegisterDto, TicketRegisterEntity,
        TicketRegisterDao, TicketRegisterConverter> implements TicketRegisterService {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final TicketDao ticketDao;
    private final RegisterTicketScheduler registerTicketScheduler;
    private final BankAccountDao bankAccountDao;
    private final EmployeeDao employeeDao;
    private final UploadFileService uploadFileService;
    private final AuthenticationService authenticationService;

    public TicketRegisterServiceImpl(TicketRegisterConverter defaultConverter,
                                     TicketRegisterDao ticketRegisterDao,
                                     TicketDao ticketDao,
                                     RegisterTicketScheduler registerTicketScheduler,
                                     BankAccountDao bankAccountDao,
                                     EmployeeDao employeeDao,
                                     UploadFileService uploadFileService,
                                     AuthenticationService authenticationService) {
        super(ticketRegisterDao, defaultConverter);
        this.ticketDao = ticketDao;
        this.registerTicketScheduler = registerTicketScheduler;
        this.bankAccountDao = bankAccountDao;
        this.employeeDao = employeeDao;
        this.uploadFileService = uploadFileService;
        this.authenticationService = authenticationService;
    }

    @Override
    //@Scheduled(cron = "0 0 8 26-31 * ?", zone = "Europe/Moscow")
    @Transactional
    public void generateZeroTicketsLastDayOfMonthScheduled() {
        LocalDate now = LocalDate.now();
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        int lastDay = end.getDayOfWeek().getValue() == 7
                || end.getDayOfWeek().getValue() == 6 ? 6 :  end.getDayOfMonth();

        if (now.getDayOfMonth() == lastDay && this.defaultEntityDao.findIncludeAllOnLastMonth().isEmpty()) {
            TicketRegisterDto ticketRegisterDto = createRegisterForAllAccounts(new GenerateTicketRegister(TypeOperation.DR, 25));

            LocalDateTime localDateTimeToExecute = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 10).plusMonths(1);

            generateReportByRegisterAndSend(ticketRegisterDto.getId(), new ReportTicketsConfig(false, localDateTimeToExecute));
        }
    }

    @Override
    @Transactional
    public TicketRegisterDto addToRegisterAllAccounts(Long regId, GenerateTicketRegister generateTicketRegister) {
        if(generateTicketRegister.getCountForAll() == null || generateTicketRegister.getTypeOperationForAll() == null) {
            throw new UnexpectedRequestParams("Все параметры обязательны");
        }

        BankAccountFilter bankAccountFilter = new BankAccountFilter();
        bankAccountFilter.setFilterDeleted(Boolean.FALSE);

        Map<String, FilterValue> filters = getFilters(bankAccountFilter);

        List<BankAccountEntity> allBankAccount = this.bankAccountDao.findAll(null, filters);

        TicketRegisterEntity ticketRegisterEntity = defaultEntityDao.findById(regId);
        ticketRegisterEntity.setIncludeAll(true);

        Integer countTicket = generateTicketRegister.getCountForAll();
        TypeOperation typeOperation = generateTicketRegister.getTypeOperationForAll();

        List<TicketEntity> tickets = allBankAccount.stream()
                .map(bankAccount -> {
                    TicketEntity ticketEntity = new TicketEntity();
                    ticketEntity.setCount(countTicket);
                    ticketEntity.setTypeOperation(typeOperation);
                    ticketEntity.setBankAccountId(bankAccount.getId());
                    ticketEntity.setRegisterId(regId);
                    return ticketEntity;
                }).collect(Collectors.toList());

        ticketDao.save(tickets);

        return defaultConverter.convertToDto(defaultEntityDao.update(ticketRegisterEntity));
    }

    @Override
    @Transactional
    public TicketRegisterDto createRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister) {
        TicketRegisterEntity ticketRegisterEntity = this.createRegister();
        addToRegisterAllAccounts(ticketRegisterEntity.getId(), generateTicketRegister);

        return defaultConverter.convertToDto(ticketRegisterEntity);
    }

    @Override
    @Transactional
    public TicketRegisterDto createEmptyRegister() {
        return defaultConverter.convertToDto(createRegister());
    }

    private TicketRegisterEntity createRegister() {
        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setCreateRegister(LocalDateTime.now());

        if(this.authenticationService.isAuth()) {
            AccountAuthorizedDto accountSimpleByAuthentication = this.authenticationService.getAccountAuthorized();
            ticketRegisterEntity.setAccountId(accountSimpleByAuthentication.getId());
        }

        return defaultEntityDao.save(ticketRegisterEntity);
    }

    private UploadFileDto generateReportByRegister(Long registerId, ReportTicketsConfig config) {
        if(registerId == null) {
            throw new UnexpectedRequestParams("ID должен быть не пустым");
        }

        if(config == null) {
            throw new UnexpectedRequestParams("Не указанна конфигурация");
        }

        TicketRegisterEntity registerEntity = this.defaultEntityDao.findById(registerId);
        if(registerEntity == null) {
            throw new NotFoundDataException("Нет такого реестра");
        }

        if(registerEntity.getAttachmentId() != null) {
            uploadFileService.delete(registerEntity.getAttachmentId());
            registerEntity.setAttachmentId(null);
        }

        List<TicketEntity> allTicketsByRegisterId = this.ticketDao.findAllByRegisterId(registerId);
        if(allTicketsByRegisterId.isEmpty()) {
            throw new NotFoundDataException("Реестр пуст");
        }

        List<Long> allIdBankAccounts = allTicketsByRegisterId.stream()
                .map(TicketEntity::getBankAccountId)
                .collect(Collectors.toList());

        Map<Long, BankAccountEntity> allBankAccounts = this.bankAccountDao.findById(allIdBankAccounts)
                .stream()
                .collect(Collectors.toMap(BankAccountEntity::getId, Function.identity()));

        List<Long> allIdEntities = allBankAccounts.values()
                .stream()
                .map(BankAccountEntity::getEmployeeId)
                .collect(Collectors.toList());

        Map<Long, EmployeeEntity> allEmployees = this.employeeDao.findById(allIdEntities)
                .stream()
                .collect(Collectors.toMap(EmployeeEntity::getId, Function.identity()));

        List<SendTicketDto> allSendTickets = allTicketsByRegisterId.stream()
                .filter(ticket -> Optional
                        .ofNullable(allBankAccounts.get(ticket.getBankAccountId()))
                        .map(bankAccount -> allEmployees.get(bankAccount.getEmployeeId()))
                        .isPresent()
                ).map(ticket -> {
                    BankAccountEntity bankAccountEntity = allBankAccounts.get(ticket.getBankAccountId());
                    EmployeeEntity employeeEntity = allEmployees.get(bankAccountEntity.getEmployeeId());

                    SendTicketDto sendTicketDto = new SendTicketDto();
                    sendTicketDto.setCount(ticket.getCount());
                    sendTicketDto.setTypeOperation(ticket.getTypeOperation());
                    sendTicketDto.setCheck(bankAccountEntity.getCheck());
                    sendTicketDto.setInitials(employeeEntity.getInitials());
                    return sendTicketDto;
                }).collect(Collectors.toList());

        return config.isImmediate()
                ? registerTicketScheduler.generateReportTickets(allSendTickets)
                : registerTicketScheduler.generateReportTickets(allSendTickets, config.getInTime());
    }

    @Override
    @Transactional
    public TicketRegisterDto generateReportByRegisterAndSend(Long registerId, ReportTicketsConfig config) {
        TicketRegisterEntity registerEntity = defaultEntityDao.findById(registerId);
        UploadFileDto uploadFileEntity = this.generateReportByRegister(registerId, config);
        if(uploadFileEntity != null) {
            registerEntity.setAttachmentId(uploadFileEntity.getId());
            registerEntity.setClose(true);

            String text = "Сформированные талоны ЛПП и время исполнения файла: " +
                    (config.isImmediate() ? "сейчас" : dateTimeFormatter.format(config.getInTime())) + ". Файл во вложении!";

            boolean send = this.registerTicketScheduler.sendMail(uploadFileEntity, text);
            if(send) {
                registerEntity.setSendRegisterTime(LocalDateTime.now());
            }

            return defaultConverter.convertToDto(defaultEntityDao.update(registerEntity));
        } else {
            throw new ServerException("Не удалось сгенерировать отчет");
        }
    }

    @Override
    @Transactional
    public TicketRegisterDto generateReportByRegisterAndDownload(Long registerId, ReportTicketsConfig config, HttpServletResponse response) {
        TicketRegisterEntity registerEntity = defaultEntityDao.findById(registerId);

        UploadFileDto uploadFileEntity = this.generateReportByRegister(registerId, config);

        if(uploadFileEntity != null) {
            registerEntity.setAttachmentId(uploadFileEntity.getId());
            registerEntity.setClose(true);

            return defaultConverter.convertToDto(defaultEntityDao.update(registerEntity));
        } else {
            throw new ServerException("Не удалось сгенерировать отчет");
        }
    }

    @Override
    @Transactional
    public long delete(Long regId) {
        ticketDao.deleteByRegisterId(regId);

        TicketRegisterEntity ticketRegisterEntity = defaultEntityDao.findById(regId);

        Optional.ofNullable(ticketRegisterEntity.getAttachmentId())
                .ifPresent(uploadFileService::delete);

        defaultEntityDao.delete(ticketRegisterEntity);
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsByRegisterId(Long regId) {
        return defaultConverter.convertToTicketDto(ticketDao.findAllByRegisterId(regId));
    }

    @Override
    @Transactional
    public TicketDto addTicket(Long regId, RequestTicketDto ticketDto) {
        BankAccountEntity bankAccountEntity = bankAccountDao.findByEmployeeId(ticketDto.getEmployeeId());

        if (bankAccountEntity == null) {
            throw new NotFoundDataException("Счет для этого сотрудника не найден");
        }

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setRegisterId(regId);
        ticketEntity.setBankAccountId(bankAccountEntity.getId());
        ticketEntity.setTypeOperation(ticketDto.getType());
        ticketEntity.setCount(ticketDto.getCount());

        return defaultConverter.convertToTicketDto(ticketDao.persist(ticketEntity));
    }

    @Override
    @Transactional
    public void deleteTicket(Long regId, Long ticketId) {
        TicketEntity ticketEntity = ticketDao.findById(ticketId);

        if(ticketEntity.getRegisterId().equals(regId)) {
            ticketDao.delete(ticketEntity);
        } else {
            throw new NotFoundDataException("Талон с ID " + ticketId + " не найден в реестре");
        }
    }

    @Override
    @Transactional
    public TicketDto updateTicket(Long regId, Long ticketId, RequestTicketDto ticketDto) {
        TicketEntity ticketEntity = ticketDao.findById(ticketId);

        if(!ticketEntity.getRegisterId().equals(regId)) {
            throw new NotFoundDataException("Талон с ID " + ticketId + " не найден в реестре");
        }

        BankAccountEntity bankAccountEntity = bankAccountDao.findByEmployeeId(ticketDto.getEmployeeId());

        ticketEntity.setRegisterId(regId);
        ticketEntity.setCount(ticketDto.getCount());
        ticketEntity.setTypeOperation(ticketDto.getType());
        ticketEntity.setBankAccountId(bankAccountEntity.getId());

        return defaultConverter.convertToTicketDto(ticketDao.update(ticketEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDto<TicketDto> getTicketsByRegisterId(Long regId, Integer number, Integer pageSize, SortParam sortParam, TicketFilter ticketFilter) {
        if(ticketFilter == null) {
            ticketFilter = new TicketFilter();
        }

        ticketFilter.setFilterRegisterId(regId);

        Map<String, FilterValue> filters = getFilters(ticketFilter);
        SortField sort = getSortField(sortParam);

        Long count = ticketDao.findAllCount(filters);
        List<TicketDto> results = defaultConverter.convertToTicketDto(ticketDao.findAll(sort, filters, number, pageSize));

        return new PageDto<>(results, number, count, pageSize);
    }
}
