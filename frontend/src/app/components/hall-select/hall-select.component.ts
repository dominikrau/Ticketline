import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {concat, Observable, of, Subject} from 'rxjs';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {Hall} from '../../dtos/hall';
import {HallService} from '../../services/hall.service';

@Component({
    selector: 'app-hall-select',
    templateUrl: './hall-select.component.html',
    styleUrls: ['./hall-select.component.css']
})
export class HallSelectComponent implements OnInit {

    @Input()
    form: FormGroup;

    @Input()
    controlName: string;

    @Input()
    venueControlName: string;

    @ViewChild('content')
    modalTemplate;

    hallForm: FormGroup;
    halls: Observable<Hall[]>;
    hallLoading = false;
    hallSearch = new Subject<string>();

    constructor(private hallService: HallService, private formBuilder: FormBuilder, private modalService: NgbModal) {
    }

    ngOnInit(): void {
        this.form.controls[this.venueControlName].valueChanges.subscribe(
            venue => {
                this.hallForm.patchValue({venue});
                this.form.controls[this.controlName].patchValue(undefined);
            }
        );
        this.hallForm = this.formBuilder.group({
            name: ['', Validators.required],
            width: ['', Validators.required],
            height: ['', Validators.required],
            venue: [this.getVenue()]
        });
        this.halls = concat(
            of([]), // default items
            this.hallSearch.pipe(
                debounceTime(300),
                distinctUntilChanged(),
                tap(() => this.hallLoading = true),
                switchMap(term => this.hallService.searchHall(this.getVenueId(), term).pipe(
                    catchError(() => of([])), // empty list on error
                    tap(() => this.hallLoading = false)
                ))
            )
        );
    }

    saveHall(modal: NgbModalRef) {
        this.hallForm.markAllAsTouched();
        if (this.hallForm.valid) {
            this.hallService.createHall(this.hallForm.value).subscribe(
                value => {
                    this.hallForm.reset({
                        venue: this.getVenue()
                    });
                    modal.close(value);
                }
            );
        }
    }

    dismiss(modal) {
        this.hallForm.reset({
            venue: this.getVenue()
        });
        modal.dismiss();
    }

    openModal = (prefilledName) => {
        this.hallForm.patchValue({name: prefilledName});
        return this.modalService.open(this.modalTemplate).result;
    }

    isInvalid(controlName: string) {
        const control = this.hallForm.controls[controlName];
        return control.touched && control.errors;
    }

    private getVenueId() {
        return this.getVenue().venueId;
    }

    getVenue() {
        return this.form.value[this.venueControlName];
    }

}
