package ru.kolaer.asmc.mvp.model;

import ru.kolaer.api.mvp.model.BaseModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by danilovey on 20.02.2017.
 */
public interface MGroupDataService extends BaseModel<List<MGroup>> {
    void addGroup(MGroup group);
    void removeGroup(MGroup group);
    void saveData() throws IOException;
    void loadData() throws FileNotFoundException, IOException;
}
