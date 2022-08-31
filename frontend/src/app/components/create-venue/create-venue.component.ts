import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VenueService} from '../../services/venue.service';
import {NotificationService} from '../../services/notification.service';

@Component({
  selector: 'app-create-venue',
  templateUrl: './create-venue.component.html',
  styleUrls: ['./create-venue.component.css']
})

export class CreateVenueComponent implements OnInit {

    venueCreationForm: FormGroup;

    error: boolean = false;
    submitted: boolean = false;

  constructor(private createVenueService: VenueService,
              private formBuilder: FormBuilder,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
      this.initVenueCreationForm();
  }

  initVenueCreationForm() {
    this.venueCreationForm = this.formBuilder.group({
        name: ['' , [Validators.required]],
        address: this.formBuilder.group({
            country: ['', [Validators.required]],
            city: ['', [Validators.required]],
            postalCode: ['', [Validators.required]],
            street: ['', [Validators.required]],
            additional: ['']
        })
    },
        error => {
            this.notificationService.error(error);
        });
  }

  addVenue() {
      this.venueCreationForm.markAllAsTouched();
      this.submitted = true;
      if (this.venueCreationForm.valid) {
          this.createVenueService.createVenue(this.venueCreationForm.value).subscribe(
              (response) => {
                  console.log(response);
                  this.notificationService.success('Venue Created!');
                  this.venueCreationForm.reset();
              },
              error => {
                  this.notificationService.error(error);
              }
          );
      }
  }

    /**
     * A helper function for validation
     * @param formName the name of the formbuilder instance
     * @param element the name of the control to be validated
     */
    isInvalid(formName, element) {
        element = this[formName].get(element);
        return element?.invalid
            && (element?.dirty
                || element?.touched);
    }



}
