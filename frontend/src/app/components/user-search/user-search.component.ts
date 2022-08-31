import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Page} from '../../dtos/page';
import {NotificationService} from '../../services/notification.service';
import {UserService} from '../../services/user.service';
import {UserProfile} from '../../dtos/user-profile';
import {Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AdminCreateUserComponent} from '../admin-create-user/admin-create-user.component';

@Component({
  selector: 'app-user-search',
  templateUrl: './user-search.component.html',
  styleUrls: ['./user-search.component.css']
})
export class UserSearchComponent implements OnInit {

    searchForm: FormGroup;
    passwordForm: FormGroup;
    results: Page<UserProfile>;
    pageSize: number = 5;
    pageNumber: number = 1;

    currentUser: UserProfile;

    @ViewChild('content')
    modalTemplate;

    modalRef;

        constructor(
      private userService: UserService,
      private formBuilder: FormBuilder,
      private notificationService: NotificationService,
      private router: Router,
      private modalService: NgbModal
  ) { }

    ngOnInit(): void {
        this.searchForm = this.formBuilder.group({
            firstName: [''],
            lastName: [''],
            blocked: ['false']
        });
        this.passwordForm = this.formBuilder.group({
            currentPassword: ['oldPassword1', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]],
            newPassword: ['', [
                Validators.required,
                Validators.pattern(new RegExp('.*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*')),
                Validators.minLength(8)]]
        });
        this.search();
    }

    /**
     * Search for users with values of searchForm
     */
    search() {
        this.userService.search(this.searchForm.value, this.pageSize, this.pageNumber - 1).subscribe(
            results => this.results = results,
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.results = undefined;
            });
    }

    changePage(page: number) {
        this.pageNumber = page;
        this.search();
    }

    /**
     * Navigate to /admin/users
     */
    navigateUserDetails(id: number) {
        this.router.navigate(['admin/users', id]);
    }

    /**
     * Returns true if user is admin
     */
    isUserAdmin(user: UserProfile): boolean {
        return user.roles.toString().includes('ROLE_ADMIN');
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

    setCurrentUser(user: UserProfile) {
        this.currentUser = this.currentUser === user ? null : user;
    }

    dismiss(modal) {
        modal.dismiss();
    }

    openModal = () => {
        this.modalRef = this.modalService.open(AdminCreateUserComponent, {size: 'lg', windowClass: 'fade-in'});
        this.modalRef.result.then((result) => {
            console.log(result);
            if ( result !== 'canceled' ) {
                if (result.user !== undefined) {
                    this.searchForm.patchValue({
                        firstName: result.user.firstName,
                        lastName: result.user.lastName,
                        blocked: ['false']
                    });
                    this.search();
                }
            }
        });
    }
}
