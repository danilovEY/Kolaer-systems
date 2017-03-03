package ru.kolaer.kolpass.mvp.presenter;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryListImpl;

import java.util.*;
import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public class PEmployeeRepositoryListImpl implements PEmployeeRepositoryList {
    private static final Logger log = LoggerFactory.getLogger(PEmployeeRepositoryListImpl.class);
    private final Map<EmployeeEntity, List<RepositoryPassword>> employeeRepMap = new HashMap<>();
    private final UniformSystemEditorKit editorKit;
    private KolpassTable model;
    private VEmployeeRepositoryList view;

    public PEmployeeRepositoryListImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
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
        if(allRepositoryPasswords.size() > 0) {
            allRepositoryPasswords.forEach(this::put);
        } else {
            this.employeeRepMap
                    .put(this.editorKit.getAuthentication().getAuthorizedUser().getEmployeeEntity(),
                            new ArrayList<>());
        }

        this.view.setOnLoadOtherEmployee(e -> {
            Tools.runOnThreadFX(() -> {
                this.employeeRepMap.keySet().forEach(this.view::removeEmployee);

                try {
                    final List<RepositoryPassword> allRepositoryPasswordsChief
                            = this.model.getAllRepositoryPasswordsChief();

                    allRepositoryPasswordsChief.stream()
                            .map(RepositoryPassword::getEmployee)
                            .forEach(this.employeeRepMap::remove);

                    allRepositoryPasswordsChief.forEach(this::put);

                } catch (Throwable ignore) {}

                this.employeeRepMap.keySet().forEach(this.view::addEmployee);
            });
            return null;
        });

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
    public void clear() {
        this.employeeRepMap.keySet().forEach(this.view::removeEmployee);
        this.employeeRepMap.clear();
    }

    @Override
    public void setOnSelectEmployee(Function<Pair<EmployeeEntity, List<RepositoryPassword>>, Void> function) {
        this.view.setOnSelectEmployee(emp -> {
            function.apply(new Pair<>(emp,
                    Optional.ofNullable(this.employeeRepMap.get(emp))
                            .orElse(Collections.emptyList())));
            return null;
        });
    }

    @Override
    public void selectIndex(int index) {
        this.view.selectIndex(index);
    }
}
