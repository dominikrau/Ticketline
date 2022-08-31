import {Component, OnInit} from '@angular/core';
import {Event} from '../../dtos/event';
import {EventService} from '../../services/event.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Show} from '../../dtos/show';
import {ShowService} from '../../services/show.service';
import {NotificationService} from '../../services/notification.service';
import {TicketService} from '../../services/ticket.service';
import {ShowStateService} from '../../services/show-state.service';
import {CheckoutService} from '../../services/checkout.service';


@Component({
    selector: 'app-event-detail',
    templateUrl: './event-detail.component.html',
    styleUrls: ['./event-detail.component.css']
})
export class EventDetailComponent implements OnInit {

    // After first submission attempt, form validation will start
    public event: Event;
    public shows: Show[];
    private eventId: number;
    venueCreationForm: FormGroup;

    dateNow: string = new Date().toLocaleDateString();

    constructor(private route: ActivatedRoute, private eventService: EventService, private showService: ShowService,
                private formBuilder: FormBuilder, private authService: AuthService,
                private notificationService: NotificationService,
                private ticketService: TicketService,
                private showStateService: ShowStateService,
                private router: Router,
                private checkoutService: CheckoutService
    ) {
    }

    ngOnInit() {
        console.log(this.dateNow);
        this.getEvent();
        this.loadShow();
        this.initVenueCreationForm();
    }

    initVenueCreationForm() {
        this.venueCreationForm = this.formBuilder.group({
                name: ['', [Validators.required]],
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


    getEvent() {
        this.eventId = +this.route.snapshot.paramMap.get('id');
        this.eventService.getEventById(this.eventId).subscribe(
            (event: Event) => {
                // console.log(event);
                this.event = event;
            },
            error => {
                this.notificationService.error(error);
            }
        );
    }

    /**
     * Loads the specified page of show from the backend
     */
    private loadShow() {
        this.showService.getShowsByEventId(this.eventId).subscribe(
            (show: Show[]) => {
                this.shows = show;
            },
            error => {
                this.notificationService.error(error);
            }
        );
    }

    /**
     * Returns true if the authenticated user is an admin
     */
    isAdmin(): boolean {
        return this.authService.getUserRole() === 'ADMIN';
    }

    createShowForEvent(show: Show) {
        this.showService.createShowForEvent(show, this.eventId).subscribe(
            () => {
                this.loadShow();
            },
            error => {
                this.notificationService.error(error);
            }
        );
    }

    createTicket(show: Show) {
        this.checkoutService.clearEnabledUrls();
        this.router.navigate(['checkout/' + show.showId]);
    }

    openShowCreator() {
        this.showStateService.setReturnUrl(this.router.url);
        this.router.navigate(['/admin/events/' + this.eventId + '/create-show']);
    }

    isAvailable(startTime: string) {
        return new Date(startTime) > new Date();
    }
}
