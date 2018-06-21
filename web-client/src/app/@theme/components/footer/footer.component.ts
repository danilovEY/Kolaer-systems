import {Component} from '@angular/core';
import {environment} from '../../../../environments/environment';

@Component({
    selector: 'ngx-footer',
    styleUrls: ['./footer.component.scss'],
    template: `
        <span class="created-by">ОИТ 2018 {{ getVersion() }}</span>
    `,
})
export class FooterComponent {
  readonly version: string = environment.version;
  readonly isProd: boolean = environment.production;

  getVersion(): string {
      return this.isProd
          ? `(v${this.version})`
          : `(v${this.version} - Тестовая версия)`
  }
}
