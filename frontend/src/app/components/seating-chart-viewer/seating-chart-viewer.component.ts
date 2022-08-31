import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SeatingChart, SeatingChartSearchResult, SeatingChartSector, SeatingChartSelection} from '../../dtos/seating-chart';
import {SeatingChartService} from '../../services/seating-chart.service';

@Component({
    selector: 'app-seating-chart-viewer',
    templateUrl: './seating-chart-viewer.component.html',
    styleUrls: ['./seating-chart-viewer.component.css']
})
export class SeatingChartViewerComponent implements OnInit {

    @Input() hall;

    @Output() private clicked: EventEmitter<SeatingChartSearchResult[]> = new EventEmitter();

    @Input() seatingChart: SeatingChart;

    @Input() selected: SeatingChartSelection = {seats: [], standing: []};

    constructor(private seatingChartService: SeatingChartService) {
    }

    ngOnInit(): void {
    }

    getViewBox() {
        return `0 0 ${this.hall.width} ${this.hall.height}`;
    }

    receiveCursorInformation(event: any) {
        if (event.clicked) {
            console.log(event);
            console.log(this.seatingChartService.findSeatAt(event.x, event.y, this.seatingChart));
            this.clicked.emit(
                this.seatingChartService.findSeatAt(event.x, event.y, this.seatingChart)
            );
        }
    }

    /**
     * returns true if the seat is in the selected seats array
     * @param id - the seat id
     */
    isSeatSelected(id: number) {
        return this.selected.seats.indexOf(id) > -1;
    }

    /**
     * calculates the height for the used places rectangle in a standing sector
     * @param sector - the standing sector
     */
    standingSectorUsedHeight(sector: SeatingChartSector) {
        return (sector.capacity - sector.available) / sector.capacity * sector.height;
    }

    /**
     * calculates the height for the selected places rectangle in a standing sector
     * @param sector - the standing sector
     */
    selectedSectorUsedHeight(sector: SeatingChartSector) {
        const foundSector = this.selected.standing.find(x => x.id === sector.id);
        if (foundSector !== undefined) {
            return Math.min((sector.available / sector.capacity) * sector.height, (foundSector.amount / sector.capacity * sector.height));
        }
        return 0;
    }
}
