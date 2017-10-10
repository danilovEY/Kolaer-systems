package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by danilovey on 30.11.2016.
 */
@Api(tags = "Талоны")
//@RestController
//@RequestMapping(value = "/tickets")
@Slf4j
public class TicketsController extends BaseController {
/*
    @Autowired
    private TicketRegisterService ticketRegisterService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private AuthenticationService serviceLDAP;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RegisterTicketScheduler registerTicketScheduler;

    @ApiOperation(value = "Сформировать отчет")
    @UrlDeclaration(description = "Сформировать отчет", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/generate/execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean generateAndMailSend() {
        return registerTicketScheduler.generateAddTicketDocument();
    }

    @ApiOperation(value = "Сформировать отчет для всех счетов")
    @UrlDeclaration(description = "Сформировать отчет для всех счетов", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/generate/execute/all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean generateAllAndMailSend(@ApiParam("Кол-во талонов") @RequestParam("count") Integer count) {
        return registerTicketScheduler
                .generateSetTicketDocument(count, "IMMEDIATE", "DR", "Сформированные талоны ЛПП для зачисления для всех счетов. Файл во вложении!");
    }

    @ApiOperation(value = "Сформировать отчет для обнуления")
    @UrlDeclaration(description = "Сформировать отчет для обнуления", requestMethod = RequestMethod.POST)
    @RequestMapping(value = "/generate/zero/execute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean generateZeroAndMailSend() {
        return registerTicketScheduler.generateZeroTicketDocument();
    }

    @ApiOperation(value = "Добавить е-майл")
    @UrlDeclaration(description = "Добавить е-майл")
    @RequestMapping(value = "/generate/add", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> addEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.addEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Удалить е-майл")
    @UrlDeclaration(description = "Удалить е-майл")
    @RequestMapping(value = "/generate/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> removeEmail(@RequestParam("mail") String mail) {
        this.registerTicketScheduler.removeEmail(mail);
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить все е-майлы")
    @UrlDeclaration(description = "Получить все е-майлы")
    @RequestMapping(value = "/generate/emails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getEmails() {
        return registerTicketScheduler.getEmails();
    }

    @ApiOperation(value = "Получить последнюю дату отправки")
    @UrlDeclaration(description = "Получить последнюю дату отправки")
    @RequestMapping(value = "/generate/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getLastSend() {
        final LocalDateTime lastSend = registerTicketScheduler.getLastSend();
        return lastSend != null ? DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(lastSend) : "none";
    }

    @ApiOperation(value = "Получить все реестры талонов")
    @UrlDeclaration(description = "Получить все реестры талонов", isAccessUser = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<TicketRegisterEntity> getAllRegister(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                                     @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        final AccountEntity accountByAuthentication = serviceLDAP.getAccountByAuthentication();
        if(accountByAuthentication.getRoles().stream()
                .map(RoleEntity::getType)
                .collect(Collectors.toList()).contains("OIT")){
            return new Page<>(this.ticketRegisterService.getAll(), 0, 0, 0);
        } else {
            return this.ticketRegisterService.getAllByDepName(number, pageSize,
                            accountByAuthentication.getEmployeeEntity()
                                    .getDepartment().getName());
        }
    }

    @ApiOperation(value = "Добавить талон в реестр")
    @UrlDeclaration(description = "Добавить талон в реестр по ID", isAccessUser = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketEntity> addTicketToRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterEntity ticketRegister) {
        //Очищаем из запроса пустые объекты
        ticketRegister.setTickets(ticketRegister.getTickets().stream()
                .filter(ticket -> ticket.getEmployee() != null && ticket.getCount() != null)
                .collect(Collectors.toList()));

        TicketRegisterEntity updateTicketRegister = this.ticketRegisterService.getById(ticketRegister.getId());
        if(updateTicketRegister.isClose()) {
            throw new BadRequestException("Добавлять в реестр запрещено!");
        }

        List<Integer> pnumberUpdate = ticketRegister.getTickets().stream().map(ticket -> ticket.getEmployee().getPersonnelNumber()).collect(Collectors.toList());
        List<TicketEntity> tickets = Optional.ofNullable(updateTicketRegister.getTickets()).orElse(new ArrayList<>());

        List<TicketEntity> ticketsDoulbes = tickets.stream().filter(ticket ->
                pnumberUpdate.contains(ticket.getEmployee().getPersonnelNumber())).collect(Collectors.toList());

        if(ticketsDoulbes.size() > 0){
            final String initials = ticketsDoulbes.stream()
                    .map(ticket -> ticket.getEmployee().getInitials()).collect(Collectors.joining(", "));
            throw new BadRequestException("Найдены дубли: " + initials);
        }

        List<TicketEntity> ticketsToAdd = ticketRegister.getTickets().stream().map(ticket -> {
            ticket.setEmployee(employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
            return ticket;
        }).collect(Collectors.toList());

        tickets.addAll(ticketsToAdd);
        updateTicketRegister.setTickets(tickets);
        this.ticketRegisterService.update(updateTicketRegister);
        return ticketsToAdd;
    }

    @ApiOperation(value = "Обновить талон")
    @UrlDeclaration(description = "Обновить талон по ID", isAccessUser = true)
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketEntity updateTicketToRegister(@ApiParam(value = "ID талона и данные", required = true) @RequestBody TicketEntity ticket) {
        TicketEntity updateTicket = this.ticketService.getById(ticket.getId());
        updateTicket.setCount(Optional.ofNullable(ticket.getCount()).orElse(updateTicket.getCount()));
        if(ticket.getEmployee() != null && !updateTicket.getEmployee().getPersonnelNumber().equals(ticket.getEmployee().getPersonnelNumber())) {
            updateTicket.setEmployee(this.employeeService.getByPersonnelNumber(ticket.getEmployee().getPersonnelNumber()));
        }
        this.ticketService.update(updateTicket);
        return updateTicket;
    }

    @ApiOperation(value = "Удалить реестр")
    @UrlDeclaration(description = "Удалить реестра", isAccessUser = true)
    @RequestMapping(value = "/register/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRegister(@ApiParam(value = "ID реестра", required = true) @RequestBody TicketRegisterEntity ticket) {
        this.ticketRegisterService.delete(ticket);
    }

    @ApiOperation(value = "Удалить талоны из реестра")
    @UrlDeclaration(description = "Удалить талоны из реестра", isAccessUser = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTicketFromRegister(@ApiParam(value = "ID талонов", required = true) @RequestBody List<TicketEntity> tickets) {
        tickets.forEach(this.ticketService::delete);
    }

    @ApiOperation(value = "Добавить реестр талонов")
    @UrlDeclaration(description = "Добавить реестр талонов", isAccessUser = true)
    @RequestMapping(value = "/register/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterEntity addTicketRegister() {
        TicketRegisterEntity ticketRegister = new TicketRegisterEntity();
        ticketRegister.setCreateRegister(new Date());
        ticketRegister.setDepartment(this.serviceLDAP.getAccountByAuthentication()
                .getEmployeeEntity().getDepartment());

        this.ticketRegisterService.add(ticketRegister);
        return ticketRegister;
    }

    @ApiOperation(value = "Получить реестр талонов по ID")
    @UrlDeclaration(description = "Получить реестр талонов по ID", isAccessUser = true)
    @RequestMapping(value = "/register/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TicketRegisterEntity getTicketRegister(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Integer id) {
        return this.ticketRegisterService.getById(id);
    }

    @ApiOperation(value = "Получить талоны по ID реестра")
    @UrlDeclaration(description = "Получить талоны по ID реестра", isAccessUser = true)
    @RequestMapping(value = "/get/by/register", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TicketEntity> getTickets(@ApiParam(value = "ID реестра", required = true) @RequestParam(value = "id") Integer id) {
        return this.ticketService.getTicketsByRegisterId(id);
    }
*/
}
