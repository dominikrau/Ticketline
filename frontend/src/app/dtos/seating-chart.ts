import {Hall} from './hall';

export interface SeatingChart {
    hall?: Hall;
    id?: number;
    name: string;
    stage: SeatingChartStage;
    sectors: SeatingChartSector[];
}

export interface SeatingChartStage {
    x: number;
    y: number;
    width: number;
    height: number;
}

export interface SeatingChartSector {
    id?: number;
    name: string;
    color: string;
    type: string;
    seats?: SeatingChartSeat[];
    x?: number;
    y?: number;
    width?: number;
    height?: number;
    capacity?: number;
    available?: number;
}

export interface SeatingChartSeat {
    id?: number;
    x: number;
    y: number;
    available?: boolean;
}

/**
 * An array of seats and Areas combined
 */
export interface SeatingChartSearchResult {
    id?: number;
    x: number;
    y: number;
    width?: number;
    height?: number;
    capacity?: number;
    available?: any;
    type?: string;
}

export interface SeatingChartSelection {
    seats: number[];
    standing: StandingSectorSelection[];
}

export interface StandingSectorSelection {
    id: number;
    amount: number;
}
