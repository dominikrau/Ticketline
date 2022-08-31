import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {concat, Observable, of, Subject} from 'rxjs';
import {Artist} from '../../../dtos/artist';
import {catchError, debounceTime, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';
import {ArtistService} from '../../../services/artist.service';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-artist-select',
    templateUrl: './artist-select.component.html',
    styleUrls: ['./artist-select.component.css']
})
export class ArtistSelectComponent implements OnInit {

    @Input()
    controlName: string;

    @Input()
    form: FormGroup;

    @ViewChild('content')
    modalTemplate;

    artistForm: FormGroup;
    artists: Observable<Artist[]>;
    artistLoading = false;
    artistSearch = new Subject<string>();

    constructor(private artistService: ArtistService, private formBuilder: FormBuilder, private modalService: NgbModal) {
    }

    ngOnInit(): void {
        this.artistForm = this.formBuilder.group({
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            pseudonym: ['', Validators.required]
        });
        this.artists = concat(
            of([]), // default items
            this.artistSearch.pipe(
                debounceTime(300),
                distinctUntilChanged(),
                tap(() => this.artistLoading = true),
                switchMap(term => this.artistService.searchArtist(term).pipe(
                    catchError(() => of([])), // empty list on error
                    tap(() => this.artistLoading = false)
                ))
            )
        );
    }

    saveArtist(modal: NgbModalRef) {
        this.artistForm.markAllAsTouched();
        if (this.artistForm.valid) {
            this.artistService.saveArtist(this.artistForm.value).subscribe(
                value => {
                    this.artistForm.reset({});
                    modal.close(value);
                }
            );
        }
    }

    openModal = (prefilledName) => {
        const nameComponents = prefilledName.split(' ');
        if (nameComponents.length > 1) {
            this.artistForm.patchValue({firstName: nameComponents[0]});
            this.artistForm.patchValue({lastName: nameComponents.slice(1).join(' ')});
            this.artistForm.patchValue({pseudonym: prefilledName});
        } else {
            this.artistForm.patchValue({pseudonym: nameComponents[0]});
        }
        return this.modalService.open(this.modalTemplate).result;
    }

    isInvalid(controlName: string) {
        const control = this.artistForm.controls[controlName];
        return control.touched && control.errors;
    }

    dismiss(modal) {
        this.artistForm.reset({});
        modal.dismiss();
    }

}
