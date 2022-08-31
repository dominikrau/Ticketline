import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegistrationService} from '../../services/registration.service';
import {NotificationService} from '../../services/notification.service';
import {NgbActiveModal, NgbCalendar, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MustMatch} from '../../services/password-validation.service';

@Component({
    selector: 'app-admin-create-user',
    templateUrl: './admin-create-user.component.html',
    styleUrls: ['./admin-create-user.component.css']
})
export class AdminCreateUserComponent implements OnInit {


    form: FormGroup;

    @Input()
    modal: NgbModal;


    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private registrationService: RegistrationService,
        private notificationService: NotificationService,
        private calendar: NgbCalendar,
        private activeModal: NgbActiveModal
    ) {
    }

    ngOnInit(): void {
        this.form = this.formBuilder.group({
                firstName: ['', Validators.required],
                lastName: ['', Validators.required],
                gender: ['', Validators.required],
                emailAddress: ['', [Validators.required, Validators.email, Validators.pattern(new RegExp('.+\@.+\\..+'))]],
                password: ['', [
                    Validators.required,
                    Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')), Validators.minLength(8)]],
                confirmPassword: ['', [
                    Validators.required,
                    Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                    Validators.minLength(8)]],
                dateOfBirth: [undefined, Validators.required],
                roles: ['', [Validators.required]],
                address: this.formBuilder.group({
                    country: ['', [Validators.required]],
                    city: ['', [Validators.required]],
                    postalCode: ['', [Validators.required]],
                    street: ['', [Validators.required]],
                    additional: ['']
                }),
            }, {
                validator: MustMatch('password', 'confirmPassword')
            }
        );
    }

    registerUserAsAdmin() {
        this.form.markAllAsTouched();
        if (this.form.valid) {
            this.registrationService.registerUserAsAdmin(this.form.value).subscribe(
                () => {
                    this.activeModal.close({message: 'success', user: this.form.value});
                    this.notificationService.success('User created successfully!');
                },
                error => this.notificationService.error(error)
            );
        }
    }

    isInvalid(controlName: string) {
        const control = this.form.controls[controlName];
        return control.touched && control.errors;
    }

    getMinAge() {
        const today = this.calendar.getToday();
        today.year -= 7;
        return today;
    }

    /**
     * convenience getter for easy access to form fields
     */
    get f() {
        return this.form.controls;
    }

    dismiss() {
        this.form.reset();
        this.activeModal.close('canceled');
    }

}
