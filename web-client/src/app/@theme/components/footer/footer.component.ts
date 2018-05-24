import {Component} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Component({
    selector: 'ngx-footer',
    styleUrls: ['./footer.component.scss'],
    template: `
        <span class="created-by">ОИТ 2018 (v{{version}})</span>
    `,
})
export class FooterComponent {
  readonly version: string = environment.version;
}
