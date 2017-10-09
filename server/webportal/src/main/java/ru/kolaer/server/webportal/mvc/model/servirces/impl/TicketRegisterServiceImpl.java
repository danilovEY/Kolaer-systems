package ru.kolaer.server.webportal.mvc.model.servirces.impl;

/**
 * Created by danilovey on 30.11.2016.
 */
/*
@Service
public class TicketRegisterServiceImpl implements TicketRegisterService {

    @Autowired
    private TicketRegisterDao ticketRegisterDao;

    @Autowired
    private TicketDao ticketDao;

    @Override
    public List<TicketRegisterEntity> getAll() {
        return this.ticketRegisterDao.findAll();
    }

    @Override
    public TicketRegisterEntity getById(Integer id) {
        return this.ticketRegisterDao.findByID(id);
    }

    @Override
    public void add(TicketRegisterEntity entity) {
        List<TicketRegisterEntity> ticketRegisterByDateAndDep = ticketRegisterDao.
                getTicketRegisterByDateAndDep(entity.getCreateRegister(), entity.getDepartment().getName());

        List<TicketRegisterEntity> collect = ticketRegisterByDateAndDep.stream().filter(ticketRegister ->
                !ticketRegister.isClose()
        ).collect(Collectors.toList());
        if(collect.size() > 0) {
            throw new BadRequestException("Открытый реестр уже существует в этом месяце и году!");
        } else {
            int day = entity.getCreateRegister()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .getDayOfMonth();
            ticketRegisterByDateAndDep.forEach(ticketRegister -> {
                    if(ticketRegister.getCreateRegister()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            .getDayOfMonth() == day)
                        throw new BadRequestException("Реестр существует!");
            });
        }

        this.ticketRegisterDao.persist(entity);
    }

    @Override
    public void delete(TicketRegisterEntity entity) {
        if (entity.getTickets() != null)
            entity.getTickets().forEach(ticketDao::delete);
        this.ticketRegisterDao.delete(entity);
    }

    @Override
    public void update(TicketRegisterEntity entity) {
        this.ticketRegisterDao.update(entity);
    }

    @Override
    public void update(List<TicketRegisterEntity> entity) {
        entity.forEach(this::update);
    }

    @Override
    public void delete(List<TicketRegisterEntity> entites) {

    }

    @Override
    public List<TicketRegisterEntity> getAllByDepName(String name) {
        return this.ticketRegisterDao.findAllByDepName(name);
    }

    @Override
    public Page<TicketRegisterEntity> getAllByDepName(int page, int pageSize, String name) {
        if(page == 0) {
            List<TicketRegisterEntity> allByDepName = this.getAllByDepName(name);
            return new Page<>(allByDepName, 0, 0, allByDepName.size());
        }

        return this.ticketRegisterDao.findAllByDepName(page, pageSize, name);
    }

    @Override
    public List<TicketRegisterEntity> getAllOpenRegister() {
        return this.ticketRegisterDao.findAllOpenRegister();
    }
}*/
