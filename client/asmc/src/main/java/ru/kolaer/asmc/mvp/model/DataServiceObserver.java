package ru.kolaer.asmc.mvp.model;

import java.util.List;

/**
 * Created by danilovey on 26.10.2017.
 */
public interface DataServiceObserver {
    void updateData(List<MGroup> groupList);
}
