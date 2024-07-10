import {inject, Injectable} from '@angular/core';
import {PageDto, TeamDetailedDto, TeamDto} from "../team/team.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";

export interface UserWithRankAndTeamDto {
  uuid: string;
  username: string;
  score: number;
  rank: number;
  team?: TeamDto;
}

export interface UserWithRankDto {
  uuid: string;
  username: string;
  score: number;
  rank: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  httpClient = inject(HttpClient);

  fetchUsers(page: number, size: number): Observable<PageDto<UserWithRankAndTeamDto>> {
    let params = new HttpParams();
    params = params.set('page', page);
    params = params.set('size', size);

    return this.httpClient.get<PageDto<UserWithRankAndTeamDto>>(
      environment.baseUrl + environment.usersUrl,
      {params});
  }

  fetchUser(uuid: string): Observable<UserWithRankAndTeamDto> {
    return this.httpClient.get<UserWithRankAndTeamDto>(`${environment.baseUrl}${environment.userUrl}${uuid}` )
  }
}
