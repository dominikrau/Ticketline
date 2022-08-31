import {Injectable} from '@angular/core';
import {NgbTimeAdapter, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class CustomTimeAdapter extends NgbTimeAdapter<string> {

    fromModel(value: string| null): NgbTimeStruct | null {
        if (!value) {
            return null;
        }
        const split = value.split(':');
        return {
            hour: parseInt(split[0], 10),
            minute: parseInt(split[1], 10),
            second: parseInt(split[2], 10)
        };
    }

    toModel(time: NgbTimeStruct | null): string | null {
        return time != null ? `${this.pad(time.hour)}:${this.pad(time.minute)}:${this.pad(time.second)}` : null;
    }

    private pad = (i: number): string => i < 10 ? `0${i}` : `${i}`;
}
