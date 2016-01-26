package ru.kolaer.birthday.mvp.viewmodel.impl;

import ru.kolaer.birthday.mvp.view.VTableWithUsersBithday;
import ru.kolaer.birthday.mvp.view.impl.VTableWithUsersBithdayImpl;
import ru.kolaer.birthday.mvp.viewmodel.VMTableWithUsersBithday;

public class VMTableWithUsersBithdayImpl implements VMTableWithUsersBithday{
	private final VTableWithUsersBithday table = new VTableWithUsersBithdayImpl();
	
	@Override
	public VTableWithUsersBithday getView() {
		return this.table;
	}

}
