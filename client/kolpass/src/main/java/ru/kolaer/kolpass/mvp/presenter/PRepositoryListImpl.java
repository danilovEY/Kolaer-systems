package ru.kolaer.kolpass.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryListImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by danilovey on 15.02.2017.
 */
public class PRepositoryListImpl implements PRepositoryList {
    private static final Logger log = LoggerFactory.getLogger(PRepositoryListImpl.class);
    private final Map<EmployeeEntity, List<RepositoryPassword>> employeeRepMap = new HashMap<>();
    private KolpassTable model;
    private VEmployeeRepositoryList view;

    public PRepositoryListImpl() {
        this.setView(new VEmployeeRepositoryListImpl());
    }

    @Override
    public KolpassTable getModel() {
        return this.model;
    }

    @Override
    public void setModel(KolpassTable model) {
        this.model = model;
        this.updateView();
    }

    @Override
    public VEmployeeRepositoryList getView() {
        return this.view;
    }

    @Override
    public void setView(VEmployeeRepositoryList view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        List<RepositoryPassword> allRepositoryPasswords = this.model.getAllRepositoryPasswords();
        allRepositoryPasswords.forEach(this::put);

        this.model.getAllRepositoryPasswordsChief().forEach(this::put);

        this.employeeRepMap.keySet().forEach(this.view::addEmployee);
    }

    private void put(RepositoryPassword repositoryPassword) {
        if(this.employeeRepMap.containsKey(repositoryPassword.getEmployee())) {
            this.employeeRepMap.get(repositoryPassword.getEmployee()).add(repositoryPassword);
        } else {
            ArrayList<RepositoryPassword> repositoryPasswords = new ArrayList<>();
            repositoryPasswords.add(repositoryPassword);
            this.employeeRepMap.put(repositoryPassword.getEmployee(), repositoryPasswords);
        }
    }

    @Override
    public void setOnSelectEmployee(Function<List<RepositoryPassword>, Void> function) {
        this.view.setOnSelectEmployee(emp -> {
            function.apply(this.employeeRepMap.get(emp));
            return null;
        });
    }
}
