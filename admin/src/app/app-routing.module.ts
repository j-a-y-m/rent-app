import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListingsComponent } from './listings/listings.component';
import { SearchComponent } from './search/search.component';

const routes: Routes = [
  {path: '', redirectTo:"listings", pathMatch:"full"},
  {path: 'listings', component: ListingsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
