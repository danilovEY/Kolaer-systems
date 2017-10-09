package ru.kolaer.server.webportal.mvc.model.servirces.impl;

/**
 * Created by danilovey on 20.01.2017.
 */
/*
@Service
public class RepositoryPasswordServiceImpl implements RepositoryPasswordService {

    @Autowired
    private RepositoryPasswordDao repositoryPasswordDao;

    @Override
    public List<RepositoryPassword> getAll() {
        return this.repositoryPasswordDao.findAll();
    }

    @Override
    public RepositoryPassword getById(Integer id) {
        if(id == null || id < 0)
            throw new BadRequestException("ID должно быть больше 0");

        return this.repositoryPasswordDao.findByID(id);
    }

    @Override
    public void add(@NonNull RepositoryPassword entity) {
        this.repositoryPasswordDao.persist(entity);
    }

    @Override
    public void delete(@NonNull RepositoryPassword entity) {
        this.repositoryPasswordDao.delete(entity);
    }

    @Override
    public void update(RepositoryPassword entity) {
        this.repositoryPasswordDao.update(entity);
    }

    @Override
    public void update(List<RepositoryPassword> entites) {
        this.repositoryPasswordDao.update(entites);
    }

    @Override
    public void delete(List<RepositoryPassword> entites) {
        this.repositoryPasswordDao.delete(entites);
    }

    @Override
    public Page<RepositoryPassword> getAllByPnumber(Integer pnumber, Integer number, Integer pageSize) {
        return this.repositoryPasswordDao.findAllByPnumber(pnumber, number, pageSize);
    }

    @Override
    public RepositoryPassword getByNameAndPnumber(String name, Integer pnumber) {
        if(name == null || pnumber == null)
            throw new BadRequestException("Имя и табельный номер не может быть пустым!");

        return this.repositoryPasswordDao.findByNameAndPnumber(name, pnumber);

    }

    @Override
    public RepositoryPassword getRepositoryWithJoinById(Integer id) {
        return this.repositoryPasswordDao.findRepositoryWithJoinById(id);
    }

    @Override
    public List<RepositoryPassword> getAllByPnumbers(List<Integer> idsChief) {
        return this.repositoryPasswordDao.findAllByPnumbers(idsChief);
    }
}
*/
