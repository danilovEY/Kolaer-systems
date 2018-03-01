import {Directive, TemplateRef, ViewContainerRef} from '@angular/core';
import {MdbBsDropdownState} from './dropdown.state';

@Directive({
	selector: '[mdbDropdownMenu],[dropdownMenu]',
	exportAs: 'bs-dropdown-menu'
})
export class MdbBsDropdownMenuDirective {
	constructor(_state: MdbBsDropdownState,
				_viewContainer: ViewContainerRef,
				_templateRef: TemplateRef<any>) {
		_state.resolveDropdownMenu({
			templateRef: _templateRef,
			viewContainer: _viewContainer
		});
	}
}
