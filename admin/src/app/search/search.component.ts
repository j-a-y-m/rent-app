import { Component, OnInit } from '@angular/core';
import { city } from './search.model';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  myControl = new FormControl();
  cities : city[] = [new city('Thane'), new city('Nerul'), new city('Bandra'), new city('Pune'), new city('Dadar')] ;
  filteredCities : Observable<city[]> = this.myControl.valueChanges.pipe(
    startWith(''),
    map(value => this._filter(value))
  );
  constructor() { }

  ngOnInit(): void {
    this.filteredCities = this.myControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

    private _filter(value: string): city[] {
      const filterValue = value.toLowerCase();

      return this.cities.filter(cityIt => cityIt.name.toLowerCase().indexOf(filterValue) === 0);
    }

}
