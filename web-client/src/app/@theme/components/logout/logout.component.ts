import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';

@Component({
    selector: 'kol-logout',
    templateUrl: './logout.component.html',
})
export class CustomLogoutComponent implements OnInit {

    constructor(protected authenticationService: AuthenticationRestService,
                protected router: Router) {
    }

    ngOnInit() {
        if (this.authenticationService.authentication) {
            this.authenticationService.logout().subscribe();
        } else {
            this.router.navigate(['/']);
        }
    }
}
