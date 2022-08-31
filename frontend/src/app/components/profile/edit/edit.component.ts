import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../services/user.service';
import {UserProfile} from '../../../dtos/user-profile';
import {map} from 'rxjs/operators';
import {NotificationService} from '../../../services/notification.service';
import {NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';
import {MustMatch} from '../../../services/password-validation.service';
import {AuthService} from '../../../services/auth.service';

@Component({
    selector: 'app-edit-profile',
    templateUrl: './edit.component.html',
    styleUrls: ['./edit.component.css']
})

export class EditProfileComponent implements OnInit {

    editUserForm: FormGroup;
    updatePasswordForm: FormGroup;

    currentUser: UserProfile;

    @ViewChild('closebutton')
    closebutton;

    constructor(
        private userService: UserService,
        private authService: AuthService,
        private formBuilder: FormBuilder,
        private notificationService: NotificationService,
        private calendar: NgbCalendar,
        private router: Router) {
    }

    ngOnInit(): void {

        this.loadUserProfile().subscribe(
            () => {
                this.initEditUserForm();
                this.initUpdatePasswordForm();
            }
        );
    }

    loadUserProfile() {
        return this.userService.getCurrentUser().pipe(
            map(
                (user: UserProfile) => {
                    this.currentUser = user;
                },
                error => {
                    this.notificationService.error(error);
                })
        );
    }

    initEditUserForm() {
        this.editUserForm = this.formBuilder.group({
                emailAddress: [this.currentUser.emailAddress, [Validators.required, Validators.email, Validators.pattern(new RegExp('.+\@.+\\..+'))]],
                firstName: [this.currentUser.firstName, [Validators.required]],
                lastName: [this.currentUser.lastName, [Validators.required]],
                dateOfBirth: [this.currentUser.dateOfBirth, [Validators.required]],
                gender: [this.currentUser.gender, [Validators.required]],
                address: this.formBuilder.group({
                    country: [this.currentUser.address?.country, [Validators.required]],
                    city: [this.currentUser.address?.city, [Validators.required]],
                    postalCode: [this.currentUser.address?.postalCode, [Validators.required]],
                    street: [this.currentUser.address?.street, [Validators.required]],
                    additional: [this.currentUser.address?.additional]
                })
            },
            error => {
                this.notificationService.error(error);
            });
    }

    initUpdatePasswordForm() {
        this.updatePasswordForm = this.formBuilder.group({
            currentPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]
            ],
            newPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]
            ],
            confirmPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]],
        }, {
            validator: MustMatch('newPassword', 'confirmPassword')
        });
    }

    editUserProfile() {
        Object.keys(this.editUserForm.value).forEach((key) => {
            if (typeof this.editUserForm.get(key).value === 'string') {
                this.editUserForm.get(key).setValue(this.editUserForm.get(key)?.value.trim());
            }
        });
        this.editUserForm.markAllAsTouched();
        if (this.editUserForm.valid) {
            this.userService.editUserProfile(this.editUserForm.value).subscribe(
                (response) => {
                    console.log(response);
                    this.notificationService.success('Profile updated!');
                },
                error => {
                    this.notificationService.error(error);
                }
            );
        }
    }

    updatePassword() {
        this.updatePasswordForm.markAllAsTouched();
        if (this.updatePasswordForm.valid) {
            console.log(this.userService.updatePassword(this.updatePasswordForm.value));
            this.userService.updatePassword(this.updatePasswordForm.value).subscribe(
                () => {
                    this.updatePasswordForm.reset();
                    this.notificationService.success('Password updated successfully!');
                },
                error => {
                    this.notificationService.error(error);

                }
            );
        }
    }

    deleteAccount() {
        this.userService.deleteAccount().subscribe(
            () => {
                this.notificationService.success('Account was successfully deleted');
                this.closebutton.nativeElement.click();
                this.authService.logoutUser();
                this.router.navigate(['/']);
            },
            (error) => {
                this.notificationService.error(error);
            }
        );
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

    getMinAge() {
        const today = this.calendar.getToday();
        today.year -= 7;
        return today;
    }


    /**
     * convenience getter for easy access to form fields
     */
    get f() {
        return this.updatePasswordForm.controls;
    }

}
