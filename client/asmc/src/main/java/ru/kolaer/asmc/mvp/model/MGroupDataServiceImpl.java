package ru.kolaer.asmc.mvp.model;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Created by danilovey on 20.02.2017.
 */
public class MGroupDataServiceImpl implements MGroupDataService {
    private final static Logger log = LoggerFactory.getLogger(MGroupDataServiceImpl.class);

    private final String FILE_PATH = "data";
    private final String FILE_NAME = "groups.xml";

    private final UniformSystemEditorKit editorKit;
    private final XmlMapper xmlMapper;
    private List<MGroup> groups;

    public MGroupDataServiceImpl(UniformSystemEditorKit editorKit, List<MGroup> groups) {
        this.editorKit = editorKit;
        this.groups = groups;
        this.xmlMapper = new XmlMapper();
    }

    public MGroupDataServiceImpl(UniformSystemEditorKit editorKit) {
        this(editorKit, new ArrayList<>());
    }


    @Override
    public void addGroup(MGroup group) {
        this.groups.add(group);
    }

    @Override
    public void removeGroup(MGroup group) {
        this.groups.remove(group);
    }

    @Override
    public boolean saveData() {
        final File fileDir = new File(FILE_PATH);
        if(fileDir.exists() || fileDir.mkdirs()) {
            try {
                this.xmlMapper.writeValue(new File(FILE_PATH + "/" + FILE_NAME),
                        this.groups.stream().toArray(MGroup[]::new));
                return true;
            } catch (IOException e) {
                log.error("Ошибка при сохранение данных!", e);
            }
        }

        return false;
    }

    @Override
    public void saveDataOnThread() {
            CompletableFuture
                    .supplyAsync(this::saveData, Executors.newSingleThreadExecutor())
                    .thenAccept(isSave -> {
                        if (!isSave)
                            this.editorKit.getUISystemUS().getNotification()
                                    .showErrorNotifi("Ошибка!", "Не удалось сохранить данные!");
                    }).exceptionally(t -> {
                        log.error("Ошибка при сохранении!", t);
                        this.editorKit.getUISystemUS().getNotification()
                            .showErrorNotifi("Ошибка!", "Не удалось сохранить данные!");
                        return null;
                    });
    }

    @Override
    public boolean loadData() {
        final File file = new File(FILE_PATH + "/" + FILE_NAME);
        if(!file.exists()) {
            log.warn("Файл: \"{}\" не найден!", FILE_PATH + "/" + FILE_NAME);
            return false;
        }

        try {
            final MGroup[] mGroups = this.xmlMapper.readValue(file, MGroup[].class);
            this.groups.addAll(Arrays.asList(mGroups));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<MGroup> getModel() {
        return this.groups;
    }

    @Override
    public void setModel(List<MGroup> model) {
        this.groups = model;
    }
}
