package ru.kolaer.asmc.mvp.model;

import ru.kolaer.api.mvp.model.BaseModel;

import java.util.List;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface DataService extends BaseModel<List<MGroup>>, DataServiceObservable {
    void addGroup(MGroup group);
    void removeGroup(MGroup group);
    boolean saveData();
    void saveDataOnThread();
    boolean loadData();
}
