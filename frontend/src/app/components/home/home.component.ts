import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {EventService} from '../../services/event.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NotificationService} from '../../services/notification.service';
import {Router} from '@angular/router';
import {Event} from '../../dtos/event';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    topTenForm: FormGroup;
    events: Event[];

    constructor(public authService: AuthService,
                private eventService: EventService,
                private formBuilder: FormBuilder,
                private notificationService: NotificationService,
                private router: Router) {
    }

    ngOnInit() {
        const newDate = new Date();
        this.topTenForm = this.formBuilder.group({
            eventType: [],
            month: [newDate.getFullYear() + '-' + ('0' + (newDate.getMonth() + 1)).slice(-2)]
        });
        this.getTopTen();
        this.topTenForm.valueChanges.subscribe(
            () => this.getTopTen()
        );
    }

    getTopTen() {
        this.events = undefined;
        const topTenValue = {month: ''};
        Object.assign(topTenValue, this.topTenForm.value);
        topTenValue.month = topTenValue.month + '-01';
        console.log(topTenValue);
        this.eventService.getTopTen(topTenValue).subscribe(
            (events: Event[]) => {
                console.log('called');
                console.log(this.topTenForm.value);
                this.events = events;
            },
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.events = undefined;
            });
    }

    eventDetails(event: Event) {
        this.router.navigate(['events', event.id]);
    }

    slicedEvents() {
        const half = Math.ceil(this.events.length / 2);
        return {
            half: half, events: [
                this.events.slice(0, half).map((el, index) => ({index: index, event: el})),
                this.events.slice(-half).map((el, index) => ({index: index + half, event: el}))]
        };
    }
}
