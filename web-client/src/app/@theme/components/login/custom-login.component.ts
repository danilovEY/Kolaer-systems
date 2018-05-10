import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {NbAuthService} from '@nebular/auth';
import {UsernamePasswordModel} from "../../../@core/models/username-password.model";
import {NgModel} from "@angular/forms";

@Component({
    selector: 'custom-login',
    templateUrl: './custom-login.component.html',
})
export class CustomLoginComponent implements OnInit {
    @ViewChild('username')
    username: NgModel;

    redirectDelay: number = 0;
    showMessages: any = {};
    provider: string = '';

    errors: string[] = [];
    messages: string[] = [];
    user: UsernamePasswordModel = new UsernamePasswordModel('');
    submitted: boolean = false;

    constructor(protected service: NbAuthService,
                protected router: Router) {
    }

    ngOnInit() {

    }

    login(): void {
        console.log(this.username);
        // this.errors = this.messages = [];
        // this.submitted = true;
        //
        // this.service.authenticate(this.provider, this.user).subscribe((result: NbAuthResult) => {
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
