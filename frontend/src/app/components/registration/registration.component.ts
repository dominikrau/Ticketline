import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegistrationService} from '../../services/registration.service';
import {NotificationService} from '../../services/notification.service';
import {NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import {MustMatch} from '../../services/password-validation.service';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    form: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private registrationService: RegistrationService,
        private notificationService: NotificationService,
        private calendar: NgbCalendar
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
            dateOfBirth: [undefined, Validators.required],
            confirmPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')), Validators.minLength(8)]],
            address: this.formBuilder.group({
                country: ['', [Validators.required]],
                city: ['', [Validators.required]],
                postalCode: ['', [Validators.required]],
                street: ['', [Validators.required]],
                additional: ['']
            })
        }, {
            validator: MustMatch('password', 'confirmPassword')
        });
    }

    password(formGroup: FormGroup) {
        const { value: password } = formGroup.get('password');
        const { value: confirmPassword } = formGroup.get('confirmPassword');
        return password === confirmPassword ? null : { passwordNotMatch: true };
    }


    registerUser() {
        this.form.markAllAsTouched();
        if (this.form.valid) {
            this.registrationService.registerUser(this.form.value).subscribe(
                () => this.router.navigate(['/']),
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
    get f() { return this.form.controls; }


}
