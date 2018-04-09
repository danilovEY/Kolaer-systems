import {NgModule} from '@angular/core';
import {TestComponent} from './test.component';
import {TestRoutingModule} from './test.routing';
import {AppCoreModule} from '../../commons/app-core.module';

@NgModule({
	declarations: [
		TestComponent
	],
	imports: [
        AppCoreModule,
		TestRoutingModule
	]
})
export class TestModule {
}
