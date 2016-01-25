package ru.kolaer.birthday.mvp.viewmodel.impl;

import ru.kolaer.birthday.mvp.view.VCalendar;
import ru.kolaer.birthday.mvp.view.impl.VCalendarImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMCalendar;

public class VMCalendarImpl implements VMCalendar {
	private VCalendar view = new VCalendarImpl();
	
	public VMCalendarImpl() {
		
	}

	@Override
	public VCalendar getView() {
		return this.view;
	}
}
