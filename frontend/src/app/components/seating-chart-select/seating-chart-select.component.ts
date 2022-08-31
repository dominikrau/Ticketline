import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Observable} from 'rxjs';
import {SeatingChart} from '../../dtos/seating-chart';
import {SeatingChartService} from '../../services/seating-chart.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ShowStateService} from '../../services/show-state.service';

@Component({
    selector: 'app-seating-chart-select',
    templateUrl: './seating-chart-select.component.html',
    styleUrls: ['./seating-chart-select.component.css']
})
export class SeatingChartSelectComponent implements OnInit {

    @Input()
    form: FormGroup;

    @Input()
    controlName: string;

    @Input()
    hallControlName: string;

    @ViewChild('content')
    modalTemplate;

    seatingChartForm: FormGroup;
    seatingCharts: Observable<SeatingChart[]>;
    loadedCharts: SeatingChart[];

    constructor(
        private seatingChartService: SeatingChartService,
        private formBuilder: FormBuilder,
        private modalService: NgbModal,
        private router: Router,
        private route: ActivatedRoute,
        private showStateService: ShowStateService) {
    }

    ngOnInit(): void {
        this.form.controls[this.hallControlName].valueChanges.subscribe(
            seatingChart => {
                this.seatingChartForm.patchValue({seatingChart});
                this.form.controls[this.controlName].patchValue(undefined);
                this.getSeatingCharts();
            }
        );
        this.seatingChartService.getSeatingCharts(this.getHallId()).subscribe(
            seatingCharts => this.loadedCharts = seatingCharts
        );

        this.seatingChartForm = this.formBuilder.group({
            name: ['', Validators.required],
        });
        this.getSeatingCharts();
    }

    private getSeatingCharts() {
        this.seatingCharts = this.seatingChartService.getSeatingCharts(this.getHallId());
    }

    seatingChartSearchFn = (term: string, item: any) => {
        term = term.toLowerCase();
        console.log({term: term, item: item});
        return item.name.toLowerCase().includes(term);
    }

    private getHallId() {
        return this.getHall().id;
    }

    getHall() {
        return this.form.value[this.hallControlName];
    }

    createSeatingChart = (prefilledName) => {
        this.showStateService.setHall(this.getHall());
        const show = this.showStateService.getCurrentShow();
        show.seatingChart = this.initialiseSeatingChart(prefilledName);
        this.showStateService.updateShow(show);
        console.log(this.showStateService.getCurrentShow());
        this.seatingChartForm.patchValue({name: prefilledName});
        this.router.navigate(['create-seating-chart'], {relativeTo: this.route});
    }

    initialiseSeatingChart(name) {
        return {
            name: name,
            stage: {
                x: 10,
                y: 10,
                width: 5,
                height: 2
            },
            sectors: [{
                name: 'Category 1',
                color: '#a262f7',
                type: 'sitting',
                seats: []
            }
            ]
        };
    }
}
