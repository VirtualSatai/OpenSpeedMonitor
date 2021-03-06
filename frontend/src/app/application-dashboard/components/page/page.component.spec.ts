import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PageComponent} from './page.component';
import {ApplicationDashboardService} from '../../services/application-dashboard.service';
import {SharedMocksModule} from '../../../testing/shared-mocks.module';
import {PageMetricComponent} from "../page-metric/page-metric.component";
import {CsiValueMediumComponent} from "../csi-value/csi-value-medium/csi-value-medium.component";
import {CsiValueBaseComponent} from "../csi-value/csi-value-base.component";

describe('PageComponent', () => {
  let component: PageComponent;
  let fixture: ComponentFixture<PageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PageComponent,
        CsiValueMediumComponent,
        CsiValueBaseComponent,
        PageMetricComponent
      ],
      imports: [
        SharedMocksModule
      ],
      providers: [
        ApplicationDashboardService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
