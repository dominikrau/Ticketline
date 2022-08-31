/**
 * This Service handles how the date is represented in scripts i.e. ngModel.
 */
import {Injectable} from '@angular/core';
import {NgbDateAdapter, NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class CustomDateAdapter extends NgbDateAdapter<string> {

  readonly DELIMITER = '-';

  fromModel(value: string | null): NgbDateStruct | null {
    if (typeof value === 'string') {
      const date = value.split(this.DELIMITER);

      return {
        year: parseInt(date[0], 10),
        month: parseInt(date[1], 10),
        day: parseInt(date[2], 10)
      };
    }
    return null;
  }

  toModel(date: NgbDateStruct | null): string | null {
    return date ? date.year + this.DELIMITER + padNumber(date.month) + this.DELIMITER + padNumber(date.day) : null;
  }


}
/**
 * This Service handles how the date is rendered and parsed from keyboard i.e. in the bound input field.
 */
@Injectable()
export class CustomDateParserFormatter extends NgbDateParserFormatter {

  readonly DELIMITER = '.';

  parse(value: string): NgbDateStruct | null {
    if (value) {
      const date = value.split(this.DELIMITER);
      return {
        year: parseInt(date[0], 10),
        month: parseInt(date[1], 10),
        day: parseInt(date[2], 10),
      };
    }
    return null;
  }

  format(date: NgbDateStruct | null): string {
    return date ? padNumber(date.day) + this.DELIMITER + padNumber(date.month) + this.DELIMITER + date.year : '';
  }
}

function padNumber(value: number) {
  if (value) {
    return `0${value}`.slice(-2);
  } else {
    return '';
  }
}
