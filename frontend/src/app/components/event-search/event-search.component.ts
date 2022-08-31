import {Component, OnInit} from '@angular/core';
import {EventService} from '../../services/event.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Event} from '../../dtos/event';
import {Page} from '../../dtos/page';
import {NotificationService} from '../../services/notification.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
    selector: 'app-event-search',
    templateUrl: './event-search.component.html',
    styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {

    public isCollapsed = true;
    searchForm: FormGroup;
    results: Page<Event>;
    pageSize: number = 5;
    pageNumber: number = 1;
    durationPickerOptions = {
        showButtons: false,
        showYears: false,
        showMonths: false,
        showWeeks: false,
        showSeconds: false,
        showPreview: false
    };

    constructor(
        private eventService: EventService,
        private formBuilder: FormBuilder,
        private notificationService: NotificationService,
        private router: Router,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit(): void {
        this.searchForm = this.formBuilder.group({
            name: [''],
            description: [''],
            eventType: [],
            pseudonym: [''],
            firstName: [''],
            lastName: [''],
            startTime: [''],
            endTime: [''],
            duration: [''],
            minPrice: ['', [Validators.min(0)]],
            maxPrice: [''],
            location: [''],
            hall: ['']
        });
        this.searchForm.controls.minPrice.valueChanges.subscribe(change => {
            let nonNegativ = change;
            if (nonNegativ < 0) {
                nonNegativ = 0;
            }
            this.searchForm.patchValue({minPrice: nonNegativ}, {emitEvent: false});
        });
        this.searchForm.controls.maxPrice.valueChanges.subscribe(change => {
            let nonNegativ = change;
            if (nonNegativ < 0) {
                nonNegativ = 0;
            }
            this.searchForm.patchValue({maxPrice: nonNegativ}, {emitEvent: false});
        });
        this.searchForm.get('eventType').valueChanges.subscribe((type) => {
            this.searchForm.get('eventType').setValue(type, {emitEvent: false});
            this.search();
        });
        this.search();
    }

    search() {
        this.results = undefined;
        this.eventService.search(this.searchForm.value, this.pageSize, this.pageNumber - 1).subscribe(
            results => this.results = results,
            err => {
                console.log(err);
                this.notificationService.error(err);
                this.results = undefined;
            });
        console.log(this.results);
    }

    changePage(page: number) {
        this.pageNumber = page;
        this.search();
    }

    eventDetails(event: Event) {
        this.router.navigate(['events', event.id], {relativeTo: this.route});
    }
}
