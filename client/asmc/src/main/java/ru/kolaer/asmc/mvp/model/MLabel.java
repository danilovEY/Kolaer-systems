package ru.kolaer.asmc.mvp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MLabel {
	private String name;
	private String info;
	private String pathImage;
	private String pathOpenAppWith;
	private String pathApplication;
	private int priority = 0;

    public MLabel(MLabel originModel) {
        this.priority = originModel.getPriority();
        this.info = originModel.getInfo();
        this.name = originModel.getName();
        this.pathImage = originModel.getPathImage();
        this.pathApplication = originModel.getPathApplication();
        this.pathOpenAppWith = originModel.getPathOpenAppWith();
    }
}
