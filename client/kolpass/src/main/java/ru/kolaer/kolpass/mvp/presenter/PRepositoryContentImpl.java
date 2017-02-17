package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VRepositoryContent;
import ru.kolaer.kolpass.mvp.view.VRepositoryContentImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryContentImpl implements PRepositoryContent {
    private final List<PRepositoryPassword> repositoryPasswordList = new ArrayList<>();
    private final UniformSystemEditorKit editorKit;
    private VRepositoryContent view;
    private KolpassTable kolpassTable;
    private EmployeeEntity employeeEntity;

    public PRepositoryContentImpl(UniformSystemEditorKit editorKit) {
        this.view = new VRepositoryContentImpl();
        this.editorKit = editorKit;
    }

    @Override
    public VRepositoryContent getView() {
        return this.view;
    }

    @Override
    public void setView(VRepositoryContent view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.view.clear();

        this.view.setOnAddRepository(rep -> {
            if(rep.getName() == null || rep.getName().isEmpty()) {
                this.editorKit.getUISystemUS().getNotification()
                        .showErrorNotifi("Ошибка!", "Имя не может быть пустым!");
                return null;
            }

            RepositoryPassword repositoryPassword = null;

            if(this.employeeEntity.getPersonnelNumber().equals(this.editorKit.getAuthentication()
                            .getAuthorizedUser().getEmployeeEntity().getPersonnelNumber())) {
                repositoryPassword = this.kolpassTable.addRepositoryPassword(rep);
            } else {
                rep.setEmployee(this.employeeEntity);
                repositoryPassword = this.kolpassTable.addRepToOtherEmployee(rep);
            }


            this.addRepositoryPassword(new PRepositoryPasswordImpl(this.editorKit, repositoryPassword));

            this.editorKit.getUISystemUS().getNotification().showInformationNotifi("Успешная операция!",
                    "Добавлен репозиторий \"" + repositoryPassword.getName() + "\"!");

            return null;
        });
    }

    @Override
    public void addRepositoryPassword(PRepositoryPassword pRepositoryPassword) {
        this.repositoryPasswordList.add(pRepositoryPassword);
        pRepositoryPassword.setOnDelete(rep -> {
            this.removeRepositoryPassword(rep);
            return null;
        });
        this.view.addRepositoryPassword(pRepositoryPassword.getView());
    }

    @Override
    public void setAllRepositoryPassword(List<PRepositoryPassword> pRepositoryPasswords) {
        this.repositoryPasswordList.forEach(rep -> this.view.removeRepositoryPassword(rep.getView()));
        this.repositoryPasswordList.clear();
        pRepositoryPasswords.forEach(this::addRepositoryPassword);
    }

    @Override
    public void removeRepositoryPassword(PRepositoryPassword pRepositoryPassword) {
        this.repositoryPasswordList.remove(pRepositoryPassword);
        this.view.removeRepositoryPassword(pRepositoryPassword.getView());
    }

    @Override
    public void clear() {
        this.setAllRepositoryPassword(Collections.emptyList());
    }

    @Override
    public void setEmployee(EmployeeEntity key) {
        this.employeeEntity = key;
    }

    @Override
    public KolpassTable getModel() {
        return this.kolpassTable;
    }

    @Override
    public void setModel(KolpassTable model) {
        this.kolpassTable = model;
        this.updateView();
    }
}
