import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {UsernamePasswordModel} from '../../../@core/models/username-password.model';
import {NgForm} from '@angular/forms';
import {AuthenticationRestService} from '../../../@core/modules/auth/authentication-rest.service';
import {ServerExceptionModel} from '../../../@core/models/server-exception.model';
import {ServerToken} from '../../../@core/models/server-token.model';

@Component({
    selector: 'kol-login',
    templateUrl: './login.component.html',
})
export class KolLoginComponent implements OnInit {
    @ViewChild('form')
    form: NgForm;

    error: ServerExceptionModel = undefined;
    user: UsernamePasswordModel = new UsernamePasswordModel('');
    submitted: boolean = false;
    success: boolean = false;



    constructor(protected authenticationService: AuthenticationRestService,
                protected router: Router) {
    }

    ngOnInit() {

    }

    login(): void {
        this.error = undefined;
        this.submitted = true;
        this.success = false;

        const username: string = this.form.value.username;
        const password: string = this.form.value.password;

        this.authenticationService.login(username, password)
            .finally(() => this.submitted = false)
            .subscribe(
                (token: ServerToken) => {
                    this.form.reset();
                    this.success = true;
                    setTimeout(() => {
                        return this.router.navigateByUrl('/');
                    }, 500);
                },
                (error: ServerExceptionModel) => {
                    this.error = error;
                });



        // this.errors = this.messages = [];
        // this.submitted = true;
        //
        // this.service.authenticate(this.provider, this.accountModel).subscribe((result: NbAuthResult) => {
        //     this.submitted = false;
        //
        //     if (result.isSuccess()) {
        //         this.messages = result.getMessages();
        //     } else {
        //         this.errors = result.getErrors();
        //     }
        //
        //     const redirect = result.getRedirect();
        //     if (redirect) {
        //         setTimeout(() => {
        //             return this.router.navigateByUrl(redirect);
        //         }, this.redirectDelay);
        //     }
        // });
    }
}
