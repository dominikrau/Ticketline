import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {SeatingChart, SeatingChartSearchResult} from '../dtos/seating-chart';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
    providedIn: 'root'
})
export class SeatingChartService {

    private seatingChartBaseUri: string = this.globals.backendUri + '';

    constructor(private httpClient: HttpClient, private globals: Globals) {
    }

    save(seatingChart: SeatingChart, hallId: number): Observable<SeatingChart> {
        console.log(seatingChart);
        return this.httpClient.post<SeatingChart>(this.seatingChartBaseUri + '/halls/' + hallId + '/seatingCharts', seatingChart);
    }

    getSeatingCharts(hallId: number): Observable<SeatingChart[]> {
        return this.httpClient.get<SeatingChart[]>(this.seatingChartBaseUri + '/halls/' + hallId + '/seatingCharts/');
    }

    findSeatAt(x: any, y: any, seatingChart: SeatingChart): SeatingChartSearchResult[] {
        return <SeatingChartSearchResult[]>seatingChart.sectors.flatMap(sector => sector.seats)
            .filter(seat => (seat && seat.x === x && seat.y === y))
            .concat( // concatenate seats with area objects
                <SeatingChartSearchResult[]>seatingChart.sectors.filter(sector =>
                    (sector.type === 'standing'
                        && sector.x <= x
                        && sector.x + sector.width > x
                        && sector.y <= y
                        && sector.y + sector.height > y))
            )
            .concat( // concatenate with stage
                [seatingChart.stage].filter(stage =>
                    (stage && stage.x <= x
                        && stage.x + stage.width > x
                        && stage.y <= y
                        && stage.y + stage.height > y))
            );
    }
}
