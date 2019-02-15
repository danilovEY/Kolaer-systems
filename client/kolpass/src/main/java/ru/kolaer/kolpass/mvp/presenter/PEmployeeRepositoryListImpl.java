package ru.kolaer.kolpass.mvp.presenter;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.common.dto.kolaerweb.kolpass.PasswordRepositoryDto;
import ru.kolaer.common.system.UniformSystemEditorKit;
import ru.kolaer.common.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryList;
import ru.kolaer.kolpass.mvp.view.VEmployeeRepositoryListImpl;

import java.util.*;
import java.util.function.Function;

/**
 * Created by danilovey on 15.02.2017.
 */
public class PEmployeeRepositoryListImpl implements PEmployeeRepositoryList {
    private static final Logger log = LoggerFactory.getLogger(PEmployeeRepositoryListImpl.class);
    private final Map<EmployeeDto, List<PasswordRepositoryDto>> employeeRepMap = new HashMap<>();
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
       /* final List<PasswordRepositoryDto> allRepositoryPasswords = this.model.getAllRepositoryPasswords();
        if(allRepositoryPasswords.size() > 0) {
            allRepositoryPasswords.forEach(this::put);
        } else {
            this.employeeRepMap
                    .put(this.editorKit.getAuthentication().getAuthorizedUser().getEmployee(),
                            new ArrayList<>());
        }

        this.employeeRepMap.keySet().forEach(this.view::addEmployee);

        this.view.setOnLoadOtherEmployee(e -> {
            this.employeeRepMap.keySet().forEach(this.view::removeEmployee);

            this.employeeRepMap.keySet().clear();

            List<PasswordRepositoryDto> allRep = this.model.getAllRepositoryPasswords();
            if(allRep.size() > 0) {
                allRep.forEach(this::put);
            } else {
                this.employeeRepMap
                        .put(this.editorKit.getAuthentication().getAuthorizedUser().getEmployee(),
                                new ArrayList<>());
            }

            final List<PasswordRepositoryDto> allRepositoryPasswordsChief
                    = this.model.getAllRepositoryPasswordsChief();

            allRepositoryPasswordsChief.stream()
                    .map(PasswordRepositoryDto::getEmployee)
                    .forEach(this.employeeRepMap::remove);

            allRepositoryPasswordsChief.forEach(this::put);

            this.employeeRepMap.keySet().forEach(this.view::addEmployee);
            return null;
        });*/ // TODO: !!!
    }

    @Override
    public void clear() {
        this.employeeRepMap.keySet().forEach(this.view::removeEmployee);
        this.employeeRepMap.clear();
    }

    @Override
    public void setOnSelectEmployee(Function<Pair<EmployeeDto, List<PasswordRepositoryDto>>, Void> function) {
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
