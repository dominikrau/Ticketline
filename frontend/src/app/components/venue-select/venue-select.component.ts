import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VenueService} from '../../services/venue.service';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {concat, Observable, of, Subject} from 'rxjs';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {Venue} from '../../dtos/venue';

@Component({
    selector: 'app-venue-select',
    templateUrl: './venue-select.component.html',
    styleUrls: ['./venue-select.component.css']
})
export class VenueSelectComponent implements OnInit {

    @Input()
    form: FormGroup;

    @Input()
    controlName: string;

    @ViewChild('content')
    modalTemplate;

    venueForm: FormGroup;
    venues: Observable<Venue[]>;
    venueLoading = false;
    venueSearch = new Subject<string>();

    constructor(private venueService: VenueService, private formBuilder: FormBuilder, private modalService: NgbModal) {
    }

    ngOnInit(): void {
        this.venueForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            address: this.formBuilder.group({
                country: ['', [Validators.required]],
                city: ['', [Validators.required]],
                postalCode: ['', [Validators.required]],
                street: ['', [Validators.required]],
                additional: ['']
            })
        });
        this.venues = concat(
            of([]), // default items
            this.venueSearch.pipe(
                debounceTime(300),
                distinctUntilChanged(),
                tap(() => this.venueLoading = true),
                switchMap(term => this.venueService.searchVenue(term).pipe(
                    catchError(() => of([])), // empty list on error
                    tap(() => this.venueLoading = false)
                ))
            )
        );
    }

    saveVenue(modal: NgbModalRef) {
        this.venueForm.markAllAsTouched();
        if (this.venueForm.valid) {
            this.venueService.createVenue(this.venueForm.value).subscribe(
                value => {
                    this.venueForm.reset({});
                    modal.close(value);
                }
            );
        }
    }

    dismiss(modal) {
        this.venueForm.reset({});
        modal.dismiss();
    }

    openModal = (prefilledName) => {
        this.venueForm.patchValue({name: prefilledName});
        return this.modalService.open(this.modalTemplate).result;
    }

    isInvalid(controlName: string) {
        const control = this.venueForm.controls[controlName];
        return control.touched && control.errors;
    }

}
