import {Component, OnInit} from '@angular/core';
import {ShowStateService} from '../../services/show-state.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ShowService} from '../../services/show.service';
import {NotificationService} from '../../services/notification.service';
import {Event} from '../../dtos/event';
import {EventService} from '../../services/event.service';

@Component({
    selector: 'app-show-creator',
    templateUrl: './show-creator.component.html',
    styleUrls: ['./show-creator.component.css']
})
export class ShowCreatorComponent implements OnInit {

    showForm: FormGroup;
    pricings: FormArray;
    eventId: number;
    event: Event;

    constructor(
        private showService: ShowService,
        private showStateService: ShowStateService,
        private route: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
        private notificationService: NotificationService,
        private eventService: EventService) {
    }


    ngOnInit(): void {
        console.log(this.router.url);
        this.eventId = Number(this.route.snapshot.paramMap.get('id'));
        if (!Number.isNaN(Number(this.eventId))) {
            this.showStateService.setEventId(this.eventId);
            this.eventService.getEventById(this.eventId).subscribe(
              event => this.event = event,
              error => this.notificationService.error(error)
            );
        } else {
            this.router.navigate(['/']);
        }

        this.showForm = this.formBuilder.group({
            startTime: ['', [Validators.required]],
            endTime: ['', [Validators.required]],
            venue: [undefined, [Validators.required]],
            hall: [undefined, [Validators.required]],
            seatingChart: [undefined, [Validators.required]],
            pricings: this.formBuilder.array([this.createPricing()])
        });

        const currentShow = this.showStateService.getCurrentShow();
        if (!currentShow.seatingChart?.id) { // seatingChart has not been saved, so it is invalid and has to be recreated!
            delete currentShow.seatingChart;
        }
        this.showForm.patchValue(currentShow);


        this.showForm.valueChanges.subscribe(
            values => {
                this.showStateService.updateShow(values);
            }
        );

        this.showForm.controls.seatingChart.valueChanges.subscribe(
            seatingChart => this.loadPricing(seatingChart)
        );

        this.loadPricing(this.showForm.value.seatingChart);

    }

    loadPricing(seatingChart) {
        if (!seatingChart) {
            return;
        }
        this.pricings = this.showForm.get('pricings') as FormArray;
        this.pricings.clear();
        seatingChart.sectors.map(() => {
            this.addPricing();
        });
        this.showForm.controls.pricings.patchValue(
            seatingChart.sectors.map(x => {
                return {sector: x, price: ''};
            })
        );
    }

    createPricing(): FormGroup {
        return this.formBuilder.group({
            price: ['', [Validators.required, Validators.min(0)]],
            sector: ['', [Validators.required]]
        });
    }

    addPricing(): void {
        this.pricings = this.showForm.get('pricings') as FormArray;
        this.pricings.push(this.createPricing());
    }

    isInvalid(controlName: string) {
        const control = this.showForm.controls[controlName];
        return control.touched && control.errors;
    }

    saveShow() {
        this.showForm.markAllAsTouched();
        if (this.showForm.valid) {
            console.log({'saving show': this.showForm.value});
            this.showService.createShowForEvent(this.showForm.value, this.showStateService.getEventId()).subscribe(
                () => {
                    this.notificationService.success('show created!');
                    this.showStateService.clear();
                    let returnUrl = this.showStateService.getReturnUrl();
                    if (returnUrl === undefined) {
                        returnUrl = '/events/' + this.eventId;
                    }
                    this.router.navigate([returnUrl]);
                },
                error => this.notificationService.error(error)
            );
        }
    }

}
