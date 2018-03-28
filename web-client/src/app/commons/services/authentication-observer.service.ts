import {AccountModel} from '../models/account.model';

export interface AuthenticationObserverService {
	login(account: AccountModel): void;
	logout(account: AccountModel): void;
}
