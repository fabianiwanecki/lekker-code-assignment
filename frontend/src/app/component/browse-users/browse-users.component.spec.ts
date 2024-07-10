import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseUsersComponent } from './browse-users.component';
import {provideHttpClient} from "@angular/common/http";
import {provideRouter, Routes} from "@angular/router";
import {provideAnimations} from "@angular/platform-browser/animations";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('BrowseUsersComponent', () => {
  let component: BrowseUsersComponent;
  let fixture: ComponentFixture<BrowseUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseUsersComponent],
      providers: [
        provideHttpClient(),
        provideRouter(routes),
        provideAnimations()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrowseUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
