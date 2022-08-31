import {Component, OnInit} from '@angular/core';
import {Event} from '../../dtos/event';
import {EventService} from '../../services/event.service';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Ng2ImgMaxService} from 'ng2-img-max';
import {NotificationService} from '../../services/notification.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-event-creator',
    templateUrl: './event-creator.component.html',
    styleUrls: ['./event-creator.component.css']
})
export class EventCreatorComponent implements OnInit {

    error: boolean = false;
    eventForm: FormGroup;
    event: Event = {
        id: null,
        name: null,
        description: null,
        imageUrl: null,
        eventType: null,
        artists: null
    };

    constructor(
        private eventService: EventService,
        private ngbPaginationConfig: NgbPaginationConfig,
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private imageResize: Ng2ImgMaxService,
        private notificationService: NotificationService,
        private router: Router) {

        this.eventForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            description: ['', [Validators.required]],
            eventType: ['', [Validators.required]],
            artists: ['', [Validators.required]],
            imageUrl: ['', [Validators.required]]
        });
    }

    ngOnInit(): void {
    }

    createEvent() {
        Object.keys(this.eventForm.value).forEach((key) => {
            if (typeof this.eventForm.get(key).value === 'string') {
                this.eventForm.get(key).setValue(this.eventForm.get(key)?.value.trim());
            }
        });
        Object.assign(this.event, this.eventForm.value);
        this.eventForm.markAllAsTouched();
        if (this.eventForm.valid) {
            this.eventService.createEvent(this.event).subscribe(
                (event1: Event) => {
                    this.event = event1;
                    this.notificationService.success(`Event "${this.event.name}" Created!`);
                    this.router.navigate(['/events/' + event1.id]);
                }, error => {
                    this.notificationService.error(error);
                }
            );
        }
    }

    onImageInputChanged(event) {
        const image = event.target.files[0];
        if (image === undefined) {
            this.eventForm.controls.imageUrl.setValue('');
            return;
        }
        const reader = new FileReader();
        const that = this;

        this.imageResize.resizeImage(image, 800, 600).subscribe(
            result => {
                console.log(result);
                reader.readAsDataURL(result);
                reader.onloadend = function () {
                    that.eventForm.controls.imageUrl.setValue(reader.result.toString());
                };
            },
            () => {
                this.notificationService.errorMessage('error while resizing image!');
            }
        );
        this.eventForm.controls.imageUrl.markAllAsTouched();
    }

    isInvalid(controlName: string) {
        const control = this.eventForm.controls[controlName];
        return control.touched && control.errors;
    }
}
