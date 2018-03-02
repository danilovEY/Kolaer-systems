import {AccountModel} from '../models/account.model';

export interface AuthenticationObserver {
	login(account: AccountModel): void;
	logout(account: AccountModel): void;
}
