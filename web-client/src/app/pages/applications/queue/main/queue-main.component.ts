import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {AccountService} from '../../../../@core/services/account.service';
import {QueueService} from '../queue.service';

@Component({
    selector: 'queue-main',
    templateUrl: './queue-main.component.html',
    styleUrls: ['./queue-main.component.scss']
})
export class QueueMainComponent {
    constructor(private queueService: QueueService,
                private router: Router,
                private accountService: AccountService) {
    }

    ngOnInit() {

    }

}
