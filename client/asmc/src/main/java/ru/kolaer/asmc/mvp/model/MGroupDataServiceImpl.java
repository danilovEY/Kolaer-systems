package ru.kolaer.asmc.mvp.model;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by danilovey on 20.02.2017.
 */
public class MGroupDataServiceImpl implements MGroupDataService {
    private final static Logger log = LoggerFactory.getLogger(MGroupDataServiceImpl.class);

    private final String FILE_PATH = "data";
    private final String FILE_NAME = "groups.xml";
    private final String FILE_NAME_BACKUP = "groups_backup.xml";

    private final XmlMapper xmlMapper = new XmlMapper();
    final File fileWithData = new File(FILE_PATH + "/" + FILE_NAME);
    final File fileBackup = new File(FILE_PATH + "/" + FILE_NAME_BACKUP);
    final File fileDir = new File(FILE_PATH);
    private List<MGroup> groups;

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
        if(fileDir.exists() || fileDir.mkdirs()) {
            if(fileWithData.exists()) {
                try {
                    Files.copy(fileWithData.toPath(), fileBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    log.error("Ошибка при создании бэкапа!", e);
                    UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                            .showErrorNotify("Ошибка!", "Ошибка при создании бэкапа!");
                }
            }
            try(final FileOutputStream fileOutputStream =
                        new FileOutputStream(fileWithData)) {
                final byte[] bytes = this.xmlMapper
                        .writeValueAsBytes(this.groups.stream().toArray(MGroup[]::new));
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
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
                .supplyAsync(this::saveData)
                .thenAccept(isSave -> {
                    if (!isSave) {
                        UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                                .showErrorNotify("Ошибка!", "Не удалось сохранить данные!");
                    }
                }).exceptionally(t -> {
                    log.error("Ошибка при сохранении!", t);
                    UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification()
                        .showErrorNotify("Ошибка!", "Не удалось сохранить данные!");
                    return null;
                });
    }

    @Override
    public boolean loadData() {
        if(!fileWithData.exists()) {
            log.warn("Файл: \"{}\" не найден!", FILE_PATH + "/" + FILE_NAME);
            groups = Collections.emptyList();
            return true;
        }

        try {
            MGroup[] mGroups = this.xmlMapper.readValue(fileWithData, MGroup[].class);
            groups = Arrays.asList(mGroups);
            return true;
        } catch (IOException e) {
            log.error("Ошибка при чтении файла!", e);
            return false;
        }
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
