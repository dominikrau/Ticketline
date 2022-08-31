import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Address} from '../../dtos/address';

@Component({
  selector: 'app-address-fields',
  templateUrl: './address-fields.component.html',
  styleUrls: ['./address-fields.component.css']
})
export class AddressFieldsComponent implements OnInit {

  @Input() formGroup: FormGroup;
  @Input() address: Address;
  @Input() groupName: string;

  constructor() {
  }

  ngOnInit(): void {
  }

  isInvalid(formName, element) {
    element = this[formName].get(element);
    return element.invalid
      && (element.dirty
        || element.touched);
  }
}
