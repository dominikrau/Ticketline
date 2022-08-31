import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../services/user.service';
import {NotificationService} from '../../../services/notification.service';
import {UserProfile} from '../../../dtos/user-profile';
import {MustMatch} from '../../../services/password-validation.service';

@Component({
    selector: 'app-admin-show-user',
    templateUrl: './admin-show-user.component.html',
    styleUrls: ['./admin-show-user.component.css']
})
export class AdminShowUserComponent implements OnInit {

    @Input()
    user: UserProfile;

    error: boolean = false;
    passwordForm: FormGroup;

    constructor(
        private userService: UserService,
        private formBuilder: FormBuilder,
        private notificationService: NotificationService
    ) {
    }

    ngOnInit(): void {
        this.passwordForm = this.formBuilder.group({
            currentPassword: ['oldPassword1', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]],
            newPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]],
            confirmPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]]
        }, {
            validator: MustMatch('newPassword', 'confirmPassword')
        });
    }

    /**
     * convenience getter for easy access to form fields
     */
    get f() {
        return this.passwordForm.controls;
    }

    /**
     * Reload current user profile
     */
    getUserById() {
        return this.userService.getUserById(this.user.userId).subscribe(
            (user: UserProfile) => {
                this.user = user;
            }, error => {
                this.notificationService.error(error);
            }
        );
    }

    /**
     * Returns true if user is admin
     */
    isUserAdmin(user: UserProfile): boolean {
        return user.roles.toString().includes('ROLE_ADMIN');
    }

    /**
     * Unblocks user by id
     * @param id of the user to unblock
     */
    unblockUser(id: number) {
        this.userService.unblockUser(id).subscribe(
            result => this.getUserById(),
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.user = undefined;
            });
    }

    /**
     * Blocks user by id
     * @param id of the user to block
     */
    blockUser(id: number) {
        this.userService.blockUser(id).subscribe(
            result => this.getUserById(),
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.user = undefined;
            });
    }

    /**
     * Resets password for a user with new password specified in passwordForm
     * @param id of the user that gets password changed
     */
    resetPasswordForUser(id: number) {

        // stop here if form is invalid
        if (this.passwordForm.invalid) {
            return;
        }
        this.userService.resetPasswordForUser(this.passwordForm.value, id).subscribe(
            result => {
                this.getUserById();
                this.notificationService.success('Password updated successfully!');
            },
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.user = undefined;
            });
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

    /**
     * Gives admin rights to a user specified by id
     * @param id of the user that gets admin rights
     */
    makeUserAdmin(id: number) {
        this.userService.makeUserAdmin(id).subscribe(
            (user: UserProfile) => {
                this.user = user;
            }, error => {
                this.notificationService.error(error);
            }
        );
    }

    getGender() {
        return {M: 'Male', F: 'Female', O: 'Other'}[this.user.gender];
    }
}
