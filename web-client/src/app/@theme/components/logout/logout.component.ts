import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';
import {Observable} from 'rxjs/Observable';

@Component({
    selector: 'kol-logout',
    templateUrl: './logout.component.html',
})
export class KolLogoutComponent implements OnInit {

    constructor(protected authenticationService: AuthenticationRestService,
                protected router: Router) {
    }

    ngOnInit() {
        if (this.authenticationService.authentication) {
            this.authenticationService.logout().subscribe(Observable.empty);
        } else {
            this.router.navigate(['/']);
        }
    }
}
