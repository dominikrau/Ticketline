import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {EventTypes} from './event-type';

@Component({
    selector: 'app-event-type-select',
    templateUrl: './event-type-select.component.html',
    styleUrls: ['./event-type-select.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class EventTypeSelectComponent implements OnInit {

    readonly eventTypes = EventTypes;

    @Input()
    formGroup: FormGroup;

    @Input()
    controlName: string;

    constructor() {
    }

    ngOnInit(): void {
    }

    isInvalid() {
        const control = this.formGroup.controls[this.controlName];
        return control.touched && control.errors;
    }

}
