import {TestBed} from '@angular/core/testing';

import {UserService, UserWithRankAndTeamDto} from './user.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {PageDto} from "../team/team.service";
import {environment} from "../../../environments/environment.development";

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserService, provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch users', () => {
    const mockResponse: PageDto<UserWithRankAndTeamDto> = {
      data: [
        { uuid: '1', username: 'user1', score: 100, rank: 1 },
        { uuid: '2', username: 'user2', score: 90, rank: 2 }
      ],
      totalElements: 10,
      page: 1,
      elements: 2
    };

    service.fetchUsers(0, 10).subscribe(users => {
      expect(users).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.usersUrl}?page=0&size=10`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should fetch a single user', () => {
    const mockUser: UserWithRankAndTeamDto = {
      uuid: '1',
      username: 'user1',
      score: 100,
      rank: 1
    };

    service.fetchUser('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${environment.baseUrl}${environment.userUrl}1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });
});
