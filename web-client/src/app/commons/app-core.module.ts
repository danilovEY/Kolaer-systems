import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthModule} from './modules/auth/auth.module';
import {SpinnerComponent} from './components/spinner/spinner.component';

@NgModule({
	declarations: [
		SpinnerComponent
	],
	imports: [
		CommonModule,
		AuthModule
	],
	exports: [
		CommonModule,
        AuthModule,
        SpinnerComponent
	]
})
export class AppCoreModule {

}
