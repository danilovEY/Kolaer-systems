package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.server.webportal.beans.RegisterTicketScheduler;
import ru.kolaer.server.webportal.beans.TypeServer;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.ServerException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.TicketRegisterConverter;
import ru.kolaer.server.webportal.mvc.model.dao.BankAccountDao;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketDao;
import ru.kolaer.server.webportal.mvc.model.dao.TicketRegisterDao;
import ru.kolaer.server.webportal.mvc.model.dto.GenerateTicketRegister;
import ru.kolaer.server.webportal.mvc.model.dto.ReportTicketsConfig;
import ru.kolaer.server.webportal.mvc.model.dto.SendTicketDto;
import ru.kolaer.server.webportal.mvc.model.dto.TicketRegisterDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.BankAccountEntity;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntity;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketEntity;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TicketRegisterEntity;
import ru.kolaer.server.webportal.mvc.model.entities.tickets.TypeOperation;
import ru.kolaer.server.webportal.mvc.model.entities.upload.UploadFileEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.TicketRegisterService;
import ru.kolaer.server.webportal.mvc.model.servirces.UploadFileService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final TypeServer typeServer;
    private final TicketDao ticketDao;
    private final RegisterTicketScheduler registerTicketScheduler;
    private final BankAccountDao bankAccountDao;
    private final EmployeeDao employeeDao;
    private final UploadFileService uploadFileService;

    public TicketRegisterServiceImpl(TicketRegisterConverter defaultConverter,
                                     TicketRegisterDao ticketRegisterDao,
                                     TypeServer typeServer,
                                     TicketDao ticketDao,
                                     RegisterTicketScheduler registerTicketScheduler,
                                     BankAccountDao bankAccountDao,
                                     EmployeeDao employeeDao,
                                     UploadFileService uploadFileService) {
        super(ticketRegisterDao, defaultConverter);
        this.typeServer = typeServer;
        this.ticketDao = ticketDao;
        this.registerTicketScheduler = registerTicketScheduler;
        this.bankAccountDao = bankAccountDao;
        this.employeeDao = employeeDao;
        this.uploadFileService = uploadFileService;
    }

    @Scheduled(cron = "0 0 8 26-31 * ?", zone = "Europe/Moscow")
    public void generateZeroTicketsLastDayOfMonthScheduled() {
        if(!typeServer.isTest()) {
            LocalDate now = LocalDate.now();
            LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
            int lastDay = end.getDayOfWeek().getValue() == 7
                    || end.getDayOfWeek().getValue() == 6 ? 6 :  end.getDayOfMonth();

            if (now.getDayOfMonth() == lastDay) {
                TicketRegisterEntity ticketRegisterEntity = this.defaultEntityDao.findIncludeAllOnLastMonth();
                if(ticketRegisterEntity == null) {
                    TicketRegisterDto ticketRegisterDto = generateRegisterForAllAccounts(new GenerateTicketRegister(TypeOperation.DR, 25));

                    LocalDateTime localDateTimeToExecute = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 2, 10).plusMonths(1);

                    generateReportByRegisterAndSend(ticketRegisterDto.getId(), new ReportTicketsConfig(false, localDateTimeToExecute));
                }
            }
        }
    }

//    @Override
//    public TicketRegisterDto save(TicketRegisterDto entity) {
//        List<TicketRegisterEntity> ticketRegisterByDateAndDep = defaultEntityDao.
//                getTicketRegisterByDateAndDep(entity.getCreateRegister(), entity.getDepartment().getName());
//
//        List<TicketRegisterEntity> collect = ticketRegisterByDateAndDep.stream().filter(ticketRegister ->
//                !ticketRegister.isClose()
//        ).collect(Collectors.toList());
//        if(collect.size() > 0) {
//            throw new UnexpectedRequestParams("Открытый реестр уже существует в этом месяце и году!");
//        } else {
//            int day = entity.getCreateRegister()
//                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//                    .getDayOfMonth();
//            ticketRegisterByDateAndDep.forEach(ticketRegister -> {
//                    if(ticketRegister.getCreateRegister()
//                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//                            .getDayOfMonth() == day)
//                        throw new UnexpectedRequestParams("Реестр существует!");
//            });
//        }
//
//        return defaultConverter.convertToDto(defaultEntityDao.persist(defaultConverter.convertToModel(entity)));
//    }

    @Override
    @Transactional
    public TicketRegisterDto generateRegisterForAllAccounts(GenerateTicketRegister generateTicketRegister) {
        List<BankAccountEntity> allBankAccount = this.bankAccountDao.findAll();

        TicketRegisterEntity ticketRegisterEntity = new TicketRegisterEntity();
        ticketRegisterEntity.setIncludeAll(true);
        ticketRegisterEntity.setCreateRegister(LocalDateTime.now());
        ticketRegisterEntity.setId(defaultEntityDao.save(ticketRegisterEntity).getId());


        Integer countTicket = Optional.ofNullable(generateTicketRegister.getCountForAll())
                .orElse(25);
        TypeOperation typeOperation = Optional.ofNullable(generateTicketRegister.getTypeOperationForAll())
                .orElse(TypeOperation.DR);

        List<TicketEntity> tickets = allBankAccount.stream()
                .map(bankAccount -> {
                    TicketEntity ticketEntity = new TicketEntity();
                    ticketEntity.setCount(countTicket);
                    ticketEntity.setTypeOperation(typeOperation);
                    ticketEntity.setBankAccountId(bankAccount.getId());
                    ticketEntity.setRegisterId(ticketRegisterEntity.getId());
                    return ticketEntity;
                }).collect(Collectors.toList());

        ticketDao.save(tickets);

        return defaultConverter.convertToDto(ticketRegisterEntity);
    }

    private UploadFileEntity generateReportByRegister(Long registerId, ReportTicketsConfig config) {
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
    public boolean generateReportByRegisterAndSend(Long registerId, ReportTicketsConfig config) {
        TicketRegisterEntity registerEntity = defaultEntityDao.findById(registerId);
        UploadFileEntity uploadFileEntity = this.generateReportByRegister(registerId, config);
        if(uploadFileEntity != null) {
            registerEntity.setAttachmentId(uploadFileEntity.getId());
            registerEntity.setClose(true);

            String text = "Сформированные талоны ЛПП и время исполнения файла: " +
                    (config.isImmediate() ? "сейчас" : Tools.dateTimeToString(config.getInTime())) +
                    ". Файл во вложении!";

            boolean send = this.registerTicketScheduler.sendMail(uploadFileEntity, text);
            if(send) {
                registerEntity.setSendRegisterTime(LocalDateTime.now());
            }

            defaultEntityDao.update(registerEntity);

            return send;
        } else {
            throw new ServerException("Не удалось сгенерировать отчет");
        }
    }

    @Override
    @Transactional
    public ResponseEntity generateReportByRegisterAndDownload(Long registerId, ReportTicketsConfig config, HttpServletResponse response) {
        TicketRegisterEntity registerEntity = defaultEntityDao.findById(registerId);

        UploadFileEntity uploadFileEntity = this.generateReportByRegister(registerId, config);

        if(uploadFileEntity != null) {
            registerEntity.setAttachmentId(uploadFileEntity.getId());
            registerEntity.setClose(true);

            defaultEntityDao.update(registerEntity);

            return uploadFileService.loadFile(uploadFileEntity.getId(), uploadFileEntity.getFileName(), response);
        } else {
            throw new ServerException("Не удалось сгенерировать отчет");
        }
    }
}
