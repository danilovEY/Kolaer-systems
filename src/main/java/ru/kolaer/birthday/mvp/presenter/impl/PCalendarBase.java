package ru.kolaer.birthday.mvp.presenter.impl;

import ru.kolaer.birthday.mvp.presenter.ObserverCalendar;
import ru.kolaer.birthday.mvp.presenter.PCalendar;
import ru.kolaer.birthday.mvp.view.VCalendar;
import ru.kolaer.birthday.mvp.view.impl.VCalendarImpl;
import ru.kolaer.client.javafx.system.UniformSystemEditorKit;

/**
 * Реализация базовых методов для presenter'ов календарей.
 *
 * @author danilovey
 * @version 0.1
 */
public abstract class PCalendarBase implements PCalendar {
	/**Имя организации.*/
	protected final String ORGANIZATION;
	/**View-календаря.*/
	protected VCalendar view = new VCalendarImpl();
	/**Слушатель календаря.*/
	protected ObserverCalendar observerCalendar;
	protected boolean isInitDayCellFactory = false;
	protected final UniformSystemEditorKit editorKid;
	
	public PCalendarBase(final String organization, final UniformSystemEditorKit editorKid) {
		this.editorKid = editorKid;
		this.ORGANIZATION = organization;
		this.init();
	}

	private void init() {
		this.view.setTitle(ORGANIZATION);
		this.view.setOnAction(e -> {
			this.notifySelectedDate(this.view.getSelectDate());
		});
	}
	
	@Override
	public boolean isInitDayCellFactory() {
		return this.isInitDayCellFactory;
	}
	
	@Override
	public void registerObserver(ObserverCalendar observer) {
		this.observerCalendar = observer;
	}

	@Override
	public void removeObserver(ObserverCalendar observer) {
		this.observerCalendar = null;
	}

	@Override
	public VCalendar getView() {
		return this.view;
	}
}
