package ru.kolaer.birthday.mvp.presenter;

import ru.kolaer.birthday.mvp.view.VSearchUsers;

public interface PSearchUsers extends ObservableSearch {
	VSearchUsers getView();
}
