package ru.kolaer.asmc.mvp.model;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danilovey on 20.02.2017.
 */
public class MGroupDataServiceImpl implements MGroupDataService {
    private final static Logger log = LoggerFactory.getLogger(MGroupDataServiceImpl.class);

    private final String FILE_PATH = "data";
    private final String FILE_NAME = "groups.xml";
    private final XmlMapper xmlMapper;
    private List<MGroup> groups;

    public MGroupDataServiceImpl(List<MGroup> groups) {
        this.groups = groups;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public MGroupDataServiceImpl() {
        this(new ArrayList<>());
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
    public void saveData() throws IOException {
        final File fileDir = new File(FILE_PATH);
        if(!fileDir.exists())
            fileDir.mkdirs();
        this.xmlMapper.writeValue(new File(FILE_PATH + "/" + FILE_NAME), this.groups.stream().toArray(MGroup[]::new));
    }

    @Override
    public void loadData() throws IOException {
        final File file = new File(FILE_PATH + "/" + FILE_NAME);
        if(!file.exists()) {
            log.warn("Файл: \"{}\" не найден!", FILE_PATH + "/" + FILE_NAME);
            return;
        }

        final MGroup[] mGroups = this.xmlMapper.readValue(file, MGroup[].class);
        this.groups.addAll(Arrays.asList(mGroups));
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
