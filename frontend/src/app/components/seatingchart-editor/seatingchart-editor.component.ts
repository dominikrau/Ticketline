import {Component, Input, OnInit} from '@angular/core';
import {SeatingChart, SeatingChartSearchResult} from '../../dtos/seating-chart';
import {SeatingChartService} from '../../services/seating-chart.service';
import {NotificationService} from '../../services/notification.service';
import {ShowStateService} from '../../services/show-state.service';
import {Hall} from '../../dtos/hall';
import {Router} from '@angular/router';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-seatingchart-editor',
    templateUrl: './seatingchart-editor.component.html',
    styleUrls: ['./seatingchart-editor.component.css']
})
export class SeatingchartEditorComponent implements OnInit {

    // data for testing
    @Input() hall: Hall;

    @Input() seatingChartName: string;

    seatingChart: SeatingChart;

    seatingChartForm: FormGroup;

    lookupTable = [];

    current;

    bounds = {
        x1: 0,
        x2: 0,
        y1: 0,
        y2: 0
    };


    constructor(
        private seatingChartService: SeatingChartService,
        private notificationService: NotificationService,
        private showStateService: ShowStateService,
        private router: Router,
        private formBuilder: FormBuilder) {
    }

    get sectors() {
        return this.seatingChartForm?.value?.sectors;
    }

    ngOnInit(): void {
        if (this.seatingChartName !== undefined) {
            this.seatingChart.name = this.seatingChartName;
        }
        console.log(this.seatingChart);
        if (this.showStateService.getCurrentShow() !== undefined) {
            this.hall = this.showStateService.getHall();
            if (this.hall === undefined) {
                this.router.navigate([this.router.url.split('/').slice(0, -1).join('/')]);
                return;
            }
            this.seatingChart = this.showStateService.getCurrentShow().seatingChart;
        }
        this.current = {
            sector: null,
            sectorIndex: 0,
            seats: [
                {x: 10, y: 10}
            ],
            x: 0,
            y: 0,
            width: 0,
            height: 0,
            deleteMode: false,
            stage: null,
            offset: null,
            resizeStage: false
        };
        this.initSeatingChartForm();
    }

    initSeatingChartForm() {
        this.seatingChartForm = this.formBuilder.group({
            name: [this.seatingChart.name, Validators.required],
            sectors: this.formBuilder.array([]),
            stage: this.formBuilder.group({
                width: [[], Validators.required],
                height: [[], Validators.required],
                x: [[], Validators.required],
                y: [[], Validators.required],
            }),
            hall: null,
        });
        this.current.sector = this.sectors[0];
        this.seatingChartForm.get('stage').patchValue(this.seatingChart.stage);
        this.addSector();
        (this.seatingChartForm.get('sectors') as FormArray).valueChanges.subscribe(
            value => console.log(value)
        );
        this.createLookupTable();
    }


    receiveCursorInformation(event) {
        // console.log(event);
        if (this.current.sector === undefined) {
            return;
        }
        if (event.action === 'mousedown') {
            const foundObject = this.findObjectAt(event.x, event.y);
            if (foundObject && foundObject.stage !== undefined) {
                this.current.stage = this.findObjectAt(event.x, event.y).stage;
                console.log(this.current.stage);
                console.log(this.findObjectAt(event.x, event.y).stage);
                this.current.offset = {
                    x: event.x - this.current.stage.x,
                    y: event.y - this.current.stage.y
                };
                if (this.current.stage.x + this.current.stage.width - event.x < 3
                    && this.current.stage.y + this.current.stage.height - event.y < 3) {
                    // clicked on resize rectangle of stage object
                    this.current.resizeStage = true;
                }
                // this.seatingChartForm.get('stage').patchValue(this.current.stage);
            }
            this.setBounds(event);

        }
        if (event.action === 'mouseup') {
            const SectorFormControl = (this.seatingChartForm.get('sectors') as FormArray).at(this.current.sectorIndex);
            if (this.current.stage) {
                this.bounds.x1 = this.current.stage.x;
                this.bounds.x2 = this.current.stage.x + this.current.stage.width - 1;
                this.bounds.y1 = this.current.stage.y;
                this.bounds.y2 = this.current.stage.y + this.current.stage.height - 1;
                this.seatingChartForm.get('stage').patchValue(this.current.stage);
                this.current.stage = null;
                this.current.resizeStage = false;
            } else if (this.current.sector.type === 'sitting') {
                SectorFormControl.patchValue({
                    seats: SectorFormControl.value.seats.concat(this.current.seats)
                });
            } else { // is standing
                const objectsInArea = this.findObjectsInArea(this.bounds);
                console.log(objectsInArea);
                if (objectsInArea.filter(x => x.sector && x.sector.type === 'standing').length === 0) {
                    this.current.sector.x = Math.min(this.bounds.x1, this.bounds.x2);
                    this.current.sector.y = Math.min(this.bounds.y1, this.bounds.y2);
                    this.current.sector.width = Math.abs(this.bounds.x2 - this.bounds.x1) + 1;
                    this.current.sector.height = Math.abs(this.bounds.y2 - this.bounds.y1) + 1;
                    console.log(this.current.sector);
                    SectorFormControl.patchValue(
                        this.current.sector
                    );
                } else {
                    this.current.sector.width = 0;
                }
                console.log(this.seatingChartForm.value);
                this.deleteSeats(); // delete all seats that might overlap with standing area
            }
            this.current.seats = [];
            if (this.current.deleteMode) {
                this.deleteSeats();
            }
            this.current.deleteMode = !this.current.deleteMode;
            this.setBounds(event);
            this.createLookupTable();
        } else if (event.clicked === true) {
            if (this.current.stage) {
                this.moveAndResizeStage(event);
            } else if (this.current.sector.type === 'sitting') {
                this.updateBounds(event);
                this.previewSeats();
            } else { // is standing
                const objectsInArea = this.findObjectsInArea({
                    x1: this.bounds.x1,
                    x2: event.x,
                    y1: this.bounds.y1,
                    y2: event.y
                });
                console.log(objectsInArea);
                console.log(this.current);
                if (this.current.deleteMode
                    || objectsInArea.length < 1) {
                    this.updateBounds(event);
                }
                this.current.width = Math.abs(this.bounds.x2 - this.bounds.x1) + 1;
                this.current.height = Math.abs(this.bounds.y2 - this.bounds.y1) + 1;
                this.current.x = Math.min(this.bounds.x1, this.bounds.x2);
                this.current.y = Math.min(this.bounds.y1, this.bounds.y2);
            }
        } else {
            if (this.findObjectAt(event.x, event.y)) {
                this.current.deleteMode = true;
                this.setBounds(event);
                this.current.seats = [];
            } else {
                this.current.deleteMode = false;
                if (this.current.sector.type === 'sitting') {
                    this.current.seats = [{x: event.x, y: event.y}];
                } else {
                    this.current.width = 1;
                    this.current.height = 1;
                    this.current.x = event.x;
                    this.current.y = event.y;
                }
            }
        }
    }

    moveAndResizeStage(event) {
        if (this.current.resizeStage) {
            const area = {
                x1: this.current.stage.x,
                y1: this.current.stage.y,
                x2: Math.max(event.x, this.current.stage.x + this.current.stage.width - 1),
                y2: Math.max(event.y, this.current.stage.y + this.current.stage.height - 1),
            };
            const objectsInArea = this.findObjectsInArea(area).filter(object => object && object.stage === undefined);
            if (objectsInArea.length > 0) {
                return;
            }
            this.current.stage.width = Math.max(event.x - this.current.stage.x, 2) + 1; // min stage width is 2
            this.current.stage.height = Math.max(event.y - this.current.stage.y, 2) + 1; // min stage height is 2
        } else {
            const area = {
                x1: Math.max(event.x - this.current.offset.x, 0),
                y1: Math.max(event.y - this.current.offset.y, 0),
                x2: Math.max(event.x - this.current.offset.x + this.current.stage.width - 1, this.current.stage.width),
                y2: Math.max(event.y - this.current.offset.y + this.current.stage.height - 1, this.current.stage.height)
            };
            const objectsInArea = this.findObjectsInArea(area).filter(object => object && object.stage === undefined);
            if (objectsInArea.length > 0) {
                return;
            }
            // move stage but keep it within the hall
            if (event.x - this.current.offset.x >= 0
                && event.x - this.current.offset.x + this.current.stage.width <= this.hall.width) {
                this.current.stage.x = event.x - this.current.offset.x;
            }
            if (event.y - this.current.offset.y >= 0
                && event.y - this.current.offset.y + this.current.stage.height <= this.hall.height) {
                this.current.stage.y = event.y - this.current.offset.y;
            }
        }
        this.seatingChartForm.get('stage').patchValue(this.current.stage);
    }

    getViewBox() {
        return `0 0 ${this.hall.width} ${this.hall.height}`;
    }

    previewSeats() {
        this.current.seats = [];
        const previewSeats = [];

        const x1 = Math.min(this.bounds.x1, this.bounds.x2);
        const x2 = Math.max(this.bounds.x1, this.bounds.x2);
        const y1 = Math.min(this.bounds.y1, this.bounds.y2);
        const y2 = Math.max(this.bounds.y1, this.bounds.y2);

        for (let x = x1; x <= x2; x++) {
            for (let y = y1; y <= y2; y++) {
                if (!this.findObjectAt(x, y)) {
                    previewSeats.push({x: x, y: y});
                }
            }
        }
        this.current.seats = previewSeats;
    }

    findSeatAt(x, y): SeatingChartSearchResult[] {
        return this.seatingChartService.findSeatAt(x, y, this.seatingChart);
    }

    findObjectsInArea(area) {
        const areaRect = {
            x1: Math.min(area.x1, area.x2),
            x2: Math.max(area.x1, area.x2),
            y1: Math.min(area.y1, area.y2),
            y2: Math.max(area.y1, area.y2)
        };

        return this.lookupTable.slice(areaRect.x1, areaRect.x2 + 1).flatMap(row =>
            row.slice(areaRect.y1, areaRect.y2 + 1)
        );
    }

    /**
     * deletes seats within current bounds
     */
    deleteSeats() {
        const x1 = Math.min(this.bounds.x1, this.bounds.x2);
        const x2 = Math.max(this.bounds.x1, this.bounds.x2);
        const y1 = Math.min(this.bounds.y1, this.bounds.y2);
        const y2 = Math.max(this.bounds.y1, this.bounds.y2);
        this.seatingChartForm.get('sectors').patchValue(
            this.sectors.map(sector => {
                if (sector.seats) {
                    sector.seats = sector.seats.filter(seat => !(seat.x >= x1 && seat.x <= x2 && seat.y >= y1 && seat.y <= y2));
                }
                return sector;
            })
        );
    }

    addSector() {
        const sectors = this.seatingChartForm.get('sectors') as FormArray;
        const newSector = this.formBuilder.group({
            name: ['', [Validators.required]],
            color: ['#' + Math.random().toString(16).substr(2, 6), Validators.required],
            type: ['sitting', Validators.required],
            seats: [[], Validators.required]
        });
        newSector.controls.type.valueChanges.subscribe(
            type => {
                if (type === 'standing') {
                    newSector.addControl('capacity', this.formBuilder.control('', [Validators.required, Validators.min(0)]));
                    newSector.addControl('x', this.formBuilder.control(0, [Validators.required, Validators.min(0)]));
                    newSector.addControl('y', this.formBuilder.control(0, [Validators.required, Validators.min(0)]));
                    newSector.addControl('width', this.formBuilder.control(0, [Validators.required, Validators.min(0)]));
                    newSector.addControl('height', this.formBuilder.control(0, [Validators.required, Validators.min(0)]));
                    newSector.removeControl('seats');
                    this.current.sector = newSector.value;
                } else {
                    newSector.removeControl('capacity');
                    newSector.removeControl('x');
                    newSector.removeControl('y');
                    newSector.removeControl('width');
                    newSector.removeControl('height');
                    newSector.addControl('seats', this.formBuilder.control([], [Validators.required]));
                    this.current.sector = newSector.value;
                }
            }
        );
        sectors.push(newSector);
        this.current.sector = newSector.value;
        this.current.sectorIndex = sectors.length - 1;
    }

    save() {
        this.seatingChartForm.markAllAsTouched();
        console.log(this.seatingChartForm.value);
        console.log(this.seatingChartForm.valid);
        if (this.seatingChartForm.valid) {
            this.seatingChartService.save(this.seatingChartForm.value, this.hall.id).subscribe(
                result => {
                    this.notificationService.success('seating chart updated!');
                    const show = this.showStateService.getCurrentShow();
                    show.seatingChart = result;
                    this.showStateService.updateShow(show);

                    console.log(result);
                    this.router.navigate([this.router.url.split('/').slice(0, -1).join('/')]);
                },
                error => this.notificationService.error(error)
            );
        }
    }

    confirmDeleteSector(sector) {
        if (confirm('are you sure you want to delete this sector?')) {
            (this.seatingChartForm.get('sectors') as FormArray).removeAt(sector);
            this.current.sectorIndex = sector - 1;
            this.current.sector = this.sectors[sector - 1];
        }
    }

    setBounds(event) {
        this.bounds.x1 = event.x;
        this.bounds.y1 = event.y;
        this.bounds.x2 = event.x;
        this.bounds.y2 = event.y;
    }

    updateBounds(event) {
        this.bounds.x2 = event.x;
        this.bounds.y2 = event.y;
    }

    setCurrentSector(index) {
        this.current.sectorIndex = index;
        this.current.sector = (this.seatingChartForm.get('sectors') as FormArray).at(index).value;
    }

    capacity() {
        return this.sectors.map(sector => {
            if (sector.type === 'sitting') {
                return sector.seats.length;
            } else if (sector.type === 'standing' && sector.capacity) {
                return sector.capacity;
            }
            return 0;
        }).reduce((acc, cur) => acc + cur);
    }

    isInvalid(control) {
        return control.touched && control.errors;
    }

    private createLookupTable() {
        this.lookupTable = [];
        const seatingChart = this.seatingChartForm.value;

        const createObjectsFromRectangle = (object) => {
            for (let x = object.x; x < object.x + object.width; x++) {
                for (let y = object.y; y < object.y + object.height; y++) {
                    if (this.lookupTable[x] === undefined) {
                        this.lookupTable[x] = [];
                    }
                    if (object.type === 'standing') {
                        this.lookupTable[x][y] = {
                            sector: object
                        };
                    } else {
                        this.lookupTable[x][y] = {
                            stage: object
                        };
                    }
                }
            }
        };

        seatingChart.sectors.map(
            sector => {
                if (sector.type === 'sitting') {
                    sector.seats.map(seat => {
                        if (this.lookupTable[seat.x] === undefined) {
                            this.lookupTable[seat.x] = [];
                        }
                        this.lookupTable[seat.x][seat.y] = {
                            sector: sector,
                            seat: seat
                        };
                    });
                } else if (sector.type === 'standing') {
                    createObjectsFromRectangle(sector);
                }
            }
        );

        createObjectsFromRectangle(seatingChart.stage);
    }

    private findObjectAt(x: any, y: any) {
        if (this.lookupTable[x] === undefined || this.lookupTable[x][y] === undefined) {
            return null;
        }
        return this.lookupTable[x][y];
    }
}
