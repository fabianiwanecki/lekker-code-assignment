import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamDetailsComponent } from './team-details.component';
import {provideRouter, Routes} from "@angular/router";
import {provideHttpClient} from "@angular/common/http";

const routes: Routes = [
  { path: '', component: {} as any },
];

describe('TeamDetailsComponent', () => {
  let component: TeamDetailsComponent;
  let fixture: ComponentFixture<TeamDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamDetailsComponent],
      providers: [
        provideRouter(routes),
        provideHttpClient(),
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
